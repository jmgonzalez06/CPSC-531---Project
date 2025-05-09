package GenresByAge;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class GenresByAgeMapper extends Mapper<LongWritable, Text, Text, Text> {
    
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String[] fields = value.toString().split("\\|");
        if (fields.length >= 2) {
            String userId = fields[0];
            int age = Integer.parseInt(fields[1]);
            String ageGroup = getAgeGroup(age);
            context.write(new Text(userId), new Text("AGEGROUP|||" + ageGroup));
        }
    }

    private String getAgeGroup(int age) {
        if (age < 18) return "<18";
        else if (age <= 25) return "18-25";
        else if (age <= 35) return "26-35";
        else if (age <= 45) return "36-45";
        else if (age <= 55) return "46-55";
        else if (age <= 65) return "56-65";
        else return "66+";
    }
}
