package com.mfizz.binlog.demo;

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

import com.mfizz.binlog.BaseBinlogFile;
import com.mfizz.binlog.Binlogger;
import com.mfizz.binlog.BinloggerConfiguration;
import com.mfizz.binlog.BinloggerFactory;
import com.mfizz.binlog.type.DateTimePeriod;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public final class BenchmarkMain {
    private static final Logger logger = LoggerFactory.getLogger(BenchmarkMain.class);
    
    public static void main(String[] args) throws Exception {
        // benchmark configuration shared with all logger threads
        Configuration config = new Configuration();
        config.targetRecordCount = 70000000;
        config.threadCount = 400;
        config.recordCreateDateTimeEnabled = false;
        config.recordDataSize = 500;
        config.recordUserDefinedType = new Integer(1);
        
        // create binlogger instance to use for benchmark
        SampleBinlogFactory factory = new SampleBinlogFactory();
        
        BinloggerConfiguration configuration = new BinloggerConfiguration();
        configuration.setBinlogDirectory(new File("target"));
        configuration.setName("benchmark");
        configuration.setFileRotatePeriod(DateTimePeriod.HALF_HOUR);
        configuration.setFileSuffix(".binlog");
        configuration.setMaxClosedBinlogSize(3);
        
        Binlogger binlogger = BinloggerFactory.create(configuration, factory);
        
        // create all logger threads
        ArrayList<WriterThread> loggerThreads = new ArrayList<WriterThread>();
        for (int i = 0; i < config.threadCount; i++) {
            WriterThread t = new WriterThread(binlogger, i, config);
            t.start();
            loggerThreads.add(t);
        }
        
        LogStatsThread logStatsThread = new LogStatsThread(config);
        logStatsThread.start();
        
        // start benchmark (as easy as this!)
        System.console().readLine("Press any key to start benchmark with " + config.threadCount + " threads...");
        
        long startTime = System.currentTimeMillis();
        config.startLatch.countDown();
        
        // wait for all threads to finish
        for (WriterThread t : loggerThreads) {
            t.join();
        }
        
        config.isDone.set(true);
        logStatsThread.interrupt();
        binlogger.stop();
        
        long stopTime = System.currentTimeMillis();
        
        //Thread.yield();
        //Thread.sleep(1000);
        
        logger.info("");
        logger.info("Pending Close Binlogs");
        logger.info("-----------------------------");
        for (BaseBinlogFile f : binlogger.getPendingCloseBinlogs()) {
            logger.info("{} [records={} event_records={} data_records={}]", new Object[] { f.getFile().getName(), f.getRecordCount(), f.getEventRecordCount(), f.getDataRecordCount() });
        }
        
        logger.info("");
        logger.info("Closed Binlogs");
        logger.info("-----------------------------");
        for (BaseBinlogFile f : binlogger.getClosedBinlogs()) {
            logger.info("{} [records={} event_records={} data_records={}]", new Object[] { f.getFile().getName(), f.getRecordCount(), f.getEventRecordCount(), f.getDataRecordCount() });
        }
        
        logger.info("");
        logger.info("Benchmark Summary");
        logger.info("-----------------------------");
        logStats(config, startTime, stopTime, config.actualRecordCount.get(), config.actualRecordCount.get());
    }
    
    static public class Configuration {
        public int recordDataSize;
        public boolean recordCreateDateTimeEnabled = false;
        public Integer recordUserDefinedType;
        public int targetRecordCount;
        public int threadCount;
        public AtomicInteger attemptedRecordCount = new AtomicInteger();
        public CountDownLatch startLatch = new CountDownLatch(1);
        public AtomicInteger actualRecordCount = new AtomicInteger();
        public AtomicBoolean isDone = new AtomicBoolean();
        public long logStatsIntervalMillis = 5000;
    }
    
    static public void logStats(Configuration config, long startTime, long stopTime, int actualRecordCount, int deltaRecordCount) {
        double percentDone = ((double)actualRecordCount/(double)config.targetRecordCount)*100;
        logger.info("record_count: {} out of {} ({}% done)", new Object[] { actualRecordCount, config.targetRecordCount, percentDone });
        logger.info("record_data_size: {}", config.recordDataSize);
        logger.info("thread_count: {}", config.threadCount);
        logger.info("time: {} ms", (stopTime - startTime));
        double recordsPerSec = ((double)deltaRecordCount)/((double)(stopTime-startTime)/(double)1000);
        logger.info("records_per_sec: {}", recordsPerSec);
    }
    
    static public class LogStatsThread extends Thread {
        
        private Configuration config;
        
        public LogStatsThread(Configuration config) {
            this.config = config;
        }
        
        @Override
        public void run() {
            try {
                config.startLatch.await();

                long lastTime = System.currentTimeMillis();
                int lastRecordCount = config.actualRecordCount.get();

                while (!config.isDone.get()) {
                    Thread.sleep(config.logStatsIntervalMillis);
                    long nowTime = System.currentTimeMillis();
                    int nowRecordCount = config.actualRecordCount.get();
                    logger.info("");
                    logger.info("------------------------");
                    logger.info("interval: {} ms", (nowTime-lastTime));
                    logStats(config, lastTime, nowTime, nowRecordCount, (nowRecordCount-lastRecordCount));
                    logger.info("------------------------");
                    lastTime = nowTime;
                    lastRecordCount = nowRecordCount;
                }
            
            } catch (InterruptedException e) {
                return;
            }
            
        }
        
    }
    
    static public class WriterThread extends Thread {
        
        private Binlogger binlogger;
        private Integer threadId;
        private Configuration config;
        private int recordCount;
        
        public WriterThread(Binlogger binlogger, int threadId, Configuration config) {
            this.binlogger = binlogger;
            this.threadId = threadId;
            this.config = config;
        }
        
        @Override
        public void run() {
            try {
                config.startLatch.await();
            } catch (InterruptedException e) {
                logger.error("Thread with id " + threadId + " was interrupted", e);
                return;
            }
            
            logger.info("Thread with id " + threadId + " starting...");
            
            while (config.attemptedRecordCount.getAndIncrement() < config.targetRecordCount) {
                try {
                    //this.binlogger.write(data, this.threadId);
                    this.binlogger.write(new byte[config.recordDataSize], this.threadId);
                    this.config.actualRecordCount.incrementAndGet();
                    this.recordCount++;
                } catch (Exception e) {
                    logger.error("Thread with id " + threadId + " failed to write record", e);
                }
            }
            
            logger.info("Thread with id " + threadId + " finished; record_count=" + recordCount);
        }
        
    }
    
    
    
}