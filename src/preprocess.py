#!/usr/bin/env python3
"""
Preprocesses MovieLens data by downloading and formatting it for Hadoop.
Places ratings.csv and movies.csv in data/input/.
"""

import os
import requests
import zipfile
import pandas as pd