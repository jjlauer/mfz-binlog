package com.mfizz.binlog.impl;

/*
 * #%L
 * mfz-binlog
 * %%
 * Copyright (C) 2012 mfizz
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.mfizz.binlog.*;
import com.mfizz.binlog.type.DateTimePeriod;
import com.mfizz.binlog.type.EventType;
import com.mfizz.util.NamingThreadFactory;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfizz
 */
public class BinloggerImpl implements Binlogger, BinlogActivator {
    private static final Logger logger = LoggerFactory.getLogger(BinloggerImpl.class);
    
    private final BinloggerConfiguration configuration;
    private final BinlogFactory factory;
    // file rotation
    private final BinloggerActiveFileWriter activeFileWriter;
    private final BinloggerFileRotater rotater;
    private final BinloggerFileRotaterRunner rotaterRunner;
    private ScheduledFuture<?> rotaterFuture;
    private final ScheduledExecutorService executors;
    // synchronized with binlog lock
    private final ReentrantLock binlogLock;
    private final AtomicReference<BinlogFileWriter> activeBinlog;
    // previous binlogs this binlogger has managed/created/etc
    private final Set<BaseBinlogFile> pendingCloseBinlogs;
    private final ConcurrentLinkedQueue<BaseBinlogFile> closedBinlogs;
 
    public BinloggerImpl(BinloggerConfiguration configuration, BinlogFactory factory) {
        this(configuration, factory, null);
    }
    
    public BinloggerImpl(BinloggerConfiguration configuration, BinlogFactory factory, ScheduledExecutorService executors) {
        this.configuration = configuration;
        this.factory = factory;
        this.rotater = new BinloggerFileRotater(configuration);
        this.rotaterRunner = new BinloggerFileRotaterRunner(this);  // weak reference stored in runner so not circular ref
        this.binlogLock = new ReentrantLock();
        this.activeBinlog = new AtomicReference<BinlogFileWriter>();
        this.pendingCloseBinlogs = Collections.newSetFromMap(new ConcurrentHashMap<BaseBinlogFile,Boolean>());
        this.closedBinlogs = new ConcurrentLinkedQueue<BaseBinlogFile>();
        
        if (executors != null) {
            this.executors = executors;
        } else {
            this.executors = Executors.newScheduledThreadPool(1, new NamingThreadFactory(configuration.getName() + "-binlogger-scheduler", true));
        }
        
        // active file writer (enabled/disabled)
        if (configuration.isActiveFileEnabled()) {
            File activeFile = new File(this.configuration.getBinlogDirectory(), this.configuration.getName() + ".active");
            this.activeFileWriter = new BinloggerActiveFileWriter(activeFile);
        } else {
            this.activeFileWriter = null;
        }
        
        //
        // important, add a shutdown hook to make sure flush() is called on the the active binlog
        //
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                BinlogFileWriter binlog = activeBinlog.get();
                if (binlog != null) {
                    logger.info("ShutdownHook flushing and closing binlog [{}]", binlog.getFile().getAbsolutePath());
                    try {
                        safelyCloseBinlog(binlog);
                    } catch (Throwable t) {
                        logger.error("Unable to properly flush and close binlog", t);
                    }
                }
            }
        });
        logger.info("ShutdownHook configured for binlogger [{}] to protect binlogs from corruption at JVM shutdown", configuration.getName());
    }
    
    public BinloggerActiveFileWriter getActiveFileWriter() {
        return this.activeFileWriter;
    }
    
    public BinlogFileWriter getActiveBinlogWriter() {
        return this.activeBinlog.get();
    }
    
    public BinlogFactory getFactory() {
        return this.factory;
    }
    
    public BinloggerFileRotater getRotater() {
        return this.rotater;
    }

    @Override
    public BinloggerConfiguration getConfiguration() {
        return this.configuration;
    }
    
    @Override
    public BaseBinlogFile getActiveBinlog() {
        return this.activeBinlog.get();
    }

    @Override
    public Collection<BaseBinlogFile> getPendingCloseBinlogs() {
        return this.pendingCloseBinlogs;
    }
    
    @Override
    public Collection<BaseBinlogFile> getClosedBinlogs() {
        return this.closedBinlogs;
    }
    
    public void scheduleNextRotateAttempt(long nextRotateMillis) {
        // only schedule if period isn't NEVER
        if (configuration.getFileRotatePeriod() != DateTimePeriod.NEVER) {
            logger.info("Next rotate attempt scheduled in [{}] ms", nextRotateMillis);
            this.rotaterFuture = this.executors.schedule(rotaterRunner, nextRotateMillis, TimeUnit.MILLISECONDS);
        }
    }
    
    @Override
    public void start() throws IOException, BinlogException, InterruptedException {
        logger.info("Starting binlogger [{}] with logs in directory [{}]...", configuration.getName(), configuration.getBinlogDirectory().getAbsolutePath());
        // create the initial binlog
        long now = System.currentTimeMillis();
        BinlogFileWriter binlog = rotater.createInitialBinlog(now, factory);
        // set this new binlog to our "initial" active one
        setActiveBinlog(binlog);
        // try to write the first event!
        writeEvent(null, EventType.BINLOGGER_STARTED, null, null);
        // schedule the next rotate attempt
        this.scheduleNextRotateAttempt(rotater.calculateMillisTillNextPeriod(now));
        logger.info("Binlogger [{}] started", configuration.getName());
    }
    
    @Override
    public void stop() throws IOException, BinlogException, InterruptedException {
        logger.info("Stopping binlogger [{}] with logs in directory [{}]...", configuration.getName(), configuration.getBinlogDirectory().getAbsolutePath());
        // unschedule next rotate attempt
        if (this.rotaterFuture != null) {
            this.rotaterFuture.cancel(true);
            this.rotaterFuture = null;
        }
        // get active binlog
        BinlogFileWriter binlog = this.activeBinlog.get();
        if (binlog != null) {
            writeEvent(null, EventType.BINLOGGER_STOPPED, null, null);
            this.activeBinlog.set(null);
            safelyCloseBinlog(binlog);
            // close all pending close binlogs
            for (BaseBinlogFile f : this.pendingCloseBinlogs) {
                safelyCloseBinlog((BinlogFileWriter)f);
            }
        }
        logger.info("Binlogger [{}] stopped", configuration.getName());
    }
    
    @Override
    public void flush() throws IOException {
        BinlogFileWriter binlog = this.activeBinlog.get();
        if (binlog != null) {
            binlog.flush();
        }
    }

    @Override
    public BinlogRecordFileInfo write(byte[] data) throws IOException, BinlogException, InterruptedException {
        return write(data, null, null);
    }

    @Override
    public BinlogRecordFileInfo write(byte[] data, Integer userDefinedType) throws IOException, BinlogException, InterruptedException {
        return write(data, userDefinedType, null);
    }

    @Override
    public BinlogRecordFileInfo write(byte[] data, Map<String, String> optionalParameters) throws IOException, BinlogException, InterruptedException {
        return write(data, null, optionalParameters);
    }
    
    @Override
    public BinlogRecordFileInfo write(byte[] data, Integer userDefinedType, Map<String, String> optionalParameters) throws IOException, BinlogException, InterruptedException {
        BinlogRecordHeader recordHeader = BinlogFileWriterImpl.createRecordHeaderForRecord(userDefinedType, optionalParameters);
        return write(recordHeader, data);
    }
    
    public BinlogRecordFileInfo writeEvent(DateTime timestamp, EventType type, String info, Map<String,String> optionalParameters) throws IOException, BinlogException, InterruptedException {
        // we always want a timestamp
        if (timestamp == null) {
            timestamp = new DateTime(DateTimeZone.UTC);
        }
        BinlogRecordHeader recordHeader = BinlogFileWriterImpl.createRecordHeaderForEvent(timestamp, type, info, optionalParameters);
        return write(recordHeader, null);
    }

    public BinlogRecordFileInfo write(BinlogRecordHeader recordHeader, byte[] data) throws IOException, BinlogException, InterruptedException {
        BinlogFileWriter binlog = getAndCheckoutActiveBinlog();     // atomic via binlogLock
        // check if the binlog checkout worked
        if (binlog == null) {
            throw new BinlogException("Unable to write to binlog; binlogger either not started, stopped, or is an invalid state");
        }
        try {
            return binlog.write(recordHeader, data);
        } finally {
            // are we not using the active binlog anymore?
            boolean usingInactiveBinlog = (this.activeBinlog.get() != binlog);
            // always check the binlog back in
            int checkouts = binlog.checkin();
            if (usingInactiveBinlog && checkouts <= 0) {
                safelyCloseBinlog(binlog);
            }
        }
    }
    
    private void safelyCloseBinlog(BinlogFileWriter binlog) {
        if (binlog == null || binlog.isClosed()) {
            // do nothing
            return;
        }
        try {
            // record "close" event before actual close
            try {
                binlog.writeEvent(EventType.BINLOGGER_CLOSED);
                binlog.flush();
            } catch (Throwable t) {
                logger.error("Unable to write close event to binlog [" + binlog.getFile().getName() + "]", t);
            }
            
            binlog.close();
            logger.info("Closed binlog [{}]", binlog.getFile().getName());
            
            // always remove a closed binlog from pending (even if it doesn't
            // exist, the underlying set means its safe to remove
            this.pendingCloseBinlogs.remove(binlog);
            
            if (this.closedBinlogs.size() >= this.configuration.getMaxClosedBinlogSize()) {
                // remove the "head" of this queue (oldest item)
                this.closedBinlogs.poll();
            }
            // always then add the binlog onto the end
            this.closedBinlogs.add(binlog);
        } catch (Throwable t) {
            logger.error("Unable to cleanly close binlog [" + binlog.getFile().getName() + "]; a file may still be open by this process!", t);
        }
    }
    
    @Override
    public void setActiveBinlog(BinlogFileWriter newBinlog) throws InterruptedException {
        this.binlogLock.lockInterruptibly();
        try {
            // set the new active binlog and get a reference to the old one
            logger.info("Activating new binlog [{}]", newBinlog.getFile().getName());
            BinlogFileWriter oldBinlog = this.activeBinlog.getAndSet(newBinlog);
            
            if (oldBinlog != null) {
                // if old binlog has a checkout value > 0 then there are other threads actively
                // trying to still write data to it -- we will have to queue this binlog for closing
                // at a later time once all the writing threads are finished with it
                int checkouts = oldBinlog.getCheckouts();
                if (checkouts > 0) {
                    logger.info("Previous binlog had [{}] checkouts, deferring close of binlog [{}]...", checkouts, oldBinlog.getFile().getName());
                    this.pendingCloseBinlogs.add(oldBinlog);
                } else {
                    logger.info("Previous binlog had 0 checkouts, closing now...", checkouts);
                    this.safelyCloseBinlog(oldBinlog);  // this also adds it to "closed" binlog set
                }
            }
        } finally {
            // always unlock
            this.binlogLock.unlock();
        }
        
        // after lock is released, write out the new active binlog
        if (this.activeFileWriter != null) {
            try {
                this.activeFileWriter.write(newBinlog.getFile().getName());
            } catch (Throwable t) {
                logger.error("Write to active file [" + this.activeFileWriter.getFile().getName() + "] failed", t);
            }
        }
    }
    
    public BinlogFileWriter getAndCheckoutActiveBinlog() throws InterruptedException {
        this.binlogLock.lockInterruptibly();
        try {
            // get reference to active binlog
            BinlogFileWriter binlog = this.activeBinlog.get();
            if (binlog == null) {
                return null;        // this is a bad error
            }
            // do a checkout by incrementing a simple counter on this file
            binlog.checkout();
            return binlog;
        } finally {
            // always unlock
            this.binlogLock.unlock();
        }
    }
    
}
