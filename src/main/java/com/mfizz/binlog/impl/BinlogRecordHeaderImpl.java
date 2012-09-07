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

import com.mfizz.binlog.BinlogRecordHeaderEvent;
import com.mfizz.binlog.WritableBinlogRecordHeader;
import java.util.LinkedHashMap;
import java.util.Map;
import org.joda.time.DateTime;

/**
 *
 * @author mfizz
 */
public class BinlogRecordHeaderImpl implements WritableBinlogRecordHeader {
    
    private Integer id;
    private DateTime createDateTime;
    private Integer userDefinedType;
    private LinkedHashMap<String,String> optionalParameters;
    private BinlogRecordHeaderEvent event;
    
    public BinlogRecordHeaderImpl() {
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
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getUserDefinedType() {
        return userDefinedType;
    }

    @Override
    public void setUserDefinedType(Integer userDefinedType) {
        this.userDefinedType = userDefinedType;
    }
    
    @Override
    public void setOptionalParameter(String key, String value) {
        this.optionalParameters.put(key, value);
    }

    @Override
    public void setOptionalParameters(Map<String, String> optionalParameters) {
        this.optionalParameters.putAll(optionalParameters);
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
    public boolean isEvent() {
        return (this.event != null);
    }

    @Override
    public boolean isRecord() {
        return (this.event == null);
    }

    @Override
    public BinlogRecordHeaderEvent getEvent() {
        return this.event;
    }

    @Override
    public void setEvent(BinlogRecordHeaderEvent event) {
        this.event = event;
    }
    
    /**
    @Override
    public String toString() {
        StringBuilder to = new StringBuilder(200);
        to.append("create_date_time [").append(this.createDateTime);
    }
     */
    
}
