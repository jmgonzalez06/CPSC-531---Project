# CPSC 531 Final Project: Movie Rating Aggregator

**Team:**  Jose M. Gonzalez & Phillip Akhnoukh
**GitHub:**   https://github.com/jmgonzalez06/CPSC-531---Project
**Date:** April 11, 2025

Analyzes MovieLens data with Hadoop to show top-rated genres by decade and a "rising star" genre, displayed via Flask web UI.

Project Structure

project-root/
├── data/          # Contains u.data and u.item
├── src/           # All Java code + compiled files + jar
├── output/        # Final output.txt (copied from HDFS or local run)
├── README.md
├── run_job.sh     # Script to compile + run MapReduce
└── requirements.txt

Running the MapReduce Job

### Step 1: Compile and package

cd src
javac -classpath "$HADOOP_HOME/share/hadoop/common/:$HADOOP_HOME/share/hadoop/mapreduce/:." *.java
jar cfm MovieRating.jar manifest.txt *.class

### Step 2: Upload data to HDFS

hdfs dfs -mkdir -p /input/ratings /input/movies
hdfs dfs -put ../data/u.data /input/ratings/
hdfs dfs -put ../data/u.item /input/movies/

### Step 3: Run the job

hdfs dfs -rm -r /output
hadoop jar MovieRating.jar MovieDriver /input/ratings /input/movies /output

### Step 4: Download results

hdfs dfs -get /output/part-r-00000 ../output/output.txt

Notes

Two mappers used with MultipleInputs (RatingsMapper, MoviesMapper)

One reducer (MovieReducer) aggregates and detects rising stars

Output format:
movieIdtitle,genre,avgRating,numRatings,isPopular
