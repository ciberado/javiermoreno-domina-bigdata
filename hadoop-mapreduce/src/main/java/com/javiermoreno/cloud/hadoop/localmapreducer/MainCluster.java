package com.javiermoreno.cloud.hadoop.localmapreducer;

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
public class MainCluster {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.set("mapred.job.tracker", "192.168.1.180:54311"); 
		conf.set("fs.default.name", "hdfs://192.168.1.180:54310");
		Job job = new Job(conf, "geocount");
		
		job.setJarByClass(MainCluster.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapperClass(GeoMapper.class);
		job.setReducerClass(IntSumReducer.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		String inPath = "/user/hduser/tweets/";
		FileInputFormat.addInputPath(job, new Path(inPath));
		String outPath = "/user/hduser/tweets-out/{0}";
		FileOutputFormat.setOutputPath(job, new Path(MessageFormat.format(outPath, System.currentTimeMillis())));

		job.waitForCompletion(true);
	}

}
