package com.javiermoreno.trends;

import static com.javiermoreno.trends.DemoHelpers.configLogging;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

import com.javiermoreno.trends.stormcomponents.EchoConsoleBolt;
import com.javiermoreno.trends.stormcomponents.FakeTweetSpout;

/**
 * Pequeña topología local para arrancar el spout fake y enviarlo a consola.
 * 
 * Configuración:
 * 
 *  1 spout en 1 tareas (como al principio, para simplificar)
 *  1 bolt en 1 worker con 2 executor con 2 tareas
 *                   Si dos tuplas tienen el mismo valor en el campo de agregación siempre la 
 *                   procesará la misma tarea.
 *                   
 *                   Dado que el hash del campo de agrupación está bien definido cada tarea recibe
 *                   (aprox) la mitad de las tuplas. Puede comprobarse con un calc copiando el log
 *                   y sumando las filas con el mismo nombre de thread. Después se puede ordenar por
 *                   la columna de coordenada y ver que la misma coordenada siempre termina en el mismo
 *                   thread.
 *          
 * @author ciberado
 * 
 */
public class Main03Fields {

    public static void main(String[] args) {
        configLogging();
        
        Config conf = new Config();
        conf.setNumWorkers(1); // 1 procesos
        conf.setDebug(true);

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("utmSource", new FakeTweetSpout(), 1); // hint: 1 executors (threads) 
        builder.setBolt("echoBolt", new EchoConsoleBolt(), 2)   // paralelism hint: 2 executors 
               .fieldsGrouping("utmSource", new Fields("utmSquare")); // Agrupa por campo utmSquare

        LocalCluster cluster = new LocalCluster();

        cluster.submitTopology("test", conf, builder.createTopology());
        Utils.sleep(2000);
        cluster.killTopology("test");
        // En windows hay una excepción con el borrado de un fichero, pero no importa.
        cluster.shutdown();
    }

}
