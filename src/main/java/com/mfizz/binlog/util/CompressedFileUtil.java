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

//import com.ning.compress.lzf.LZFInputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author mfizz
 */
public class CompressedFileUtil {
    
    static public InputStream createInputStream(File file) throws IOException {
        String filename = file.getName().toLowerCase();
        if (filename.endsWith(".gz")) {
            // a buffered output stream sped processing up by 4X! in prod
            GZIPInputStream gis = new GZIPInputStream(new FileInputStream(file), 1024*1024);   // 1 MB buffer
            return new BufferedInputStream(gis, 1024*1024);    // 1 MB buffer
/**        } else if (filename.endsWith(".lzf")) {
            // a buffered output stream is not necessary
            return new LZFInputStream(new FileInputStream(file));
 */
        } else {
            return null;
        }
    }
    
}
