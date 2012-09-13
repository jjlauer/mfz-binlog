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

import com.mfizz.util.HexUtil;
import com.mfizz.binlog.WritableBinlogFileDeclaration;
import com.mfizz.binlog.type.TranscoderType;

/**
 *
 * @author mfizz
 */
public class BinlogFileDeclarationImpl implements WritableBinlogFileDeclaration {

    private byte[] magicBytes;
    private byte version;
    private TranscoderType transcoderType;

    public BinlogFileDeclarationImpl() {
        // do nothing
    }
    
    public BinlogFileDeclarationImpl(byte[] magicBytes, byte version, TranscoderType transcoderType) {
        this.magicBytes = magicBytes;
        this.version = version;
        this.transcoderType = transcoderType;
    }

    @Override
    public byte[] getMagicBytes() {
        return magicBytes;
    }

    @Override
    public void setMagicBytes(byte[] magicBytes) {
        this.magicBytes = magicBytes;
    }

    @Override
    public TranscoderType getTranscoderType() {
        return transcoderType;
    }

    @Override
    public void setTranscoderType(TranscoderType transcoderType) {
        this.transcoderType = transcoderType;
    }

    @Override
    public byte getVersion() {
        return version;
    }

    @Override
    public void setVersion(byte version) {
        this.version = version;
    }

    @Override
    public String getVersionString() {
        String t = HexUtil.toHexString(this.version);
        return t.substring(0, 1) + "." + t.substring(1);
    }
    
}
