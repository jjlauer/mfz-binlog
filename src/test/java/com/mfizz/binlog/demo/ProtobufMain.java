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

import com.mfizz.binlog.protobuf.ProtobufBinlogFileTranscoder;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public final class ProtobufMain {
    private static final Logger logger = LoggerFactory.getLogger(ProtobufMain.class);
    
    public static void main(String[] argv) throws Exception {
        //ProtobufBinlogFileTranscoder transcoder = new ProtobufBinlogFileTranscoder();
        
        // create a sample file
        File f = new File("target", "filelocktest.txt");
        RandomAccessFile rafile = new RandomAccessFile(f, "rw");
        rafile.writeUTF("test");
        rafile.close();
        
        // create it again
        RandomAccessFile rafile1 = new RandomAccessFile(f, "rw");
        FileChannel fc1 = rafile1.getChannel();
        FileLock flock1 = fc1.lock();
        
        
        /**
        WritableBinlogFileHeader header = new BinlogFileHeaderImpl();
        header.setName("test-name");
        header.setCreateDateTime(new DateTime(2011, 6, 1, 5, 0, 0, 0));
        header.setContentType("test-content-type");
        header.setRecordLengthByteLength(1);
        
        // add one optional parameter
        //header.setOptionalParameter("key1", "value1");
        //header.setOptionalParameter("key2", "value2");
        
        byte[] data = transcoder.doCreateByteArray(header);
        logger.info(HexUtil.toHexString(data));
         */
        
        /**
        WritableBinlogRecordHeader recordHeader = new BinlogRecordHeaderImpl();
        recordHeader.setId(1234567890);
        recordHeader.setCreateDateTime(new DateTime(2011, 6, 1, 5, 0, 0, 0));
        recordHeader.setUserDefinedType(9);
        */
        
        /**
        DateTime ts = new DateTime(2011, 6, 1, 5, 0, 0, 0, DateTimeZone.UTC);
        BinlogRecordHeader recordHeader = BinlogFileWriterImpl.createRecordHeaderForEvent(ts, EventType.BINLOGGER_STARTED, null);
        
        byte[] data = transcoder.doCreateByteArray(recordHeader);
        logger.info(HexUtil.toHexString(data));
        */
         
        /**
        BinlogProtos.BinlogHeader header = BinlogProtos.BinlogHeader.newBuilder()
                .setId(1)
                .setFirstName("Joe")
                .setLastName("Lauer")
                .build();
        
        
        logger.debug("header: {}", header);
        logger.debug("header: {}", HexUtil.toHexString(header.toByteArray()));
         */
        
        
        /**
        Kryo kryo = new Kryo();
        kryo.register(Person.class);
        
        ByteBuffer buffer = ByteBuffer.allocate(256);
        
        Person person0 = new Person("Joe", null);
        kryo.writeObject(buffer, person0);
        
        logger.debug("person0: " + HexUtil.toHexString(buffer.array(), 0, buffer.position()));
        
        //SomeClass someObject = kryo.readObject(buffer, SomeClass.class);
         */
    }
    
}