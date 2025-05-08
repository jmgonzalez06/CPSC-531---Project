public class UserMovieReducer extends Reducer<Text, Text, Text, Text> {
  private Map<String, String> movieMap = new HashMap<>();

  @Override
  protected void setup(Context context) throws IOException {
    // Load movie titles from distributed cache or use a preload
  }

  public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
    String userInfo = "";
    List<String> ratings = new ArrayList<>();

    for (Text val : values) {
      String[] parts = val.toString().split("\\|");
      if (parts[0].equals("USER")) {
        userInfo = parts[1] + "," + parts[2] + "," + parts[3] + "," + parts[4]; // age, gender, occupation, zip
      } else if (parts[0].equals("RATING")) {
        ratings.add(parts[1] + ":" + parts[2]); // movieId:rating
      }
    }

    for (String r : ratings) {
      String[] movieData = r.split(":");
      String movieId = movieData[0];
      String rating = movieData[1];
      String title = movieMap.get(movieId);
      if (title != null && !userInfo.isEmpty()) {
        context.write(key, new Text(userInfo + "," + movieId + "," + title + "," + rating));
      }
    }
  }
}