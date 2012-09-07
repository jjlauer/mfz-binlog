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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public final class BinlogMain {
    private static final Logger logger = LoggerFactory.getLogger(BinlogMain.class);
    
    public static void main(String[] args) throws Exception {
        // command line should be file to work with
        if (args.length != 1) {
            throw new Exception("One command line argument is file to work with");
        }
        
        SampleBinlogFactory factory = new SampleBinlogFactory();
        
        BinloggerConfiguration configuration = new BinloggerConfiguration();
        configuration.setBinlogDirectory(new File("target"));
        configuration.setName("sample");
        configuration.setFileSuffix(".binlog");
        configuration.setFileRotatePeriod(DateTimePeriod.MINUTE);
        
        Binlogger binlogger = BinloggerFactory.create(configuration, factory);
        
        System.console().readLine("Press any key to start writing...");
        
        int flushes = 0;
        for (int i = 0; i < 10*1200*1; i++) {
            //byte[] testBytes = HexUtil.toByteArray("010203040506070809101112");
            // 300 bytes of text
            //byte[] testBytes = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer nec augue ac erat tempus elementum. Praesent scelerisque, quam in semper pretium, massa est tempus tellus, in rutrum elit mauris ut ligula. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis cras amet.".getBytes();
            // 600 bytes of text
            byte[] testBytes = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi faucibus iaculis ligula, ac iaculis purus malesuada eu. Nunc luctus lectus non urna condimentum lacinia. Ut vitae mauris mi. Nulla suscipit sapien vitae magna semper molestie. Nam quis blandit ipsum. Suspendisse non nisi nec quam malesuada iaculis. Mauris laoreet porttitor erat sit amet congue. Quisque rhoncus, velit aliquet cursus molestie, diam mauris tempus nulla, sit amet tincidunt ipsum mi in turpis. Proin risus sem, varius id faucibus sit amet, commodo gravida odio. Proin lobortis tincidunt erat, in molestie urna turpis duis.".getBytes();
            //binlog.write(recordHeader, testBytes);
            
            binlogger.write(testBytes);
            binlogger.flush();
           
            Thread.sleep(100);
            
            //if (i % 21 == 0) {
            //  flushes++;
            //  binlog.flush();
            //}
        }
        
        System.console().readLine("Press any key to stop binlogger...");
        
        binlogger.stop();
        
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
        
        binlogger = null;
        System.console().readLine("Press any key to exit...");
    }
    
}