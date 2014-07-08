package com.javiermoreno.cassandra;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.BasicConfigurator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javiermoreno.cassandra.domain.ClassificationProperties.SentimentType;
import com.javiermoreno.cassandra.domain.Tweet;

public class Main {

    public static void main(String[] args) 
    throws Exception {
        
        BasicConfigurator.configure();
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZ yyyy", Locale.ENGLISH));

        String path = "/temp/tweets_json/tweets.json";
        BufferedReader in = new BufferedReader(new FileReader(path));
        String line = null;
        int count = 0;
        long t0 = System.nanoTime();

        TwitterDAO dao = new TwitterDAO();
        dao.inicializar();
        
        do {
                line = in.readLine();
                if (line != null && line.trim().length() > 0) {
                        try {
                                Tweet tweet = mapper.readValue(line, Tweet.class);
                                String place = tweet.getPlace() == null ? "ES" : tweet.getPlace().getCountryCode();
                                tweet.getClassificationProperties().setCountryCode(place);
                                String sentiment = Math.random() > 0.5 ? "P" : "N";
                                tweet.getClassificationProperties().setSentiment(SentimentType.valueOf(sentiment));
                                dao.guardarTweet(tweet);
                                count = count + 1;
                        } catch (Exception exc) {
                                System.out.println(exc);
                                System.out.println(line);
                        }
                }
        } while (line != null ); // añade count < 1000 si necesario para evitar outofmemory
        in.close();
        long tf = System.nanoTime();
        System.out.println(count + " filas insertdas en  " + (tf-t0) + " ns.");
        
          List<Tweet> tweets = 
                  dao.recuperarTweetsImpl("20130423:P:4", 
                                  "Brasil", "Brasil:9999999999999999999", 5);
          System.out.println(tweets);     
        
    }
    
    
}
