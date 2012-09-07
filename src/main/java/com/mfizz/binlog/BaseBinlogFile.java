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

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * Parent interface for common BinlogFile functionality between readers or writers.
 * 
 * @author mfizz
 */
public interface BaseBinlogFile extends Closeable {
    
    public BinlogFileDeclaration getDeclaration();

    public BinlogFileHeader getHeader();
    
    public File getFile();

    /**
     * Close the binlog from any further writing or reading.  In cases where
     * a resource was created (managed) by the binlog, this method ensures this
     * resource is closed. After this method completes, all reads or writes will
     * throw an IOException.
     * @throws IOException Thrown if there is an exception while closing the
     *      underlying file IO.
     */
    @Override
    public void close() throws IOException;
    
    public boolean isOpen();
    public boolean isClosed();
    
    /**
     * Gets the current absolute byte position in the binlog file. If reading
     * this is the absolute byte position that was read.  If writing this is
     * the absolute byte position for the next write.  If the stream is based
     * on compressed data, please note that this value always represents the
     * uncompressed byte position.
     * @return The current absolute byte position
     */
    public long getPosition();
    
    /**
     * Returns whether the final total size of the binlog is known.  A binlog 
     * writer will return false until it is closed since additional records
     * may still be written.  A binlog reader may return either true or false
     * depending on how the binlog reader was created.  If the binlog reader
     * was opened from an uncompressed file, the total final size will be known
     * and return true.  If the binlog reader was opened by reading a stream,
     * the total final size will not be known until the binlog is closed.  In
     * all cases where the final size is unknown, getSize() will usually return
     * the current getPosition() value.
     * @return True if the final size is known otherwise false.
     */
    public boolean isFinalSizeKnown();
    
    /**
     * Gets the size (in bytes) of the binlog file.
     * @return The size (in bytes) of the binlog file.
     */
    public long getSize();
    
    public long getRecordHeaderSize();
    
    public long getRecordDataSize();
    
    /**
     * Gets the total number of records read or written. This total includes
     * both data and event records.
     * @return The total number of records read or written.
     * @see #getDataRecordCount()
     * @see #getEventRecordCount() 
     */
    public long getRecordCount();
    
    public long getDataRecordCount();
    
    public long getEventRecordCount();
    
}
