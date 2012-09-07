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

import com.mfizz.binlog.FileUtil;
import java.io.File;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfizz
 */
public class BinloggerActiveFileWriterTest {
    static private final Logger logger = LoggerFactory.getLogger(BinloggerActiveFileWriterTest.class);
    
    @Test
    public void write() throws Exception {
        File f = FileUtil.createTempFile();
        
        String longString = "This is a long string";
        String shortString = "short string";
        
        BinloggerActiveFileWriter afw = new BinloggerActiveFileWriter(f);
        
        afw.write(longString);
        
        // confirm this was written
        byte[] bytes0 = FileUtil.readFileFully(f);
        Assert.assertArrayEquals(longString.getBytes("UTF-8"), bytes0);
        
        afw.write(shortString);
        
        // confirm this was written
        byte[] bytes1 = FileUtil.readFileFully(f);
        Assert.assertArrayEquals(shortString.getBytes("UTF-8"), bytes1);
    }
}
