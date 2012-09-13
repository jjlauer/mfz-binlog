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

import com.mfizz.util.DateTimeUtil;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author mfizz
 */
public enum DateTimePeriod {
    
    // pattern, number of millis in this period
    NEVER(null, 0L),
    MINUTE("yyyyMMdd-HHmm", 1L*60L*1000L),
    FIVE_MINUTES("yyyyMMdd-HHmm", 5L*60L*1000L),
    TEN_MINUTES("yyyyMMdd-HHmm", 10L*60L*1000L),
    QUARTER_HOUR("yyyyMMdd-HHmm", 15L*60L*1000L),
    HALF_HOUR("yyyyMMdd-HHmm", 30L*60L*1000L),
    HOUR("yyyyMMdd-HH", 60L*60L*1000L),
    DAY("yyyyMMdd", 24L*60L*60L*1000L);
    
    private String pattern;
    private DateTimeFormatter formatter;
    private long millis;
    
    private DateTimePeriod(String pattern, long millis) {
        this.pattern = pattern;
        if (pattern != null) {
            this.formatter = DateTimeFormat.forPattern(pattern).withZone(DateTimeZone.UTC);
        }
        this.millis = millis;
    }

    /**
     * Gets the pattern to use to display this timestamp with the minimum number
     * of variables.
     */
    public String getPattern() {
        return this.pattern;
    }
    
    /**
     * Get the formatter to display the minimum pattern required to accurately
     * format timestamps of this period.  For example, if the period represents
     * an HOUR, then a format of "yyyyMMdd-HH" would be used.
     * @return The formatter to use to print timestamps as strings.
     */
    public DateTimeFormatter getFormatter() {
        return this.formatter;
    }

    /**
     * Get the number of milliseconds in this period.  This amount is open-ended
     * such that [first timestamp in period] + [millis] = [first timestamp in next period].
     * For example, a MINUTE would return 60000 milliseconds.
     * @return The number of milliseconds in this period.
     */
    public long getMillis() {
        return millis;
    }
    
    /**
     * Calculates the first timestamp in the period representing the given
     * currentTimeMillis.
     */
    public long floorMillis(long currentTimeMillis) {
        DateTime dt = new DateTime(currentTimeMillis, DateTimeZone.UTC);
        if (this == MINUTE) {
            return DateTimeUtil.floorToMinute(dt).getMillis();
        } else if (this == FIVE_MINUTES) {
            return DateTimeUtil.floorToFiveMinutes(dt).getMillis();
        } else if (this == TEN_MINUTES) {
            return DateTimeUtil.floorToTenMinutes(dt).getMillis();
        } else if (this == QUARTER_HOUR) {
            return DateTimeUtil.floorToQuarterHour(dt).getMillis();
        } else if (this == HALF_HOUR) {
            return DateTimeUtil.floorToHalfHour(dt).getMillis();
        } else if (this == HOUR) {
            return DateTimeUtil.floorToHour(dt).getMillis();
        } else if (this == DAY) {
            return DateTimeUtil.floorToDay(dt).getMillis();
        } else {
            return currentTimeMillis;
        }
    }
}
