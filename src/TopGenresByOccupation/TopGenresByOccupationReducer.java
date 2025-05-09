package TopGenresByOccupation;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;
import java.util.*;

public class TopGenresByOccupationReducer extends Reducer<Text, Text, Text, FloatWritable> {
    private final Map<String, Float> sumMap = new HashMap<>();
    private final Map<String, Integer> countMap = new HashMap<>();

    
    protected void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {

        String occupation = null;
        List<String[]> genreRatings = new ArrayList<>();

        for (Text val : values) {
            String str = val.toString();
            if (str.startsWith("OCCUPATION|||")) {
                occupation = str.split("\\|\\|\\|")[1];
            } else if (str.startsWith("GENRE|||")) {
                String[] parts = str.split("\\|\\|\\|");
                if (parts.length == 3) {
                    genreRatings.add(new String[] { parts[1], parts[2] });  // genre, rating
                }
            }
        }

        if (occupation != null) {
            for (String[] pair : genreRatings) {
                String genre = pair[0];
                float rating = Float.parseFloat(pair[1]);

                String combo = occupation + "\t" + genre;
                sumMap.put(combo, sumMap.getOrDefault(combo, 0f) + rating);
                countMap.put(combo, countMap.getOrDefault(combo, 0) + 1);
            }
        }
    }

    
    protected void cleanup(Context context) throws IOException, InterruptedException {
        for (String key : sumMap.keySet()) {
            float avg = sumMap.get(key) / countMap.get(key);
            context.write(new Text(key), new FloatWritable(avg));
        }
    }
}
