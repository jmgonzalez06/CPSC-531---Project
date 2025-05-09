#!/bin/bash
echo "Stopping Hadoop services manually..."

echo "Stopping NodeManager..."
yarn --daemon stop nodemanager || true

echo "Stopping ResourceManager..."
yarn --daemon stop resourcemanager || true

echo "Stopping DataNode..."
hdfs --daemon stop datanode || true

echo "Stopping NameNode..."
hdfs --daemon stop namenode || true

sleep 3

echo "Checking for leftover Hadoop Java processes..."
LEFTOVER=$(jps | grep -E 'NameNode|DataNode|ResourceManager|NodeManager' || true)

if [ -n "$LEFTOVER" ]; then
    echo "Leftover Hadoop processes detected:"
    echo "$LEFTOVER"
    echo "Force killing all Hadoop-related Java processes..."
    pkill -f hadoop || true
    pkill -f java || true
    sleep 2
else
    echo "No leftover Hadoop processes found."
fi

echo "Final process check:"
jps

echo ""
echo "All Hadoop services fully stopped and cleaned."
