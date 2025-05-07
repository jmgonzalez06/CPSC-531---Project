#!/bin/bash
set -e

LOCAL_OUTPUT_DIR="./output"
#LOCAL_OUTPUT_FILE="$LOCAL_OUTPUT_DIR/movie_results.csv"
#LOCAL_OUTPUT_FILE="$LOCAL_OUTPUT_DIR/movie_results.txt"

echo "[Output] Creating local output directory: $LOCAL_OUTPUT_DIR"
mkdir -p "$LOCAL_OUTPUT_DIR"

echo "[Output] Saving HDFS output to $LOCAL_OUTPUT_FILE..."
#hdfs dfs -cat /output-local/part-r-00000 > "$LOCAL_OUTPUT_FILE"

hdfs dfs -cat /output-local/part-r-00000 > "$LOCAL_OUTPUT_DIR/movie_results.csv"
hdfs dfs -cat /output-local/part-r-00000 > "$LOCAL_OUTPUT_DIR/movie_results.txt"


echo "[Output] Fetching users grouped by movie..."
hdfs dfs -cat /output/users-r-00000 > "$LOCAL_OUTPUT_DIR/movie_users.txt"

echo "[Output] Done. Output saved to: $LOCAL_OUTPUT_FILE"

hdfs dfs -cat /output-local/part-r-00000


