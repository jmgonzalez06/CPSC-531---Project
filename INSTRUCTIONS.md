# CPSC 531 – MovieLens Hadoop Project: Setup & Execution Guide

## Project Structure Overview

```
/data/                         # Raw dataset files: u.data, u.item, u.user
/src/                          # Java MapReduce source code
  └── MovieRatings/            # First job: Movie avg rating by movie
  └── AvgRatingByOccupation/   # Second job: Avg rating by user occupation
/scripts/                      # Bash scripts for HDFS setup, compiling, output
/output/                       # Local directory where final results are saved
```

---

## What You Need to Edit

### scripts/1-setup-hdfs.sh

- This script removes any existing HDFS directories and uploads the input files.
- Make sure the following files exist in your local `./data/` folder:
  - `u.data`: rating records (userId, movieId, rating, timestamp)
  - `u.item`: movie metadata
  - `u.user`: user demographics
- If you rename or add files, update these lines:
  ```bash
  hdfs dfs -put -f ./data/u.data /input/ratings/
  hdfs dfs -put -f ./data/u.item /input/movies/
  hdfs dfs -put -f ./data/u.user /input/users/
  ```

---

### scripts/2-compile.sh

This script:
1. Cleans and creates a `classes/` directory for compiled `.class` files.
2. Compiles all `.java` files recursively.
3. Builds `.jar` files for each job using `jar -cfe`.

Each line like this:
```bash
jar -cfe MovieRatings/MovieRating.jar MovieRatings.MovieDriver -C classes .
```

Means:
- `MovieRatings/MovieRating.jar`: create the JAR inside the `MovieRatings/` folder under `src/`
- `MovieRatings.MovieDriver`: fully qualified class name of your `public static void main()` entry point
- `-C classes .`: take all compiled `.class` files inside `classes/`, preserving package structure

Repeat this for every job:
```bash
jar -cfe <output_path>.jar <MainClass> -C classes .
```

---

### scripts/3-output.sh

This script:
1. Creates the local `./output/` directory if it doesn’t exist.
2. Downloads Hadoop output files from:
   - `/output/MovieRatings` -> writes `movie_results.csv` and `.txt`
   - `/output/avg_rating_occupation` -> writes `avg_rating_occupation.csv` and `.txt`
3. Optionally runs Python scripts:
   - `datapreprocess.py` for early cleanup
   - `preprocess.py` for final formatting or name adjustments

You can add other outputs or modify file naming here.

---

## How to Run the Whole Pipeline

```bash
./scripts/1-setup-hdfs.sh     # Step 1: Reset and upload dataset to HDFS
./scripts/2-compile.sh        # Step 2: Compile .java and build JARs
./scripts/3-output.sh         # Step 3: Download HDFS output + run Python cleanup
```

---

## Notes for Group Members

- Add new jobs by:
  - Creating a new folder in `/src/`
  - Adding your Mapper/Reducer/Driver `.java` files inside
  - Adding a new `jar -cfe` line in `2-compile.sh`
  - Ensuring output paths in `3-output.sh` are updated if you want to download results

- JAR output goes into:
  - `src/MovieRatings/MovieRating.jar`
  - `src/AvgRatingByOccupation/AvgRatingByOccupation.jar`

- Hadoop jobs are run like:
  ```bash
  hadoop jar MovieRatings/MovieRating.jar
  hadoop jar AvgRatingByOccupation/AvgRatingByOccupation.jar
  ```

- Important Note on Hardcoded Paths in Driver
  - Paths in Driver Classes:
  - In the `AverageRatingByOccupationDriver.java` and similar driver classes, the input and output paths are hardcoded for simplicity. This avoids the need for command-line arguments when running the job.

  - Example for `AverageRatingByOccupationDriver.java`:
    ```bash
    String ratingsPath = "/input/ratings/u.data";
    String usersPath = "/input/users/u.user";
    String outputPath = "/output/AvgRatingByOccupation";
    ```
  - This ensures the paths are fixed and the job runs with the expected inputs and outputs, without needing to pass arguments from the command line.

- If you rename any `public class`, you must match the name in the `jar -cfe` command.

- All input to Hadoop must exist in HDFS. Use `hdfs dfs -ls /` to inspect.

---

## Tips

- Don’t hardcode anything unless you know it won’t change across group members.
- Use relative paths in scripts where possible.
- Use `|| true` when deleting from HDFS to avoid "directory not found" errors breaking the script.
