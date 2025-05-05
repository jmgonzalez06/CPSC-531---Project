#!/bin/bash
set -e

LOCAL_OUTPUT_DIR="./output"
LOCAL_OUTPUT_FILE="$LOCAL_OUTPUT_DIR/movie_results.csv"

echo "[Output] Creating local output directory: $LOCAL_OUTPUT_DIR"
mkdir -p "$LOCAL_OUTPUT_DIR"

echo "[Output] Saving HDFS output to $LOCAL_OUTPUT_FILE..."
hdfs dfs -cat /output-local/part-r-00000 > "$LOCAL_OUTPUT_FILE"

echo "[Output] Done. Output saved to: $LOCAL_OUTPUT_FILE"

hdfs dfs -cat /output-local/part-r-00000
