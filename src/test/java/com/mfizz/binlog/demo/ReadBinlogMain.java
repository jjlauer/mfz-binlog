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

import com.cloudhopper.commons.util.DecimalUtil;
import com.mfizz.binlog.BinlogException;
import com.mfizz.binlog.BinlogFactory;
import com.mfizz.binlog.BinlogFile;
import com.mfizz.binlog.BinlogFileHeader;
import com.mfizz.binlog.BinlogFileReader;
import com.mfizz.binlog.BinlogRecord;
import com.mfizz.binlog.BinlogRecordHeaderEvent;
import com.mfizz.binlog.WritableBinlogFileHeader;
import java.io.File;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public final class ReadBinlogMain {
    private static final Logger logger = LoggerFactory.getLogger(ReadBinlogMain.class);
    
    public static void main(String[] args) throws Exception {
        // command line should be file to work with
        if (args.length != 1) {
            throw new Exception("One command line argument is file to work with");
        }
        
        File f = new File(args[0]);
        
        BinlogFactory factory = new BinlogFactory() {
            @Override
            public void createBinlogFileHeader(WritableBinlogFileHeader header) { }

            @Override
            public void verifyBinlogFileHeader(BinlogFileHeader header) throws BinlogException { }
        };
        
        /**
        BinlogFileReader binlog = null;
        // is this actually compressed?
        if (f.getName().endsWith(".gz")) {
            logger.info("opening binlog as gzip compressed inputstream...");
            FileInputStream fis = new FileInputStream(f);
            GZIPInputStream gis = new GZIPInputStream(fis);
            binlog = BinlogFile.read(gis, factory, true);
        } else {
            logger.info("opening binlog as normal file...");
            binlog = BinlogFile.open(f, factory);
        }
         */
        
        BinlogFileReader binlog = BinlogFile.open(f, factory, true);
        
        logger.info("");
        logger.info("binlog_file: {}", (binlog.getFile() != null ? binlog.getFile().getName() : "<inputstream>"));
        
        logger.info("");
        logger.info("Binlog File Declaration");
        logger.info("-----------------------------------");
        logger.info("magic_bytes: {}", new String(binlog.getDeclaration().getMagicBytes()));
        logger.info("version: {}", binlog.getDeclaration().getVersionString());
        logger.info("transcoder_type: {} ({})", binlog.getDeclaration().getTranscoderType(), binlog.getDeclaration().getTranscoderType().getId());
        
        logger.info("");
        logger.info("Binlog File Header");
        logger.info("-----------------------------------");
        logger.info("create_date_time: {}", binlog.getHeader().getCreateDateTime());
        logger.info("name: {}", binlog.getHeader().getName());
        logger.info("content_type: {}", binlog.getHeader().getContentType());
        logger.info("record_length_byte_length: {}", binlog.getHeader().getRecordLengthByteLength());
        logger.info("optional_parameter_count: {}", binlog.getHeader().getOptionalParameters().size());
        for (Map.Entry<String,String> optionalParameter : binlog.getHeader().getOptionalParameters().entrySet()) {
            logger.info(" {}: {}", optionalParameter.getKey(), optionalParameter.getValue());
        }
        
        /**
        BinlogRecord record = binlog.read(true, true);
        
        logger.info("Binlog Record");
        logger.info(" Header");
        logger.info("  id: {}", record.getHeader().getId());
        logger.info("  create_date_time: {}", record.getHeader().getCreateDateTime());
        logger.info("  user_defined_type: {}", record.getHeader().getUserDefinedType());
        logger.info("  optional_parameter_count: {}", record.getHeader().getOptionalParameters().size());
        for (Map.Entry<String,String> optionalParameter : record.getHeader().getOptionalParameters().entrySet()) {
            logger.info("  {}: {}", optionalParameter.getKey(), optionalParameter.getValue());
        }
        logger.info(" File Info");
        logger.info("  position: {}", record.getFileInfo().getPosition());
        logger.info("  length: {}", record.getFileInfo().getLength());
        logger.info("  header_position: {}", record.getFileInfo().getHeaderPosition());
        logger.info("  header_length: {}", record.getFileInfo().getHeaderLength());
        logger.info("  data_position: {}", record.getFileInfo().getDataPosition());
        logger.info("  data_length: {}", record.getFileInfo().getDataLength());
        logger.info(" Data");
        logger.info("  data: {}", HexUtil.toHexString(record.getData()));
        
        
        BinlogRecord record1 = binlog.read(true, true);
        logger.info(" record1: {}", record1);
        */
        
        logger.info("");
        logger.info("Binlog Events");
        logger.info("-----------------------------------");
        
        BinlogRecord record = null;
        int records = 0;
        while (true) {
            record = binlog.read(true, true);
            if (record == null) {
                break;
            }
            // if this is an "event" print it out
            if (record.isEvent()) {
                BinlogRecordHeaderEvent event = record.getEvent();
                StringBuilder extraBuf = new StringBuilder();
                if (record.getHeader().getOptionalParameters() != null) {
                    for (Map.Entry<String,String> s : record.getHeader().getOptionalParameters().entrySet()) {
                        extraBuf.append(", ");
                        extraBuf.append(s.getKey());
                        extraBuf.append("=");
                        extraBuf.append(s.getValue());
                    }
                }
                logger.info("event: timestamp={}, type={}{}", new Object[] { record.getHeader().getCreateDateTime(), event.getType(), extraBuf });
            } else {
                records++;
            }
        }
        
        logger.info("");
        logger.info("Binlog Summary");
        logger.info("-----------------------------------");
        logger.info("      record_count: {}", binlog.getRecordCount());
        logger.info("event_record_count: {}", binlog.getEventRecordCount());
        logger.info(" data_record_count: {}", binlog.getDataRecordCount());
        logger.info("total_size: {} bytes", binlog.getSize());
        double recordHeaderSizePercentage = (double)binlog.getRecordHeaderSize()/(double)binlog.getSize() * 100;
        double recordDataSizePercentage = (double)binlog.getRecordDataSize()/(double)binlog.getSize() * 100;
        logger.info("total_record_header_size: {} bytes ({}% of size)", binlog.getRecordHeaderSize(), DecimalUtil.toString(recordHeaderSizePercentage, 2));
        logger.info("total_record_data_size: {} bytes ({}% of size)", binlog.getRecordDataSize(), DecimalUtil.toString(recordDataSizePercentage, 2));
        
        if (binlog.getRecordCount() > 0) {
            double avgRecordHeaderSize = (double)binlog.getRecordHeaderSize()/(double)binlog.getRecordCount();
            double avgRecordDataSize = (double)binlog.getRecordDataSize()/(double)binlog.getRecordCount();
            logger.info("avg_record_header_size: {} bytes", DecimalUtil.toString(avgRecordHeaderSize, 1));
            logger.info("avg_record_data_size: {} bytes", DecimalUtil.toString(avgRecordDataSize, 1));
        }
        
    }
    
}