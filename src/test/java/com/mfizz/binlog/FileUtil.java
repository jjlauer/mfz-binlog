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

import java.io.File;
import java.io.RandomAccessFile;

/**
 *
 * @author mfizz
 */
public class FileUtil {
    
    static public File createTempFile() throws Exception {
        File f = File.createTempFile("test", ".binlog", new File("target"));
        f.delete();
        return f;
    }
    
    static public File createTempFile(byte[] data) throws Exception {
        File f = createTempFile();
        RandomAccessFile rafile = new RandomAccessFile(f, "rw");
        rafile.write(data);
        rafile.close();
        return f;
    }
    
    static public byte[] readFileFully(File f) throws Exception {
        RandomAccessFile rafile = new RandomAccessFile(f, "r");
        int size = (int)rafile.length();
        byte[] data = new byte[size];
        rafile.readFully(data);
        return data;
    }
    
}
