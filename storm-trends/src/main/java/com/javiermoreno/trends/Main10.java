package com.javiermoreno.trends;

import static com.javiermoreno.trends.DemoHelpers.configLogging;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

import com.javiermoreno.trends.stormcomponents.FakeTweetSpout;
import com.javiermoreno.trends.stormcomponents.TrendingAgregadorBolt;
import com.javiermoreno.trends.stormcomponents.TrendingValueBolt;

public class Main10 {

    public static void main(String[] args) {
        org.apache.log4j.BasicConfigurator.configure();
        org.apache.log4j.LogManager.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        org.apache.log4j.PatternLayout layout = new org.apache.log4j.PatternLayout("%d{ss.SSS} %c{1}\t%t %m%n");
        org.apache.log4j.ConsoleAppender appender = new org.apache.log4j.ConsoleAppender(layout); 
        org.apache.log4j.LogManager.getRootLogger().removeAllAppenders();
        org.apache.log4j.LogManager.getRootLogger().addAppender(appender);
        org.apache.log4j.LogManager.getLogger(TrendingValueBolt.class).setLevel(org.apache.log4j.Level.INFO);
        org.apache.log4j.LogManager.getLogger(TrendingAgregadorBolt.class).setLevel(org.apache.log4j.Level.ALL);
        
        Config conf = new Config();
        conf.setDebug(true);
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("utmSource", new FakeTweetSpout(), 1); 
        builder.setBolt("trendingValueBolt", new TrendingValueBolt<String>(2 /* frecuenciar utmSquare*/), 2)  
               .fieldsGrouping("utmSource", new Fields("utmSquare"));  
        builder.setBolt("trendingAgregadorBolt", new TrendingAgregadorBolt<>(), 1)
               .allGrouping("trendingValueBolt");

        LocalCluster cluster = new LocalCluster();

        cluster.submitTopology("test", conf, builder.createTopology());
//        Utils.sleep(1000*60);
//        cluster.killTopology("test");
//        cluster.shutdown();
        
    }
    
}
