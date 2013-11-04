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

import com.mfizz.binlog.type.DateTimePeriod;
import java.io.File;

/**
 *
 * @author mfizz
 */
public class BinloggerConfiguration {
    
    private String name;
    private File binlogDirectory;
    private String fileSuffix;
    private DateTimePeriod fileRotatePeriod;
    private boolean setFileNameAsHeaderNameEnabled;
    // enables [name].active file containing the active binlog filename
    private boolean activeFileEnabled;
    // how many "closed" binlogs to maintain as a history in memory
    private int maxClosedBinlogSize;
    // disable the shutdown hook
    private boolean shutdownHookDisabled;
    
    public BinloggerConfiguration() {
        this.name = "default";
        this.fileRotatePeriod = DateTimePeriod.NEVER;
        this.setFileNameAsHeaderNameEnabled = true;
        this.activeFileEnabled = true;
        this.maxClosedBinlogSize = 10;
        this.shutdownHookDisabled = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public File getBinlogDirectory() {
        return binlogDirectory;
    }

    public void setBinlogDirectory(File binlogDirectory) {
        this.binlogDirectory = binlogDirectory;
    }

    public DateTimePeriod getFileRotatePeriod() {
        return fileRotatePeriod;
    }

    public void setFileRotatePeriod(DateTimePeriod fileRotatePeriod) {
        this.fileRotatePeriod = fileRotatePeriod;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public boolean isSetFileNameAsHeaderNameEnabled() {
        return setFileNameAsHeaderNameEnabled;
    }

    public void setSetFileNameAsHeaderNameEnabled(boolean setFileNameAsHeaderNameEnabled) {
        this.setFileNameAsHeaderNameEnabled = setFileNameAsHeaderNameEnabled;
    }

    public boolean isActiveFileEnabled() {
        return activeFileEnabled;
    }

    public void setActiveFileEnabled(boolean activeFileEnabled) {
        this.activeFileEnabled = activeFileEnabled;
    }

    public int getMaxClosedBinlogSize() {
        return maxClosedBinlogSize;
    }

    public void setMaxClosedBinlogSize(int maxClosedBinlogSize) {
        if (maxClosedBinlogSize < 1) {
            throw new IllegalArgumentException("maxClosedBinlogSize must be >= 1");
        }
        this.maxClosedBinlogSize = maxClosedBinlogSize;
    }
    
    public boolean isShutdownHookDisabled() {
        return this.shutdownHookDisabled;
    }

    public void setShutdownHookDisabled(boolean value) {
        this.shutdownHookDisabled = value;
    }
}
