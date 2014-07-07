package com.javiermoreno.stormcluster;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;

/**
 */
public class Main {

    public static void main(String[] args) {
            Config conf = new Config();
            conf.setMessageTimeoutSecs(5);
            
            TopologyBuilder builder = new TopologyBuilder();

            // Cada uno de estos set crea un Executor nuevo
            builder.setSpout("mensajesSource", new MensajesAlAzarSpout(), 1);
            builder.setBolt("echoBolt", new EchoConsoleBolt(), 1).shuffleGrouping("mensajesSource");

            StormTopology topology = builder.createTopology();
//            StormSubmitter.submitTopology("mensatopologia", conf, topology);

            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology("mensatopologia", conf, topology);
    }

}
