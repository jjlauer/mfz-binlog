package com.mfizz.binlog.protobuf;

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

import com.mfizz.binlog.BinlogException;
import com.mfizz.binlog.BinlogFileHeader;
import com.mfizz.binlog.BinlogRecordHeader;
import com.mfizz.binlog.BinlogRecordHeaderEvent;
import com.mfizz.binlog.WritableBinlogFileHeader;
import com.mfizz.binlog.WritableBinlogRecordHeader;
import com.mfizz.binlog.WritableBinlogRecordHeaderEvent;
import com.mfizz.binlog.impl.BinlogFileHeaderImpl;
import com.mfizz.binlog.impl.BinlogFileTranscoder;
import com.mfizz.binlog.impl.BinlogRecordHeaderEventImpl;
import com.mfizz.binlog.impl.BinlogRecordHeaderImpl;
import com.mfizz.binlog.type.EventType;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfizz
 */
public class ProtobufBinlogFileTranscoder extends BinlogFileTranscoder {
    static private final Logger logger = LoggerFactory.getLogger(ProtobufBinlogFileTranscoder.class);
    
    public ProtobufBinlogFileTranscoder() {
        // do nothing
    }
    
    @Override
    public byte[] doCreateByteArray(BinlogFileHeader header) throws BinlogException {
        // build protobuf object representation first
        BinlogProtos.BinlogFileHeader.Builder headerBuilder = BinlogProtos.BinlogFileHeader.newBuilder();
        
        // all 3 properties were validated in BinlogFileTranscoder.validateBinlogFileHeader()
        headerBuilder.setRecordLengthByteLength(header.getRecordLengthByteLength());
        headerBuilder.setCreateDateTime(header.getCreateDateTime().getMillis());
        headerBuilder.setName(header.getName());
        headerBuilder.setContentType(header.getContentType());
        // set all optional parameters
        for (Map.Entry<String,String> entry : header.getOptionalParameters().entrySet()) {
            BinlogProtos.KeyValue.Builder kvBuilder = BinlogProtos.KeyValue.newBuilder();
            kvBuilder.setKey(entry.getKey());
            kvBuilder.setValue(entry.getValue());
            headerBuilder.addOptionalParameter(kvBuilder.build());
        }
        
        BinlogProtos.BinlogFileHeader protobufHeader = headerBuilder.build();
        
        return protobufHeader.toByteArray();
    }

    @Override
    protected BinlogFileHeader doCreateBinlogFileHeader(byte[] data) throws BinlogException {
        try {
            BinlogProtos.BinlogFileHeader protobufHeader = BinlogProtos.BinlogFileHeader.parseFrom(data);
            
            WritableBinlogFileHeader header = new BinlogFileHeaderImpl();
            if (protobufHeader.hasRecordLengthByteLength()) {
                header.setRecordLengthByteLength(protobufHeader.getRecordLengthByteLength());
            }
            if (protobufHeader.hasCreateDateTime()) {
                // always create a DateTime in UTC
                header.setCreateDateTime(new DateTime(protobufHeader.getCreateDateTime(), DateTimeZone.UTC));
            }
            if (protobufHeader.hasName()) {
                header.setName(protobufHeader.getName());
            }
            if (protobufHeader.hasContentType()) {
                header.setContentType(protobufHeader.getContentType());
            }
            for (BinlogProtos.KeyValue protobufKeyValue : protobufHeader.getOptionalParameterList()) {
                // check for duplicates (maybe warn here?)
                if (header.hasOptionalParameter(protobufKeyValue.getKey())) {
                    logger.warn("Duplicate key [" + protobufKeyValue.getKey() + "] found in binlog file header; the value will be overwritten with the new one");
                }
                header.setOptionalParameter(protobufKeyValue.getKey(), protobufKeyValue.getValue());
            }
            return header;
        } catch (Exception e) {
            throw new BinlogException("Unable to convert bytes into BinlogFileHeader: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] doCreateByteArray(BinlogRecordHeader header) throws BinlogException {
        // build protobuf object representation first
        BinlogProtos.BinlogRecordHeader.Builder headerBuilder = BinlogProtos.BinlogRecordHeader.newBuilder();
        
        // no properties are technically required
        if (header.getId() != null) {
            headerBuilder.setId(header.getId().intValue());
        }
        
        if (header.getCreateDateTime() != null) {
            headerBuilder.setCreateDateTime(header.getCreateDateTime().getMillis());
        }
        
        if (header.getUserDefinedType() != null) {
            headerBuilder.setUserDefinedType(header.getUserDefinedType().intValue());
        }
        
        // set all optional parameters
        for (Map.Entry<String,String> entry : header.getOptionalParameters().entrySet()) {
            BinlogProtos.KeyValue.Builder kvBuilder = BinlogProtos.KeyValue.newBuilder();
            kvBuilder.setKey(entry.getKey());
            kvBuilder.setValue(entry.getValue());
            headerBuilder.addOptionalParameter(kvBuilder.build());
        }
        
        // event?
        if (header.isEvent()) {
            BinlogRecordHeaderEvent event = header.getEvent();
            if (event.getId() != null) {
                headerBuilder.setEventId(event.getId());
            }
            if (event.getName() != null) {
                headerBuilder.setEventName(event.getName());
            }
            if (event.getInfo() != null) {
                headerBuilder.setEventInfo(event.getInfo());
            }
        }
        
        BinlogProtos.BinlogRecordHeader protobufHeader = headerBuilder.build();
        
        return protobufHeader.toByteArray();
    }
    
    @Override
    protected BinlogRecordHeader doCreateBinlogRecordHeader(byte[] data) throws BinlogException {
        try {
            BinlogProtos.BinlogRecordHeader protobufHeader = BinlogProtos.BinlogRecordHeader.parseFrom(data);
            
            WritableBinlogRecordHeader header = new BinlogRecordHeaderImpl();
            if (protobufHeader.hasCreateDateTime()) {
                // always create a DateTime in UTC
                header.setCreateDateTime(new DateTime(protobufHeader.getCreateDateTime(), DateTimeZone.UTC));
            }
            if (protobufHeader.hasId()) {
                header.setId(protobufHeader.getId());
            }
            if (protobufHeader.hasUserDefinedType()) {
                header.setUserDefinedType(protobufHeader.getUserDefinedType());
            }
            for (BinlogProtos.KeyValue protobufKeyValue : protobufHeader.getOptionalParameterList()) {
                // check for duplicates (maybe warn here?)
                if (header.hasOptionalParameter(protobufKeyValue.getKey())) {
                    logger.warn("Duplicate key [" + protobufKeyValue.getKey() + "] found in binlog record header; the value will be overwritten with the new one");
                }
                header.setOptionalParameter(protobufKeyValue.getKey(), protobufKeyValue.getValue());
            }
            
            // does this have an event?
            if (protobufHeader.hasEventId()) {
                // create an event
                WritableBinlogRecordHeaderEvent event = new BinlogRecordHeaderEventImpl();
                event.setId(protobufHeader.getEventId());
                
                // try to map types
                event.setType(EventType.get(event.getId()));
                
                if (protobufHeader.hasEventName()) {
                    event.setName(protobufHeader.getEventName());
                }
                if (protobufHeader.hasEventInfo()) {
                    event.setInfo(protobufHeader.getEventInfo());
                }
                
                header.setEvent(event);
            }
            
            return header;
        } catch (Exception e) {
            throw new BinlogException("Unable to convert bytes into BinlogRecordHeader: " + e.getMessage(), e);
        }
    }
    
}
