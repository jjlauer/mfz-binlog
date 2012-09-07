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
public interface BinlogRecordFileInfo {
    
    /**
     * Gets the absolute position in the binlog file where record_length byte starts.
     */
    public long getPosition();
    
    /**
     * Gets the length of the entire record including the record_length, 
     * record_header_length, header, and data parts.
     */
    public long getLength();
    
    public long getHeaderPosition();
    
    public long getHeaderLength();
    
    public long getDataPosition();
    
    public long getDataLength();
    
}
