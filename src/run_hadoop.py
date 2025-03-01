#!/usr/bin/env python3
"""
Runs the Hadoop MapReduce job on MovieLens data.
Uploads input to HDFS and triggers MovieMapper/MovieReducer.
"""

import os
import subprocess
import pyhdfs