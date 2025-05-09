package TopGenresByOccupation;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class OccupationMapper extends Mapper<LongWritable, Text, Text, Text> {
    
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String[] fields = value.toString().split("\\|");
        if (fields.length >= 4) {
            String userId = fields[0];
            String occupation = fields[3];
            context.write(new Text(userId), new Text("OCCUPATION|||" + occupation));
        }
    }
}
