/**
* MovieMapper.java
* Hadoop MapReduce Mapper to process MovieLens data.
* Extracts genre and rating info from input CSV.
*/

package movierating;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MovieMapper extends Mapper<LongWritable, Text, Text, Text> {
    // Placeholder for map method
}