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

import com.mfizz.binlog.type.EventType;
import java.io.Flushable;
import java.io.IOException;
import java.util.Map;
import org.joda.time.DateTime;

/**
 *
 * @author mfizz
 */
public interface BinlogFileWriter extends BaseBinlogFile, Flushable {

    public int getCheckouts();
    
    public int checkout();
    
    public int checkin();
    
    @Override
    public void flush() throws IOException;
    
    public BinlogRecordFileInfo write(byte[] data) throws IOException, BinlogException;

    public BinlogRecordFileInfo write(byte[] data, Integer userDefinedType) throws IOException, BinlogException;
    
    public BinlogRecordFileInfo write(byte[] data, Map<String,String> optionalParameters) throws IOException, BinlogException;
    
    public BinlogRecordFileInfo write(byte[] data, Integer userDefinedType, Map<String,String> optionalParameters) throws IOException, BinlogException;
    
    public BinlogRecordFileInfo write(BinlogRecordHeader recordHeader, byte[] data) throws IOException, BinlogException;
    
    public BinlogRecordFileInfo writeEvent(EventType type) throws IOException, BinlogException;
    
    public BinlogRecordFileInfo writeEvent(EventType type, DateTime timestamp) throws IOException, BinlogException;
    
    public BinlogRecordFileInfo writeEvent(EventType type, DateTime timestamp, String info) throws IOException, BinlogException;
    
    public BinlogRecordFileInfo writeEvent(EventType type, DateTime timestamp, String info, Map<String,String> optionalParameters) throws IOException, BinlogException;
    
}
