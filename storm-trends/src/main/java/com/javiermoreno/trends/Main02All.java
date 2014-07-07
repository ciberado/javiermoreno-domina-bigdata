package com.javiermoreno.trends;

import static com.javiermoreno.trends.DemoHelpers.configLogging;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

import com.javiermoreno.trends.stormcomponents.EchoConsoleBolt;
import com.javiermoreno.trends.stormcomponents.FakeTweetSpout;

/**
 * Pequeña topología local para arrancar el spout fake y enviarlo a consola.
 * 
 * Configuración:
 * 
 *  1 spout en 2 tareas, con lo que se generan el doble de tweets
 *  1 bolt en 1 worker con 2 executor con 2 tareas
 *         allgrouping hace que cada tarea del bolt quiera procesar todas las tuplas
 *         con lo que el stream se divide y cada instancia del bolt 
 *         obtiene una copia de las tuplas.
 *          
 * @author ciberado
 * 
 */
public class Main02All {

    public static void main(String[] args) {
        configLogging();
        
        Config conf = new Config();
        conf.setNumWorkers(1); // 1 procesos
        conf.setDebug(true);

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("utmSource", new FakeTweetSpout(), 2); // hint: 2 executors (threads) 
        builder.setBolt("echoBolt", new EchoConsoleBolt(), 2)   // paralelism hint:  2 executors 
               .allGrouping("utmSource"); // Recibe todas las tuplas de utmSource

        LocalCluster cluster = new LocalCluster();

        cluster.submitTopology("test", conf, builder.createTopology());
        Utils.sleep(2000);
        cluster.killTopology("test");
        // En windows hay una excepción con el borrado de un fichero, pero no importa.
        cluster.shutdown();
    }

}
