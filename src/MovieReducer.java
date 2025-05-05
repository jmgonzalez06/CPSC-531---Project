/**
* MovieReducer.java
* Hadoop MapReduce Reducer to aggregate ratings by genre and decade.
* Computes top-rated genres and identifies a rising star.
*/

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

// Reducer to aggregate ratings and join with movie metadata
public class MovieReducer extends Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        // Initialize variables for ratings and metadata
        String movieId = key.toString();
        String title = "Unknown";
        String genre = "Unknown";
        ArrayList<Double> ratings = new ArrayList<>();
        ArrayList<Long> timestamps = new ArrayList<>();
        
        // Debug line to make sure reducer receives data for movieId
        System.err.println("DEBUG: Reducing for movieId " + movieId);

        // Process values: split into ratings and movie metadata
        for (Text val : values) {
            String[] parts = val.toString().split(",");
            if (parts[0].equals("RATING") && parts.length >= 3) {
                try {
                    ratings.add(Double.parseDouble(parts[1]));
                    timestamps.add(Long.parseLong(parts[2]));
                } catch (NumberFormatException e) {
                    // Debug line to catch parsing errors
                    System.err.println("DEBUG: Skipping invalid rating/timestamp for movieId " + movieId);
                }
            } else if (parts[0].equals("MOVIE") && parts.length >= 3) {
                title = parts[1];
                genre = parts[2];
            }
        }

        // Debug line to make sure ratings and metadata are collected
        System.err.println("DEBUG: Collected " + ratings.size() + " ratings, title: " + title + ", genre: " + genre);

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
            String output = String.format("%s,%s,%.2f,%d,%s", title, genre, avgRating, count, risingStar);
            context.write(key, new Text(output));

            // Debug line to make sure output is written
            System.err.println("DEBUG: Output for movieId " + movieId + ": " + output);
        }
    }
}