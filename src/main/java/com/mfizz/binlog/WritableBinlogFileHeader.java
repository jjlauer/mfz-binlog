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

import java.util.Map;
import org.joda.time.DateTime;

/**
 *
 * @author mfizz
 */
public interface WritableBinlogFileHeader extends BinlogFileHeader {

    public void setCreateDateTime(DateTime dt);

    public void setName(String name);

    public void setContentType(String contentType);
    
    public void setRecordLengthByteLength(int byteLength) throws BinlogPropertyException;
    
    public void setOptionalParameter(String key, String value);
    
    public void setOptionalParameters(Map<String,String> optionalParameters);
    
}
