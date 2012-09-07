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

import com.mfizz.binlog.BaseBinlogFile;
import com.mfizz.binlog.BinlogCorruptionException;
import com.mfizz.binlog.BinlogFileDeclaration;
import com.mfizz.binlog.BinlogFileHeader;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfizz
 */
public abstract class BinlogFileImpl implements BaseBinlogFile {
    private static final Logger logger = LoggerFactory.getLogger(BinlogFileImpl.class);
    
    // IO stuff
    protected File file;
    protected RandomAccessFile rafile;
    protected FileChannel fc;
    protected FileLock flock;
    // binlog file parts
    protected BinlogFileTranscoder transcoder;
    protected BinlogFileDeclaration declaration;
    protected BinlogFileHeader header;
    // common info tracked for each file
    protected long position;
    protected long size;
    protected long recordCount;
    protected long dataRecordCount;
    protected long eventRecordCount;
    protected long recordHeaderSize;
    protected long recordDataSize;
    
    public BinlogFileImpl(File file, RandomAccessFile rafile, FileChannel fc, BinlogFileTranscoder transcoder, BinlogFileDeclaration declaration, BinlogFileHeader header, boolean lockFile, long recordCount, long dataRecordCount, long eventRecordCount, long recordHeaderSize, long recordDataSize) throws IOException {
        this.file = file;
        this.rafile = rafile;
        this.fc = fc;
        this.transcoder = transcoder;
        this.declaration = declaration;
        this.header = header;
        this.recordCount = recordCount;
        this.dataRecordCount = dataRecordCount;
        this.eventRecordCount = eventRecordCount;
        this.recordHeaderSize = recordHeaderSize;
        this.recordDataSize = recordDataSize;
        /**
         * On Linux, this doesn't prevent a file delete; need another way to fix this
        if (lockFile) {
            // try to get an exclusive lock on this file -- if we can't we need
            // to throw an exception and give up!
            this.flock = this.fc.tryLock();
            if (this.flock == null) {
                throw new IOException("Unable to acquire exclusive file lock on [" + file.getAbsolutePath() + "]; usually means another program holds the lock");
            }
        }
         */
        // only if a filechannel was provided (otherwise this is likey from an inputstream)
        if (fc == null) {
            this.size = -1;                     // size of input isn't known
            this.position = -1;                 // position of input isn't known
        } else {
            try {
                this.size = fc.size();
                this.position = fc.position();
            } catch (IOException e) {
                // do nothing
            }
        }
    }
    
    public RandomAccessFile getRandomAccessFile() {
        return this.rafile;
    }
    
    public FileChannel getFileChannel() {
        return this.fc;
    }
    
    public BinlogFileTranscoder getTranscoder() {
        return this.transcoder;
    }
    
    public boolean isFileLocked() {
        return (this.flock != null);
    }

    @Override
    public File getFile() {
        return this.file;
    }
    
    @Override
    public BinlogFileDeclaration getDeclaration() {
        return this.declaration;
    }

    @Override
    public BinlogFileHeader getHeader() {
        return header;
    }
    
    @Override
    public boolean isOpen() {
        return (this.fc != null && this.fc.isOpen());
    }
    
    @Override
    public boolean isClosed() {
        return (this.fc == null);
    }
    
    @Override
    public void close() throws IOException {
        IOException saved = null;
        if (fc != null) {
            // flush data out
            fc.force(false);
            try {
                fc.close();
            } catch (IOException e) {
                saved = e;
            }
            fc = null;
        }
        if (rafile != null) {
            try { 
                rafile.close();
            } catch (IOException e) {
                if (saved != null) {
                    saved = e;
                }
            }
            rafile = null;
        }
        if (saved != null) {
            throw saved;
        }
    }
    
    @Override
    public long getPosition() {
        return this.position;
    }
    
    @Override
    public boolean isFinalSizeKnown() {
        // if binlog is closed, final size should always be known
        // if this is a binlog writer, this method is okay
        // if this is a binlog reader, a final size may be known (needs overridden)
        return isClosed();
    }
    
    @Override
    public long getSize() {
        return this.size;
    }
    
    public void truncate(long size) throws IOException {
        // set "position" first to where we want the new size
        this.fc.position(size);
        // now "truncate" the file to match this new size
        this.fc.truncate(size);
        // force the changes back out
        this.fc.force(true);
        this.size = this.fc.size();
        this.position = this.fc.position();
    }
    
    @Override
    public long getRecordCount() {
        return this.recordCount;
    }
    
    @Override
    public long getDataRecordCount() {
        return this.dataRecordCount;
    }
    
    @Override
    public long getEventRecordCount() {
        return this.eventRecordCount;
    }

    @Override
    public long getRecordDataSize() {
        return recordDataSize;
    }

    @Override
    public long getRecordHeaderSize() {
        return recordHeaderSize;
    }
     
    static public void verifyFileHasEnoughData(String parsingLocation, long filePosition, long requiredLength, long actualFileSize) throws BinlogCorruptionException {
        if (filePosition + requiredLength > actualFileSize) {
            long actualLength = actualFileSize - filePosition;
            throw new BinlogCorruptionException("Binlog file corrupted or does not contain enough data @ parsing_location [" + parsingLocation + "] file_position [" + filePosition + "] required_length [" + requiredLength + "] actual_length [" + actualLength + "] actual_file_size [" + actualFileSize + "]");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BinlogFileImpl other = (BinlogFileImpl) obj;
        if (this.file != other.file && (this.file == null || !this.file.equals(other.file))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.file != null ? this.file.hashCode() : 0);
        return hash;
    }
    
}
