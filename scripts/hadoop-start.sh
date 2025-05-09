#!/bin/bash
echo "Starting Hadoop services manually..."

sudo service ssh start
rm -f /tmp/hadoop-$(whoami)-resourcemanager.pid

echo "Starting NameNode..."
hdfs --daemon start namenode

echo "Starting DataNode..."
hdfs --daemon start datanode

echo "Starting ResourceManager..."
yarn --daemon start resourcemanager

echo "Starting NodeManager..."
yarn --daemon start nodemanager

echo "All Hadoop services started!"
