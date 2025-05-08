public class MovieMapperTwo extends Mapper<LongWritable, Text, Text, Text> {
  public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    String[] fields = value.toString().split("\\|");
    if (fields.length > 1) {
      String movieId = fields[0];
      String title = fields[1];
      context.write(new Text(movieId), new Text("MOVIE|" + title));
    }
  }
}