package AvgRatingByOccupation;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class AvgRatingByOccupationMapper extends Mapper<LongWritable, Text, Text, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split("\\|");
        if (fields.length >= 5) {
            String userId = fields[0];
            String occupation = fields[3];
            context.write(new Text(userId), new Text("OCCUPATION|||" + occupation)); // ✅ consistent with reducer
        }
    }
}
