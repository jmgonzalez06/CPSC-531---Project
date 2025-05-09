package TopGenresByOccupation;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class TopGenresByOccupationDriver {
    public static void main(String[] args) throws Exception {
        String ratingsPath = "/input/ratings/u.data";
        String usersPath = "/input/users/u.user";
        String moviesPath = "/input/movies/u.item";
        String outputPath = "/output/TopGenresByOccupation";

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Top Genres by Occupation");

        job.setJarByClass(TopGenresByOccupationDriver.class);

        MultipleInputs.addInputPath(job, new Path(ratingsPath), TextInputFormat.class, GenreMapper.class);
        MultipleInputs.addInputPath(job, new Path(usersPath), TextInputFormat.class, OccupationMapper.class);

        job.setReducerClass(TopGenresByOccupationReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.addCacheFile(new Path(moviesPath).toUri());
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
