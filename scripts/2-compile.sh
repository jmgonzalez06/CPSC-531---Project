#!/bin/bash
set -e

cd ./src

echo "[Compile] Cleaning old classes..."
rm -rf classes
mkdir -p classes

#----------------- Compile Java Files -----------------#
echo "[Compile] Compiling ALL Java files (recursively)..."
find . -name "*.java" | xargs javac -classpath "$HADOOP_HOME/share/hadoop/common/*:$HADOOP_HOME/share/hadoop/mapreduce/*:$HADOOP_HOME/share/hadoop/common/lib/*" -d classes


#-------------- Jar Paths, create Dedicated Folders for JARs --------------#

echo "[Compile] Creating JAR..."
jar -cfe MovieRatings/MovieRating.jar MovieRatings.MovieDriver -C classes .
jar -cfe AvgRatingByOccupation/AvgRatingByOccupation.jar AvgRatingByOccupation.AvgRatingByOccupationDriver -C classes .
jar -cfe TopGenresByOccupation/TopGenresByOccupation.jar TopGenresByOccupation.TopGenresByOccupationDriver -C classes .
jar -cfe TopGenresByGender/TopGenresByGender.jar TopGenresByGender.TopGenresByGenderDriver -C classes .
jar -cfe GenresByAge/GenresByAge.jar GenresByAge.GenresByAgeDriver -C classes .


echo "[Run] Removing previous output if exists..."
hdfs dfs -rm -r -skipTrash /output/ || true



#----------------- Input Paths for Jars-----------------#

echo "[Run] Running Hadoop job..."
hadoop jar MovieRatings/MovieRating.jar
hadoop jar AvgRatingByOccupation/AvgRatingByOccupation.jar
hadoop jar TopGenresByOccupation/TopGenresByOccupation.jar
hadoop jar TopGenresByGender/TopGenresByGender.jar
hadoop jar GenresByAge/GenresByAge.jar

echo "[Run] Job finished."

