#!/bin/bash
set -e

LOCAL_OUTPUT_DIR="./output"

#MIGHT NEED TO CHANGE THIS ################################################
#NEW_DATA_DIR="$LOCAL_OUTPUT_DIR/newdata"
#MIGHT NEED TO CHANGE THIS ################################################

echo "[Output] Creating local output directory: $LOCAL_OUTPUT_DIR"
mkdir -p "$LOCAL_OUTPUT_DIR"

#MIGHT NEED TO CHANGE THIS ################################################
#echo "[Output] Creating new data directory: $NEW_DATA_DIR"
#mkdir -p "$NEW_DATA_DIR"
#MIGHT NEED TO CHANGE THIS ################################################


echo "[Output] Saving HDFS output to $LOCAL_OUTPUT_FILE..."
#hdfs dfs -cat /output-local/part-r-00000 > "$LOCAL_OUTPUT_FILE"

hdfs dfs -cat /output-local/part-r-00000 > "$LOCAL_OUTPUT_DIR/movie_results.csv"
hdfs dfs -cat /output-local/part-r-00000 > "$LOCAL_OUTPUT_DIR/movie_results.txt"


echo "[Output] Done. Output saved to: $LOCAL_OUTPUT_FILE"

hdfs dfs -cat /output-local/part-r-00000


python3 ./src/preprocessing/datapreprocess.py
python3 ./src/preprocessing/preprocess.py