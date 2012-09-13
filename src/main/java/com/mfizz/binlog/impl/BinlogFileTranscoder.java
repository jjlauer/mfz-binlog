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

import com.mfizz.binlog.BinlogConstants;
import com.mfizz.binlog.BinlogException;
import com.mfizz.binlog.BinlogFileDeclaration;
import com.mfizz.binlog.BinlogFileHeader;
import com.mfizz.binlog.BinlogPropertyException;
import com.mfizz.binlog.BinlogRecordHeader;
import com.mfizz.binlog.type.TranscoderType;
import com.mfizz.util.HexUtil;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Abstract class that defines how "binlog file" parts such as intro and headers
 * are serialized and de-serialized.
 * 
 * @author mfizz
 */
public abstract class BinlogFileTranscoder {
    
    static public void validateBinlogFileDeclaration(BinlogFileDeclaration declaration) throws BinlogPropertyException {
        // magic bytes must always be equal
        if (!Arrays.equals(declaration.getMagicBytes(), BinlogConstants.MAGIC_BYTES)) {
            throw new BinlogPropertyException("Invalid binlog file declaration: magic_bytes [" + HexUtil.toHexString(declaration.getMagicBytes()) + "] do not equal expected [" + HexUtil.toHexString(BinlogConstants.MAGIC_BYTES) + "]");
        }
        if (declaration.getVersion() != BinlogConstants.VERSION) {
            throw new BinlogPropertyException("Invalid binlog file declaration: version [" + HexUtil.toHexString(declaration.getVersion()) + "] does not equal expected [" + HexUtil.toHexString(BinlogConstants.VERSION) + "]");
        }
        if (declaration.getTranscoderType() == null) {
            throw new BinlogPropertyException("Invalid binlog file declaration: transcoder_type is null");
        }
    }
    
    static public void validateBinlogFileHeader(BinlogFileHeader header) throws BinlogPropertyException {
        // by default, a file header *must* have a createDateTime, name, and contentType
        if (header.getCreateDateTime() == null) {
            throw new BinlogPropertyException("Invalid binlog file header: create_date_time is null");
        }
        if (header.getName() == null) {
            throw new BinlogPropertyException("Invalid binlog file header: name is null");
        }
        if (header.getContentType() == null) {
            throw new BinlogPropertyException("Invalid binlog file header: content_type is null");
        }
    }
    
    static public void validateBinlogRecordHeader(BinlogRecordHeader recordHeader) throws BinlogPropertyException {
        // by default, a record header doesn't need to have any properties
    }
    
    /**
     * Creates a ByteBuffer containing the binlog declaration and header. The
     * buffer will already be "flipped" which means the caller can write it
     * without needing to calling flip().
     */
    public ByteBuffer createByteBuffer(BinlogFileDeclaration declaration, BinlogFileHeader header) throws BinlogException {
        // write binlog file declaration and header
        byte[] declarationBytes = createByteArray(declaration);
        byte[] headerBytes = createByteArray(header);

        // declaration is always 6 bytes, then 4 for length, then header bytes length
        int length = declarationBytes.length + 4 + headerBytes.length;

        ByteBuffer buffer = ByteBuffer.allocate(length).order(ByteOrder.BIG_ENDIAN);
        buffer.put(declarationBytes);
        buffer.putInt(headerBytes.length);
        buffer.put(headerBytes);        
        buffer.flip();
        return buffer;
    }
    
    public byte[] createByteArray(BinlogFileDeclaration declaration) throws BinlogException {
        validateBinlogFileDeclaration(declaration);
        byte[] bytes = new byte[declaration.getMagicBytes().length + 2];
        int i = declaration.getMagicBytes().length;
        System.arraycopy(declaration.getMagicBytes(), 0, bytes, 0, i);
        bytes[i++] = declaration.getVersion();
        bytes[i++] = declaration.getTranscoderType().getId();
        return bytes;
    }  
    
    public byte[] createByteArray(BinlogFileHeader header) throws BinlogException {
        validateBinlogFileHeader(header);
        return doCreateByteArray(header);
    }
    
    public byte[] createByteArray(BinlogRecordHeader header) throws BinlogException {
        validateBinlogRecordHeader(header);
        return doCreateByteArray(header);
    }
    
    static public BinlogFileDeclaration createBinlogFileDeclaration(ByteBuffer buffer) throws BinlogException {
        if (buffer.remaining() < BinlogConstants.FILE_DECLARATION_BYTE_LENGTH) {
            throw new BinlogException("Invalid binlog file declaration: buffer did not contain at least [" + BinlogConstants.FILE_DECLARATION_BYTE_LENGTH + "] bytes");
        }
        
        byte[] magicBytes = new byte[BinlogConstants.MAGIC_BYTES.length];
        buffer.get(magicBytes);
        byte version = buffer.get();
        byte transcoderTypeId = buffer.get();
        
        BinlogFileDeclaration declaration = new BinlogFileDeclarationImpl(magicBytes, version, TranscoderType.get(transcoderTypeId));
        validateBinlogFileDeclaration(declaration);
        
        return declaration;
    }
    
    public BinlogFileHeader createBinlogFileHeader(byte[] data) throws BinlogException {
        BinlogFileHeader header = doCreateBinlogFileHeader(data);
        validateBinlogFileHeader(header);
        return header;
    }

    public BinlogRecordHeader createBinlogRecordHeader(byte[] data) throws BinlogException {
        BinlogRecordHeader header = doCreateBinlogRecordHeader(data);
        validateBinlogRecordHeader(header);
        return header;
    }
 
    // these are implemented by specific transcoders
    
    protected abstract byte[] doCreateByteArray(BinlogFileHeader header) throws BinlogException;
    
    protected abstract byte[] doCreateByteArray(BinlogRecordHeader header) throws BinlogException;
    
    protected abstract BinlogFileHeader doCreateBinlogFileHeader(byte[] data) throws BinlogException;
 
    protected abstract BinlogRecordHeader doCreateBinlogRecordHeader(byte[] data) throws BinlogException;

}
