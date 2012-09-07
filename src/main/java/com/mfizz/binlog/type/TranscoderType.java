package com.mfizz.binlog.type;

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

import com.mfizz.binlog.impl.BinlogFileTranscoder;
import com.mfizz.binlog.protobuf.ProtobufBinlogFileTranscoder;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mfizz
 */
public enum TranscoderType {
    
    PROTOBUF((byte)0x01, ProtobufBinlogFileTranscoder.class);
    
    private static final Map<Byte,TranscoderType> lookup = new HashMap<Byte,TranscoderType>();

    static {
        for (TranscoderType e : EnumSet.allOf(TranscoderType.class)) {
            lookup.put(e.getId(), e);
        }
    }
    
    private byte id;
    private Class<? extends BinlogFileTranscoder> type;

    private TranscoderType(byte id, Class<? extends BinlogFileTranscoder> type) {
        this.id = id;
        this.type = type;
    }

    public byte getId() {
        return id;
    }

    public static TranscoderType get(byte id) {
        return lookup.get(id);
    }
    
    public BinlogFileTranscoder createTranscoder() throws InstantiationException, IllegalAccessException {
        return this.type.newInstance();
    }
}
