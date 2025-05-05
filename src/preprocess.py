# preprocess.py
# This script filters and sorts the output from the MapReduce job.
# It reads the output.txt file (from Hadoop), keeps only the movies marked as "isPopular == Yes",
# sorts them by average rating in descending order, and writes them to popular_movies.txt.

import os  # Import standard library to interact with the file system

# Define input and output file paths
INPUT_FILE = "../output/output.txt"  # Input file from MapReduce output
OUTPUT_FILE = "../output/popular_movies.txt"  # Output file to write filtered, sorted results

def parse_line(line):
    """
    Parses a single line from the output.txt file.
    Each line is expected to look like:
    movieId<TAB>title,genre,avgRating,numRatings,isPopular

    Returns a dictionary with extracted fields, or None if parsing fails.
    """
    try:
        movie_id, rest = line.strip().split("\t")  # Split line by tab
        title, genre, avg, count, is_popular = rest.split(",")  # Split rest of line by commas
        return {
            "movie_id": movie_id,
            "title": title,
            "genre": genre,
            "avg": float(avg),  # Convert avgRating to float for sorting
            "count": int(count),  # Convert count to integer
            "is_popular": is_popular
        }
    except ValueError:
        # If the line doesn't match the expected format, return None
        return None

def main():
    """
    Main function to handle reading, filtering, sorting, and writing the data.
    """
    # Check if the input file exists
    if not os.path.exists(INPUT_FILE):
        print(f"ERROR: Input file not found at {INPUT_FILE}")
        return

    popular_movies = []  # List to store popular movie records

    # Open and read the input file line-by-line
    with open(INPUT_FILE, "r", encoding="utf-8") as infile:
        for line in infile:
            record = parse_line(line)  # Parse the line into fields
            # Only keep movies marked as popular (isPopular == Yes)
            if record and record["is_popular"].lower() == "yes":
                # Store tuple of (avgRating, original line) for sorting
                popular_movies.append((record["avg"], line.strip()))

    # Sort the movies by average rating in descending order
    popular_movies.sort(reverse=True)

    # Write the sorted, filtered results to the output file
    with open(OUTPUT_FILE, "w", encoding="utf-8") as outfile:
        for _, line in popular_movies:
            outfile.write(line + "\n")

    print(f"✅ Wrote {len(popular_movies)} popular movies to {OUTPUT_FILE}")

# Run the main function if this script is executed directly
if __name__ == "__main__":
    main()
