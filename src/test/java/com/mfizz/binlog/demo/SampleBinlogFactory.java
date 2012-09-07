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

import com.mfizz.binlog.BinlogException;
import com.mfizz.binlog.BinlogFactory;
import com.mfizz.binlog.BinlogFileHeader;
import com.mfizz.binlog.BinlogPropertyException;
import com.mfizz.binlog.WritableBinlogFileHeader;

/**
 *
 * @author mfizz
 */
public class SampleBinlogFactory implements BinlogFactory {

    @Override
    public void createBinlogFileHeader(WritableBinlogFileHeader header) {
        header.setContentType("application/sample");
        header.setOptionalParameter("id", "12");
    }
    
    @Override
    public void verifyBinlogFileHeader(BinlogFileHeader header) throws BinlogException {
        if (!header.getContentType().equals("application/sample")) {
            throw new BinlogPropertyException("Header content-type [" + header.getContentType() + "] does not match expected [application/sample]");
        }
    }
    
}
