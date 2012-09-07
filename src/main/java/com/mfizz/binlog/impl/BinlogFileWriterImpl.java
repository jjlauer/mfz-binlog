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

import com.mfizz.binlog.BinlogFileWriter;
import com.mfizz.binlog.BinlogException;
import com.mfizz.binlog.BinlogFileDeclaration;
import com.mfizz.binlog.BinlogFileHeader;
import com.mfizz.binlog.BinlogRecordFileInfo;
import com.mfizz.binlog.BinlogRecordHeader;
import com.mfizz.binlog.WritableBinlogRecordHeader;
import com.mfizz.binlog.WritableBinlogRecordHeaderEvent;
import com.mfizz.binlog.type.EventType;
import com.mfizz.binlog.util.ByteChannelUtil;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfizz
 */
public class BinlogFileWriterImpl extends BinlogFileImpl implements BinlogFileWriter {
    private static final Logger logger = LoggerFactory.getLogger(BinlogFileWriterImpl.class);
    
    static private final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    
    // useful for tracking "checkouts" to properly close a writer at a later time
    private final AtomicInteger checkouts;
    
    public BinlogFileWriterImpl(BinlogFileReaderImpl reader) throws IOException {
        this(reader.getFile(), reader.getRandomAccessFile(), reader.getFileChannel(), reader.getTranscoder(), reader.getDeclaration(), reader.getHeader(), reader.getRecordCount(), reader.getDataRecordCount(), reader.getEventRecordCount(), reader.getRecordHeaderSize(), reader.getRecordDataSize());
    }
    
    public BinlogFileWriterImpl(File file, RandomAccessFile rafile, FileChannel fc, BinlogFileTranscoder transcoder, BinlogFileDeclaration declaration, BinlogFileHeader header, long recordCount, long dataRecordCount, long eventRecordCount, long recordHeaderSize, long recordDataSize) throws IOException {
        super(file, rafile, fc, transcoder, declaration, header, true, recordCount, dataRecordCount, eventRecordCount, recordHeaderSize, recordDataSize);
        this.checkouts = new AtomicInteger();
    }
    
    // these are only used in the binlogger
    
    @Override
    public int getCheckouts() {
        return this.checkouts.get();
    }
    
    @Override
    public int checkout() {
        return this.checkouts.incrementAndGet();
    }
    
    @Override
    public int checkin() {
        return this.checkouts.decrementAndGet();
    }
    
    @Override
    public void flush() throws IOException {
        // check if file still open
        if (fc == null || rafile == null) {
            throw new IOException("Failed to write record; binlog file [" + file.getAbsolutePath() + "] closed");
        }
        
        fc.force(false);
    }
    
    @Override
    public BinlogRecordFileInfo write(byte[] data) throws IOException, BinlogException {
        return write(data, null, null);
    }

    @Override
    public BinlogRecordFileInfo write(byte[] data, Integer userDefinedType) throws IOException, BinlogException {
        return write(data, userDefinedType, null);
    }

    @Override
    public BinlogRecordFileInfo write(byte[] data, Map<String, String> optionalParameters) throws IOException, BinlogException {
        return write(data, null, optionalParameters);
    }

    @Override
    public BinlogRecordFileInfo write(byte[] data, Integer userDefinedType, Map<String, String> optionalParameters) throws IOException, BinlogException {
        if (userDefinedType == null && optionalParameters == null) {
            return write(null, data);
        } else {
            // create a header for this record data
            WritableBinlogRecordHeader recordHeader = new BinlogRecordHeaderImpl();
            recordHeader.setUserDefinedType(userDefinedType);
            recordHeader.setOptionalParameters(optionalParameters);
            return write(recordHeader, data);
        }
    }
    
    @Override
    public BinlogRecordFileInfo writeEvent(EventType type) throws IOException, BinlogException {
        return writeEvent(type, null, null, null);
    }
    
    @Override
    public BinlogRecordFileInfo writeEvent(EventType type, DateTime timestamp) throws IOException, BinlogException {
        return writeEvent(type, timestamp, null, null);
    }
    
    @Override
    public BinlogRecordFileInfo writeEvent(EventType type, DateTime timestamp, String info) throws IOException, BinlogException {
        return writeEvent(type, timestamp, info, null);
    }
    
    @Override
    public BinlogRecordFileInfo writeEvent(EventType type, DateTime timestamp, String info, Map<String,String> optionalParameters) throws IOException, BinlogException {
        if (timestamp == null) {
            timestamp = new DateTime(DateTimeZone.UTC);
        }
        return write(createRecordHeaderForEvent(timestamp, type, info, optionalParameters), null);
    }
    
    static public BinlogRecordHeader createRecordHeaderForRecord(Integer userDefinedType, Map<String,String> optionalParameters) {
        if (userDefinedType == null && optionalParameters == null) {
            return null;
        } else {
            // create a header for this record data
            WritableBinlogRecordHeader recordHeader = new BinlogRecordHeaderImpl();
            recordHeader.setUserDefinedType(userDefinedType);
            if (optionalParameters != null) {
                recordHeader.setOptionalParameters(optionalParameters);
            }
            return recordHeader;
        }
    }
    
    static public BinlogRecordHeader createRecordHeaderForEvent(DateTime timestamp, EventType type, String info, Map<String,String> optionalParameters) {
        // create event
        WritableBinlogRecordHeaderEvent event = new BinlogRecordHeaderEventImpl();
        event.setId(type.getId());
        event.setType(type);
        event.setName(type.getName());
        if (info != null) {
            event.setInfo(info);
        }
        // create a header for this event
        WritableBinlogRecordHeader recordHeader = new BinlogRecordHeaderImpl();
        recordHeader.setCreateDateTime(timestamp);
        recordHeader.setEvent(event);
        if (optionalParameters != null) {
            recordHeader.setOptionalParameters(optionalParameters);
        }
        return recordHeader;
    }
    
    @Override
    public BinlogRecordFileInfo write(BinlogRecordHeader recordHeader, byte[] data) throws IOException, BinlogException {
        // check if file still open
        if (fc == null || rafile == null) {
            throw new IOException("Failed to write record; binlog file [" + file.getAbsolutePath() + "] closed");
        }
        
        // convert header into a byte array (so we can know its length)
        byte[] headerBytes = null;
        if (recordHeader == null) {
            headerBytes = EMPTY_BYTE_ARRAY;
        } else {
            headerBytes = transcoder.createByteArray(recordHeader);
        }
        
        // in case data is null, use an empty array
        if (data == null) {
            data = EMPTY_BYTE_ARRAY;
        }
        
        // record_length = record_header_length + record_header + record_data
        // the record_length includes the bytes to store the record_header_length so that
        // entire records can be easily skipped when the file is being read back :-)
        int recordHeaderLength = headerBytes.length;
        int recordLength = header.getRecordLengthByteLength() + recordHeaderLength + data.length;
        
        // note: the record_length is checked against the max in ByteChannelUtil
        
        // only new buffer we need to create is the one to store the record_length and record_header_length
        ByteBuffer lengthBuffer = ByteBuffer.allocate(header.getRecordLengthByteLength() * 2).order(ByteOrder.BIG_ENDIAN);
        // record_length
        ByteChannelUtil.putNumeric(lengthBuffer, header.getRecordLengthByteLength(), recordLength);
        // record_header_length
        ByteChannelUtil.putNumeric(lengthBuffer, header.getRecordLengthByteLength(), headerBytes.length);
        lengthBuffer.flip();
        
        // create other 2 buffers wrapped around already allocated buffers
        ByteBuffer headerBuffer = ByteBuffer.wrap(headerBytes);
        ByteBuffer dataBuffer = ByteBuffer.wrap(data);
        
        // a "gathering byte channel" method is nearly 40% faster than separate write calls!
        ByteBuffer[] buffers = new ByteBuffer[] { lengthBuffer, headerBuffer, dataBuffer };
        
        synchronized(this) {
            // synchronize writing of all these records to file atomically
            // NOTE: we potentially could increase performance by buffering the write call
            ByteChannelUtil.writeAll(fc, buffers);
            
            // create info for record of where it exists within the binlog
            BinlogRecordFileInfo fileInfo = new BinlogRecordFileInfoImpl(position,
                    (recordLength + header.getRecordLengthByteLength()), recordHeaderLength, header.getRecordLengthByteLength());
            
            // increment file position and size since we know it was just written
            this.recordCount++;
            if (recordHeader != null && recordHeader.isEvent()) {
                this.eventRecordCount++;
            } else {
                this.dataRecordCount++;
            }
            this.recordHeaderSize += fileInfo.getHeaderLength();
            this.recordDataSize += fileInfo.getDataLength();
            this.position += fileInfo.getLength();
            this.size += fileInfo.getLength();
            
            return fileInfo;
        }
    }
     
}
