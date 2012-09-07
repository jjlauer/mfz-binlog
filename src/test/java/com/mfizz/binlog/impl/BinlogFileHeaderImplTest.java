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

import com.mfizz.binlog.BinlogPropertyException;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfizz
 */
public class BinlogFileHeaderImplTest {
    static private final Logger logger = LoggerFactory.getLogger(BinlogFileHeaderImplTest.class);
    
    @Test
    public void setRecordLengthByteLength() throws Exception {
        BinlogFileHeaderImpl header = new BinlogFileHeaderImpl();
        header.setRecordLengthByteLength(1);
        header.setRecordLengthByteLength(2);
        header.setRecordLengthByteLength(4);
        
        for (int i = -10; i < 20; i++) {
            // skip the valid ones
            if (i == 1 | i == 2 | i == 4) {
                continue;
            }
            try {
                header.setRecordLengthByteLength(8);
                Assert.fail("Did not fail on length of " + i);
            } catch (BinlogPropertyException e) {
                // correct behavior
            }
        }
        
    }
}
