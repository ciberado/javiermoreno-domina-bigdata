package com.javiermoreno.trends;

import static com.javiermoreno.trends.DemoHelpers.configLogging;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

import com.javiermoreno.trends.stormcomponents.EchoConsoleBolt;
import com.javiermoreno.trends.stormcomponents.FakeTweetEventoSpout;
import com.javiermoreno.trends.stormcomponents.FakeTweetSpout;

/**
 * Pequeña topología local para arrancar el spout fake y enviarlo a consola.
 * 
 * Configuración:
 * 
 *  2 spout en 2 tareas
 *  1 bolt en 1 worker con 1 executor con 1 tarea
 *         El bolt recibe tweets de dos spouts
 *          
 * @author ciberado
 * 
 */
public class Main05Multispout {

    public static void main(String[] args) {
        configLogging();
        
        Config conf = new Config();
        conf.setDebug(true);

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("utmSource", new FakeTweetSpout(), 1); 
        builder.setSpout("evtSource", new FakeTweetEventoSpout(), 1);
        builder.setBolt("echoBolt", new EchoConsoleBolt(), 1)   
               .allGrouping("utmSource") 
               .allGrouping("evtSource");

        LocalCluster cluster = new LocalCluster();

        cluster.submitTopology("test", conf, builder.createTopology());
        Utils.sleep(2000);
        cluster.killTopology("test");
        // En windows hay una excepción con el borrado de un fichero, pero no importa.
        cluster.shutdown();
    }

}
