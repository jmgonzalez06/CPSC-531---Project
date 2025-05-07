import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class MovieReducerTwo extends Reducer<Text, Text, Text, Text> {

    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        List<String> userInfo = new ArrayList<>();
        List<String> ratings = new ArrayList<>();

        // Process all values for a given movieId
        for (Text value : values) {
            String val = value.toString();
            if (val.startsWith("USER:")) {
                userInfo.add(val.substring(5));  // Store user info: age, gender, position, zipcode
            } else if (val.startsWith("RATING:")) {
                ratings.add(val.substring(7));  // Store rating: userId, rating
            }
        }

        // Merge the userInfo with ratings, and emit results
        for (String rating : ratings) {
            for (String info : userInfo) {
                // Emit movieId with the combined user info and rating
                context.write(key, new Text(info + ", Rating: " + rating));
            }
        }
    }
}
