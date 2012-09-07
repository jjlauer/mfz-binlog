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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 *
 * @author mfizz
 */
public class EmptyTestBinlogFactory implements BinlogFactory {

    @Override
    public void createBinlogFileHeader(WritableBinlogFileHeader header) {
        header.setName("");
        header.setContentType("");
        header.setCreateDateTime(new DateTime(2011, 6, 1, 4, 4, 0, 0, DateTimeZone.UTC));
    }
    
    @Override
    public void verifyBinlogFileHeader(BinlogFileHeader header) throws BinlogException {
        if (!header.getName().equals("")) {
            throw new BinlogPropertyException("Header name [" + header.getName() + "] does not match expected []");
        }
        if (!header.getContentType().equals("")) {
            throw new BinlogPropertyException("Header content-type [" + header.getContentType() + "] does not match expected []");
        }
    }
}
