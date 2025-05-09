package AvgRatingByOccupation;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class AvgRatingByOccupationDriver {
    public static void main(String[] args) throws Exception {
        //Path
        String ratingsPath = "/input/ratings/u.data";
        String usersPath = "/input/users/u.user";
        String outputPath = "/output/AvgRatingByOccupation";

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Average Rating by Occupation");

        job.setJarByClass(AvgRatingByOccupationDriver.class);

        MultipleInputs.addInputPath(job, new Path(ratingsPath), TextInputFormat.class, RatingMapper.class);
        MultipleInputs.addInputPath(job, new Path(usersPath), TextInputFormat.class, AvgRatingByOccupationMapper.class);

        job.setReducerClass(AvgRatingByOccupationReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);


        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
