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

import com.mfizz.binlog.BinlogFileReader;
import com.mfizz.binlog.BinlogException;
import com.mfizz.binlog.BinlogFileDeclaration;
import com.mfizz.binlog.BinlogFileHeader;
import com.mfizz.binlog.BinlogFileSeekForwardInfo;
import com.mfizz.binlog.BinlogPropertyException;
import com.mfizz.binlog.BinlogRecord;
import com.mfizz.binlog.BinlogRecordFileInfo;
import com.mfizz.binlog.BinlogRecordHeader;
import com.mfizz.binlog.util.ByteChannelUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 *
 * @author mfizz
 */
public class BinlogFileReaderImpl extends BinlogFileImpl implements BinlogFileReader {
    
    protected InputStream is;
    protected ReadableByteChannel rbc;
    protected boolean closeInputStream;
    
    public BinlogFileReaderImpl(File file, RandomAccessFile rafile, FileChannel fc, BinlogFileTranscoder transcoder, BinlogFileDeclaration declaration, BinlogFileHeader header, long recordCount, long dataRecordCount, long eventRecordCount, long recordHeaderSize, long recordDataSize) throws IOException {
        super(file, rafile, fc, transcoder, declaration, header, false, recordCount, dataRecordCount, eventRecordCount, recordHeaderSize, recordDataSize);
        // the readable byte channel is just a reference to the file channel
        this.rbc = fc;
    }
    
    public BinlogFileReaderImpl(File file, InputStream is, boolean closeInputStream, ReadableByteChannel rbc, long position, BinlogFileTranscoder transcoder, BinlogFileDeclaration declaration, BinlogFileHeader header, long recordCount, long dataRecordCount, long eventRecordCount, long recordHeaderSize, long recordDataSize) throws IOException {
        // this constructor is NOT tied to a filechannel
        super(file, null, null, transcoder, declaration, header, false, recordCount, dataRecordCount, eventRecordCount, recordHeaderSize, recordDataSize);
        this.is = is;
        this.closeInputStream = closeInputStream;
        this.rbc = rbc;
        // position initially set to whatever is passed in
        this.position = position;
    }
    
    @Override
    public boolean isOpen() {
        return (this.rbc != null || super.isOpen());
    }
    
    @Override
    public boolean isClosed() {
        return (this.rbc == null && super.isClosed());
    }
    
    @Override
    public void close() throws IOException {
        rbc = null;     // always set this to null first
        if (closeInputStream && this.is != null) {
            try {
                this.is.close();
            } catch (IOException e) { }
        }
        this.is = null;
        super.close();
    }
    
    @Override
    public BinlogRecord read() throws IOException, BinlogException {
        return read(true, true);
    }
    
    @Override
    public long getSize() {
        // if size is -1 then size is always the current position
        if (this.size < 0) {
            return this.position;
        } else {
            return this.size;
        }
    }
    
    @Override
    public boolean isFinalSizeKnown() {
        // if size is >= 0, then this reader was originally tied to a filechannel
        // in which case we always know the final size ahead of time
        if (this.size >= 0) {
            return true;
        }
        // otherwise, default to parent implementation which only returns true
        // if the binlog is closed
        return super.isFinalSizeKnown();
    }

    @Override
    public BinlogFileSeekForwardInfo seekForward(long filePosition, boolean verifyFilePositionStartsRecord) throws IOException, BinlogException {
        // default positioin to current readPosition
        long readFilePosition = this.getPosition();
        int skippedRecordCount = 0;
        
        // verify the user isn't trying to seek backward
        if (filePosition < readFilePosition) {
            throw new BinlogException("Seeking backwards in binlog unsupported; position [" + filePosition + "] < current read position [" + readFilePosition + "]");
        }
        
        while (readFilePosition < filePosition) {
            // only read the record header, not data (saves some time not allocating bytes just to skip the record)
            BinlogRecord record = read(true, false);
            if (record == null) {
                throw new BinlogException("Not enough data in binlog to seek forward to position [" + filePosition + "]; last record successfully read byte position [" + readFilePosition + "]");
            }
            // move to next record file position
            // our "read" position is the current record + length
            readFilePosition = record.getFileInfo().getPosition() + record.getFileInfo().getLength();
            skippedRecordCount++;
        }
        
        if (verifyFilePositionStartsRecord) {
            if (readFilePosition != filePosition) {
                // can only get here if we had to "read" past the target position
                throw new BinlogException("Binlog seek forward to position [" + filePosition + "] worked but does not start a record; target position [" + filePosition + "], actual read position [" + readFilePosition + "]");
            } 
        }
        
        return new BinlogFileSeekForwardInfoImpl(skippedRecordCount, filePosition, readFilePosition);
    }
    
    @Override
    synchronized public BinlogRecord read(boolean readHeader, boolean readData) throws IOException, BinlogException {
        // check if file channel AND readable byte channels are null
        if (fc == null && rafile == null && rbc == null) {
            throw new IOException("Failed to read record; binlog file [" + file.getAbsolutePath() + "] closed");
        }
        
        // each record starts with 2 integer values of variable byte size depending on the settings
        // when the original binlog file was created
        int lengthBufferLength = header.getRecordLengthByteLength() * 2;
        
        // read record_length AND record_header_length
        ByteBuffer lengthBuffer = ByteBuffer.allocate(lengthBufferLength).order(ByteOrder.BIG_ENDIAN);
        try {
            ByteChannelUtil.readAll("binlog_record_lengths", position, rbc, lengthBuffer);
        } catch (NoDataReadException e) {
            // special case on attempt to read start of a record -- this means there
            // are no more records rather than anything actually corrupted
            return null;
        }
        lengthBuffer.flip();
        
        // should have 2 numeric values
        int recordLength = ByteChannelUtil.getNumeric(lengthBuffer, header.getRecordLengthByteLength());
        int recordHeaderLength = ByteChannelUtil.getNumeric(lengthBuffer, header.getRecordLengthByteLength());
        
        // validate record_length (should always be > header.getRecordLengthByteLength() since this length
        // is supposed to include the length record_header_length value as well
        if (recordLength < header.getRecordLengthByteLength()) {
            throw new BinlogPropertyException("A valid record must have a record_length value >= [" + header.getRecordLengthByteLength() + "] actual was [" + recordLength + "]");
        }
        
        if (recordHeaderLength < 0) {
            throw new BinlogPropertyException("A valid record must have a record_header_length value >= [0] actual was [" + recordHeaderLength + "]");
        }
        
        // create info for record of where it exists within the binlog
        BinlogRecordFileInfo fileInfo = new BinlogRecordFileInfoImpl(position,
                (recordLength + header.getRecordLengthByteLength()), recordHeaderLength, header.getRecordLengthByteLength());
        
        // new "read" position is just the 2 integers we read
        position += lengthBufferLength;
        
        int recordDataLength = (int)fileInfo.getDataLength();
        BinlogRecordHeader recordHeader = null;
        byte[] recordData = null;
        
        if (this.fc == null || readHeader) {
            // read header then parse it
            ByteBuffer recordHeaderBuffer = ByteBuffer.allocate(recordHeaderLength);
            ByteChannelUtil.readAll("record_header", position, rbc, recordHeaderBuffer);
            recordHeaderBuffer.flip();
            position += recordHeaderLength;
            recordHeader = transcoder.createBinlogRecordHeader(recordHeaderBuffer.array());
        } else {
            // even if we aren't reading the header, check if there are enough bytes in file
            BinlogFileImpl.verifyFileHasEnoughData("record_header", position, recordHeaderLength, size);
            // skip header
            position += recordHeaderLength;
            fc.position(position);
        }
        
        if (this.fc == null || readData) {
            // read data then parse it
            ByteBuffer recordDataBuffer = ByteBuffer.allocate(recordDataLength);
            ByteChannelUtil.readAll("record_data", position, rbc, recordDataBuffer);
            recordDataBuffer.flip();
            position += recordDataLength;
            recordData = recordDataBuffer.array();
        } else {
            // even if we aren't reading the data, check if there are enough bytes in file
            BinlogFileImpl.verifyFileHasEnoughData("record_data", position, recordDataLength, size);
            // skip data
            position += recordDataLength;
            fc.position(position);
        }

        // update counters
        this.recordCount++;
        if (recordHeader != null && recordHeader.isEvent()) {
            this.eventRecordCount++;
        } else {
            this.dataRecordCount++;
        }
        this.recordHeaderSize += fileInfo.getHeaderLength();
        this.recordDataSize += fileInfo.getDataLength();
        
        return new BinlogRecordImpl(recordHeader, recordData, fileInfo);
    }
     
}
