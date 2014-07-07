/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javiermoreno.hadoop.mapreduce;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 *
 * @author ciberado
 */
public class DifferentUsersReducer extends Reducer<LongWritable, Text, LongWritable, IntWritable> implements Serializable {
    private static final long serialVersionUID = 1L;
    private IntWritable userCounts = new IntWritable();

    public void reduce(LongWritable key, Iterable<Text> values, Context context) 
    throws IOException, InterruptedException {
        Set<String> users = new HashSet<>();
        for (Text val : values) {
           users.add(val.toString());
        }
        userCounts.set(users.size());
        context.write(key, userCounts);
    }
    
}
