public class UserDriverTwo extends Configured implements Tool {
  public int run(String[] args) throws Exception {
    Job job = Job.getInstance(getConf(), "User Movie Join");

    job.setJarByClass(UserDriverTwo.class);

    MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, UserMapperTwo.class);
    MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, RatingMapperTwo.class);

    job.setReducerClass(UserMovieReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);

    FileOutputFormat.setOutputPath(job, new Path(args[2]));

    return job.waitForCompletion(true) ? 0 : 1;
  }

  public static void main(String[] args) throws Exception {
    int exitCode = ToolRunner.run(new UserDriver(), args);
    System.exit(exitCode);
  }
}