package com.javiermoreno.trends;

import static java.text.MessageFormat.format;

import java.util.Random;

import com.javiermoreno.trends.domain.TweetDTOThrift;

public class DemoHelpers {

    private static final Random rnd = new Random();
    
    public static void configLogging() {
        org.apache.log4j.BasicConfigurator.configure();
        org.apache.log4j.LogManager.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        org.apache.log4j.PatternLayout layout = new org.apache.log4j.PatternLayout("%d{ss.SSS} %c{1}\t%t %m%n");
        org.apache.log4j.ConsoleAppender appender = new org.apache.log4j.ConsoleAppender(layout); 
        org.apache.log4j.LogManager.getRootLogger().removeAllAppenders();
        org.apache.log4j.LogManager.getRootLogger().addAppender(appender);
        org.apache.log4j.LogManager.getLogger("com.javiermoreno").setLevel(org.apache.log4j.Level.ALL);
    }
    
    public static String instanceName(Object obj) {
        int pos = obj.toString().lastIndexOf('@');
        return obj.toString().substring(pos);
    }
    
    public static TweetDTOThrift crearTweet() {
        String userId = format("Usuario{0}", rnd.nextInt(100));
        String message = format("Me gusta contar hasta {0}.", rnd.nextInt(100) + 5);
        double lat = 41.437 + rnd.nextDouble() * (41.437 - 41.370);
        double lon = 2.196 + rnd.nextDouble() * (2.196 - 2.132);
        TweetDTOThrift tweet = new TweetDTOThrift(userId, message);
        tweet.setLat(lat);
        tweet.setLon(lon);
        tweet.setTimestamp(System.currentTimeMillis());
    
        return tweet;

    }    
}
