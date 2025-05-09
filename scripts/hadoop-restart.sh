#!/bin/bash
echo "Restarting Hadoop services..."

~/hadoop-scripts/hadoop-stop.sh
sleep 3
~/hadoop-scripts/hadoop-start.sh

echo "Hadoop services restarted!"
