package MovieRatings;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

// Reducer to aggregate ratings and join with movie metadata
public class MovieReducer extends Reducer<Text, Text, Text, Text> {
    
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        // Initialize variables for ratings and metadata
		if (key.toString().equals("1")) {
			context.write(null, new Text("\"movieId\",\"title\",\"genre\",\"avgRating\",\"numRatings\",\"isPopular\""));
		}
        String movieId = key.toString();
        String title = "Unknown";
        String genre = "Unknown";
        ArrayList<Double> ratings = new ArrayList<>();
        ArrayList<Long> timestamps = new ArrayList<>();
        
        // Process values: split into ratings and movie metadata
        for (Text val : values) {
            String[] parts;
		if (val.toString().startsWith("MOVIE|||")) {
			// Split only into 3 parts: type, title, genre
			parts = val.toString().split("\\|\\|\\|", 3);
			if (parts.length == 3) {
				title = parts[1];
				genre = parts[2];
			}
		} else {
			// Assume RATING, use comma
			parts = val.toString().split(",", 3);
			if (parts.length == 3 && parts[0].equals("RATING")) {
				try {
					ratings.add(Double.parseDouble(parts[1]));
					timestamps.add(Long.parseLong(parts[2]));
				} catch (NumberFormatException e) {
					System.err.println("DEBUG: Skipping invalid rating/timestamp for movieId " + movieId);
				}
			}
		}
        }

        // Calculate average rating and count
        if (!ratings.isEmpty()) {
            double sum = 0.0;
            for (Double rating : ratings) {
                sum += rating;
            }
            double avgRating = sum / ratings.size();
            int count = ratings.size();

            // Determine "Rising Star" based on top 10% of timestamps
            String risingStar = "No";
            if (!timestamps.isEmpty()) {
                Collections.sort(timestamps);
                long threshold = timestamps.get((int)(0.9 * timestamps.size())); // Top 10%
                // Check if any timestamp is in the top 10%
                for (Long ts : timestamps) {
                    if (ts >= threshold) {
                        risingStar = "Yes";
                        break;
                    }
                }
            }

            // Format output: movie_id, title, genre, avg_rating, count, rising_star
            String safeTitle = title.replace("\"", "\"\"");  // Escape internal quotes
			String safeGenre = genre.replace("\"", "\"\"");  // (if needed)
            String csvLine = String.format("\"%s\",\"%s\",\"%s\",\"%.2f\",\"%d\",\"%s\"",
				movieId, safeTitle, safeGenre, avgRating, count, risingStar);

			context.write(null, new Text(csvLine));
        }
    }
}