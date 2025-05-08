public class RatingMapperTwo extends Mapper<LongWritable, Text, Text, Text> {
  public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    String[] fields = value.toString().split("\\t");
    if (fields.length >= 3) {
      String userId = fields[0];
      String movieId = fields[1];
      String rating = fields[2];
      context.write(new Text(userId), new Text("RATING|" + movieId + "|" + rating));
    }
  }
}