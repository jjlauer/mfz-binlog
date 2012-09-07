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
import com.mfizz.binlog.BinlogRecordHeader;
import com.mfizz.binlog.BinlogRecordHeaderEvent;
import com.mfizz.binlog.WritableBinlogRecord;

/**
 *
 * @author mfizz
 */
public class BinlogRecordImpl implements WritableBinlogRecord {
    
    private BinlogRecordHeader header;
    private byte[] data;
    private BinlogRecordFileInfo fileInfo;
    
    public BinlogRecordImpl(BinlogRecordHeader header, byte[] data) {
        this(header, data, null);
    }

    public BinlogRecordImpl(BinlogRecordHeader header, byte[] data, BinlogRecordFileInfo fileInfo) {
        this.header = header;
        this.data = data;
        this.fileInfo = fileInfo;
    }
    
    @Override
    public BinlogRecordHeader getHeader() {
        return header;
    }

    @Override
    public void setHeader(BinlogRecordHeader header) {
        this.header = header;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public BinlogRecordFileInfo getFileInfo() {
        return fileInfo;
    }

    @Override
    public void setFileInfo(BinlogRecordFileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    @Override
    public boolean isEvent() {
        if (this.header == null) {
            return false;
        } else {
            return this.header.isEvent();
        }
    }

    @Override
    public boolean isRecord() {
        if (this.header == null) {
            return true;
        } else {
            return this.header.isRecord();
        }
    }

    @Override
    public BinlogRecordHeaderEvent getEvent() {
        if (this.header == null) {
            return null;
        } else {
            return this.header.getEvent();
        }
    }
}
