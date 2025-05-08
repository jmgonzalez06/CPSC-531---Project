package MovieRatings;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class RatingsMapper extends Mapper<LongWritable, Text, Text, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] parts = value.toString().split("\t");
        if (parts.length >= 4) {
            String movieId = parts[1];
            String rating = parts[2];
            String timestamp = parts[3];
			System.err.println("DEBUG: RATING emitted for movieId " + movieId);
			System.err.flush();
            context.write(new Text(movieId), new Text("RATING," + rating + "," + timestamp));
        }
    }
}
