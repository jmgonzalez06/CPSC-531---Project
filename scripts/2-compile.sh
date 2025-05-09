#!/bin/bash
set -e

cd ./src

echo "[Compile] Cleaning old classes..."
rm -rf classes
mkdir -p classes

#echo "[Compile] Compiling Java files..."
#javac -classpath "$HADOOP_HOME/share/hadoop/common/*:$HADOOP_HOME/share/hadoop/mapreduce/*:$HADOOP_HOME/share/hadoop/common/lib/*" -d classes *.java

echo "[Compile] Compiling ALL Java files (recursively)..."
find . -name "*.java" | xargs javac -classpath "$HADOOP_HOME/share/hadoop/common/*:$HADOOP_HOME/share/hadoop/mapreduce/*:$HADOOP_HOME/share/hadoop/common/lib/*" -d classes


#-------------- Jar Paths, create Dedicated Folders for JARs --------------#

echo "[Compile] Creating JAR..."
jar -cfe MovieRatings/MovieRating.jar MovieRatings.MovieDriver -C classes .
jar -cfe AvgRatingByOccupation/AvgRatingByOccupation.jar AvgRatingByOccupation.AvgRatingByOccupationDriver -C classes .
jar -cfe OccupationGenre/OccupationGenre.jar OccupationGenre.OccupationGenreDriver -C classes .


echo "[Run] Removing previous output if exists..."
#hdfs dfs -rm -r -skipTrash /output/MovieRatings || true
#hdfs dfs -rm -r -skipTrash /output/AvgRatingByOccupation || true
#hdfs dfs -rm -r -skipTrash /output/OccupationGenre || true
hdfs dfs -rm -r -skipTrash /output/ || true



#----------------- Input Paths for Jars-----------------#

echo "[Run] Running Hadoop job..."
hadoop jar MovieRatings/MovieRating.jar
hadoop jar AvgRatingByOccupation/AvgRatingByOccupation.jar
hadoop jar OccupationGenre/OccupationGenre.jar

echo "[Run] Job finished."

