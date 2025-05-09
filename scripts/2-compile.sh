#!/bin/bash
set -e

cd ./src

echo "[Compile] Cleaning old classes..."
rm -rf classes
mkdir -p classes

<<<<<<< HEAD
echo "[Compile] Compiling Java files..."
#javac -classpath "$HADOOP_HOME/share/hadoop/common/*:$HADOOP_HOME/share/hadoop/mapreduce/*:$HADOOP_HOME/share/hadoop/common/lib/*" -d classes *.java
find . -name "*.java" > sources.txt
javac -classpath "$HADOOP_HOME/share/hadoop/common/*:$HADOOP_HOME/share/hadoop/mapreduce/*:$HADOOP_HOME/share/hadoop/common/lib/*" -d classes @sources.txt

=======
#echo "[Compile] Compiling Java files..."
#javac -classpath "$HADOOP_HOME/share/hadoop/common/*:$HADOOP_HOME/share/hadoop/mapreduce/*:$HADOOP_HOME/share/hadoop/common/lib/*" -d classes *.java

echo "[Compile] Compiling ALL Java files (recursively)..."
find . -name "*.java" | xargs javac -classpath "$HADOOP_HOME/share/hadoop/common/*:$HADOOP_HOME/share/hadoop/mapreduce/*:$HADOOP_HOME/share/hadoop/common/lib/*" -d classes


#-------------- Jar Paths, create Dedicated Folders for JARs --------------#
>>>>>>> origin/main

echo "[Compile] Creating JAR..."
jar -cfe MovieRatings/MovieRating.jar MovieRatings.MovieDriver -C classes .
jar -cfe AvgRatingByOccupation/AvgRatingByOccupation.jar AvgRatingByOccupation.AvgRatingByOccupationDriver -C classes .


echo "[Run] Removing previous output if exists..."
hdfs dfs -rm -r -skipTrash /output-local || true
hdfs dfs -rm -r -skipTrash /output/avg_rating_occupation || true



#----------------- Input Paths for Jars-----------------#

echo "[Run] Running Hadoop job..."
hadoop jar MovieRatings/MovieRating.jar
hadoop jar AvgRatingByOccupation/AvgRatingByOccupation.jar






echo "[Run] Running for the attempt on the user extraction with movie title..."
javac -classpath $(hadoop classpath) -d . UserMapper.java MovieMapperTwo.java MovieReducerTwo.java MovieDriverTwo.java
jar -cvf moviegrouping.jar *.class
#hadoop jar moviegrouping.jar MovieDriver /input/u.user /input/u.data /output
hadoop jar moviegrouping.jar MovieDriverTwo /input/movies/u.item /input/ratings/u.data /output





echo "[Run] Job finished."

