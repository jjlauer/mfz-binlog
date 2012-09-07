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

import com.cloudhopper.commons.util.StringUtil;
import com.mfizz.binlog.BinlogConstants;
import com.mfizz.binlog.BinlogPropertyException;
import com.mfizz.binlog.WritableBinlogFileHeader;
import java.util.LinkedHashMap;
import java.util.Map;
import org.joda.time.DateTime;

/**
 *
 * @author mfizz
 */
public class BinlogFileHeaderImpl implements WritableBinlogFileHeader {
    
    private DateTime createDateTime;
    private String name;
    private String contentType;
    private int recordLengthByteLength;
    private LinkedHashMap<String,String> optionalParameters;
    
    public BinlogFileHeaderImpl() {
        this(null);
    }
    
    public BinlogFileHeaderImpl(DateTime createDateTime) {
        this.createDateTime = createDateTime;
        this.recordLengthByteLength = BinlogConstants.DEFAULT_RECORD_LENGTH_BYTE_LENGTH;
        this.optionalParameters = new LinkedHashMap<String,String>();
    }

    @Override
    public DateTime getCreateDateTime() {
        return createDateTime;
    }

    @Override
    public void setCreateDateTime(DateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public int getRecordLengthByteLength() {
        return recordLengthByteLength;
    }

    @Override
    public void setRecordLengthByteLength(int recordLengthByteLength) throws BinlogPropertyException {
        // only 4 values accepted here (1, 2, or 4)
        switch (recordLengthByteLength) {
            case 1:
            case 2:
            case 4:
                break;
            default:
                throw new BinlogPropertyException("Only record_length byte lengths of 1, 2, or 4 accepted");
        }
        this.recordLengthByteLength = recordLengthByteLength;
    }

    @Override
    public void setOptionalParameter(String key, String value) {
        this.optionalParameters.put(key, value);
    }

    @Override
    public boolean hasOptionalParameter(String key) {
        return this.optionalParameters.containsKey(key);
    }

    @Override
    public String getOptionalParameter(String key) {
        return this.optionalParameters.get(key);
    }
    
    @Override
    public Map<String,String> getOptionalParameters() {
        return this.optionalParameters;
        
    }

    @Override
    public void setOptionalParameters(Map<String, String> optionalParameters) {
        this.optionalParameters.putAll(optionalParameters);
    }

    @Override
    public int getServerId() throws BinlogPropertyException {
        if (this.optionalParameters == null || this.optionalParameters.isEmpty()) {
            throw new BinlogPropertyException("binlog file header has no optional parameters; server-id does not exist");
        }
        
        String serverIdText = getOptionalParameter("server-id");
        if (serverIdText == null || serverIdText.equals("")) {
            throw new BinlogPropertyException("binlog file header server-id key exists but the value was empty!");
        }
        try {
            return Integer.valueOf(serverIdText);
        } catch (NumberFormatException e) {
            throw new BinlogPropertyException("unable to parse server-id [" + serverIdText + "] into an int");
        }
    }

    @Override
    public boolean hasServerId() {
        return hasOptionalParameter(BinlogConstants.HEADER_KEY_SERVER_ID);
    }

    @Override
    public void setServerId(int serverId) {
        setOptionalParameter(BinlogConstants.HEADER_KEY_SERVER_ID, Integer.toString(serverId));
    }
    
}
