package com.mfizz.binlog.util;

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

/**
 *
 * @author mfizz
 */
public class UnsignedUtil {
    
    public static short byteToUnsigned(byte signed) {
        if (signed >= 0) {
            return signed;
        } else {
            return (short) (256 + (short) signed);
        }
    }
    
    public static int shortToUnsigned(short signed)
    {
        if (signed >= 0) {
            return signed;
        } else {
            return (int)(65536+(int)signed);
        }
    }
    
    public static long intToUnsigned(int signed)
    {
        if (signed >= 0) {
            return signed;
        } else {
            return (long)(4294967296L+(long)signed);
        }
    }
    
}
