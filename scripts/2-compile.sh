#!/bin/bash
set -e

cd ./src

echo "[Compile] Cleaning old classes..."
rm -rf classes
mkdir -p classes

echo "[Compile] Compiling Java files..."
#javac -classpath "$HADOOP_HOME/share/hadoop/common/*:$HADOOP_HOME/share/hadoop/mapreduce/*:$HADOOP_HOME/share/hadoop/common/lib/*" -d classes *.java
find . -name "*.java" > sources.txt
javac -classpath "$HADOOP_HOME/share/hadoop/common/*:$HADOOP_HOME/share/hadoop/mapreduce/*:$HADOOP_HOME/share/hadoop/common/lib/*" -d classes @sources.txt


echo "[Compile] Creating JAR..."
jar -cvf MovieRating.jar -C classes .

echo "[Run] Removing previous output if exists..."
hdfs dfs -rm -r -skipTrash /output-local || true

echo "[Run] Running Hadoop job..."
hadoop jar MovieRating.jar MovieDriver






echo "[Run] Running for the attempt on the user extraction with movie title..."
javac -classpath $(hadoop classpath) -d . UserMapper.java MovieMapperTwo.java MovieReducerTwo.java MovieDriverTwo.java
jar -cvf moviegrouping.jar *.class
#hadoop jar moviegrouping.jar MovieDriver /input/u.user /input/u.data /output
hadoop jar moviegrouping.jar MovieDriverTwo /input/movies/u.item /input/ratings/u.data /output





echo "[Run] Job finished."

