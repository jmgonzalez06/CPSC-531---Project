#!/bin/bash

set -e

echo "Starting Hadoop uninstallation..."

# Step 1: Stop all running Hadoop services
echo ""
echo "[Step 1] Stopping Hadoop services..."

# Attempt graceful stop
if command -v yarn >/dev/null 2>&1; then
    yarn --daemon stop nodemanager || true
    yarn --daemon stop resourcemanager || true
fi

if command -v hdfs >/dev/null 2>&1; then
    hdfs --daemon stop datanode || true
    hdfs --daemon stop namenode || true
fi

sleep 3

# Step 2: Kill any leftover Hadoop-related Java processes
echo ""
echo "[Step 2] Killing leftover Hadoop Java processes (if any)..."

LEFTOVER=$(jps | grep -E 'NameNode|DataNode|ResourceManager|NodeManager' || true)

if [ -n "$LEFTOVER" ]; then
    echo "Leftover Hadoop processes detected:"
    echo "$LEFTOVER"
    echo "Force killing all Hadoop-related processes..."
    pkill -f hadoop || true
    pkill -f java || true
    sleep 2
else
    echo "No leftover Hadoop processes found."
fi

# Step 3: Remove Hadoop installation directory
echo ""
echo "[Step 3] Removing Hadoop installation directory..."

rm -rf ~/hadoop

# Step 4: Remove Hadoop HDFS data
echo ""
echo "[Step 4] Removing Hadoop HDFS data directories..."

rm -rf ~/hadoopdata

# Step 5: Remove Hadoop management scripts
echo ""
echo "[Step 5] Removing Hadoop management scripts..."

rm -rf ~/hadoop-scripts

# Step 6: Clean up environment variables from .bashrc
echo ""
echo "[Step 6] Cleaning environment variables from ~/.bashrc..."

sed -i '/# Hadoop Environment Variables/,+6d' ~/.bashrc
sed -i '/# Hadoop Management Scripts/d' ~/.bashrc

# Step 7: Final instructions
echo ""
echo "Hadoop uninstallation completed successfully!"
echo ""
echo "IMPORTANT: Please run the following to refresh your environment:"
echo "    source ~/.bashrc"
echo ""
