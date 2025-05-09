import os
import csv
import re

INPUT_FILE = "./output/movie_results.csv"
CSV_OUTPUT = "./output-cleaned/final_movie_list.csv"

# This script processes a CSV file containing movie data, cleans the titles, and filters based on rating count.
def normalize_title(title):
    match = re.match(r"^(.*),\s(The|A|An)\s*(\(\d{4}\))?$", title) # Match titles like "Movie, The (2020)"
    if match:
        base = match.group(1).strip()
        article = match.group(2).strip()
        year = match.group(3) if match.group(3) else ""
        return f"{article} {base} {year}".strip()
    return title

def main():
    if not os.path.exists(INPUT_FILE):
        print(f"ERROR: Input file not found at {INPUT_FILE}")
        return

    results = []

    with open(INPUT_FILE, "r", encoding="utf-8") as csvfile:
        reader = csv.reader(csvfile)
        next(reader) 

        for row in reader:
            if len(row) < 6:
                continue

            movie_id, title, genre_str, avg, count, _ = row 

            try:
                avg_float = float(avg)
                count_int = int(count)

                if count_int >= 10:
                    clean_title = normalize_title(title.strip())
                    genre_combined = ", ".join(g.strip() for g in genre_str.split(",")) # clean up genre
                    results.append((avg_float, [movie_id, clean_title, genre_combined, avg.strip(), count.strip()])) 
            except ValueError:
                continue

    results.sort(key=lambda x: x[0], reverse=True)

    os.makedirs(os.path.dirname(CSV_OUTPUT), exist_ok=True)

    with open(CSV_OUTPUT, "w", newline="", encoding="utf-8") as csvfile:
        writer = csv.writer(csvfile, quoting=csv.QUOTE_ALL)
        writer.writerow(["movieId", "title", "genre", "avgRating", "numRatings"]) # header
        for _, row in results:
            writer.writerow(row)

    print(f"Cleaned and saved {len(results)} movies to:") 
    print(f"{CSV_OUTPUT}")

if __name__ == "__main__":
    main()
