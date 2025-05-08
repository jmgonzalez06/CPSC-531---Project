#!/bin/bash
set -e

echo "[Setup] Cleaning all top-level HDFS directories..."
for path in $(hdfs dfs -ls / | awk '{print $8}'); do
  hdfs dfs -rm -r -skipTrash "$path"
done


#-------------- Create DIRS for Dataset input --------------#

echo "[Setup] Creating input directories..."
hdfs dfs -mkdir -p /input/ratings
hdfs dfs -mkdir -p /input/movies
hdfs dfs -mkdir -p /input/users


#-------------- Dataset Upload to HDFS --------------#

echo "[Setup] Uploading dataset files..."
hdfs dfs -put -f ./data/u.data /input/ratings/
hdfs dfs -put -f ./data/u.item /input/movies/
hdfs dfs -put -f ./data/u.user /input/users/

echo "[Setup] HDFS setup complete."
