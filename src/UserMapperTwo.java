public class UserMapperTwo extends Mapper<LongWritable, Text, Text, Text> {
  public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    String[] fields = value.toString().split("\\|");
    if (fields.length == 5) {
      String userId = fields[0];
      String userInfo = "USER|" + fields[1] + "|" + fields[2] + "|" + fields[3] + "|" + fields[4];
      context.write(new Text(userId), new Text(userInfo));
    }
  }
}