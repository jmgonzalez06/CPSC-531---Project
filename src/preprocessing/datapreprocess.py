import os
import re

INPUT_FILE = "./data/u.item"
OUTPUT_FILE = "./output/item.cleaned"

ARTICLES = {"The", "A", "An"}


def normalize_title(title):
    """
    Example: "Great Escape, The (1963)" -> "The Great Escape (1963)"
    """
    match = re.match(r"^(.*),\s(The|A|An)\s*(\(\d{4}\))?$", title)
    if match:
        base = match.group(1).strip()
        article = match.group(2).strip()
        year = match.group(3) if match.group(3) else ""
        return f"{article} {base} {year}".strip()
    return title


def main():
    if not os.path.exists(INPUT_FILE):
        print(f"File not found: {INPUT_FILE}")
        return

    with open(INPUT_FILE, "r", encoding="ISO-8859-1") as infile, open(OUTPUT_FILE, "w", encoding="ISO-8859-1") as outfile:
        for line in infile:
            fields = line.strip().split("|")
            if len(fields) > 1:
                fields[1] = normalize_title(fields[1])
            outfile.write("|".join(fields) + "\n")

    print(f"Cleaned titles written to: {OUTPUT_FILE}")



