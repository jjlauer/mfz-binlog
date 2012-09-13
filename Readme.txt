
Changing the FileRotatePeriod of a binlogger is dangerous since the alphabetical
ordering of files is critical for the proper sequencing when reading.  For example,
if the FileRotatePeriod is MINUTE and the current file is:

   default.20110601_0007.binlog

Then the period is switched to FIVE_MINUTES only a few seconds later, the new
file would be:

   default.20110601_0005.binlog

Unfortunately, default.20110601_0005.binlog would now contain records that
actually occurred AFTER records in default.20110601_0007.binlog, but by alphabetical
ordering, an application would read default.20110601_0005.binlog first then
read default.20110601_0007.binlog.

Therefore, the period can only truly be "rotated" at the next start of the next
period you want to switch to.  This is currently a limitation of this library.

Please note this limitation only occurs when switching DOWN in granularity
such as MINUTE -> FIVE_MINUTES or FIVE_MINUTES to HOUR.  The reverse situation
does not apply.  HOUR -> FIVE_MINUTES is okay.

In the future, the last configured PERIOD can likely be guessed from the .active
file and a safe switch can be performed at the "next" period.


In Linux, to HexDump a file on command line:

  > perl -e 'local $/; print uc unpack "H*", <>; print "\n"' target/test.bl

To create protobuf:

  > protoc --java_out=src/main/java/ src/main/proto/binlog.proto


utf8_short_string (1 byte length, then string)
int (4 bytes)
long (8 bytes)

binlog file
  Must always start with an declaration and header
  Thus, minimum file length is 12 bytes (8 for declaration and at least 4 for header)
  
  binlog_file_declaration (always 6 bytes long)
    magic_bytes (bytes to indicate this is a 'BINLOG')
    version (1 byte with each half-byte for major and minor e.g. 0x10 for 1.0)
    transcoder_type (1 byte e.g. 1=protobuf, etc)

  binlog_file_header  
    header_length (int) (4 bytes) (length of bytes following this)

    // serialized BinlogHeader object (using transcoder_type declared...)
    create_date_time (number of milliseconds e.g. System.currentTimeMillis(), required)
    name (string, required)
    content_type (string, required)
    optional_parameter (string, string)

  binlog_record(s)
    * variable number of bytes to store record_length -- default is 4

    record_length (int) (1, 2, or 4 bytes: length of entire record following this)
       (this length includes the bytes to store the record_header_length too)
    
      record_header_length (int) (variable!) (length of bytes following this) 
        id                  (4 bytes) (unsigned int)
        create_date_time
        user_defined_type  (user defined type (type of record e.g. INSERT vs. UPDATE))
        optional_parameter (string, string)

      data                (user of lib responsible for this...)

   for example: if the header was 1 byte (0x01) and the data was 1 byte (0x02)
     and 2 bytes are used to store the record_length
         000400010102


Example Usage:
  binlogger.write(record);
  binlogger.write(type, record);
  binlogger.write(type, optionalHeaders, record);

Examples of how compact we can make this:
  id, create_date_time, and user_defined_type per record
  30M records, with a 600 byte payload
  268 seconds to create
  final file size: 18570000084 bytes
  read back in 75 seconds!!

  after not storing id or create_date_time
  30M records, with a 600 byte payload
  235 seconds to create
  final file size: 18180000084 bytes (390000000 bytes saved = 390MB


  when reading a file back, how does skipping reading the header and/or data
  affect performance?
    2.9GB file, 5M records
    read header and data: 20 seconds
    read no header and no data: 15 seconds (25% performance bump?)
    read header and no data: 16 seconds


  flush after every write
    1M records, 600 byte records
    171 seconds = 5847/second
    160 seconds (after using GatheringByteChannel)

  flush every 7 writes
    1M records, 600 byte records
    46 seconds = 21739/second

  flush every 21 writes
    1M records, 600 byte records
    27 seconds = 37037/second
    22 seconds (after using GatheringByteChannel)

  no explicit flush
    1M records, 600 byte records
    11 seconds = 90909/second
    7 seconds (after using GatheringByteChannel)