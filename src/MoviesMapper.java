/**
* MovieMapper.java
* Hadoop MapReduce Mapper to process MovieLens data.
* Extracts genre and rating info from input CSV.
*/

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class MoviesMapper extends Mapper<LongWritable, Text, Text, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] parts = value.toString().split("\\|");
        if (parts.length >= 24) {
            String movieId = parts[0];
            String title = parts[1];

            String genre = "Unknown";
            String[] genreNames = {
                "Action", "Adventure", "Animation", "Children", "Comedy", "Crime", "Documentary",
                "Drama", "Fantasy", "Film-Noir", "Horror", "Musical", "Mystery",
                "Romance", "Sci-Fi", "Thriller", "War", "Western"
            };

            for (int i = 0; i < 18; i++) {
                if (parts[5 + i].equals("1")) {
                    genre = genreNames[i];
                    break;
                }
            }
			System.err.println("DEBUG: MOVIE emitted for movieId " + movieId + ": " + title);
			System.err.flush();
            context.write(new Text(movieId), new Text("MOVIE," + title + "," + genre));
        }
    }
}
