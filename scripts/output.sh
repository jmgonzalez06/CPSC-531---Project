#!/bin/bash
set -e

echo "[Output] Listing output directory..."
hdfs dfs -ls /output-local

echo "[Output] Showing result content:"
hdfs dfs -cat /output-local/part-r-00000

