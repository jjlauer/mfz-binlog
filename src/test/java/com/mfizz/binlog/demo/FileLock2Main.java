package com.mfizz.binlog.demo;

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

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public final class FileLock2Main {
    private static final Logger logger = LoggerFactory.getLogger(FileLock2Main.class);
    
    public static void main(String[] argv) throws Exception {
        //ProtobufBinlogFileTranscoder transcoder = new ProtobufBinlogFileTranscoder();
        
        // create a sample file
        File f = new File("target", "filelocktest.txt");
        
        // create it again
        RandomAccessFile rafile1 = new RandomAccessFile(f, "rw");
        FileChannel fc1 = rafile1.getChannel();
        FileLock flock1 = fc1.tryLock();
        
        if (flock1 == null) {
            logger.info("File " + f.getName() + " NOT locked!");
        } else {
            logger.info("File " + f.getName() + " locked!");
        }
        
        System.console().readLine("Press any key to exit...");
    }
    
}