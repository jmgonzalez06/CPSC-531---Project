package AvgRatingByOccupation;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class RatingMapper extends Mapper<LongWritable, Text, Text, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split("\t");
        if (fields.length >= 3) {
            String userId = fields[0];
            String rating = fields[2];
            context.write(new Text(userId), new Text(rating));
        }
    }
}