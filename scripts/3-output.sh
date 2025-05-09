#!/bin/bash
set -e

LOCAL_OUTPUT_DIR="./output"


echo "[Output] Creating local output directory: $LOCAL_OUTPUT_DIR"
mkdir -p "$LOCAL_OUTPUT_DIR"


#---------------- This is the output paths ----------------#

echo "[Output] Saving MovieRatings to $LOCAL_OUTPUT_DIR..."
hdfs dfs -cat /output/MovieRatings/part-r-00000 > "$LOCAL_OUTPUT_DIR/movie_results.csv"


echo "[Output] AvgRatingByOccupation to $LOCAL_OUTPUT_DIR..."
hdfs dfs -cat /output/AvgRatingByOccupation/part-r-00000 > "$LOCAL_OUTPUT_DIR/AvgRatingByOccupation.csv"

echo "[Output] TopGenresByOccupation to $LOCAL_OUTPUT_DIR..."
hdfs dfs -cat /output/TopGenresByOccupation/part-r-00000 > "$LOCAL_OUTPUT_DIR/TopGenresByOccupation.csv"

echo "[Output] TopGenreByGender to $LOCAL_OUTPUT_DIR..."
hdfs dfs -cat /output/TopGenresByGender/part-r-00000 > "$LOCAL_OUTPUT_DIR/TopGenresByGender.csv"

echo "[Output] GenresByAge to $LOCAL_OUTPUT_DIR..."
hdfs dfs -cat /output/GenresByAge/part-r-00000 > "$LOCAL_OUTPUT_DIR/GenresByAge.csv"


echo "[Output] Done. Outputs saved to: $LOCAL_OUTPUT_DIR"



#python3 ./src/preprocessing/datapreprocess.py
python3 ./src/preprocessing/preprocess.py
python3 ./src/preprocessing/postprocessing.py

cp ./output-cleaned/* ./frontend/public/output/
cp ./output/final_movie_list.csv ./frontend/public/output/final_movie_list.csv