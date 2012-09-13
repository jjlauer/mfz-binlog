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

import com.mfizz.util.HexUtil;
import com.mfizz.binlog.impl.BinlogRecordHeaderImpl;
import com.mfizz.binlog.type.EventType;
import com.mfizz.binlog.type.TranscoderType;
import com.mfizz.util.CompressUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
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
public class BinlogFileReaderTest {
    static private final Logger logger = LoggerFactory.getLogger(BinlogFileReaderTest.class);
    
    @Test
    public void openFileNotExist() throws Exception {
        try {
            BinlogFileReader binlog = BinlogFile.open(new File("target", "doesnotexist.binlog"), null);
            Assert.fail();
        } catch (FileNotFoundException e) {
            // correct behavior
        }
    }
    
    @Test
    public void openEmptyFile() throws Exception {
        // create an empty file
        File f = FileUtil.createTempFile(new byte[0]); 
        try {
            BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());
            Assert.fail();
        } catch (BinlogCorruptionException e) {
            // correct behavior
        } finally {
            f.delete();
        }
    }
    
    @Test
    public void readRecordAfterBinlogClosedThrowsIOException() throws Exception {
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000029090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652802";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        
        BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());
        
        binlog.close();
        
        try {
            BinlogRecord record = binlog.read(true, true);
            Assert.fail();
        } catch (IOException e) {
            // correct behavior
        }
    }
    
    @Test
    public void openBadMagicBytesInFileDeclaration() throws Exception {
        // magic bytes, version, then transcoder, then 4 bytes of "header_length"
        String hex = HexUtil.toHexString(BinlogConstants.MAGIC_BYTES);
        // strip last char, then add lowercase "g"
        hex = hex.substring(0, hex.length()-2);
        hex += "67" + "100100000000";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        try {
            BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());
            Assert.fail();
        } catch (BinlogPropertyException e) {
            Assert.assertTrue(e.getMessage().contains("magic_bytes"));
        } finally {
            f.delete();
        }
    }
    
    @Test
    public void openBadVersionInFileDeclaration() throws Exception {
        // magic bytes, version, then transcoder, then 4 bytes of "header_length"
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47150100000000";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        try {
            BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());
            Assert.fail();
        } catch (BinlogPropertyException e) {
            Assert.assertTrue(e.getMessage().contains("version"));
        } finally {
            f.delete();
        }
    }
    
    @Test
    public void openBadTranscoderTypeInFileDeclaration() throws Exception {
        // magic bytes, version, then transcoder, then 4 bytes of "header_length"
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47101000000000";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        try {
            BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());
            Assert.fail();
        } catch (BinlogPropertyException e) {
            Assert.assertTrue(e.getMessage().contains("transcoder_type"));
        } finally {
            f.delete();
        }
    }
    
    @Test
    public void openValidDeclarationButEmptyHeader() throws Exception {
        // magic bytes, version, then transcoder, then 4 bytes of "header_length"
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000000";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        try {
            BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());
            Assert.fail();
        } catch (BinlogPropertyException e) {
            Assert.assertTrue(e.getMessage().contains("create_date_time is null"));
        } finally {
            f.delete();
        }
    }
    
    @Test
    public void openValidDeclarationButNotEnoughDataForHeader() throws Exception {
        // magic bytes, version, then transcoder, then 4 bytes of "header_length"
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000001";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        try {
            BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());
            Assert.fail();
        } catch (BinlogCorruptionException e) {
            Assert.assertTrue(e.getMessage().contains("binlog_file_header"));
        } finally {
            f.delete();
        }
    }
    
    @Test
    public void openValidDeclarationAndHeaderDataButProtobufParsingShouldFail() throws Exception {
        // magic bytes, version, then transcoder, then 4 bytes of "header_length"
        String hex = "4d46495a5a2e434f4d2d42494e4c4f4710010000000100";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        try {
            BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());
            Assert.fail();
        } catch (BinlogException e) {
            Assert.assertTrue(e.getMessage().contains("message contained an invalid tag"));
        } finally {
            f.delete();
        }
    }
    
    // 090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652802
    @Test
    public void openValidDeclarationAndHeader() throws Exception {
        // valid declaration AND header using protobuf transcoder
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000029090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652802";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        
        BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());
        
        Assert.assertTrue(binlog.isFinalSizeKnown());
        Assert.assertArrayEquals(BinlogConstants.MAGIC_BYTES, binlog.getDeclaration().getMagicBytes());
        Assert.assertEquals("1.0", binlog.getDeclaration().getVersionString());
        Assert.assertEquals(TranscoderType.PROTOBUF, binlog.getDeclaration().getTranscoderType());
        
        Assert.assertEquals("test-name", binlog.getHeader().getName());
        Assert.assertEquals("test-content-type", binlog.getHeader().getContentType());
        Assert.assertEquals(2, binlog.getHeader().getRecordLengthByteLength());     // this should be the default since not explicitly set in file header
        DateTime dt = new DateTime(2011, 6, 1, 12, 0, 0, 0, DateTimeZone.UTC);
        Assert.assertEquals(dt, binlog.getHeader().getCreateDateTime());
        Assert.assertEquals(0, binlog.getHeader().getOptionalParameters().size());
        
        // should be no records
        BinlogRecord record = binlog.read(true, true);
        Assert.assertNull(record);
    }
    
    // same previous test, but with 2 optional parameters in header
    // 090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D74797065220E0A046B657931120676616C756531220E0A046B657932120676616C7565322802
    @Test
    public void openValidDeclarationAndHeaderWith2OptionalParameters() throws Exception {
        // valid declaration AND header using protobuf transcoder
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000049090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D74797065220E0A046B657931120676616C756531220E0A046B657932120676616C7565322802";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        
        try {
            BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());

            Assert.assertArrayEquals(BinlogConstants.MAGIC_BYTES, binlog.getDeclaration().getMagicBytes());
            Assert.assertEquals("1.0", binlog.getDeclaration().getVersionString());
            Assert.assertEquals(TranscoderType.PROTOBUF, binlog.getDeclaration().getTranscoderType());

            Assert.assertEquals("test-name", binlog.getHeader().getName());
            Assert.assertEquals("test-content-type", binlog.getHeader().getContentType());
            Assert.assertEquals(2, binlog.getHeader().getRecordLengthByteLength());     // this should be the default since not explicitly set in file header
            DateTime dt = new DateTime(2011, 6, 1, 12, 0, 0, 0, DateTimeZone.UTC);
            Assert.assertEquals(dt, binlog.getHeader().getCreateDateTime());
            Assert.assertEquals(2, binlog.getHeader().getOptionalParameters().size());
            Assert.assertEquals("value1", binlog.getHeader().getOptionalParameter("key1"));
            Assert.assertEquals("value2", binlog.getHeader().getOptionalParameter("key2"));
            Assert.assertNull(binlog.getHeader().getOptionalParameter("key3"));

            // should be no records
            BinlogRecord record = binlog.read(true, true);
            Assert.assertNull(record);
        } finally {
            f.delete();
        }
    }
    
    @Test
    public void openRecordWithNotEnoughDataForLength() throws Exception {
        // bare minimum binlog file
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000029090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652802";
        // record length is 2 bytes long -- we'll do just 3
        hex += "000000";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        
        // this should open okay
        BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());
        
        try {
            BinlogRecord record = binlog.read(true, true);
            Assert.fail();
        } catch (BinlogCorruptionException e) {
            // correct behavior
            Assert.assertTrue(e.getMessage().contains("binlog_record_lengths"));
        }
    }
    
    @Test
    public void openRecordWithInvalidRecordLength() throws Exception {
        // bare minimum binlog file
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000029090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652802";
        // a record_length of 0 and record_header_length of 0
        hex += "00000000";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        
        // this should open okay
        BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());
        
        try {
            BinlogRecord record = binlog.read(true, true);
            Assert.fail();
        } catch (BinlogPropertyException e) {
            // correct behavior
            Assert.assertTrue(e.getMessage().contains("record_length"));
        }
        
        // these shouldn't change after a failure
        Assert.assertEquals(0L, binlog.getRecordCount());
        Assert.assertEquals(0L, binlog.getRecordHeaderSize());
        Assert.assertEquals(0L, binlog.getRecordDataSize());
    }
    
    @Test
    public void openEmptyRecord() throws Exception {
        // bare minimum binlog file
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000029090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652802";
        // a completely empty record
        hex += "00020000";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        
        // this should open okay
        BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());
        
        Assert.assertTrue(binlog.isFinalSizeKnown());
        
        // this should be okay too
        BinlogRecord record = binlog.read(true, true);
        
        Assert.assertEquals(1L, binlog.getRecordCount());
        Assert.assertEquals(0L, binlog.getRecordHeaderSize());
        Assert.assertEquals(0L, binlog.getRecordDataSize());
        Assert.assertNull(record.getHeader().getCreateDateTime());
        Assert.assertNull(record.getHeader().getId());
        Assert.assertNull(record.getHeader().getUserDefinedType());
        Assert.assertEquals(0, record.getHeader().getOptionalParameters().size());
        Assert.assertArrayEquals(new byte[0], record.getData());
        // the position of the record in this file
        Assert.assertEquals(63L, record.getFileInfo().getPosition());
        Assert.assertEquals(4L, record.getFileInfo().getLength());
        Assert.assertEquals(67L, record.getFileInfo().getHeaderPosition());
        Assert.assertEquals(0L, record.getFileInfo().getHeaderLength());
        Assert.assertEquals(67L, record.getFileInfo().getDataPosition());
        Assert.assertEquals(0L, record.getFileInfo().getDataLength());
    }
    
    @Test
    public void openRecordWithNoHeaderAnd1ByteOfData() throws Exception {
        // bare minimum binlog file
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000029090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652802";
        // a completely empty record
        hex += "00030000FF";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        
        // this should open okay
        BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());
        
        // this should be okay too
        BinlogRecord record = binlog.read(true, true);
        
        Assert.assertEquals(1L, binlog.getRecordCount());
        Assert.assertEquals(0L, binlog.getRecordHeaderSize());
        Assert.assertEquals(1L, binlog.getRecordDataSize());
        Assert.assertNull(record.getHeader().getCreateDateTime());
        Assert.assertNull(record.getHeader().getId());
        Assert.assertNull(record.getHeader().getUserDefinedType());
        Assert.assertEquals(0, record.getHeader().getOptionalParameters().size());
        Assert.assertArrayEquals(HexUtil.toByteArray("FF"), record.getData());
        // the position of the record in this file
        Assert.assertEquals(63L, record.getFileInfo().getPosition());
        Assert.assertEquals(5L, record.getFileInfo().getLength());
        Assert.assertEquals(67L, record.getFileInfo().getHeaderPosition());
        Assert.assertEquals(0L, record.getFileInfo().getHeaderLength());
        Assert.assertEquals(67L, record.getFileInfo().getDataPosition());
        Assert.assertEquals(1L, record.getFileInfo().getDataLength());
    }
    
    @Test
    public void openRecordWithInvalidHeader() throws Exception {
        // bare minimum binlog file
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000029090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652802";
        // a completely empty record
        hex += "0003000100";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        
        // this should open okay
        BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());
        
        // confirm correct info after opening
        Assert.assertEquals(63L, binlog.getPosition());
        Assert.assertEquals(68L, binlog.getSize());
        
        try {
            // this should fail parsing
            BinlogRecord record = binlog.read(true, true);
            Assert.fail();
        } catch (BinlogException e) {
            // correct behavior
            Assert.assertTrue(e.getMessage().contains("invalid tag"));
        }
        
        // after a failed record parse, check position afterwards
        Assert.assertEquals(68L, binlog.getPosition());
    }
    
    @Test
    public void openRecordWithHeaderAndDataButNoActualReading() throws Exception {
        // bare minimum binlog file
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000029090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652802";
        // a completely empty record
        hex += "00040001EEFF";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        
        // this should open okay
        BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());
        
        Assert.assertEquals(63L, binlog.getPosition());
        Assert.assertEquals(69L, binlog.getSize());
        
        // this is *only* okay on a bad header since we skipped parsing it
        BinlogRecord record = binlog.read(false, false);
        
        Assert.assertEquals(69L, binlog.getPosition());
        Assert.assertEquals(69L, binlog.getSize());
        
        Assert.assertEquals(1L, binlog.getRecordCount());
        Assert.assertEquals(1L, binlog.getRecordHeaderSize());
        Assert.assertEquals(1L, binlog.getRecordDataSize());
        // we didn't actually parse the header OR data
        Assert.assertNull(record.getHeader());
        Assert.assertNull(record.getData());
        // the position of the record in this file
        Assert.assertEquals(63L, record.getFileInfo().getPosition());
        Assert.assertEquals(6L, record.getFileInfo().getLength());
        Assert.assertEquals(67L, record.getFileInfo().getHeaderPosition());
        Assert.assertEquals(1L, record.getFileInfo().getHeaderLength());
        Assert.assertEquals(68L, record.getFileInfo().getDataPosition());
        Assert.assertEquals(1L, record.getFileInfo().getDataLength());
    }
    
    @Test
    public void openRecordWithHeaderAndDataButOnlyReadingOfData() throws Exception {
        // bare minimum binlog file
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000029090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652802";
        // a completely empty record
        hex += "00040001EEFF";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        
        // this should open okay
        BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());
        
        Assert.assertEquals(63L, binlog.getPosition());
        Assert.assertEquals(69L, binlog.getSize());
        
        // this is *only* okay on a bad header since we skipped parsing it
        BinlogRecord record = binlog.read(false, true);
        
        Assert.assertEquals(69L, binlog.getPosition());
        Assert.assertEquals(69L, binlog.getSize());
        
        // we didn't actually parse the header OR data
        Assert.assertNull(record.getHeader());
        Assert.assertArrayEquals(HexUtil.toByteArray("FF"), record.getData());
        // the position of the record in this file
        Assert.assertEquals(63L, record.getFileInfo().getPosition());
        Assert.assertEquals(6L, record.getFileInfo().getLength());
        Assert.assertEquals(67L, record.getFileInfo().getHeaderPosition());
        Assert.assertEquals(1L, record.getFileInfo().getHeaderLength());
        Assert.assertEquals(68L, record.getFileInfo().getDataPosition());
        Assert.assertEquals(1L, record.getFileInfo().getDataLength());
    }
    
    @Test
    public void openRecordWithHeaderParsingEnabledButNotEnoughDataForHeader() throws Exception {
        // bare minimum binlog file
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000029090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652802";
        // a completely empty record
        hex += "00030001";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        
        // this should open okay
        BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());
        
        // confirm correct info after opening
        Assert.assertEquals(63L, binlog.getPosition());
        Assert.assertEquals(67L, binlog.getSize());
        
        try {
            // this should fail parsing at the record_header part
            BinlogRecord record = binlog.read(true, true);
            Assert.fail();
        } catch (BinlogCorruptionException e) {
            // correct behavior
           Assert.assertTrue(e.getMessage().contains("record_header"));
       }
        
       // after a failed record parse, check position afterwards
       Assert.assertEquals(67L, binlog.getPosition());
    }
    
    @Test
    public void openRecordWithNoHeaderParsingEnabledButNotEnoughDataForData() throws Exception {
        // bare minimum binlog file
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000029090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652802";
        // a completely empty record
        hex += "00030000";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        
        // this should open okay
        BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());
        
        // confirm correct info after opening
        Assert.assertEquals(63L, binlog.getPosition());
        Assert.assertEquals(67L, binlog.getSize());
        
        try {
            // this should fail parsing at the record_data part
            BinlogRecord record = binlog.read(true, true);
            Assert.fail();
        } catch (BinlogCorruptionException e) {
            // correct behavior
           Assert.assertTrue(e.getMessage().contains("record_data"));
       }
        
       // after a failed record parse, check position afterwards
       Assert.assertEquals(67L, binlog.getPosition());
       Assert.assertEquals(0L, binlog.getRecordHeaderSize());
       Assert.assertEquals(0L, binlog.getRecordDataSize());
    }
    
    // header where recordLengthByteLength is 1
    // 090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652801
    @Test
    public void openRecordWithEmptyRecordAnd1ByteRecordLengthByteLengths() throws Exception {
        // bare minimum binlog file
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000029090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652801";
        // a completely empty record with 1 byte lengths
        hex += "0100";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        
        // this should open okay
        BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());
        
        // confirm correct info after opening
        Assert.assertEquals(63L, binlog.getPosition());
        Assert.assertEquals(65L, binlog.getSize());
        
        BinlogRecord record = binlog.read(true, true);
        
        Assert.assertEquals(65L, binlog.getPosition());
    }
    
    // recordHeader of 08D285D8CC04110016134B300100001809
    @Test
    public void openRecordWithHeaderAndData() throws Exception {
        // bare minimum binlog file
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000029090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652801";
        // a record with a header!
        hex += "131108D285D8CC04110016134B300100001809AA";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        
        // this should open okay
        BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());
        
        // confirm correct info after opening
        Assert.assertEquals(63L, binlog.getPosition());
        Assert.assertEquals(83L, binlog.getSize());
        Assert.assertEquals(0, binlog.getRecordCount());
        Assert.assertEquals(0, binlog.getRecordHeaderSize());
        Assert.assertEquals(0, binlog.getRecordDataSize());
        
        BinlogRecord record = binlog.read(true, true);
        
        Assert.assertEquals(1, binlog.getRecordCount());
        Assert.assertEquals(17L, binlog.getRecordHeaderSize());
        Assert.assertEquals(1L, binlog.getRecordDataSize());
        Assert.assertEquals(new DateTime(2011, 6, 1, 12, 0, 0, 0, DateTimeZone.UTC).getMillis(), record.getHeader().getCreateDateTime().getMillis());
        Assert.assertEquals(new Integer(1234567890), record.getHeader().getId());
        Assert.assertEquals(new Integer(9), record.getHeader().getUserDefinedType());
        Assert.assertEquals(0, record.getHeader().getOptionalParameters().size());
        Assert.assertArrayEquals(HexUtil.toByteArray("AA"), record.getData());
        // the position of the record in this file
        Assert.assertEquals(63L, record.getFileInfo().getPosition());
        Assert.assertEquals(20L, record.getFileInfo().getLength());
        Assert.assertEquals(65L, record.getFileInfo().getHeaderPosition());
        Assert.assertEquals(17L, record.getFileInfo().getHeaderLength());
        Assert.assertEquals(82L, record.getFileInfo().getDataPosition());
        Assert.assertEquals(1L, record.getFileInfo().getDataLength());
        
        // after a failed record parse, check position afterwards
        Assert.assertEquals(83L, binlog.getPosition());
    }
    
    @Test
    public void openRecordEvent() throws Exception {
        // bare minimum binlog file
        String hex = "4D46495A5A2E434F4D2D42494E4C4F4710010000000F09804B5F493001000012001A002802";
        // a record (with only an event)
        hex += "0020001E" + "1180909249300100002801321162696E6C6F676765722D73746172746564";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        
        // this should open okay
        BinlogFileReader binlog = BinlogFile.open(f, new EmptyTestBinlogFactory());
        
        // confirm correct info after opening
        Assert.assertEquals(37L, binlog.getPosition());
        Assert.assertEquals(71L, binlog.getSize());
        Assert.assertEquals(0, binlog.getRecordCount());
        Assert.assertEquals(0, binlog.getRecordHeaderSize());
        Assert.assertEquals(0, binlog.getRecordDataSize());
        
        BinlogRecord record = binlog.read(true, true);

        Assert.assertEquals(1, binlog.getRecordCount());
        Assert.assertEquals(30L, binlog.getRecordHeaderSize());
        Assert.assertEquals(0L, binlog.getRecordDataSize());
        Assert.assertEquals(new DateTime(2011, 6, 1, 5, 0, 0, 0, DateTimeZone.UTC).getMillis(), record.getHeader().getCreateDateTime().getMillis());
        
        Assert.assertEquals(0, binlog.getDataRecordCount());
        Assert.assertEquals(1, binlog.getEventRecordCount());
        Assert.assertTrue(record.isEvent());
        Assert.assertFalse(record.isRecord());
        Assert.assertTrue(record.getHeader().isEvent());
        Assert.assertFalse(record.getHeader().isRecord());
        
        Assert.assertEquals(EventType.BINLOGGER_STARTED, record.getHeader().getEvent().getType());
        Assert.assertEquals(new Integer(EventType.BINLOGGER_STARTED.getId()), record.getHeader().getEvent().getId());
        Assert.assertEquals("binlogger-started", record.getHeader().getEvent().getName());
        Assert.assertNull(record.getHeader().getEvent().getInfo()); 
        Assert.assertArrayEquals(new byte[0], record.getData());
        
        // the position of the record in this file
        Assert.assertEquals(37, record.getFileInfo().getPosition());
        Assert.assertEquals(34L, record.getFileInfo().getLength());
        Assert.assertEquals(41L, record.getFileInfo().getHeaderPosition());
        Assert.assertEquals(30L, record.getFileInfo().getHeaderLength());
        Assert.assertEquals(71L, record.getFileInfo().getDataPosition());
        Assert.assertEquals(0L, record.getFileInfo().getDataLength());
    }
    
    @Test
    public void readAfterCloseThrowsException() throws Exception {
        // bare minimum binlog file
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000029090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652802";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        
        BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());
        
        Assert.assertTrue(binlog.isOpen());
        Assert.assertFalse(binlog.isClosed());
        
        binlog.close();
        
        Assert.assertFalse(binlog.isOpen());
        Assert.assertTrue(binlog.isClosed());
        
        try {
            BinlogRecord record = binlog.read();
            Assert.fail();
        } catch (IOException e) {
            // correct behavior
        }
        
        // close twice should be fine
        binlog.close();
    }
    
    @Test
    public void invalidBinlogFileHeaderThrowsExceptionAtOpen() throws Exception {
        // bare minimum binlog file
        String hex = "4D46495A5A2E434F4D2D42494E4C4F4710010000000F09804B5F493001000012001A002802";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        
        // the header in the file has empty name and content-type
        // the other factory DefaultTestBinlogFactory requires these to be actually set
        try {
            BinlogFileReader binlog = BinlogFile.open(f, new DefaultTestBinlogFactory());
            Assert.fail();
        } catch (BinlogException e) {
            logger.debug("Expected exception", e);
            // correct behavior
        }
    }
    
    @Test
    public void readCompressedFileWithBadMagicBytesInFileDeclaration() throws Exception {
        // magic bytes, version, then transcoder, then 4 bytes of "header_length"
        String hex = HexUtil.toHexString(BinlogConstants.MAGIC_BYTES);
        // strip last char, then add lowercase "g"
        hex = hex.substring(0, hex.length()-2);
        hex += "67" + "100100000000";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex));
        // compress it!
        File cf = CompressUtil.compress(f, "gzip", true);
        logger.debug("compressed file: " + cf.getName());
        FileInputStream fis = new FileInputStream(cf);
        GZIPInputStream gis = new GZIPInputStream(fis);
        
        try {
            BinlogFileReader binlog = BinlogFile.read(gis, new DefaultTestBinlogFactory(), false);
            Assert.fail();
        } catch (BinlogPropertyException e) {
            Assert.assertTrue(e.getMessage().contains("magic_bytes"));
        } finally {
            f.delete();
        }
    }
    
    @Test
    public void readCompressedFileWithValidDeclarationButNotEnoughDataForHeader() throws Exception {
        // magic bytes, version, then transcoder, then 4 bytes of "header_length"
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000001";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex));
        // compress it!
        File cf = CompressUtil.compress(f, "gzip", true);
        logger.debug("compressed file: " + cf.getName());
        FileInputStream fis = new FileInputStream(cf);
        GZIPInputStream gis = new GZIPInputStream(fis);
        
        try {
            BinlogFileReader binlog = BinlogFile.read(gis, new DefaultTestBinlogFactory(), false);
            Assert.fail();
        } catch (BinlogCorruptionException e) {
            Assert.assertTrue(e.getMessage().contains("binlog_file_header"));
        } finally {
            f.delete();
        }
    }
    
    @Test
    public void readCompressedFileWithValidDeclarationAndHeader() throws Exception {
        // valid declaration AND header using protobuf transcoder
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000029090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652802";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        // compress it!
        File cf = CompressUtil.compress(f, "gzip", true);
        logger.debug("compressed file: " + cf.getName());
        FileInputStream fis = new FileInputStream(cf);
        GZIPInputStream gis = new GZIPInputStream(fis);
        
        BinlogFileReader binlog = BinlogFile.read(gis, new DefaultTestBinlogFactory(), false);
        
        Assert.assertFalse(binlog.isFinalSizeKnown());
        Assert.assertTrue(binlog.isOpen());
        Assert.assertFalse(binlog.isClosed());
        // size should just match uncompressed position
        Assert.assertEquals(63L, binlog.getSize());
        
        Assert.assertArrayEquals(BinlogConstants.MAGIC_BYTES, binlog.getDeclaration().getMagicBytes());
        Assert.assertEquals("1.0", binlog.getDeclaration().getVersionString());
        Assert.assertEquals(TranscoderType.PROTOBUF, binlog.getDeclaration().getTranscoderType());
        
        Assert.assertEquals("test-name", binlog.getHeader().getName());
        Assert.assertEquals("test-content-type", binlog.getHeader().getContentType());
        Assert.assertEquals(2, binlog.getHeader().getRecordLengthByteLength());     // this should be the default since not explicitly set in file header
        DateTime dt = new DateTime(2011, 6, 1, 12, 0, 0, 0, DateTimeZone.UTC);
        Assert.assertEquals(dt, binlog.getHeader().getCreateDateTime());
        Assert.assertEquals(0, binlog.getHeader().getOptionalParameters().size());
        
        // should be no records
        BinlogRecord record = binlog.read(true, true);
        Assert.assertNull(record);
    }
    
    @Test
    public void readCompressedFileVerifyInputStreamIsNotClosed() throws Exception {
        // valid declaration AND header using protobuf transcoder
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000029090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652802";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        // compress it!
        File cf = CompressUtil.compress(f, "gzip", true);
        logger.debug("compressed file: " + cf.getName());
        FileInputStream fis = new FileInputStream(cf);
        GZIPInputStream gis = new GZIPInputStream(fis);
        
        BinlogFileReader binlog = BinlogFile.read(gis, new DefaultTestBinlogFactory(), false);
        
        Assert.assertTrue(binlog.isOpen());
        Assert.assertFalse(binlog.isClosed());
        
        // should be no records
        BinlogRecord record = binlog.read(true, true);
        Assert.assertNull(record);
        
        Assert.assertTrue(binlog.isOpen());
        Assert.assertFalse(binlog.isClosed());
        
        binlog.close();
        
        // input streams should still be open since BinlogFile.read() was used
        try {
            gis.available();        // these should work if stream wasn't closed
            fis.available();        
        } catch (IOException e) {
            Assert.fail();
        }
        
        Assert.assertTrue(binlog.isFinalSizeKnown());
    }
    
    @Test
    public void readCompressedFileVerifyInputStreamIsClosed() throws Exception {
        // valid declaration AND header using protobuf transcoder
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000029090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652802";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        // compress it!
        File cf = CompressUtil.compress(f, "gzip", true);
        logger.debug("compressed file: " + cf.getName());
        FileInputStream fis = new FileInputStream(cf);
        GZIPInputStream gis = new GZIPInputStream(fis);
        
        BinlogFileReader binlog = BinlogFile.read(gis, new DefaultTestBinlogFactory(), true);
        
        Assert.assertTrue(binlog.isOpen());
        Assert.assertFalse(binlog.isClosed());
        
        // should be no records
        BinlogRecord record = binlog.read(true, true);
        Assert.assertNull(record);
        
        Assert.assertTrue(binlog.isOpen());
        Assert.assertFalse(binlog.isClosed());
        
        binlog.close();
        
        // input streams should now be closed BinlogFile was closed
        try {
            gis.available();
            Assert.fail();
        } catch (IOException e) {
            // correct behavior
        }
        
        // input streams should now be closed BinlogFile was closed
        try {
            fis.available();
            Assert.fail();
        } catch (IOException e) {
            // correct behavior
        }
        
        Assert.assertTrue(binlog.isFinalSizeKnown());
    }
    
    @Test
    public void readInputStreamIfClosedThrowsException() throws Exception {
        // valid declaration AND header using protobuf transcoder
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000029090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652802";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        // compress it!
        FileInputStream fis = new FileInputStream(f);
        
        BinlogFileReader binlog = BinlogFile.read(fis, new DefaultTestBinlogFactory(), false);
        
        Assert.assertTrue(binlog.isOpen());
        Assert.assertFalse(binlog.isClosed());
        Assert.assertFalse(binlog.isFinalSizeKnown());
        
        // should be no records
        BinlogRecord record = binlog.read(true, true);
        Assert.assertNull(record);
        
        // close the inputstream, then check to see what happens
        fis.close();
        
        try {
            record = binlog.read(true, true);
            Assert.fail();
        } catch (IOException e) {
            // correct behavior
        }
        
        // this should still be okay to do
        binlog.close();
        binlog.close();     // 2nd time should work okay too
        Assert.assertTrue(binlog.isFinalSizeKnown());   // final size should be known now
    }
    
    @Test
    public void readCompressedFileWithRecordWithHeaderParsingEnabledButNotEnoughDataForHeader() throws Exception {
        // bare minimum binlog file
        String hex = "4d46495a5a2e434f4d2d42494e4c4f47100100000029090016134B300100001209746573742D6E616D651A11746573742D636F6E74656E742D747970652802";
        // a completely empty record
        hex += "00030001";
        File f = FileUtil.createTempFile(HexUtil.toByteArray(hex)); 
        
        // compress it!
        File cf = CompressUtil.compress(f, "gzip", true);
        logger.debug("compressed file: " + cf.getName());
        FileInputStream fis = new FileInputStream(cf);
        GZIPInputStream gis = new GZIPInputStream(fis);
        
        BinlogFileReader binlog = BinlogFile.read(gis, new DefaultTestBinlogFactory(), false);
        
        // confirm correct info after opening
        Assert.assertEquals(63L, binlog.getPosition());
        Assert.assertEquals(63L, binlog.getSize());
        
        try {
            // this should fail parsing at the record_header part
            BinlogRecord record = binlog.read(true, true);
            Assert.fail();
        } catch (BinlogCorruptionException e) {
            // correct behavior
           Assert.assertTrue(e.getMessage().contains("record_header"));
       }
        
       // after a failed record parse, check position afterwards
       Assert.assertEquals(67L, binlog.getPosition());
    }
    
    @Test
    public void seekForward() throws Exception {
        File f = FileUtil.createTempFile();

        byte[] recordBytes0 = "Record 0: blajajjdjdjf;afdsfjdsf".getBytes("ISO-8859-1");
        byte[] recordBytes1 = "Record 1: fhdklsjhfdskafjhsdakjfhsdkjfhsadlkfhsdlkjhfasdf".getBytes("ISO-8859-1");
        byte[] recordBytes2 = "Record 1: jkldhekjhho9uruyiyhnioyinwe,n3jkhyuhziuyoi73".getBytes("ISO-8859-1");
        
        // create as empty a binlog file as possible
        BinlogFileWriter binlog = BinlogFile.create(f, new EmptyTestBinlogFactory());  
        ArrayList<BinlogRecordFileInfo> fileInfos = new ArrayList<BinlogRecordFileInfo>();
        
        // write 3 records of various size
        WritableBinlogRecordHeader recordHeader0 = new BinlogRecordHeaderImpl();
        recordHeader0.setUserDefinedType(0);
        BinlogRecordFileInfo fileInfo0 = binlog.write(recordHeader0, recordBytes0);
        fileInfos.add(fileInfo0);
        
        WritableBinlogRecordHeader recordHeader1 = new BinlogRecordHeaderImpl();
        recordHeader1.setUserDefinedType(1);
        BinlogRecordFileInfo fileInfo1 = binlog.write(recordHeader1, recordBytes1);
        fileInfos.add(fileInfo1);
        
        WritableBinlogRecordHeader recordHeader2 = new BinlogRecordHeaderImpl();
        recordHeader2.setUserDefinedType(2);
        BinlogRecordFileInfo fileInfo2 = binlog.write(recordHeader2, recordBytes2);
        fileInfos.add(fileInfo2);
        
        // close the binlog and then start reading it
        binlog.close();
        
        // open file for reading
        BinlogFileReader binlog0 = BinlogFile.open(f, new EmptyTestBinlogFactory(), true);
        
        // skip to second record
        BinlogFileSeekForwardInfo sfi = binlog0.seekForward(fileInfo1.getPosition(), true);
        
        BinlogRecord record1 = binlog0.read();
        
        Assert.assertArrayEquals(recordBytes1, record1.getData());
        Assert.assertEquals(fileInfo1.getPosition(), record1.getFileInfo().getPosition());
        Assert.assertEquals(fileInfo1.getLength(), record1.getFileInfo().getLength());
    }
    
    @Test
    public void seekForwardToNonRecordStartThrowsException() throws Exception {
        File f = FileUtil.createTempFile();

        byte[] recordBytes0 = "Record 0: blajajjdjdjf;afdsfjdsf".getBytes("ISO-8859-1");
        byte[] recordBytes1 = "Record 1: fhdklsjhfdskafjhsdakjfhsdkjfhsadlkfhsdlkjhfasdf".getBytes("ISO-8859-1");
        byte[] recordBytes2 = "Record 1: jkldhekjhho9uruyiyhnioyinwe,n3jkhyuhziuyoi73".getBytes("ISO-8859-1");
        
        // create as empty a binlog file as possible
        BinlogFileWriter binlog = BinlogFile.create(f, new EmptyTestBinlogFactory());  
        ArrayList<BinlogRecordFileInfo> fileInfos = new ArrayList<BinlogRecordFileInfo>();
        
        // write 3 records of various size
        WritableBinlogRecordHeader recordHeader0 = new BinlogRecordHeaderImpl();
        recordHeader0.setUserDefinedType(0);
        BinlogRecordFileInfo fileInfo0 = binlog.write(recordHeader0, recordBytes0);
        fileInfos.add(fileInfo0);
        
        WritableBinlogRecordHeader recordHeader1 = new BinlogRecordHeaderImpl();
        recordHeader1.setUserDefinedType(1);
        BinlogRecordFileInfo fileInfo1 = binlog.write(recordHeader1, recordBytes1);
        fileInfos.add(fileInfo1);
        
        WritableBinlogRecordHeader recordHeader2 = new BinlogRecordHeaderImpl();
        recordHeader2.setUserDefinedType(2);
        BinlogRecordFileInfo fileInfo2 = binlog.write(recordHeader2, recordBytes2);
        fileInfos.add(fileInfo2);
        
        // close the binlog and then start reading it
        binlog.close();
        
        // open file for reading (1 byte before next record)
        BinlogFileReader binlog0 = BinlogFile.open(f, new EmptyTestBinlogFactory(), true); 
        try {
            // skip to second record (1 byte before)
            BinlogFileSeekForwardInfo sfi = binlog0.seekForward(fileInfo1.getPosition()-1, true);
            Assert.fail();
        } catch (BinlogException e) {
            // correct behavior (we're actually at the next record already!
            Assert.assertEquals(fileInfo1.getPosition(), binlog0.getPosition());
        } finally {
            binlog0.close();
        }
        
        // open file for reading (1 byte after next record)
        BinlogFileReader binlog1 = BinlogFile.open(f, new EmptyTestBinlogFactory(), true); 
        try {
            // skip to second record (1 byte before)
            BinlogFileSeekForwardInfo sfi = binlog1.seekForward(fileInfo1.getPosition()+1, true);
            Assert.fail();
        } catch (BinlogException e) {
            // correct behavior (we're actually at the next-next record already!
            Assert.assertEquals(fileInfo2.getPosition(), binlog1.getPosition());
        } finally {
            binlog1.close();
        }
    }
    
    @Test
    public void seekForwardToPositionThatIsBackwardsThrowsException() throws Exception {
        File f = FileUtil.createTempFile();

        byte[] recordBytes0 = "Record 0: blajajjdjdjf;afdsfjdsf".getBytes("ISO-8859-1");
        byte[] recordBytes1 = "Record 1: fhdklsjhfdskafjhsdakjfhsdkjfhsadlkfhsdlkjhfasdf".getBytes("ISO-8859-1");
        byte[] recordBytes2 = "Record 1: jkldhekjhho9uruyiyhnioyinwe,n3jkhyuhziuyoi73".getBytes("ISO-8859-1");
        
        // create as empty a binlog file as possible
        BinlogFileWriter binlog = BinlogFile.create(f, new EmptyTestBinlogFactory());  
        ArrayList<BinlogRecordFileInfo> fileInfos = new ArrayList<BinlogRecordFileInfo>();
        
        // write 3 records of various size
        WritableBinlogRecordHeader recordHeader0 = new BinlogRecordHeaderImpl();
        recordHeader0.setUserDefinedType(0);
        BinlogRecordFileInfo fileInfo0 = binlog.write(recordHeader0, recordBytes0);
        fileInfos.add(fileInfo0);
        
        WritableBinlogRecordHeader recordHeader1 = new BinlogRecordHeaderImpl();
        recordHeader1.setUserDefinedType(1);
        BinlogRecordFileInfo fileInfo1 = binlog.write(recordHeader1, recordBytes1);
        fileInfos.add(fileInfo1);
        
        WritableBinlogRecordHeader recordHeader2 = new BinlogRecordHeaderImpl();
        recordHeader2.setUserDefinedType(2);
        BinlogRecordFileInfo fileInfo2 = binlog.write(recordHeader2, recordBytes2);
        fileInfos.add(fileInfo2);
        
        // close the binlog and then start reading it
        binlog.close();
        
        // open file for reading (1 byte before current position)
        BinlogFileReader binlog0 = BinlogFile.open(f, new EmptyTestBinlogFactory(), true);
        long startPosition = binlog0.getPosition();
        try {
            // try to seek backwards!
            BinlogFileSeekForwardInfo sfi = binlog0.seekForward(startPosition-1, false);
            Assert.fail();
        } catch (BinlogException e) {
            // correct behavior (position shouldn't have changed)
            Assert.assertEquals(startPosition, binlog0.getPosition());
        } finally {
            binlog0.close();
        }
        
        // try to seek to current read position
        BinlogFileSeekForwardInfo sfi = binlog0.seekForward(startPosition, false);
    }
    
    @Test
    public void serverIdHeaderValue() throws Exception {
        File f = FileUtil.createTempFile();
        
        BinlogFactory bf = new BinlogFactory() {
            @Override
            public void createBinlogFileHeader(WritableBinlogFileHeader header) {
                header.setName("");
                header.setServerId(65689);
                header.setContentType("application/test");
            }
            @Override
            public void verifyBinlogFileHeader(BinlogFileHeader header) throws BinlogException {
                // do nothing
            }
        };

        // create as empty a binlog file as possible
        BinlogFileWriter binlog = BinlogFile.create(f, bf);
        
        // close the binlog and then start reading it
        binlog.close();
        
        // open file for reading (1 byte before current position)
        BinlogFileReader binlog0 = BinlogFile.open(f, bf, true);
        
        // verify the server-id values worked
        Assert.assertTrue(binlog0.getHeader().hasServerId());
        Assert.assertEquals(65689, binlog0.getHeader().getServerId());
    }
}
