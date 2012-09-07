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
import com.mfizz.binlog.type.EventType;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
public class BinlogFileWriterTest {
    static private final Logger logger = LoggerFactory.getLogger(BinlogFileWriterTest.class);
    
    @Test
    public void createFileAlreadyExists() throws Exception {
        File f = FileUtil.createTempFile();
        
        // create first should work
        BinlogFileWriter binlog = BinlogFile.create(f, new DefaultTestBinlogFactory());
        
        // second one should fail
        try {
            BinlogFileWriter binlog2 = BinlogFile.create(f, new DefaultTestBinlogFactory());
            Assert.fail();
        } catch (IOException e) {
            // correct behavior
        } finally {
            f.delete();
        }
    }
    
    @Test
    public void createFileNoRecords() throws Exception {
        File f = FileUtil.createTempFile();
        
        // create as empty a binlog file as possible
        BinlogFileWriter binlog = BinlogFile.create(f, new EmptyTestBinlogFactory());
        binlog.close();
        
        Assert.assertEquals(0, binlog.getRecordCount());
        Assert.assertEquals(37, binlog.getPosition());
        Assert.assertEquals(37, binlog.getSize());
        
        String actualHex = HexUtil.toHexString(FileUtil.readFileFully(f));
        String expectedHex = "4D46495A5A2E434F4D2D42494E4C4F4710010000000F09804B5F493001000012001A002802";
        
        Assert.assertEquals(expectedHex, actualHex);        
    }
    
    @Test
    public void createFileEmptyRecordWithNonNullObjects() throws Exception {
        File f = FileUtil.createTempFile();
        
        // create as empty a binlog file as possible
        BinlogFileWriter binlog = BinlogFile.create(f, new EmptyTestBinlogFactory());
        
        Assert.assertEquals(37L, binlog.getPosition());
        Assert.assertEquals(37L, binlog.getSize());
        Assert.assertFalse(binlog.isFinalSizeKnown());
        
        // write an empty record
        BinlogRecordFileInfo fileInfo = binlog.write(new BinlogRecordHeaderImpl(), new byte[0]);
        
        // after writing record, both position and size should be incremented!
        Assert.assertEquals(41L, binlog.getPosition());
        Assert.assertEquals(41L, binlog.getSize());
        // the position of the record in this file
        Assert.assertEquals(37L, fileInfo.getPosition());
        Assert.assertEquals(4L, fileInfo.getLength());
        Assert.assertEquals(41L, fileInfo.getHeaderPosition());
        Assert.assertEquals(0L, fileInfo.getHeaderLength());
        Assert.assertEquals(41L, fileInfo.getDataPosition());
        Assert.assertEquals(0L, fileInfo.getDataLength());
        Assert.assertFalse(binlog.isFinalSizeKnown());
        Assert.assertEquals(1, binlog.getRecordCount());
        
        binlog.close();
        
        String actualHex = HexUtil.toHexString(FileUtil.readFileFully(f));
        String expectedHex = "4D46495A5A2E434F4D2D42494E4C4F4710010000000F09804B5F493001000012001A00280200020000";
        
        Assert.assertEquals(expectedHex, actualHex);
        Assert.assertTrue(binlog.isFinalSizeKnown());   // final size is now known
    }
    
    @Test
    public void createFileEmptyRecordWithNullObjects() throws Exception {
        File f = FileUtil.createTempFile();
        
        // create as empty a binlog file as possible
        BinlogFileWriter binlog = BinlogFile.create(f, new EmptyTestBinlogFactory());
        
        // write an empty record
        BinlogRecordFileInfo fileInfo = binlog.write((BinlogRecordHeader)null, (byte[])null);
        
        // after writing record, both position and size should be incremented!
        Assert.assertEquals(41L, binlog.getPosition());
        Assert.assertEquals(41L, binlog.getSize());
        // the position of the record in this file
        Assert.assertEquals(37L, fileInfo.getPosition());
        Assert.assertEquals(4L, fileInfo.getLength());
        Assert.assertEquals(41L, fileInfo.getHeaderPosition());
        Assert.assertEquals(0L, fileInfo.getHeaderLength());
        Assert.assertEquals(41L, fileInfo.getDataPosition());
        Assert.assertEquals(0L, fileInfo.getDataLength());
        
        Assert.assertEquals(1, binlog.getRecordCount());
        
        binlog.close();
        
        String actualHex = HexUtil.toHexString(FileUtil.readFileFully(f));
        String expectedHex = "4D46495A5A2E434F4D2D42494E4C4F4710010000000F09804B5F493001000012001A00280200020000";
        
        Assert.assertEquals(expectedHex, actualHex);        
    }
    
    @Test
    public void createFileRecordWithNullHeaderBut1ByteData() throws Exception {
        File f = FileUtil.createTempFile();
        
        // create as empty a binlog file as possible
        BinlogFileWriter binlog = BinlogFile.create(f, new EmptyTestBinlogFactory());
        
        // write an empty record
        BinlogRecordFileInfo fileInfo = binlog.write(null, HexUtil.toByteArray("FF"));
        
        // after writing record, both position and size should be incremented!
        Assert.assertEquals(42L, binlog.getPosition());
        Assert.assertEquals(42L, binlog.getSize());
        // the position of the record in this file
        Assert.assertEquals(37L, fileInfo.getPosition());
        Assert.assertEquals(5L, fileInfo.getLength());
        Assert.assertEquals(41L, fileInfo.getHeaderPosition());
        Assert.assertEquals(0L, fileInfo.getHeaderLength());
        Assert.assertEquals(41L, fileInfo.getDataPosition());
        Assert.assertEquals(1L, fileInfo.getDataLength());
        
        Assert.assertEquals(1, binlog.getRecordCount());
        
        binlog.close();
        
        String actualHex = HexUtil.toHexString(FileUtil.readFileFully(f));
        String expectedHex = "4D46495A5A2E434F4D2D42494E4C4F4710010000000F09804B5F493001000012001A00280200030000FF";
        
        Assert.assertEquals(expectedHex, actualHex);        
    }
    
    @Test
    public void createFileRecordWithHeaderAndData() throws Exception {
        File f = FileUtil.createTempFile();
        
        // create as empty a binlog file as possible
        BinlogFileWriter binlog = BinlogFile.create(f, new EmptyTestBinlogFactory());
        
        // create a record header
        WritableBinlogRecordHeader recordHeader = new BinlogRecordHeaderImpl();
        recordHeader.setId(1234567890);
        recordHeader.setCreateDateTime(new DateTime(2011, 6, 1, 12, 0, 0, 0, DateTimeZone.UTC));
        recordHeader.setUserDefinedType(9);
        
        // write a record
        BinlogRecordFileInfo fileInfo = binlog.write(recordHeader, HexUtil.toByteArray("FF"));
        
        // after writing record, both position and size should be incremented!
        Assert.assertEquals(59L, binlog.getPosition());
        Assert.assertEquals(59L, binlog.getSize());
        // the position of the record in this file
        Assert.assertEquals(37L, fileInfo.getPosition());
        Assert.assertEquals(22L, fileInfo.getLength());
        Assert.assertEquals(41L, fileInfo.getHeaderPosition());
        Assert.assertEquals(17L, fileInfo.getHeaderLength());
        Assert.assertEquals(58L, fileInfo.getDataPosition());
        Assert.assertEquals(1L, fileInfo.getDataLength());
        
        Assert.assertEquals(1, binlog.getRecordCount());
        
        binlog.close();
        
        String actualHex = HexUtil.toHexString(FileUtil.readFileFully(f));
        String expectedHex = "4D46495A5A2E434F4D2D42494E4C4F4710010000000F09804B5F493001000012001A0028020014001108D285D8CC04110016134B300100001809FF";
        
        Assert.assertEquals(expectedHex, actualHex);        
    }
    
    @Test
    public void createFileRecordWith2RecordsWithHeaderAndData() throws Exception {
        File f = FileUtil.createTempFile();
        
        // create as empty a binlog file as possible
        BinlogFileWriter binlog = BinlogFile.create(f, new EmptyTestBinlogFactory());
        
        // create a record header
        WritableBinlogRecordHeader recordHeader = new BinlogRecordHeaderImpl();
        recordHeader.setId(1234567890);
        recordHeader.setCreateDateTime(new DateTime(2011, 6, 1, 12, 0, 0, 0, DateTimeZone.UTC));
        recordHeader.setUserDefinedType(9);
        
        // write record #1
        BinlogRecordFileInfo fileInfo = binlog.write(recordHeader, HexUtil.toByteArray("FF"));
        
        // after writing record, both position and size should be incremented!
        Assert.assertEquals(59L, binlog.getPosition());
        Assert.assertEquals(59L, binlog.getSize());
        // the position of the record in this file
        Assert.assertEquals(37L, fileInfo.getPosition());
        Assert.assertEquals(22L, fileInfo.getLength());
        Assert.assertEquals(41L, fileInfo.getHeaderPosition());
        Assert.assertEquals(17L, fileInfo.getHeaderLength());
        Assert.assertEquals(58L, fileInfo.getDataPosition());
        Assert.assertEquals(1L, fileInfo.getDataLength());
        
        Assert.assertEquals(1, binlog.getRecordCount());
        
        // write record #2
        BinlogRecordFileInfo fileInfo2 = binlog.write(recordHeader, HexUtil.toByteArray("FF"));
        
        // after writing record, both position and size should be incremented!
        Assert.assertEquals(81L, binlog.getPosition());
        Assert.assertEquals(81L, binlog.getSize());
        // the position of the record in this file
        Assert.assertEquals(59L, fileInfo2.getPosition());
        Assert.assertEquals(22L, fileInfo2.getLength());
        Assert.assertEquals(63L, fileInfo2.getHeaderPosition());
        Assert.assertEquals(17L, fileInfo2.getHeaderLength());
        Assert.assertEquals(80L, fileInfo2.getDataPosition());
        Assert.assertEquals(1L, fileInfo2.getDataLength());
        
        Assert.assertEquals(2, binlog.getRecordCount());
        
        binlog.close();
        
        String actualHex = HexUtil.toHexString(FileUtil.readFileFully(f));
        String expectedHex = "4D46495A5A2E434F4D2D42494E4C4F4710010000000F09804B5F493001000012001A0028020014001108D285D8CC04110016134B300100001809FF0014001108D285D8CC04110016134B300100001809FF";
        
        Assert.assertEquals(expectedHex, actualHex);        
    }
    
    
    @Test
    public void createFileWriteAndRead100KRecords() throws Exception {
        File f = FileUtil.createTempFile();
        
        // create as empty a binlog file as possible
        BinlogFileWriter binlog0 = BinlogFile.create(f, new EmptyTestBinlogFactory());
        
        DateTime dt = new DateTime(2011, 6, 1, 12, 0, 0, 0, DateTimeZone.UTC);
        
        ArrayList<BinlogRecordFileInfo> fileInfos = new ArrayList<BinlogRecordFileInfo>();
        
        int records = 100000;
        
        // write 1K records
        for (int i = 0; i < records; i++) {
            // create a record header
            WritableBinlogRecordHeader recordHeader = new BinlogRecordHeaderImpl();
            recordHeader.setId(i);
            recordHeader.setCreateDateTime(dt);
            recordHeader.setUserDefinedType(1);
            recordHeader.setOptionalParameter("key1", "value1");
            
            String s = "This is record #" + i;
            byte[] data = s.getBytes("ISO-8859-1");
            
            BinlogRecordFileInfo fileInfo = binlog0.write(recordHeader, data);
            fileInfos.add(fileInfo);
        }
        
        // close the binlog and then start reading it
        binlog0.close();
        Assert.assertEquals(records, binlog0.getRecordCount());
        
        BinlogFileReader binlog1 = BinlogFile.open(f, new EmptyTestBinlogFactory());
        
        // read 1K records
        for (int i = 0; i < records; i++) {
            BinlogRecord record = binlog1.read(true, true);
            
            Assert.assertEquals(dt, record.getHeader().getCreateDateTime());
            Assert.assertEquals(new Integer(i), record.getHeader().getId());
            Assert.assertEquals(new Integer(1), record.getHeader().getUserDefinedType());
            Assert.assertEquals("value1", record.getHeader().getOptionalParameter("key1"));
            
            String s = "This is record #" + i;
            byte[] data = s.getBytes("ISO-8859-1");
            Assert.assertArrayEquals(data, record.getData());
            
            BinlogRecordFileInfo fileInfo1 = record.getFileInfo();
            BinlogRecordFileInfo fileInfo0 = fileInfos.get(i);
            
            Assert.assertEquals(fileInfo0.getPosition(), fileInfo1.getPosition());
            Assert.assertEquals(fileInfo0.getLength(), fileInfo1.getLength());
            Assert.assertEquals(fileInfo0.getHeaderPosition(), fileInfo1.getHeaderPosition());
            Assert.assertEquals(fileInfo0.getHeaderLength(), fileInfo1.getHeaderLength());
            Assert.assertEquals(fileInfo0.getDataPosition(), fileInfo1.getDataPosition());
            Assert.assertEquals(fileInfo0.getDataLength(), fileInfo1.getDataLength());
        }
        
        binlog1.close();
        Assert.assertEquals(records, binlog1.getRecordCount());
    }
    
    @Test
    public void writeRecordAfterBinlogClosedThrowsIOException() throws Exception {
        File f = FileUtil.createTempFile();
        
        BinlogFileWriter binlog = BinlogFile.create(f, new EmptyTestBinlogFactory());
        
        binlog.close();
        
        try {
            BinlogRecordFileInfo fileInfo = binlog.write((BinlogRecordHeader)null, (byte[])null);
            Assert.fail();
        } catch (IOException e) {
            // correct behavior
        }
    }
    
    @Test
    public void writeAndReadRecordWithMaxRecordLengthByteLengthOf2Bytes() throws Exception {
        File f = FileUtil.createTempFile();
        
        // create as empty a binlog file as possible
        BinlogFileWriter binlog = BinlogFile.create(f, new EmptyTestBinlogFactory());
        
        // 65533 data + 2 bytes for length
        byte[] data = new byte[65533];
        
        // write a record
        BinlogRecordFileInfo fileInfo = binlog.write(null, data);
        
        String actualHex = HexUtil.toHexString(FileUtil.readFileFully(f));
        StringBuilder expectedHexBuilder = new StringBuilder("4D46495A5A2E434F4D2D42494E4C4F4710010000000F09804B5F493001000012001A002802");
        
        expectedHexBuilder.append("FFFF0000");
        for (int i = 0; i < 65533; i++) {
            expectedHexBuilder.append("00");
        }
        
        Assert.assertEquals(1L, binlog.getRecordCount());
        Assert.assertEquals(0L, binlog.getRecordHeaderSize());
        Assert.assertEquals(65533L, binlog.getRecordDataSize());
        
        binlog.close();
        
        String expectedHex = expectedHexBuilder.toString();
        Assert.assertEquals(expectedHex, actualHex);
        
        
        // read back this large record
        BinlogFileReader binlog0 = BinlogFile.open(f, new EmptyTestBinlogFactory());
        BinlogRecord record = binlog0.read(true, true);
        Assert.assertEquals(HexUtil.toHexString(new byte[65533]), HexUtil.toHexString(record.getData()));
                
        binlog0.close();
        
        Assert.assertEquals(1L, binlog.getRecordCount());
        Assert.assertEquals(0L, binlog.getRecordHeaderSize());
        Assert.assertEquals(65533L, binlog.getRecordDataSize());
    }
    
    @Test
    public void writeTooLargeRecordLengthByteLengthOf2Bytes() throws Exception {
        File f = FileUtil.createTempFile();
        
        // create as empty a binlog file as possible
        BinlogFileWriter binlog = BinlogFile.create(f, new EmptyTestBinlogFactory());
        
        // 65533 data + 2 bytes for length
        byte[] data = new byte[65534];
        
        try {
            // this should fail to write since data is larger than record_length can hold!
            binlog.write(null, data);
            Assert.fail();
        } catch (BinlogPropertyException e) {
            // correct behavior
        }
        
        Assert.assertEquals(0, binlog.getRecordCount());
        Assert.assertEquals(0L, binlog.getRecordHeaderSize());
        Assert.assertEquals(0L, binlog.getRecordDataSize());
    }
    
    @Test
    public void writeRecordEvent() throws Exception {
        File f = FileUtil.createTempFile();
        
        DateTime actualTimestamp = new DateTime(2011, 6, 1, 5, 0, 0, 0, DateTimeZone.UTC);
        
        // create as empty a binlog file as possible
        BinlogFileWriter binlog = BinlogFile.create(f, new EmptyTestBinlogFactory());
        
        // write a record
        BinlogRecordFileInfo fileInfo = binlog.writeEvent(EventType.BINLOGGER_STARTED, actualTimestamp);
        
        binlog.close();
        
        String actualHex = HexUtil.toHexString(FileUtil.readFileFully(f));
        String expectedHex = "4D46495A5A2E434F4D2D42494E4C4F4710010000000F09804B5F493001000012001A002802" + "0020001E" + "1180909249300100002801321162696E6C6F676765722D73746172746564";
        
        logger.info("  actualHex: {}", actualHex);
        logger.info("expectedHex: {}", expectedHex);
        Assert.assertEquals(expectedHex, actualHex);
        
        // after writing record, both position and size should be incremented!
        Assert.assertEquals(71L, binlog.getPosition());
        Assert.assertEquals(71L, binlog.getSize());
        // the position of the record in this file
        Assert.assertEquals(37L, fileInfo.getPosition());
        Assert.assertEquals(34L, fileInfo.getLength());
        Assert.assertEquals(41L, fileInfo.getHeaderPosition());
        Assert.assertEquals(30L, fileInfo.getHeaderLength());
        Assert.assertEquals(71L, fileInfo.getDataPosition());
        Assert.assertEquals(0L, fileInfo.getDataLength());
        
        Assert.assertEquals(1, binlog.getRecordCount());
    }
    
    @Test
    public void writeAfterCloseThrowsException() throws Exception {
        File f = FileUtil.createTempFile();
        BinlogFileWriter binlog = BinlogFile.create(f, new EmptyTestBinlogFactory());
        
        Assert.assertTrue(binlog.isOpen());
        Assert.assertFalse(binlog.isClosed());
        
        binlog.close();
        
        Assert.assertFalse(binlog.isOpen());
        Assert.assertTrue(binlog.isClosed());
        
        try {
            BinlogRecordFileInfo fileInfo = binlog.write(new byte[0]);
            Assert.fail();
        } catch (IOException e) {
            // correct behavior
        }
    }
    
    @Test
    public void flushAfterCloseThrowsException() throws Exception {
        File f = FileUtil.createTempFile();
        BinlogFileWriter binlog = BinlogFile.create(f, new EmptyTestBinlogFactory());
        
        binlog.close();
        
        try {
            binlog.flush();
            Assert.fail();
        } catch (IOException e) {
            // correct behavior
        }
    }
}
