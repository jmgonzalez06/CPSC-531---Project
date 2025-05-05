#!/bin/bash
set -e

cd ../src

echo "[Compile] Cleaning old classes..."
rm -rf classes
mkdir -p classes

echo "[Compile] Compiling Java files..."
javac -classpath "$HADOOP_HOME/share/hadoop/common/*:$HADOOP_HOME/share/hadoop/mapreduce/*:$HADOOP_HOME/share/hadoop/common/lib/*" -d classes *.java

echo "[Compile] Creating JAR..."
jar -cvf MovieRating.jar -C classes .

echo "[Run] Removing previous output if exists..."
hdfs dfs -rm -r -skipTrash /output-local || true

echo "[Run] Running Hadoop job..."
hadoop jar MovieRating.jar MovieDriver

echo "[Run] Job finished."

