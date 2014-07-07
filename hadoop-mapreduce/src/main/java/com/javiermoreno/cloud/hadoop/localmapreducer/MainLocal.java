package com.javiermoreno.cloud.hadoop.localmapreducer;

import java.net.URL;
import java.text.MessageFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

//http://www.earthpoint.us/Convert.aspx
public class MainLocal {

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("mapred.job.tracker", "local");
        //conf.set("fs.default.name", "local");
        Job job = new Job(conf, "getcount");

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(GeoMapper.class);
        job.setReducerClass(IntSumReducer.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        URL url = MainLocal.class.getClassLoader().getResource("tweets.csv");
        String inPath = url.getPath();
        if (inPath.startsWith("/C:")) inPath = inPath.substring(1);
        FileInputFormat.addInputPath(job, new Path(inPath));
        String outPath = "/tmp/tweets_out_{0}.txt";
        FileOutputFormat.setOutputPath(job, new Path(MessageFormat.format(outPath, System.currentTimeMillis())));

        job.waitForCompletion(true);
    }

}
