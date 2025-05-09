package OccupationGenre;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;


public class OccupationGenreDriver {
    public static void main(String[] args) throws Exception {
        String ratingsPath = "/input/ratings/u.data";
        String usersPath = "/input/users/u.user";
        String moviesPath = "/input/movies/u.item";
        String outputPath = "/output/OccupationGenre";

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Occupation and Genre Average Rating");

        job.setJarByClass(OccupationGenreDriver.class);

        // Define input paths
        MultipleInputs.addInputPath(job, new Path(ratingsPath), TextInputFormat.class, OccupationGenreMapper.class);
        MultipleInputs.addInputPath(job, new Path(usersPath), TextInputFormat.class, OccupationGenreMapper.class);  // You may need a different mapper here
        MultipleInputs.addInputPath(job, new Path(moviesPath), TextInputFormat.class, OccupationGenreMapper.class);

        // Define reducer
        job.setReducerClass(OccupationGenreReducer.class);

        // Set output key and value types
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FloatWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);

        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
