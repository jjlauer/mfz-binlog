package com.mfizz.binlog.demo;

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

import com.cloudhopper.commons.util.HexUtil;
import com.esotericsoftware.kryo.Kryo;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public final class KryoMain {
    private static final Logger logger = LoggerFactory.getLogger(KryoMain.class);
    
    public static void main(String[] argv) {
        Kryo kryo = new Kryo();
        kryo.register(Person.class);
        
        ByteBuffer buffer = ByteBuffer.allocate(256);
        
        Person person0 = new Person("Joe", null);
        //kryo.writeObject(buffer, person0);
        
        logger.debug("person0: " + HexUtil.toHexString(buffer.array(), 0, buffer.position()));
        
        //SomeClass someObject = kryo.readObject(buffer, SomeClass.class);
    }
    
}