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
import java.io.FileInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public final class ProtobufReadMain {
    private static final Logger logger = LoggerFactory.getLogger(ProtobufReadMain.class);
    
    public static void main(String[] args) throws Exception {
        // command line should be file to work with
        if (args.length != 1) {
            throw new Exception("One command line argument is file to work with");
        }
        
        // file to test with
        File file = new File(args[0]);
        logger.debug("file: {}", file.getAbsolutePath());
        logger.debug("exists?: {}", file.exists());
        
        FileInputStream fis = new FileInputStream(file);
        
        BinlogProtos.BinlogFileHeader header = BinlogProtos.BinlogFileHeader.parseFrom(fis);
        
        logger.debug("header: createDateTime [{}] name [{}] contentType [{}]", new Object[] { header.getCreateDateTime(), header.getName(), header.getContentType() });
        for (BinlogProtos.KeyValue oh : header.getOptionalParameterList()) {
            logger.debug(" opt param: {}={}", oh.getKey(), oh.getValue());
        }
        
    }
    
}