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

import java.lang.ref.WeakReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfizz
 */
public class BinloggerFileRotaterRunner implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(BinloggerFileRotaterRunner.class);

    private final WeakReference<BinloggerImpl> binloggerRef;
    
    public BinloggerFileRotaterRunner(BinloggerImpl binlogger) {
        this.binloggerRef = new WeakReference<BinloggerImpl>(binlogger);
    }
    
    @Override
    public void run() {
        // when this method gets called, we need to attempt a rotate
        // but only if the parent binlogger still exists!
        BinloggerImpl binlogger = this.binloggerRef.get();
        if (binlogger == null) {
            logger.error("Weak reference to BinloggerImpl was null, parent binlogger was not properly stopped() and this reference was lost!");
            return;
        }
        
        long now = System.currentTimeMillis();
        long nextRotateMillis = binlogger.getRotater().rotate(now, binlogger);
        binlogger.scheduleNextRotateAttempt(nextRotateMillis);
    }
    
}
