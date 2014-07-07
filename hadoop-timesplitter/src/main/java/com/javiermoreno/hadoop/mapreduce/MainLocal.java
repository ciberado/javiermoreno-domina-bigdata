package com.javiermoreno.hadoop.mapreduce;

import java.text.MessageFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

//http://www.earthpoint.us/Convert.aspx
// Ejercicio: extraer topónimos http://www20.gencat.cat/portal/site/dadesobertes/menuitem.db4d3cf2bccf921baacf3010b0c0e1a0/?vgnextoid=49b19ee9acb42310VgnVCM1000000b0c1e0aRCRD&vgnextchannel=49b19ee9acb42310VgnVCM1000000b0c1e0aRCRD&vgnextfmt=detall2&id=99&newLang=es_ES
public class MainLocal {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.set("mapred.job.tracker", "local"); 
		//conf.set("fs.default.name", "local");
		
		Job job = new Job(conf, "getcount"); // Job clonará la conf que se le pase como parámetro

		// Si queremos agregar un fichero a la cache distribuída: 
		//DistributedCache.addCacheFile(new URI("/user/peter/cacheFile/testCache1"), job.getConfiguration());
		
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(TimeUserMapper.class);
		job.setReducerClass(DifferentUsersReducer.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		String inPath = "file:///C:/temp/tweets_json/tweets.json";
		FileInputFormat.addInputPath(job, new Path(inPath));
		String outPath = "file:///C:/temp/tweets_json/results{0}/";
		FileOutputFormat.setOutputPath(job, new Path(MessageFormat.format(outPath, System.currentTimeMillis())));

		job.waitForCompletion(true);
	}

}
