/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javiermoreno.hadoop.mapreduce;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * 
 *
 * @author ciberado
 */
public class TimeUserMapper extends Mapper<Object, Text, LongWritable, Text> 
implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static final long INTERVALO = 1000 * 60 * 5; // cinco minutos
    
    private LongWritable time = new LongWritable ();
    private Text username = new Text();
    private ObjectMapper mapper;

    public TimeUserMapper() {
        mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZ yyyy", Locale.ENGLISH));
    }
    
    public void map(Object key, Text value, Context context) {
    	try {
    	    String line = value.toString();
    	    if (line.isEmpty() == false) {
                Tweet tweet = mapper.readValue(line, Tweet.class);
                time.set(tweet.getCreatedAt().getTime() / INTERVALO * INTERVALO);
                username.set(tweet.getUser().getName());
                context.write(time, username); 
    	    }
        } catch (IOException exc) {
            context.getCounter("Errores", "IOException").increment(1);
        } catch (InterruptedException exc) {
            context.getCounter("Errores", "InterruptedException").increment(1);
    	}
    }
    
}
