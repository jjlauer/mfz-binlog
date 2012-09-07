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
public interface BinlogFileHeader {

    public DateTime getCreateDateTime();

    public String getName();

    public String getContentType();
    
    public int getRecordLengthByteLength();
    
    //public int getRecordLengthByteWidth();
    
    /**
     * Utility method for checking if an optional parameter value for key "server-id"
     * is already set on this header.
     */
    public boolean hasServerId();
    
    /**
     * Utility method for getting an optional parameter value for key "server-id".
     * Many applications that use this library now rely on a "server-id" value
     * which is why a utility method is now included by default.
     * @return The integer value of the optional parameter "server-id".
     * @throws BinlogPropertyException Thrown if "server-id" key is not in current
     *      list of optional parameters.
     */
    public int getServerId() throws BinlogPropertyException;
    
    /**
     * Utility method for setting an optional parameter value for key "server-id".
     * Many applications that use this library now rely on a "server-id" value
     * which is why a utility method is now included by default.
     * @param serverId The server id value
     */
    public void setServerId(int serverId);
    
    public boolean hasOptionalParameter(String key);
    
    public String getOptionalParameter(String key);
    
    public Map<String,String> getOptionalParameters();
    
}
