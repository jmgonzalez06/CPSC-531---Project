#!/bin/bash
set -e

LOCAL_OUTPUT_DIR="./output"


echo "[Output] Creating local output directory: $LOCAL_OUTPUT_DIR"
mkdir -p "$LOCAL_OUTPUT_DIR"


#---------------- This is the output paths ----------------#

echo "[Output] Saving HDFS output to $LOCAL_OUTPUT_FILE..."
hdfs dfs -cat /output-local/part-r-00000 > "$LOCAL_OUTPUT_DIR/movie_results.csv"
hdfs dfs -cat /output-local/part-r-00000 > "$LOCAL_OUTPUT_DIR/movie_results.txt"


echo "[Output] Saving /output/AvgRatingByOccupation to:"
hdfs dfs -cat /output/AvgRatingByOccupation/part-r-00000 > "$LOCAL_OUTPUT_DIR/AvgRatingByOccupation.csv"
hdfs dfs -cat /output/AvgRatingByOccupation/part-r-00000 > "$LOCAL_OUTPUT_DIR/AvgRatingByOccupation.txt"


echo "[Output] Done. Output saved to: $LOCAL_OUTPUT_FILE"



python3 ./src/preprocessing/datapreprocess.py
python3 ./src/preprocessing/preprocess.py