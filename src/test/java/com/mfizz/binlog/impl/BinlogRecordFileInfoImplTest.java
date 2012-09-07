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
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfizz
 */
public class BinlogRecordFileInfoImplTest {
    static private final Logger logger = LoggerFactory.getLogger(BinlogRecordFileInfoImplTest.class);
    
    @Test
    public void write() throws Exception {
        // for example: if the header was 1 byte (0x01) and the data was 1 byte (0x02)
        // and 2 bytes are used to store the record_length
        //  000400010102
        BinlogRecordFileInfo fileInfo = new BinlogRecordFileInfoImpl(0, 6, 1, 2);
        
        Assert.assertEquals(0, fileInfo.getPosition());
        Assert.assertEquals(6, fileInfo.getLength());
        Assert.assertEquals(4, fileInfo.getHeaderPosition());
        Assert.assertEquals(1, fileInfo.getHeaderLength());
        Assert.assertEquals(5, fileInfo.getDataPosition());
        Assert.assertEquals(1, fileInfo.getDataLength());
        
        //  0003000002
        fileInfo = new BinlogRecordFileInfoImpl(101, 5, 0, 2);
        
        Assert.assertEquals(101, fileInfo.getPosition());
        Assert.assertEquals(5, fileInfo.getLength());
        Assert.assertEquals(105, fileInfo.getHeaderPosition());
        Assert.assertEquals(0, fileInfo.getHeaderLength());
        Assert.assertEquals(105, fileInfo.getDataPosition());
        Assert.assertEquals(1, fileInfo.getDataLength());
        
        //  020002
        fileInfo = new BinlogRecordFileInfoImpl(101, 3, 0, 1);
        
        Assert.assertEquals(101, fileInfo.getPosition());
        Assert.assertEquals(3, fileInfo.getLength());
        Assert.assertEquals(103, fileInfo.getHeaderPosition());
        Assert.assertEquals(0, fileInfo.getHeaderLength());
        Assert.assertEquals(103, fileInfo.getDataPosition());
        Assert.assertEquals(1, fileInfo.getDataLength());
    }
}
