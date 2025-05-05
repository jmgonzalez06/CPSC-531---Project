# preprocess.py
# Filters movie_results.csv to extract only popular movies,
# sorts by average rating descending, and outputs both .txt and .csv files.

import os
import csv

# Define input/output file paths
INPUT_FILE = "../output/movie_results.csv"
TXT_OUTPUT = "../output/popular_movies.txt"
CSV_OUTPUT = "../output/pop_movies.csv"

def main():
    # Ensure the input file exists
    if not os.path.exists(INPUT_FILE):
        print(f"ERROR: Input file not found at {INPUT_FILE}")
        return

    popular = []  # List to hold filtered rows

    with open(INPUT_FILE, "r", encoding="utf-8") as csvfile:
        reader = csv.reader(csvfile)
        header = next(reader)  # Save header
        for row in reader:
            if len(row) != 6:
                continue  # Skip malformed rows
            movie_id, title, genre, avg, count, is_popular = row
            if is_popular.strip().lower() == "yes":
                try:
                    avg_float = float(avg)
                    popular.append((avg_float, row))
                except ValueError:
                    continue  # Skip rows with bad numeric values

    # Sort by avgRating descending
    popular.sort(key=lambda x: x[0], reverse=True)

    # Write plain-text output
    with open(TXT_OUTPUT, "w", encoding="utf-8") as txtfile:
        for _, row in popular:
            txtfile.write("\t".join(row) + "\n")

    # Write clean CSV output
    with open(CSV_OUTPUT, "w", newline="", encoding="utf-8") as csvfile:
        writer = csv.writer(csvfile, quoting=csv.QUOTE_ALL)
        writer.writerow(header)  # Write header
        for _, row in popular:
            writer.writerow(row)

    print(f"✅ Wrote {len(popular)} movies to:")
    print(f"- {TXT_OUTPUT}")
    print(f"- {CSV_OUTPUT}")

if __name__ == "__main__":
    main()
