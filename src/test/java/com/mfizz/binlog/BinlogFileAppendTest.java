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

import com.cloudhopper.commons.util.HexUtil;
import com.mfizz.binlog.impl.BinlogRecordHeaderImpl;
import java.io.File;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfizz
 */
public class BinlogFileAppendTest {
    static private final Logger logger = LoggerFactory.getLogger(BinlogFileAppendTest.class);
    
    @Test
    public void appendToBinlogWithNoRecords() throws Exception {
        // bare minimum binlog file
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000029090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652801".toUpperCase();
        
        // a record with a header!
        //hex += "131108D285D8CC04110016134B300100001809AA";
        
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        
        BinlogFileWriter binlog = BinlogFile.append(f, new DefaultTestBinlogFactory(), true);
        
        Assert.assertEquals(0, binlog.getRecordCount());
        
        // create record to append
        WritableBinlogRecordHeader recordHeader = new BinlogRecordHeaderImpl();
        recordHeader.setId(1234567890);
        recordHeader.setUserDefinedType(9);
        recordHeader.setCreateDateTime(new DateTime(2011, 6, 1, 12, 0, 0, 0, DateTimeZone.UTC));
        
        BinlogRecordFileInfo fileInfo = binlog.write(recordHeader, HexUtil.toByteArray("AA")); 
        // the position of the record in this file
        Assert.assertEquals(63L, fileInfo.getPosition());
        Assert.assertEquals(20L, fileInfo.getLength());
        Assert.assertEquals(65L, fileInfo.getHeaderPosition());
        Assert.assertEquals(17L, fileInfo.getHeaderLength());
        Assert.assertEquals(82L, fileInfo.getDataPosition());
        Assert.assertEquals(1L, fileInfo.getDataLength());
        
        Assert.assertEquals(1, binlog.getRecordCount());
        
        // close this binlog and check if its data matches what we'd expect
        binlog.close();
        
        // append the record we wrote
        hex += "131108D285D8CC04110016134B300100001809AA";
        
        String actualHex = HexUtil.toHexString(FileUtil.readFileFully(f));
        String expectedHex = hex;
        
        Assert.assertEquals(expectedHex, actualHex);
    }
    
    @Test
    public void appendToBinlogWith1ExistingRecord() throws Exception {
        // bare minimum binlog file
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000029090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652801".toUpperCase();
        // a record with a header
        hex += "131108D285D8CC04110016134B300100001809AA";
        
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        
        BinlogFileWriter binlog = BinlogFile.append(f, new DefaultTestBinlogFactory(), true);
        
        Assert.assertFalse(binlog.isFinalSizeKnown());
        Assert.assertEquals(1, binlog.getRecordCount());
        Assert.assertEquals(17L, binlog.getRecordHeaderSize());
        Assert.assertEquals(1L, binlog.getRecordDataSize());
        Assert.assertEquals(83L, binlog.getPosition());
        Assert.assertEquals(83L, binlog.getSize());
        
        // create another record to append
        WritableBinlogRecordHeader recordHeader = new BinlogRecordHeaderImpl();
        recordHeader.setId(1234567890);
        recordHeader.setUserDefinedType(9);
        recordHeader.setCreateDateTime(new DateTime(2011, 6, 1, 12, 0, 0, 0, DateTimeZone.UTC));
        
        BinlogRecordFileInfo fileInfo = binlog.write(recordHeader, HexUtil.toByteArray("AA")); 
        // the position of the record in this file
        Assert.assertEquals(83L, fileInfo.getPosition());
        Assert.assertEquals(20L, fileInfo.getLength());
        Assert.assertEquals(85L, fileInfo.getHeaderPosition());
        Assert.assertEquals(17L, fileInfo.getHeaderLength());
        Assert.assertEquals(102L, fileInfo.getDataPosition());
        Assert.assertEquals(1L, fileInfo.getDataLength());
        
        Assert.assertEquals(2, binlog.getRecordCount());
        Assert.assertEquals(34L, binlog.getRecordHeaderSize());
        Assert.assertEquals(2L, binlog.getRecordDataSize());
        Assert.assertFalse(binlog.isFinalSizeKnown());
        
        // close this binlog and check if its data matches what we'd expect
        binlog.close();
        
        // append the record we wrote
        hex += "131108D285D8CC04110016134B300100001809AA";
        
        String actualHex = HexUtil.toHexString(FileUtil.readFileFully(f));
        String expectedHex = hex;
        
        Assert.assertEquals(expectedHex, actualHex);
        Assert.assertTrue(binlog.isFinalSizeKnown());
    }
    
    @Test
    public void appendToBinlogWithExistingCorruptedRecord() throws Exception {
        // bare minimum binlog file
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000029090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652801".toUpperCase();
        // a record with a header (but missing data of AA at end)
        hex += "131108D285D8CC04110016134B300100001809";
        
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        
        BinlogFileWriter binlog = BinlogFile.append(f, new DefaultTestBinlogFactory(), true);
        
        // the final corrupted record should have been truncated
        Assert.assertEquals(0, binlog.getRecordCount());
        Assert.assertEquals(0L, binlog.getRecordHeaderSize());
        Assert.assertEquals(0L, binlog.getRecordDataSize());
        Assert.assertEquals(63L, binlog.getSize());
        Assert.assertEquals(63L, binlog.getPosition());

        // create another record to append
        WritableBinlogRecordHeader recordHeader = new BinlogRecordHeaderImpl();
        recordHeader.setId(1234567890);
        recordHeader.setUserDefinedType(9);
        recordHeader.setCreateDateTime(new DateTime(2011, 6, 1, 12, 0, 0, 0, DateTimeZone.UTC));
        
        BinlogRecordFileInfo fileInfo = binlog.write(recordHeader, HexUtil.toByteArray("AA")); 
        // the position of the record in this file
        Assert.assertEquals(63L, fileInfo.getPosition());
        Assert.assertEquals(20L, fileInfo.getLength());
        Assert.assertEquals(65L, fileInfo.getHeaderPosition());
        Assert.assertEquals(17L, fileInfo.getHeaderLength());
        Assert.assertEquals(82L, fileInfo.getDataPosition());
        Assert.assertEquals(1L, fileInfo.getDataLength());
        
        Assert.assertEquals(1, binlog.getRecordCount());
        Assert.assertEquals(17L, binlog.getRecordHeaderSize());
        Assert.assertEquals(1L, binlog.getRecordDataSize());
        
        // close this binlog and check if its data matches what we'd expect
        binlog.close();
        
        // append the final correct byte onto what we wanted
        hex += "AA";
        
        String actualHex = HexUtil.toHexString(FileUtil.readFileFully(f));
        String expectedHex = hex;
        
        Assert.assertEquals(expectedHex, actualHex);
    }
    
}
