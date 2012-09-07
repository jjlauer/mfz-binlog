package com.mfizz.binlog.util;

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

import com.mfizz.binlog.BinlogCorruptionException;
import com.mfizz.binlog.BinlogPropertyException;
import com.mfizz.binlog.impl.NoDataReadException;
import java.io.EOFException;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ReadableByteChannel;

/**
 *
 * @author mfizz
 */
public class ByteChannelUtil {
    
    static public int getMaxRecordLength(int byteLength) throws BinlogPropertyException {
        if (byteLength == 1) {
            return 255;
        } else if (byteLength == 2) {
            return 65535;
        } else if (byteLength == 4) {
            // not quite an unsigned length, but 1 record should never really
            // be this so this shouldn't really matter
            return Integer.MAX_VALUE;
        } else {
            throw new BinlogPropertyException("Unsupported byteLength [" + byteLength + "] found; only 1, 2, or 4 is supported");
        }
    }
    
    static public void putNumeric(ByteBuffer buffer, int byteLength, int value) throws BinlogPropertyException {
        int maxValue = getMaxRecordLength(byteLength);
        if (value > maxValue) {
            throw new BinlogPropertyException("Value [" + value + "] exceeds max value [" + maxValue + "] since byteLength [" + byteLength + "]");
        }
        if (byteLength == 1) {
            buffer.put((byte)value);
        } else if (byteLength == 2) {
            buffer.putShort((short)value);
        } else if (byteLength == 4) {
            buffer.putInt((int)value);
        }
    }
    
    static public int getNumeric(ByteBuffer buffer, int byteLength) throws BufferUnderflowException, BinlogPropertyException {
        if (buffer.remaining() < byteLength) {
            throw new BufferUnderflowException();
        }
        
        // FIXME: these are all unsigned
        if (byteLength == 1) {
            return UnsignedUtil.byteToUnsigned(buffer.get());
        } else if (byteLength == 2) {
            return UnsignedUtil.shortToUnsigned(buffer.getShort());
        } else if (byteLength == 4) {
            return (int)UnsignedUtil.intToUnsigned(buffer.getInt());
        } else {
            throw new BinlogPropertyException("Unsupported byteLength");
        }
    }
    
    static public void writeAll(ByteChannel channel, ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }
    
    static public void writeAll(GatheringByteChannel channel, ByteBuffer[] buffers) throws IOException {
        while (true) {
            // this supposedly is more efficient than calling write multiple times!
            channel.write(buffers);
            // did they all write?
            boolean remaining = false;
            for (ByteBuffer buf : buffers) {
                if (buf.hasRemaining()) {
                    remaining = true;
                }
            }
            if (!remaining) {
                return;
            }
        }
    }
    
    static public void readAll(ReadableByteChannel channel, ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            int numRead = channel.read(buffer);
            if (numRead < 0) {
                throw new EOFException();
            }
        }
    }
    
    static public void readAll(String parsingLocation, long position, ReadableByteChannel channel, ByteBuffer buffer) throws NoDataReadException, BinlogCorruptionException, IOException {
        int requiredLength = buffer.remaining();
        int totalBytesRead = 0;
        while (buffer.hasRemaining()) {
            int bytesRead = channel.read(buffer);
            if (bytesRead < 0) {
                String message = "Binlog file corrupted or does not contain enough data @ parsing_location [" + parsingLocation + "] position [" + position + "] required_length [" + requiredLength + "] actual_length [" + totalBytesRead + "]";
                if (totalBytesRead <= 0) {
                    // not a single byte of data read
                    throw new NoDataReadException(message);
                } else {
                    // reached EOF but did read some data
                    throw new BinlogCorruptionException(message);
                }
            }
            totalBytesRead += bytesRead;
        }
    }

}
