package com.mfizz.binlog.type;

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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mfizz
 */
public enum EventType {
    
    /**
     * Binlogger framework started.
     */
    BINLOGGER_STARTED((byte)0x01, "binlogger-started"),
    /**
     * Binlogger framework stopped.
     */
    BINLOGGER_STOPPED((byte)0x02, "binlogger-stopped"),
    /**
     * Binlogger "heartbeat" event to make sure active binlog works.
     */
    BINLOGGER_HEARTBEAT((byte)0x03, "binlogger-heartbeat"),
    /**
     * Binlogger rotate to this next binlog filename.
     */
    BINLOGGER_ROTATE_NEXT((byte)0x04, "binlogger-rotate-next"),
    /**
     * Binlogger rotate from this previous binlog filename.
     */
    BINLOGGER_ROTATE_PREV((byte)0x05, "binlogger-rotate-prev"),
    /**
     * Binlogger opened (either create or append) this binlog.
     */
    BINLOGGER_OPENED((byte)0x06, "binlogger-opened"),
    /**
     * Binlogger closed this binlog.
     */
    BINLOGGER_CLOSED((byte)0x07, "binlogger-closed");
    
    private static final Map<Integer,EventType> lookup = new HashMap<Integer,EventType>();

    static {
        for (EventType e : EnumSet.allOf(EventType.class)) {
            lookup.put(e.getId(), e);
        }
    }
    
    private int id;
    private String name;

    private EventType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public static EventType get(int id) {
        return lookup.get(id);
    }
}
