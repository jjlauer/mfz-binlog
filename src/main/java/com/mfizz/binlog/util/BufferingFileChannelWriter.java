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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.util.concurrent.ArrayBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * THIS CLASS WAS JUST A TEST -- DOES NOT WORK CORRECTLY!
 * 
 * @author mfizz
 */
public class BufferingFileChannelWriter {
    private static final Logger logger = LoggerFactory.getLogger(BufferingFileChannelWriter.class);
    
    private final GatheringByteChannel channel;
    private final ArrayBlockingQueue<ByteBuffer> bufferQueue;
    private final Thread writerThread;
    
    public BufferingFileChannelWriter(GatheringByteChannel channel, int capacity) {
        this.channel = channel;
        this.bufferQueue = new ArrayBlockingQueue<ByteBuffer>(capacity);
        this.writerThread = new WriterThread();
        this.writerThread.start();
    }
    
    public void addAll(ByteBuffer[] buffers) throws InterruptedException {
        //this.bufferQueue.addAll(Arrays.asList(buffers));
        for (ByteBuffer buffer : buffers) {
            this.bufferQueue.put(buffer);
        }
    }
    
    class WriterThread extends Thread {
        
        @Override
        public void run() {
            while (true) {
                try {
                    // wait for first buffer
                    ByteBuffer firstBuffer = bufferQueue.take();
                    
                    // to take advantage of the gathering bytechannel -- we will
                    // also try to grab as many buffers in the queue as possible (without blocking though)
                    int size = bufferQueue.size();
                    
                    ByteBuffer[] buffers = new ByteBuffer[size+1];
                    buffers[0] = firstBuffer;
                    
                    for (int i = 1; i <= size; i++) {
                        buffers[i] = bufferQueue.poll();
                    }
                    try {
                        logger.info("Writing {} buffers in single call", buffers.length);
                        ByteChannelUtil.writeAll(channel, buffers);
                    } catch (IOException e) {
                        logger.error("IO error writing record to channel", e);
                    }
                    
                } catch (InterruptedException e) {
                    logger.warn("Interrupted waiting for take()", e);
                }
            }
        }
        
    }
    
}
