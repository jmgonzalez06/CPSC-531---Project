#!/bin/bash
echo "Checking Hadoop services..."

jps

echo ""
echo "If you see NameNode, DataNode, ResourceManager, NodeManager -> GOOD"
echo "If missing any -> Something is wrong, check logs or restart."
