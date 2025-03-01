# CPSC 531 Final Project: Movie Rating Aggregator

**Team:**  Jose M. Gonzalez & Phillip Akhnoukh
**GitHub:**   https://github.com/jmgonzalez06/CPSC-531---Project
**Date:** April 11, 2025

Analyzes MovieLens data with Hadoop to show top-rated genres by decade and a "rising star" genre, displayed via Flask web UI.

## Setup and Run Instructions
(TBD - See previous web UI instructions for full details)

##Current Standing
input/ will hold ratings.csv and movies.csv (user adds via preprocess.py or manual download).
output/ will store Hadoop results (e.g., part-r-00000).

### Compiling Java Files (Optional)
To compile `MovieMapper.java` and `MovieReducer.java` manually:
- Ensure Hadoop is installed (e.g., `C:\hadoop-3.4.1`).
- Add Hadoop JARs to classpath: