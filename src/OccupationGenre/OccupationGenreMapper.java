package OccupationGenre;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OccupationGenreMapper extends Mapper<LongWritable, Text, Text, FloatWritable> {
    private Text occupationGenreKey = new Text();
    private FloatWritable ratingValue = new FloatWritable();

    // In-memory maps to store occupation and genre mappings
    private Map<String, String> occupationMap = new HashMap<>();
    private Map<String, String> genreMap = new HashMap<>();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        // Load occupation data (u.user) into memory (userId -> occupation)
        // Example data load: userId -> occupation
        occupationMap.put("1", "student");  // Example: userId 1 is a student
        occupationMap.put("2", "artist");   // Example: userId 2 is an artist
        // Add more users and occupations based on u.user data

        // Load genre data (u.item) into memory (movieId -> genre)
        // Example data load: movieId -> genre
        genreMap.put("1", "Action");        // Example: movieId 1 is Action
        genreMap.put("2", "Comedy");        // Example: movieId 2 is Comedy
        // Add more movies and genres based on u.item data
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split("\t");

        if (fields.length >= 3) {
            String userId = fields[0];          // userId
            String movieId = fields[1];         // movieId
            float rating = Float.parseFloat(fields[2]);  // rating

            // Lookup occupation from u.user and genre from u.item
            String occupation = getUserOccupation(userId);  // Get occupation for userId
            String genre = getMovieGenre(movieId);          // Get genre for movieId

            // Form the key as Occupation + Genre and emit the rating
            occupationGenreKey.set(occupation + "," + genre);
            ratingValue.set(rating);
            context.write(occupationGenreKey, ratingValue);  // Emit key-value pair (occupation,genre) -> rating
        }
    }

    private String getUserOccupation(String userId) {
        // Fetch occupation from the in-memory occupationMap
        return occupationMap.getOrDefault(userId, "Unknown");  // Return "Unknown" if occupation not found
    }

    private String getMovieGenre(String movieId) {
        // Fetch genre from the in-memory genreMap
        return genreMap.getOrDefault(movieId, "Unknown");  // Return "Unknown" if genre not found
    }
}
