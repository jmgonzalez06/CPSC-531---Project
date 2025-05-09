package MovieRatings;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class MovieMapper extends Mapper<LongWritable, Text, Text, Text> {

    private static final String[] genreNames = {
        "unknown", "Action", "Adventure", "Animation", "Children's", "Comedy", "Crime",
        "Documentary", "Drama", "Fantasy", "Film-Noir", "Horror", "Musical",
        "Mystery", "Romance", "Sci-Fi", "Thriller", "War", "Western"
    };


    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String[] parts = value.toString().split("\\|");

        if (parts.length >= 24) {
            String movieId = parts[0];
            String title = parts[1];

            // Some movies have more than one genre.
            StringBuilder genreBuilder = new StringBuilder();
            for (int i = 0; i < 18; i++) {
                if (parts[5 + i].equals("1")) {
                    if (genreBuilder.length() > 0) {
                        genreBuilder.append(",");
                    }
                    genreBuilder.append(genreNames[i]);
                }
            }

            String genre = genreBuilder.length() > 0 ? genreBuilder.toString() : "Unknown";
            context.write(new Text(movieId), new Text("MOVIE|||" + title + "|||" + genre));
        }
    }
}
