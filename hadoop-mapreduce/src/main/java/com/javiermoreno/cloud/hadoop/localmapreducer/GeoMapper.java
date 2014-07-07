/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javiermoreno.cloud.hadoop.localmapreducer;

import java.io.IOException;
import java.io.Serializable;
import java.util.StringTokenizer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import au.com.bytecode.opencsv.CSVParser;

/**
 *
 * @author ciberado
 */
public class GeoMapper extends Mapper<Object, Text, Text, IntWritable>
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final CSVParser parser = new CSVParser(',', '"');

    private static final IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(Object key, Text value, Context context) {
        try {
            String[] parsedLine = parser.parseLine(value.toString());
            if (parsedLine.length > 2 && parsedLine[2].isEmpty() == false) {
                double lon = Double.parseDouble(parsedLine[2]);
                double lat = Double.parseDouble(parsedLine[3]);
                CoordinateLatLon latLon = new CoordinateLatLon(lat, lon);
                CoordinateUTM utm = Datum.WGS84.latLonToUTM(latLon, -1);
                utm.setAccuracy(1000.0);
                String mgrs = utm.getShortForm();
                word.set(mgrs);
                context.write(word, one);
            }
        } catch (Exception exc) {
            System.out.println("Line: " + value.toString());
            exc.printStackTrace(System.out);
            context.getCounter("errores", "ioexceptions").increment(1);
        }
    }

}
