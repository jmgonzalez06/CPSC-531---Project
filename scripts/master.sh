#! /bin/bash

echo "Running 1-setup-hdfs.sh"
./scripts/1-setup-hdfs.sh

echo "Running 2-compile.sh"
./scripts/2-compile.sh

echo "Running 3-output.sh"
./scripts/3-output.sh

