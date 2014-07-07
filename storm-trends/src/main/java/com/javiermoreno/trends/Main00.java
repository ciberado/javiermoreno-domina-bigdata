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
 *  1 spout en 1 tarea
 *  1 bolt en 1 worker con 1 executor con 1 tarea
 *         agrupando todas las tuplas en la misma (única) tarea
 *          
 * @author ciberado
 * 
 */
public class Main00 {

    public static void main(String[] args) {
        configLogging();
        
        Config conf = new Config();
        // Número de workers (procesos, aka jvm) que ocupará la topología en el
        // clúster
        conf.setNumWorkers(1); // 1 procesos
        // Emite logs a cada emisión de un bolt
        conf.setDebug(true);
        // Si un spout emite una tupla y esta no finaliza su recorrido por la
        // topología antes de un segundo se considerará fail (por defecto, 30)
        // (solo para tuplas fiables y correctamente ancladas)
        conf.setMessageTimeoutSecs(5);
        // Los ackers son tareas que determinan cuándo una tupla está
        // correctamente
        // procesada. Si el número de tuplas es muy grande debería aumentarse
        // este parámetro
        conf.setNumAckers(1);
        // Por muchas tareas que agreguemos al bolt reduce el máximo número de
        // threads a los indicados
        // (util para desarrollar)
        conf.setMaxTaskParallelism(10);

        TopologyBuilder builder = new TopologyBuilder();

        // Cada uno de estos set crea un Executor nuevo
        builder.setSpout("utmSource", new FakeTweetSpout(), 1); // hint: 1 executors (threads) 
        builder.setBolt("echoBolt", new EchoConsoleBolt(), 1)   // paralelism hint: 1 executors 
               .allGrouping("utmSource"); // Recibe todas las tuplas de utmSource

        LocalCluster cluster = new LocalCluster();

        cluster.submitTopology("test", conf, builder.createTopology());
        Utils.sleep(2000);
        cluster.killTopology("test");
        // En windows hay una excepción con el borrado de un fichero, pero no importa.
        cluster.shutdown();
    }

}
