package com.mfizz.binlog;

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

import com.mfizz.binlog.impl.BinloggerImpl;
import com.mfizz.binlog.type.DateTimePeriod;
import com.mfizz.binlog.type.EventType;
import java.io.File;
import java.util.Iterator;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfizz
 */
public class BinloggerTest {
    static private final Logger logger = LoggerFactory.getLogger(BinloggerTest.class);
    
    public BinloggerConfiguration createConfiguration() throws Exception {
        File f = FileUtil.createTempFile();
        BinloggerConfiguration configuration = new BinloggerConfiguration();
        configuration.setBinlogDirectory(f.getParentFile());
        configuration.setName(f.getName());
        return configuration;
    }
    
    @Test
    public void rotate() throws Exception {
        DefaultTestBinlogFactory factory = new DefaultTestBinlogFactory();
        
        BinloggerConfiguration configuration = createConfiguration();
        configuration.setFileRotatePeriod(DateTimePeriod.NEVER);
        
        BinloggerImpl binlogger = (BinloggerImpl)BinloggerFactory.create(configuration, factory);
        
        String firstBinlogFileName = binlogger.getActiveBinlog().getFile().getName();
        BaseBinlogFile firstBinlogFile = binlogger.getActiveBinlog();
        
        // verify the "active" file was named correctly
        byte[] bytes0 = FileUtil.readFileFully(binlogger.getActiveFileWriter().getFile());
        Assert.assertArrayEquals(firstBinlogFileName.getBytes("UTF-8"), bytes0);
        
        Assert.assertEquals(0, binlogger.getClosedBinlogs().size());
        Assert.assertEquals(0, binlogger.getPendingCloseBinlogs().size());
        // first binlog should have 2 events and 0 data records
        Assert.assertEquals(2, firstBinlogFile.getEventRecordCount());
        Assert.assertEquals(0, firstBinlogFile.getDataRecordCount());
        
        String recordData0 = "This is the first record to insert";
        binlogger.write(recordData0.getBytes());
        
        // rotate the files
        String nextBinlogFileName = firstBinlogFileName + ".1";
        binlogger.getRotater().doRotate((BinlogFileWriter)binlogger.getActiveBinlog(), nextBinlogFileName, factory, binlogger);
        BaseBinlogFile nextBinlogFile = binlogger.getActiveBinlog();
        
        // verify the "active" file was named correctly after rotater
        bytes0 = FileUtil.readFileFully(binlogger.getActiveFileWriter().getFile());
        Assert.assertArrayEquals(nextBinlogFileName.getBytes("UTF-8"), bytes0);
        
        // verify the original binlog file has new events
        Assert.assertEquals(1, binlogger.getClosedBinlogs().size());
        Assert.assertEquals(0, binlogger.getPendingCloseBinlogs().size());
        Assert.assertEquals(4, firstBinlogFile.getEventRecordCount());
        Assert.assertEquals(1, firstBinlogFile.getDataRecordCount());
        
        Assert.assertEquals(nextBinlogFileName, nextBinlogFile.getFile().getName());
        Assert.assertEquals(2, nextBinlogFile.getEventRecordCount());
        Assert.assertEquals(0, nextBinlogFile.getDataRecordCount());
        
        // shutdown the binlogger
        binlogger.stop();
        
        Assert.assertEquals(4, nextBinlogFile.getEventRecordCount());
        Assert.assertEquals(0, nextBinlogFile.getDataRecordCount());
        

        // read the binlogs back in and verify the exact events
        BinlogFileReader binlog0 = BinlogFile.open(firstBinlogFile.getFile(), factory);
        
        // 1st record is an opened event
        BinlogRecord record0 = binlog0.read();
        Assert.assertEquals(EventType.BINLOGGER_OPENED, record0.getEvent().getType());
        
        // 2nd record is a started event
        BinlogRecord record1 = binlog0.read();
        Assert.assertEquals(EventType.BINLOGGER_STARTED, record1.getEvent().getType());
        
        // 3rd record is data
        BinlogRecord record2 = binlog0.read();
        Assert.assertNull(record2.getEvent());
        Assert.assertEquals(recordData0, new String(record2.getData())); 
        
        // 4th record is rotate event
        BinlogRecord record3 = binlog0.read();
        Assert.assertEquals(EventType.BINLOGGER_ROTATE_NEXT, record3.getEvent().getType());
        // file parameter should be set to what we rotated to
        Assert.assertEquals(nextBinlogFileName, record3.getHeader().getOptionalParameter("file"));
        
        // 5th record is a closed event
        BinlogRecord record4 = binlog0.read();
        Assert.assertEquals(EventType.BINLOGGER_CLOSED, record4.getEvent().getType());
        
        binlog0.close();
        
        
        
        BinlogFileReader binlog1 = BinlogFile.open(nextBinlogFile.getFile(), factory);
        
        // 1st record is an opened event
        BinlogRecord record10 = binlog1.read();
        Assert.assertEquals(EventType.BINLOGGER_OPENED, record10.getEvent().getType());
        
        // 2nd record is a started event
        BinlogRecord record11 = binlog1.read();
        Assert.assertEquals(EventType.BINLOGGER_ROTATE_PREV, record11.getEvent().getType());
        // file parameter should be set to what we rotated to
        Assert.assertEquals(firstBinlogFileName, record11.getHeader().getOptionalParameter("file"));
        
        // 3rd record is rotate event
        BinlogRecord record12 = binlog1.read();
        Assert.assertEquals(EventType.BINLOGGER_STOPPED, record12.getEvent().getType());
        
        // 5th record is a closed event
        BinlogRecord record13 = binlog1.read();
        Assert.assertEquals(EventType.BINLOGGER_CLOSED, record13.getEvent().getType());
        
        binlog1.close();
    }
    
    
    @Test
    public void maxClosedBinlogSize() throws Exception {
        DefaultTestBinlogFactory factory = new DefaultTestBinlogFactory();
        
        BinloggerConfiguration configuration = createConfiguration();
        configuration.setFileRotatePeriod(DateTimePeriod.NEVER);
        configuration.setMaxClosedBinlogSize(2);
        
        BinloggerImpl binlogger = (BinloggerImpl)BinloggerFactory.create(configuration, factory);
        
        BaseBinlogFile binlogFile0 = binlogger.getActiveBinlog();
        String binlogFileName0 = binlogFile0.getFile().getName();
        
        // rotate the files
        String binlogFileName1 = binlogFile0.getFile().getName() + ".1";
        binlogger.getRotater().doRotate((BinlogFileWriter)binlogger.getActiveBinlog(), binlogFileName1, factory, binlogger);
        BaseBinlogFile binlogFile1 = binlogger.getActiveBinlog();
        
        Assert.assertEquals(1, binlogger.getClosedBinlogs().size());
        
        // rotate the files
        String binlogFileName2 = binlogFile0.getFile().getName() + ".2";
        binlogger.getRotater().doRotate((BinlogFileWriter)binlogger.getActiveBinlog(), binlogFileName2, factory, binlogger);
        BaseBinlogFile binlogFile2 = binlogger.getActiveBinlog();
        
        Assert.assertEquals(2, binlogger.getClosedBinlogs().size());
        
        // rotate the files
        String binlogFileName3 = binlogFile0.getFile().getName() + ".3";
        binlogger.getRotater().doRotate((BinlogFileWriter)binlogger.getActiveBinlog(), binlogFileName3, factory, binlogger);
        BaseBinlogFile binlogFile3 = binlogger.getActiveBinlog();
        
        // max is 2, so there should only be 2
        Assert.assertEquals(2, binlogger.getClosedBinlogs().size());
        
        // confirm the 2 files are the last 2
        Iterator<BaseBinlogFile> it = binlogger.getClosedBinlogs().iterator();
        Assert.assertEquals(binlogFileName1, it.next().getFile().getName());
        Assert.assertEquals(binlogFileName2, it.next().getFile().getName()); 
        
        // shutdown the binlogger
        binlogger.stop();
        
        // confirm the 2 files are the last 2
        it = binlogger.getClosedBinlogs().iterator();
        Assert.assertEquals(binlogFileName2, it.next().getFile().getName());
        Assert.assertEquals(binlogFileName3, it.next().getFile().getName()); 
    }
}
