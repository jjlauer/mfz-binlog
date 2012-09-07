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

import com.mfizz.binlog.BinlogException;
import com.mfizz.binlog.BinlogFactory;
import com.mfizz.binlog.BinlogFile;
import com.mfizz.binlog.BinlogFileHeader;
import com.mfizz.binlog.BinlogFileWriter;
import com.mfizz.binlog.BinloggerConfiguration;
import com.mfizz.binlog.WritableBinlogFileHeader;
import com.mfizz.binlog.type.DateTimePeriod;
import com.mfizz.binlog.type.EventType;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfizz
 */
public class BinloggerFileRotater {
    private static final Logger logger = LoggerFactory.getLogger(BinloggerFileRotater.class);
    
    private final BinloggerConfiguration configuration;
    // calculating active and next filename
    private long currentPeriodTimestamp;
    private long nextPeriodTimestamp;
    
    public BinloggerFileRotater(BinloggerConfiguration configuration) {
        // verify the configuration is valid first
        if (configuration == null) {
            throw new NullPointerException("configuration cannot be null");
        }
        if (configuration.getName() == null) {
            throw new IllegalArgumentException("name in configuration cannot be null");
        }
        if (configuration.getFileRotatePeriod() == null) {
            throw new IllegalArgumentException("fileRotatePeriod in configuration cannot be null");
        }
        this.configuration = configuration;
    }
    
    /**
     * Calculate the normalized timestamp (in milliseconds since epoch) for the 
     * configured period.  For example, if configured for "every 5 minutes", 
     * this method will calculate the first timestamp that represents this
     * "5 minute period".
     * @param timestamp The timestamp to calculate the period based on
     * @return The first timestamp representing the configured period
     */
    public long calculatePeriodTimestamp(long timestamp) {
        return configuration.getFileRotatePeriod().floorMillis(timestamp);
    }
    
    /**
     * Builds a filename for the given timestamp using the formatter determined
     * by the configured period.  For example, if the period is "every 5 minutes"
     * then the timestamp would use a format for the datetime portion of the filename
     * in the format YYYYMMDD-HHmm.  This method builds a filename in the format:
     * name.datetime[.suffix]
     * @param timestamp
     * @return 
     */
    public String buildFileName(long timestamp) {
        StringBuilder fn = new StringBuilder(40);
        fn.append(configuration.getName());
        if (configuration.getFileRotatePeriod() != null && configuration.getFileRotatePeriod() != DateTimePeriod.NEVER) {
            fn.append('.');
            fn.append(configuration.getFileRotatePeriod().getFormatter().print(timestamp));
        }
        if (configuration.getFileSuffix() != null) {
            fn.append(configuration.getFileSuffix());
        }
        return fn.toString();
    }
    
    /**
     * Returns true if the timestamp falls in the next period.
     * @param timestamp The timestamp to check
     * @return True if the timestamp falls in the next period, otherwise false.
     */
    public boolean isReadyForRotate(long now) {
        return (now >= this.nextPeriodTimestamp);
    }
    
    /**
     * Calculates the number of milliseconds to the start of the next period.
     * If zero or a negative value is returned, the timestamp already is in the
     * next period.
     * @param now The timestamp to consider "now"
     * @return The number of milliseconds to the start of the next period.
     */
    public long calculateMillisTillNextPeriod(long now) {
        return (this.nextPeriodTimestamp - now);
    }
    
    public void setCurrentPeriod(long now) {
        this.currentPeriodTimestamp = this.calculatePeriodTimestamp(now);
        this.nextPeriodTimestamp = this.currentPeriodTimestamp + this.configuration.getFileRotatePeriod().getMillis();
    }
    
    public BinlogFileWriter createInitialBinlog(long now, BinlogFactory factory) throws IOException, BinlogException {
        setCurrentPeriod(now);
        String currentFileName = this.buildFileName(this.currentPeriodTimestamp);
        return createOrOpenBinlog(currentFileName, factory);
    }

    public BinlogFileWriter createOrOpenBinlog(final String fileName, final BinlogFactory factory) throws IOException, BinlogException {
        // create the file object for the new binlog
        File f = (this.configuration.getBinlogDirectory() == null ? new File(fileName) : new File(this.configuration.getBinlogDirectory(), fileName));

        // either create or append to binlog (fix any possible corruption)
        // NOTE: if the file already exists and is large, this could take a long time to finish
        // FIXME: some sort of progress indicator would be interesting to consider
        try {
            // default the factory we'll use to create the file to the parameter
            BinlogFactory tempFactory = factory;
            // if "setFileNameAsHeaderName" is true, create a wrapper that will set the header name!
            if (configuration.isSetFileNameAsHeaderNameEnabled()) {
                tempFactory = new BinlogFactory() {
                    @Override
                    public void createBinlogFileHeader(WritableBinlogFileHeader header) {
                        // set the header name to match the filename
                        header.setName(fileName);
                        // delegate to original factory
                        factory.createBinlogFileHeader(header);
                    }

                    @Override
                    public void verifyBinlogFileHeader(BinlogFileHeader header) throws BinlogException {
                        // delegate to original factory
                        factory.verifyBinlogFileHeader(header);
                    }
                };
            }
            
            BinlogFileWriter binlog = BinlogFile.append(f, tempFactory, true);

            // if the binlog append was successful, try to write an info event
            // record "open" event to new binlog (nice side effect is guaranteeing we can write to it as well!)
            binlog.writeEvent(EventType.BINLOGGER_OPENED);
            binlog.flush();

            return binlog;
        } catch (IOException e) {
            throw new IOException("IO error with binlog file [" + f.getAbsolutePath() + "]", e);
        } catch (BinlogException e) {
            throw new BinlogException("Unable to open binlog file [" + f.getAbsolutePath() + "]", e);
        }
    }
    
    public long rotate(long now, BinloggerImpl binlogger) {
        // is a rotate required?
        if (!isReadyForRotate(now)) {
            // not ready yet for a rotate, returning how many milliseconds to try again in
            return calculateMillisTillNextPeriod(now);
        }
        
        // a rotate is required, set the new "current" period
        this.setCurrentPeriod(now);
        
        // create the next binlog filename to create based on the new current timestamp
        String nextFileName = this.buildFileName(this.currentPeriodTimestamp);
        
        try {
            // do the rotation and catch any errors that may arise
            doRotate(binlogger.getActiveBinlogWriter(), nextFileName, binlogger.getFactory(), binlogger);
        } catch (Throwable t) {
            logger.error("Severe error during rotate to a new binlog [" + nextFileName + "]; skipping and will retry on next period", t);
        }
        
        // even if this rotate failed this time, we will always want to try
        // again at the time of the next period
        return this.calculateMillisTillNextPeriod(now);
    }
    
    public void doRotate(BinlogFileWriter currentBinlog, String nextFileName, BinlogFactory factory, BinlogActivator activator) throws IOException, BinlogException, InterruptedException {
        logger.info("Binlog rotate to [{}] from [{}]", nextFileName, currentBinlog.getFile().getName());
        
        BinlogFileWriter nextBinlog = null;
        try {
            nextBinlog = createOrOpenBinlog(nextFileName, factory);
        } catch (Throwable t) {
            logger.error("Unable to create next binlog [" + nextFileName + "] for rotate; skipping rotate", t);
            // FIXME: throw error OR return something like false?
            return;
        }
        
        // write rotate event in "next" binlog of current binlog filename
        try {
            String currentFileName = currentBinlog.getFile().getName();
            Map<String,String> optionalParameters = new TreeMap<String,String>();
            optionalParameters.put("file", currentFileName);
            nextBinlog.writeEvent(EventType.BINLOGGER_ROTATE_PREV, null, null, optionalParameters);
            nextBinlog.flush();
        } catch (Throwable t) {
            logger.error("Unable to write rotate event in next binlog [" + nextFileName + "]; skipping rotate", t);
            // FIXME: throw error OR return something like false?
            return;
        }
        
        // write rotate event in "current" binlog of next binlog filename
        try {
            Map<String,String> optionalParameters = new TreeMap<String,String>();
            optionalParameters.put("file", nextFileName);
            currentBinlog.writeEvent(EventType.BINLOGGER_ROTATE_NEXT, null, null, optionalParameters);
            currentBinlog.flush();
        } catch (Throwable t) {
            logger.error("Unable to write rotate event in current binlog [" + currentBinlog.getFile().getName() + "]; still moving forward with rotate", t);
        }
        
        // at this point, the next binlog is ready to be activated!
        // NOTE: this is atomic inside BinloggerImpl
        activator.setActiveBinlog(nextBinlog); 
    }
    
}
