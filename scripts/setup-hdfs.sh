#!/bin/bash
set -e

echo "[Setup] Cleaning all top-level HDFS directories..."
for path in $(hdfs dfs -ls / | awk '{print $8}'); do
  hdfs dfs -rm -r -skipTrash "$path"
done

echo "[Setup] Creating input directories..."
hdfs dfs -mkdir -p /input/ratings
hdfs dfs -mkdir -p /input/movies

echo "[Setup] Uploading dataset files..."
hdfs dfs -put ../data/u.data /input/ratings/
hdfs dfs -put ../data/u.item /input/movies/

echo "[Setup] HDFS setup complete."

