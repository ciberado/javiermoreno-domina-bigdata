/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javiermoreno.cloud.hadoop.localmapreducer;

import java.io.IOException;
import java.io.Serializable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 *
 * @author ciberado
 */
public class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> implements Serializable {
    private static final long serialVersionUID = 1L;
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values, Context context) 
    throws IOException, InterruptedException {
        int sum = 0;
        for (IntWritable val : values) {
            sum += val.get();
        }
        result.set(sum);
        context.write(key, result);
    }
    
}




