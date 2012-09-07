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

import com.mfizz.binlog.impl.BinlogFileDeclarationImpl;
import com.mfizz.binlog.impl.BinlogFileHeaderImpl;
import com.mfizz.binlog.impl.BinlogFileReaderImpl;
import com.mfizz.binlog.impl.BinlogFileTranscoder;
import com.mfizz.binlog.impl.BinlogFileWriterImpl;
import com.mfizz.binlog.impl.PartialBinlogFile;
import com.mfizz.binlog.type.TranscoderType;
import com.mfizz.binlog.util.ByteChannelUtil;
import com.mfizz.binlog.util.CompressedFileUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.GZIPInputStream;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfizz
 */
public class BinlogFile {
    private static final Logger logger = LoggerFactory.getLogger(BinlogFile.class);
    
    /**
     * Creates a new binlog file and returns a binlog writer.
     * @param file The binlog file to create
     * @param factory The factory to create the binlogs with
     * @return A binlog writer ready to write new records
     * @throws IOException Thrown if there was an IO error while creating the
     *      binlog file.
     * @throws BinlogException  Thrown if there was an error creating the binlog
     *      file.
     */
    static synchronized public BinlogFileWriter create(File file, BinlogFactory factory) throws IOException, BinlogException {
        // file must NOT exist
        if (file.exists()) {
            throw new IOException("Unable to create binlog file [" + file.getAbsolutePath() + "]; file already exists");
        }
        
        // create a new file, channel, header
        RandomAccessFile rafile = new RandomAccessFile(file, "rw");
        FileChannel fc = null;
        try {
            fc = rafile.getChannel();
            
            // create a transcoder to convert objects to/from bytes
            TranscoderType transcoderType = TranscoderType.PROTOBUF;
            BinlogFileTranscoder transcoder = null;
            try {
                transcoder = transcoderType.createTranscoder();
            } catch (Throwable t) {
                throw new BinlogException("Unable to create transcoder: " + t.getMessage(), t);
            }

            // create declaration tied to the transcoder
            BinlogFileDeclaration declaration = new BinlogFileDeclarationImpl(BinlogConstants.MAGIC_BYTES, BinlogConstants.VERSION, transcoderType);

            // create header, then delegate actual creation to caller-supplied factory
            BinlogFileHeaderImpl header = new BinlogFileHeaderImpl(new DateTime());
            factory.createBinlogFileHeader(header);

            // write declaration and header to binlog file
            ByteBuffer buffer = transcoder.createByteBuffer(declaration, header);
            ByteChannelUtil.writeAll(fc, buffer);
            
            // make sure the data is written out to the file
            fc.force(true);
            
            return new BinlogFileWriterImpl(file, rafile, fc, transcoder, declaration, header, 0, 0, 0, 0, 0);
        } catch (Throwable t) {
            closeQuietlyAndThrow(fc, rafile, t);
            return null;    // should be unreachable but netbeans complains
        }
    }
    
    /**
     * Creates a new binlog reader associated with the file.
     * @param file The binlog file to read
     * @param factory The factory to verify if the binlog header is valid.
     * @return A binlog reader ready to read records with
     * @throws IOException Thrown if there was an IO error while opening or
     *      reading the binlog file.
     * @throws BinlogException  Thrown if there was an error reading or parsing
     *      the binlog file.
     */
    static synchronized public BinlogFileReader open(File file, BinlogFactory factory) throws IOException, BinlogException {
        // file must exist
        if (!file.exists()) {
            throw new FileNotFoundException("Unable to open binlog file [" + file.getAbsolutePath() + "]; file does not exist");
        }
        
        // create a new file, channel, header
        RandomAccessFile rafile = new RandomAccessFile(file, "rw");
        FileChannel fc = null;
        try {
            fc = rafile.getChannel();

            // complete a partial read where the file channel is the readable byte channel
            PartialBinlogFile pbf = read(fc, factory);

            return new BinlogFileReaderImpl(file, rafile, fc, pbf.getTranscoder(), pbf.getDeclaration(), pbf.getHeader(), 0, 0, 0, 0, 0);
        } catch (Throwable t) {
            closeQuietlyAndThrow(fc, rafile, t);
            return null;    // should be unreachable but netbeans complains
        }
    }
    
    /**
     * Creates a new binlog reader associated with the file.
     * @param file The binlog file to read
     * @param factory The factory to verify if the binlog header is valid
     * @param detectCompressedFile If true this method will attempt to detect if the file
     *      is compressed by using the file extension.  If the file is compressed
     *      then the correct inputstream will be created to read the binlog.
     *      This new underlying inputstream will be automatically closed when
     *      the binlog is closed.
     * @return A binlog reader ready to read records with
     * @throws IOException Thrown if there was an IO error while opening or
     *      reading the binlog file.
     * @throws BinlogException  Thrown if there was an error reading or parsing
     *      the binlog file.
     */
    static synchronized public BinlogFileReader open(File file, BinlogFactory factory, boolean detectCompressedFile) throws IOException, BinlogException {
        // file must exist
        if (!file.exists()) {
            throw new FileNotFoundException("Unable to open binlog file [" + file.getAbsolutePath() + "]; file does not exist");
        }
        
        // we may open the file as an input stream if its compressed
        InputStream is = null;
        if (detectCompressedFile) {
            // creates proper input stream (including making it buffered if its faster perf)
            is = CompressedFileUtil.createInputStream(file);
        }
        
        if (is != null) {
            try {
                // create a readable byte channel from the stream
                ReadableByteChannel rbc = Channels.newChannel(is);

                // complete a partial read
                PartialBinlogFile pbf = read(rbc, factory);

                return new BinlogFileReaderImpl(file, is, true, rbc, pbf.getPosition(), pbf.getTranscoder(), pbf.getDeclaration(), pbf.getHeader(), 0, 0, 0, 0, 0);
            } catch (Throwable t) {
                closeQuietlyAndThrow(is, t);
                return null;    // should be unreachable but netbeans complains
            }
        } else {
            // open file normally with a file channel
            // create a new file, channel, header
            RandomAccessFile rafile = new RandomAccessFile(file, "rw");
            FileChannel fc = null;
            try {
                fc = rafile.getChannel();

                // complete a partial read where the file channel is the readable byte channel
                PartialBinlogFile pbf = read(fc, factory);

                return new BinlogFileReaderImpl(file, rafile, fc, pbf.getTranscoder(), pbf.getDeclaration(), pbf.getHeader(), 0, 0, 0, 0, 0);
            } catch (Throwable t) {
                closeQuietlyAndThrow(fc, rafile, t);
                return null;    // should be unreachable but netbeans complains
            }
        }
    }
    
    static synchronized public BinlogFileWriter append(File file, BinlogFactory factory, boolean truncateCorruptedRecords) throws IOException, BinlogException {
        if (!file.exists()) {
            // if file does not exist, this is a simple create
            return BinlogFile.create(file, factory);
        } else {
            // if file does exist, then we need to read it and all records first
            boolean writerCreated = false;
            BinlogFileReader binlog = BinlogFile.open(file, factory);
            
            try {
                // need access to underlying file IO objects
                BinlogFileReaderImpl binlogImpl = (BinlogFileReaderImpl)binlog;

                // read all records
                BinlogRecord record = null;
                while (true) {
                    // save the position (in case of corrupted record, this is 
                    // potentially the position we will truncate the file to
                    long position = binlog.getPosition();
                    try {
                        // read the header, but not the data (to speed this up)
                        record = binlog.read(true, false);
                        if (record == null) {
                            break;      // finished reading all the records
                        }
                    } catch (BinlogException e) {
                        if (!truncateCorruptedRecords) {
                            // if we aren't going to truncate, rethrow the error
                            throw e;
                        }
                        
                        // calculate what we'd truncate
                        long size = binlog.getSize();
                        long truncateSize = size - position;
                        // log the warning about truncation
                        logger.warn("Unable to open binlog file cleanly for appending; an invalid or corrupted record found @ position [" + position + "]; truncating [" + truncateSize + "] bytes from end of file [" + file.getAbsolutePath() + "] to roll back from corrupted record", e); 
                        // truncate file to get rid of this corrupted record
                        binlogImpl.truncate(position);
                        break;         // finished reading
                    }
                }

                // if we get here, then all records were read -- ready to create
                // a writer to continue appending records -- need access to impl vars
                writerCreated = true;
                return new BinlogFileWriterImpl(binlogImpl);                
            } finally {
                if (binlog != null && !writerCreated) {
                    // make sure to close all resources
                    binlog.close();
                }
            }
        }
    }
    
    static private void closeQuietlyAndThrow(FileChannel fc, RandomAccessFile rafile, Throwable t) throws IOException, BinlogException {
        if (fc != null) {
            try { fc.close(); } catch (IOException e) { }
        }
        if (rafile != null) {
            try { rafile.close(); } catch (IOException e) { }
        }
        if (t instanceof IOException) {
            throw (IOException)t;
        } else if (t instanceof BinlogException) {
            throw (BinlogException)t;
        } else {
            throw new BinlogException(t.getMessage(), t);
        }
    }
    
    static private void closeQuietlyAndThrow(InputStream is, Throwable t) throws IOException, BinlogException {
        if (is != null) {
            try { is.close(); } catch (IOException e) { }
        }
        if (t instanceof IOException) {
            throw (IOException)t;
        } else if (t instanceof BinlogException) {
            throw (BinlogException)t;
        } else {
            throw new BinlogException(t.getMessage(), t);
        }
    }
    
    /**
     * Creates a binlog reader tied to this input stream.  This method will 
     * read the binlog transcoder, declaration, and header from the input stream
     * and then return a reader that can read records from the input stream.
     * A binlog reader returned from this method will not attempt to close()
     * the input stream if you close the binlog.
     * @param is The input stream to read from
     * @param factory The factory to create binlogs from
     * @param closeInputStream Whether the inputstream should be closed when
     *      the binlog is closed.
     * @return A binlog reader tied to this input stream.
     * @throws IOException Thrown if there was an IO error while reading the
     *      input stream.
     * @throws BinlogException  Thrown if there was an error parsing a binlog
     *      from the input stream.
     */
    static synchronized public BinlogFileReader read(InputStream is, BinlogFactory factory, boolean closeInputStream) throws IOException, BinlogException {
        // create a readable byte channel from the stream
        ReadableByteChannel rbc = Channels.newChannel(is);
        
        // complete a partial read
        PartialBinlogFile pbf = read(rbc, factory);
        
        return new BinlogFileReaderImpl(null, is, closeInputStream, rbc, pbf.getPosition(), pbf.getTranscoder(), pbf.getDeclaration(), pbf.getHeader(), 0, 0, 0, 0, 0);
    }
    
    static private PartialBinlogFile read(ReadableByteChannel rbc, BinlogFactory factory) throws IOException, BinlogException {
        long position = 0;
            
        // read declaration from file - required to figure out what type of transcoder was used
        ByteBuffer declarationBuffer = ByteBuffer.allocate(BinlogConstants.FILE_DECLARATION_BYTE_LENGTH);
        ByteChannelUtil.readAll("binlog_file_declaration", position, rbc, declarationBuffer);
        declarationBuffer.flip();
        position += declarationBuffer.remaining();
        BinlogFileDeclaration declaration = BinlogFileTranscoder.createBinlogFileDeclaration(declarationBuffer);

        // create a transcoder to convert objects to/from bytes
        TranscoderType transcoderType = declaration.getTranscoderType();
        BinlogFileTranscoder transcoder = null;
        try {
            transcoder = transcoderType.createTranscoder();
        } catch (Throwable t) {
            throw new BinlogException("Unable to create transcoder: " + t.getMessage(), t);
        }

        // read file_header_length
        ByteBuffer headerLengthBuffer = ByteBuffer.allocate(BinlogConstants.FILE_HEADER_LENGTH_BYTE_LENGTH).order(ByteOrder.BIG_ENDIAN);
        ByteChannelUtil.readAll("binlog_file_header_length", position, rbc, headerLengthBuffer);
        headerLengthBuffer.flip();
        position += headerLengthBuffer.remaining();
        int fileHeaderLength = headerLengthBuffer.getInt();

        // read the file_header
        ByteBuffer headerBuffer = ByteBuffer.allocate(fileHeaderLength);
        ByteChannelUtil.readAll("binlog_file_header", position, rbc, headerBuffer);
        headerBuffer.flip();
        position += headerBuffer.remaining();
        BinlogFileHeader header = transcoder.createBinlogFileHeader(headerBuffer.array());

        // verify the binlog header
        factory.verifyBinlogFileHeader(header);
        
        // a partially created binlog file
        return new PartialBinlogFile(position, transcoder, declaration, header);
    }
    
}
