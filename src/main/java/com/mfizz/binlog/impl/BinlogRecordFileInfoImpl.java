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

import com.mfizz.binlog.BinlogRecordFileInfo;

/**
 *
 * @author mfizz
 */
public class BinlogRecordFileInfoImpl implements BinlogRecordFileInfo {

    private final long position;
    private final long length;
    private final long headerLength;
    private final int recordLengthByteLength;
    
    public BinlogRecordFileInfoImpl(long position, long length, long headerLength, int recordLengthByteLength) {
        this.position = position;
        this.length = length;
        this.headerLength = headerLength;
        this.recordLengthByteLength = recordLengthByteLength;
        // everything else can be calculated from these 4
    }
    
    @Override
    public long getPosition() {
        return this.position;
    }

    @Override
    public long getLength() {
        return this.length;
    }

    @Override
    public long getHeaderPosition() {
        return this.position + this.recordLengthByteLength + this.recordLengthByteLength;
    }

    @Override
    public long getHeaderLength() {
        return this.headerLength;
    }

    @Override
    public long getDataPosition() {
        return getHeaderPosition() + this.headerLength;
    }

    @Override
    public long getDataLength() {
        return this.length - this.headerLength - this.recordLengthByteLength - this.recordLengthByteLength;
    }
    
}
