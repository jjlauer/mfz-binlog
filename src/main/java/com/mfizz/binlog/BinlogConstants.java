package com.mfizz.binlog;

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

/**
 *
 * @author mfizz
 */
public class BinlogConstants {
    
    static public final byte[] MAGIC_BYTES = { (byte)'M', (byte)'F', (byte)'I', (byte)'Z', (byte)'Z', (byte)'.', (byte)'C', (byte)'O', (byte)'M', (byte)'-', (byte)'B', (byte)'I', (byte)'N', (byte)'L', (byte)'O', (byte)'G' };
    static public final byte VERSION = (byte)0x10;      // "1.0"
    
    static public final int FILE_DECLARATION_BYTE_LENGTH = MAGIC_BYTES.length + 2;
    static public final int MINIMUM_FILE_SIZE = FILE_DECLARATION_BYTE_LENGTH + 4;        // decl + file_header_length
    static public final int FILE_HEADER_LENGTH_BYTE_LENGTH = 4;
    static public final int DEFAULT_RECORD_LENGTH_BYTE_LENGTH = 2;
    
    static public final byte TRANSCODER_TYPE_PROTOBUF = (byte)0x01;
    
    
    static public final int TAG_LENGTH = 1;
    static public final int INT_TYPE_LENGTH = 4;
    static public final int LONG_TYPE_LENGTH = 8;
    static public final int SHORT_STRING_LENGTH_TYPE_LENGTH = 1;    // 255 bytes
    static public final int MEDIUM_STRING_LENGTH_TYPE_LENGTH = 2;   // 65535 bytes
    static public final int BYTE_ARRAY_LENGTH = 4;
    
    static public final byte TAG_HEADER_CREATE_DATETIME = (byte)1; // long
    static public final byte TAG_HEADER_NAME = (byte)2; // utf8_short_string
    static public final byte TAG_HEADER_TYPE = (byte)3; // utf8_short_string
    
    static public final byte TAG_RECORD_ID = (byte)1;   // int
    static public final byte TAG_RECORD_CREATE_DATETIME = (byte)2; // long
    static public final byte TAG_RECORD_DATA = (byte)3; // byte array
    
    static public final String HEADER_KEY_SERVER_ID = "server-id";
    
}
