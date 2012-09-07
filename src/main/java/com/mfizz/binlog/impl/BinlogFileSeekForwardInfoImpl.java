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

import com.mfizz.binlog.BinlogFileSeekForwardInfo;

/**
 *
 * @author mfizz
 */
public class BinlogFileSeekForwardInfoImpl implements BinlogFileSeekForwardInfo {

    private final int skippedRecordCount;
    private final long targetFilePosition;
    private final long readFilePosition;

    public BinlogFileSeekForwardInfoImpl(int skippedRecordCount, long targetFilePosition, long readFilePosition) {
        this.skippedRecordCount = skippedRecordCount;
        this.targetFilePosition = targetFilePosition;
        this.readFilePosition = readFilePosition;
    }
    
    @Override
    public long getReadFilePosition() {
        return readFilePosition;
    }

    @Override
    public int getSkippedRecordCount() {
        return skippedRecordCount;
    }

    @Override
    public long getTargetFilePosition() {
        return targetFilePosition;
    }

}
