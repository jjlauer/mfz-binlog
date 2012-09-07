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
public interface BinlogFactory {
    
    /**
     * Finish creating a new BinlogFileHeader by setting any properties this
     * factory wants to include in new binlog files.
     * @param header The unfinished header with only a minimum number of properties
     *      set by this framework.
     */
    public void createBinlogFileHeader(WritableBinlogFileHeader header);
    
    /**
     * Verifies a BinlogFileHeader is valid (usually during the open() method).
     * It is suggested to verify the header.getContentType() matches what is
     * expected, optional parameters that are requested are present, etc.
     * @param header The header to verify
     * @throws BinlogException Throw if the header is not valid.
     */
    public void verifyBinlogFileHeader(BinlogFileHeader header) throws BinlogException;
    
}
