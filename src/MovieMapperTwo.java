import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MovieMapperTwo extends Mapper<LongWritable, Text, Text, Text> {

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] fields = line.split("\t");

        if (fields.length == 4) {
            String userId = fields[0];
            String movieId = fields[1];
            String rating = fields[2];
            String timestamp = fields[3];  // Timestamp is not needed, but it is in the file.

            // Emit key-value: (movieId, userId, rating)
            context.write(new Text(movieId), new Text("RATING:" + userId + "," + rating));
        }
    }
}
