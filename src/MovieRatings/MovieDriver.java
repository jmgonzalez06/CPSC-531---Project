package MovieRatings;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MovieDriver {
    public static void main(String[] args) throws Exception {

        // Hardcoded input and output paths
        String ratingsPath = "/input/ratings/u.data";
        String moviesPath = "/input/movies/u.item";
        String outputPath = "/output/MovieRatings";

        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://localhost:9000");

        Job job = Job.getInstance(conf, "Movie Ratings Aggregation");
        job.setJarByClass(MovieDriver.class);

        MultipleInputs.addInputPath(job, new Path(ratingsPath), TextInputFormat.class, RatingsMapper.class);
        MultipleInputs.addInputPath(job, new Path(moviesPath), TextInputFormat.class, MovieMapper.class);

        job.setReducerClass(MovieReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        //Sucess or failure message
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
