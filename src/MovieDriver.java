import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MovieDriver {
    public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.set("mapreduce.framework.name", "local");  // Run without YARN
		conf.set("fs.defaultFS", "file:///");

		Job job = Job.getInstance(conf, "Movie Rating Aggregator");

		job.setJarByClass(MovieDriver.class);

		MultipleInputs.addInputPath(job, new Path("data/u.data"), TextInputFormat.class, RatingsMapper.class);
		MultipleInputs.addInputPath(job, new Path("data/u.item"), TextInputFormat.class, MoviesMapper.class);

		job.setReducerClass(MovieReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		FileOutputFormat.setOutputPath(job, new Path("output-local"));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
