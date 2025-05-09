# CPSC 531 Final Project: Movie Rating Aggregator

**Team:**  Jose M. Gonzalez & Phillip Akhnoukh & Edgardo Lopez
**GitHub:**   https://github.com/jmgonzalez06/CPSC-531---Project
**Date:** May 09, 2025


# Hadoop Movie Aggregator

## Overview
This project analyzes the MovieLens dataset using Hadoop MapReduce to generate meaningful insights about user ratings based on attributes such as occupation, age, and gender.

https://grouplens.org/datasets/movielens/100k/

---

## Recommended: Run on a WSL Terminal

---

## How to Run

### Optional: Install Hadoop (if not already installed)
To easily install Hadoop and helper scripts, run:
```bash
./scripts/hadoop-install.sh
```

> **Note:** This will install Hadoop in your home directory along with start/stop scripts in `hadoop-scripts`.

---

## Step-by-Step

### Step 1. **Start Hadoop**
```bash
./scripts/hadoop-start.sh
```

### Step 2. **Set Up HDFS Input**
```bash
./scripts/1-setup-hdfs.sh
```

### Step 3. **Compile Java Files**
```bash
./scripts/2-compile.sh
```

### Step 4. **Display Output from HDFS**
```bash
./scripts/3-output.sh
```

---

### One-Click Option
Alternatively, run steps 2-4 with one command:
```bash
./scripts/master.sh
```

---

### Manual HDFS Setup (Alternative)

1. Place input files in HDFS:
```bash
hdfs dfs -mkdir -p /input/ratings /input/users /input/movies
hdfs dfs -put u.data /input/ratings/
hdfs dfs -put u.user /input/users/
hdfs dfs -put u.item /input/movies/
```

2. Compile and package the `.java` files into a `.jar`.
```bash
jar -cfe MovieRatings/MovieRating.jar MovieRatings.MovieDriver -C classes .

jar -cfe AvgRatingByOccupation/AvgRatingByOccupation.jar AvgRatingByOccupation.AvgRatingByOccupationDriver -C classes .

jar -cfe TopGenresByOccupation/TopGenresByOccupation.jar TopGenresByOccupation.TopGenresByOccupationDriver -C classes .

jar -cfe TopGenresByGender/TopGenresByGender.jar TopGenresByGender.TopGenresByGenderDriver -C classes .

jar -cfe GenresByAge/GenresByAge.jar GenresByAge.GenresByAgeDriver -C classes .
```

3. Run Hadoop Job
```bash
hadoop jar MovieRatings/MovieRating.jar
hadoop jar AvgRatingByOccupation/AvgRatingByOccupation.jar
hadoop jar TopGenresByOccupation/TopGenresByOccupation.jar
hadoop jar TopGenresByGender/TopGenresByGender.jar
hadoop jar GenresByAge/GenresByAge.jar
```

# Frontend (React)

1. Change directory to frontend
```bash
cd /frontend
```
2. To install npm packages
```bash
npm i
```
3. To run Frontend
```bash 
npm start
```



---

## Dataset Files

- `u.data`: User ratings (userId, movieId, rating, timestamp)
- `u.user`: User demographics (userId, age, gender, occupation, zip)
- `u.item`: Movie metadata (movieId, title, genre)

---

## MapReduce Jobs

### 1. **Movie Ratings Aggregation**
- **Goal**: Compute average rating, number of ratings, and identify "Rising Star" movies.
- **Mappers**: `RatingsMapper`, `MovieMapper`
- **Reducer**: `MovieReducer`
- **Output**: `"movieId","title","genre","avgRating","numRatings","isPopular"`

### 2. **Average Rating by Occupation**
- **Goal**: Calculate the average rating submitted by each occupation group.
- **Mappers**: `RatingMapper`, `AvgRatingByOccupationMapper`
- **Reducer**: `AvgRatingByOccupationReducer`
- **Output**: `<occupation>\t<average_rating>`

### 3. **Genres by Age**
- **Goal**: Determine the average rating per genre across different age groups.
- **Mappers**: `GenreMapper`, `GenresByAgeMapper`
- **Reducer**: `GenresByAgeReducer`
- **Output**: `<ageGroup>\t<genre>\t<average_rating>`

### 4. **Top Genres by Gender**
- **Goal**: Compute average rating per genre separated by gender.
- **Mappers**: `GenreMapper`, `GenderMapper`
- **Reducer**: `TopGenresByGenderReducer`
- **Output**: `<gender>\t<genre>\t<average_rating>`

### 5. **Top Genres by Occupation**
- **Goal**: Compute average rating per genre for each occupation.
- **Mappers**: `GenreMapper`, `OccupationMapper`
- **Reducer**: `TopGenresByOccupationReducer`
- **Output**: `<occupation>\t<genre>\t<average_rating>`
