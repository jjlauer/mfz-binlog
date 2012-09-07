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

import java.io.Flushable;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author mfizz
 */
public interface Binlogger extends Flushable {
    
    public BinloggerConfiguration getConfiguration();
    
    public BaseBinlogFile getActiveBinlog();
    
    public Collection<BaseBinlogFile> getPendingCloseBinlogs();
    
    /**
     * Gets the history of previously used binlogs that are now closed. Only
     * the last X number of binlogs are kept in memory up to configuration.getMaxClosedBinlogSize().
     * The order of the collection is the order of oldest-to-newest (or in
     * alphabetical order).
     */
    public Collection<BaseBinlogFile> getClosedBinlogs();
    
    public void start() throws IOException, BinlogException, InterruptedException;
    
    public void stop() throws IOException, BinlogException, InterruptedException;
    
    /**
     * Flushes currently active binlog.
     * @throws IOException Thrown if there is an exception while attempting to
     *      flush the underlying active binlog.
     */
    @Override
    public void flush() throws IOException;
    
    public BinlogRecordFileInfo write(byte[] data) throws IOException, BinlogException, InterruptedException;

    public BinlogRecordFileInfo write(byte[] data, Integer userDefinedType) throws IOException, BinlogException, InterruptedException;
    
    public BinlogRecordFileInfo write(byte[] data, Map<String,String> optionalParameters) throws IOException, BinlogException, InterruptedException;
    
    public BinlogRecordFileInfo write(byte[] data, Integer userDefinedType, Map<String,String> optionalParameters) throws IOException, BinlogException, InterruptedException;
    
}
