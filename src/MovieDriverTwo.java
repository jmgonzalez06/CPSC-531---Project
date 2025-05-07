import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MovieDriverTwo {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://localhost:9000");

        Job job = Job.getInstance(conf, "Group Users by MovieId");
        job.setJarByClass(MovieDriverTwo.class);

        MultipleInputs.addInputPath(job, new Path("/input/ratings/u.data"), TextInputFormat.class, RatingsMapper.class);
        MultipleInputs.addInputPath(job, new Path("/input/movies/u.user"), TextInputFormat.class, MovieMapperTwo.class);
        
        // Set mappers and reducer
        //job.setMapperClass(UserMapper.class);  // Mapper for u.user
        //job.setMapperClass(MovieMapperTwo.class); // Mapper for u.data
        job.setReducerClass(MovieReducerTwo.class);

        // Set output key and value types
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // Set input and output paths
        //FileInputFormat.addInputPath(job, new Path(args[0]));  // u.user file
        //FileInputFormat.addInputPath(job, new Path(args[1]));  // u.data file


        FileOutputFormat.setOutputPath(job, new Path("/output-local"));

        // Wait for job to complete
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
