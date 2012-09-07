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

import com.mfizz.binlog.protobuf.BinlogProtos;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public final class FileChannelMain {
    private static final Logger logger = LoggerFactory.getLogger(FileChannelMain.class);
    
    public static void main(String[] args) throws Exception {
        // command line should be file to work with
        if (args.length != 1) {
            throw new Exception("One command line argument is file to work with");
        }
        
        // file to test with
        File file = new File(args[0]);
        logger.debug("file: {}", file.getAbsolutePath());
        logger.debug("exists?: {}", file.exists());
        
        // open a file for both reading and writing
        RandomAccessFile rafile = new RandomAccessFile(file, "rw");
        logger.debug("current size: {}", file.length());
        
        // try to write some data to it (probably overwrite if we don't seek)
        FileChannel fc = rafile.getChannel();
        
        int record_count = 1;
        int bytes_written = 0;
        long start = System.currentTimeMillis();
        
        for (int i = 0; i < record_count; i++) {
        
            BinlogProtos.BinlogFileHeader header = BinlogProtos.BinlogFileHeader.newBuilder()
                    .setCreateDateTime(System.currentTimeMillis())
                    .setName("test-binlog")
                    .setContentType("application/stratus-binlog")
                    .addOptionalParameter(BinlogProtos.KeyValue.newBuilder().setKey("serverId").setValue("12").build())
                    .build();
            
            logger.debug("create-datetime: {}", header.getCreateDateTime());
            
            byte[] data = header.toByteArray();
            ByteBuffer bb = ByteBuffer.wrap(data);
            //logger.debug("writing [{}] bytes to file...", data.length);
            fc.write(bb);
            bytes_written += data.length;
        }
        
        fc.force(true);
        fc.close();
        long stop = System.currentTimeMillis();
        logger.debug("final size: {}", file.length());
        
        logger.debug("total bytes written: {}", bytes_written);
        logger.debug("time: {} ms", (stop-start)); 
    }
    
}