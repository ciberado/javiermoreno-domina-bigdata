package com.javiermoreno.stormdrpc;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.drpc.LinearDRPCTopologyBuilder;
import backtype.storm.tuple.Fields;


@SuppressWarnings("deprecation")
public class Main {

    public static void main(String[] args) {
        LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("montecarloPI");
        builder.addBolt(new ClonaTuplasBolt(), 1);
        builder.addBolt(new MonteCarloPIBolt() , 4)
                    .shuffleGrouping();
        builder.addBolt(new PIAgregadorBolt(), 1)
                    .fieldsGrouping(new Fields("id"));        
        
        LocalDRPC drpc = new LocalDRPC();
        LocalCluster cluster = new LocalCluster();
        Config conf = new Config();
        cluster.submitTopology("computeProductSumTopology", conf, builder.createLocalTopology(drpc));
        
        // En producción: DRPCClient client = new DRPCClient("10.24.0.166", 1234);
        // double resultado = Double.parseDouble(clien.execute("montecarloPI", "10000000"));
        
        double resultado = Double.parseDouble(drpc.execute("montecarloPI", "1000"));
        resultado = Double.parseDouble(drpc.execute("montecarloPI", "1000"));
        
        System.out.println("RESULTADO: ************ " + resultado + "(" + (resultado - Math.PI) + ")");
        drpc.shutdown();
        cluster.shutdown();
    }

    
}
