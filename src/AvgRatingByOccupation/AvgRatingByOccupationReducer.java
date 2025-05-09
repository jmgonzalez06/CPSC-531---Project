package AvgRatingByOccupation;


import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AvgRatingByOccupationReducer extends Reducer<Text, Text, Text, FloatWritable> {
    private Map<String, Integer> countMap = new HashMap<>();
    private Map<String, Float> sumMap = new HashMap<>();

    
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        String occupation = null;
        float total = 0;
        int count = 0;

        for (Text val : values) {
            String str = val.toString();
            if (str.startsWith("OCCUPATION|||")) {
                occupation = str.split("\\|\\|\\|")[1];
            } else {
                try {
                    total += Float.parseFloat(str);
                    count++;
                } catch (NumberFormatException ignored) {}
            }
        }

        if (occupation != null && count > 0) {
            sumMap.put(occupation, sumMap.getOrDefault(occupation, 0f) + total);
            countMap.put(occupation, countMap.getOrDefault(occupation, 0) + count);
        }
    }

    
    protected void cleanup(Context context) throws IOException, InterruptedException {
        for (String occ : sumMap.keySet()) {
            float avg = sumMap.get(occ) / countMap.get(occ);
            context.write(new Text(occ), new FloatWritable(avg));
        }
    }
}