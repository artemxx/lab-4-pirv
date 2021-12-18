import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordFilter {
    public static class FilterMapper
    extends Mapper<Object, Text, Text, Text> {

        private String article = new String();
        private boolean first = true;

        public void map(Object key, Text value, Context context
                       ) throws IOException, InterruptedException 
        {
            String word = value.toString();
            if (first) {
                first = false;
                article = word;
                return;
            }


            String[] filterWords = context.getConfiguration().getStrings("filter_words");
            for (String w : filterWords) {
                if (word.equals(w)) {
                    context.write(new Text(word), new Text(article));
                }
            }
        }
    }

    public static class FilterReducer
    extends Reducer<Text, Text, Text, Text> {

        public void reduce(Text key, Iterable<Text> values, Context context
                          ) throws IOException, InterruptedException 
        {
            StringBuilder builder = new StringBuilder();
            builder.append(key);
            for (Text val : values) {
                builder.append("\t");
                builder.append(val);
            }
            context.write(new Text(builder.toString()), new Text());
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        List<String> filterWords = Files.readAllLines(Paths.get(args[2]));
        conf.setStrings("filter_words", filterWords.toArray(new String[filterWords.size()]));

        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(WordFilter.class);

        job.setMapperClass(FilterMapper.class);
        job.setReducerClass(FilterReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
