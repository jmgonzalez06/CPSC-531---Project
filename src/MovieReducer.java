/**
* MovieReducer.java
* Hadoop MapReduce Reducer to aggregate ratings by genre and decade.
* Computes top-rated genres and identifies a rising star.
*/

package movierating;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class MovieReducer extends Reducer<Text, Text, Text, Text> {
    // Placeholder for reduce method
}