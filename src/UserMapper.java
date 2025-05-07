import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class UserMapper extends Mapper<LongWritable, Text, Text, Text> {

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] fields = line.split("\\|");

        if (fields.length == 5) {
            String userId = fields[0];
            String age = fields[1];
            String gender = fields[2];
            String position = fields[3];
            String zipcode = fields[4];

            // Emit key-value: (userId, age, gender, position, zipcode)
            context.write(new Text(userId), new Text("USER:" + age + "," + gender + "," + position + "," + zipcode));
        }
    }
}