
run-filechannel:
	mvn -e test-compile exec:java -Dexec.classpathScope="test" -Dexec.mainClass="com.mfizz.binlog.demo.FileChannelMain" -Dexec.args="target/test.binlog"

run-protobufread:
	mvn -e test-compile exec:java -Dexec.classpathScope="test" -Dexec.mainClass="com.mfizz.binlog.demo.ProtobufReadMain" -Dexec.args="target/test.binlog"

run-kryo:
	mvn -e test-compile exec:java -Dexec.classpathScope="test" -Dexec.mainClass="com.mfizz.binlog.demo.KryoMain" -Dexec.args=""

run-protobuf:
	mvn -e test-compile exec:java -Dexec.classpathScope="test" -Dexec.mainClass="com.mfizz.binlog.demo.ProtobufMain" -Dexec.args=""

run-binlog:
	mvn -e test-compile exec:java -Dexec.classpathScope="test" -Dexec.mainClass="com.mfizz.binlog.demo.BinlogMain" -Dexec.args="target/test.binlog"

run-readbinlog:
	mvn -e test-compile exec:java -Dexec.classpathScope="test" -Dexec.mainClass="com.mfizz.binlog.demo.ReadBinlogMain" -Dexec.args="target/test.binlog"

run-benchmark:
	mvn -e test-compile exec:java -Dexec.classpathScope="test" -Dexec.mainClass="com.mfizz.binlog.demo.BenchmarkMain" -Dexec.args=""

run-filelock1:
	mvn -e test-compile exec:java -Dexec.classpathScope="test" -Dexec.mainClass="com.mfizz.binlog.demo.FileLock1Main" -Dexec.args=""

run-filelock2:
	mvn -e test-compile exec:java -Dexec.classpathScope="test" -Dexec.mainClass="com.mfizz.binlog.demo.FileLock2Main" -Dexec.args=""