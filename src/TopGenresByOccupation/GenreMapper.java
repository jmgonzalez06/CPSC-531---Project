package TopGenresByOccupation;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.*;
import java.util.*;

public class GenreMapper extends Mapper<LongWritable, Text, Text, Text> {

    private Map<String, List<String>> movieGenreMap = new HashMap<>();
    private static final String[] GENRES = {
        "unknown", "Action", "Adventure", "Animation", "Children's", "Comedy", "Crime",
        "Documentary", "Drama", "Fantasy", "Film-Noir", "Horror", "Musical",
        "Mystery", "Romance", "Sci-Fi", "Thriller", "War", "Western"
    };

    
    protected void setup(Context context) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("u.item"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split("\\|");
            if (fields.length > 5) {
                String movieId = fields[0];
                List<String> genres = new ArrayList<>();
                for (int i = 5; i < fields.length; i++) {
                    if ("1".equals(fields[i])) {
                        genres.add(GENRES[i - 5]);
                    }
                }
                movieGenreMap.put(movieId, genres);
            }
        }
        reader.close();
    }

    
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String[] fields = value.toString().split("\t");
        if (fields.length >= 3) {
            String userId = fields[0];
            String movieId = fields[1];
            String rating = fields[2];

            List<String> genres = movieGenreMap.get(movieId);
            if (genres != null) {
                for (String genre : genres) {
                    context.write(new Text(userId), new Text("GENRE|||" + genre + "|||" + rating));
                }
            }
        }
    }
}
