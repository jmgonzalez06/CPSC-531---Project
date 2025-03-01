#!/usr/bin/env python3
"""
Flask web application to display MovieLens genre analysis.
Serves results from Hadoop output on a web UI.
"""

from flask import Flask, render_template
import os
import pandas as pd