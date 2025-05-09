import os
import csv

# Input/output directories
INPUT_DIR = "./output"
OUTPUT_DIR = "./output-cleaned"

# List only the files you want cleaned (must exist in INPUT_DIR)
FILES_TO_CLEAN = [
    "TopGenresByGender.csv",
    "GenresByAge.csv",
    "AvgRatingByOccupation.csv",
    "TopGenresByOccupation.csv"
]

# Header presets based on known output types
HEADER_MAP = {
    3: {
        "AvgRatingByOccupation": ["Occupation", "Genre", "AvgRating"],
        "GenresByAge": ["AgeGroup", "Genre", "AvgRating"],
        "TopGenresByGender": ["Gender", "Genre", "AvgRating"],
        "TopGenresByOccupation": ["Occupation", "Genre", "AvgRating"]
    },
    2: {
        "SimpleCounts": ["Key", "Value"]
    }
}

def infer_headers(filename, num_cols):
    for job_type, headers in HEADER_MAP.get(num_cols, {}).items():
        if job_type.lower() in filename.lower():
            return headers
    return [f"Col{i+1}" for i in range(num_cols)]

def clean_file(input_path, output_path):
    try:
        with open(input_path, "r", newline="", encoding="utf-8") as infile:
            reader = csv.reader(infile, delimiter="\t")
            rows = list(reader)

        if not rows:
            print(f"Skipped empty file: {input_path}")
            return

        num_cols = len(rows[0])
        headers = infer_headers(os.path.basename(input_path), num_cols)

        os.makedirs(os.path.dirname(output_path), exist_ok=True)
        with open(output_path, "w", newline="", encoding="utf-8") as outfile:
            writer = csv.writer(outfile, quoting=csv.QUOTE_ALL)
            writer.writerow(headers)
            writer.writerows(rows)

        print(f"Cleaned: {input_path} → {output_path}")
    except Exception as e:
        print(f"Failed to process {input_path}: {e}")

def main():
    for filename in FILES_TO_CLEAN:
        input_path = os.path.join(INPUT_DIR, filename)
        if not os.path.exists(input_path):
            print(f"File not found: {input_path}")
            continue
        output_filename = os.path.splitext(filename)[0] + "_clean.csv"
        output_path = os.path.join(OUTPUT_DIR, output_filename)
        clean_file(input_path, output_path)

if __name__ == "__main__":
    main()
