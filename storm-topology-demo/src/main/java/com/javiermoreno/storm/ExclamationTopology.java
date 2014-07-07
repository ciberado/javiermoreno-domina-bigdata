package com.javiermoreno.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.testing.TestWordSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import java.util.Map;

/**
 * This is a basic example of a Storm topology.
 */
public class ExclamationTopology {
    
    public static class PrefixBolt extends BaseRichBolt {
		private static final long serialVersionUID = 1L;

		OutputCollector _collector;
        String _name;
        
        public PrefixBolt(String name) {
			_name = name;
		}
        
        @Override
        public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
            _collector = collector;
        }

        @Override
        public void execute(Tuple tuple) {
        	String message = _name +  tuple.getString(0);
            _collector.emit(tuple, new Values(message));
            _collector.ack(tuple);
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("word"));
        }
    }

    public static class ConsoleBolt extends BaseRichBolt {
		private static final long serialVersionUID = 1L;

		OutputCollector _collector;
        
        public ConsoleBolt() {
		}
        
        @Override
        public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
            _collector = collector;
        }

        @Override
        public void execute(Tuple tuple) {
        	System.out.println("Tupla final: " + tuple.getString(0));
            _collector.ack(tuple);
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("word"));
        }
    }
    
    
    public static void main(String[] args) throws Exception {
        Config conf = new Config();
        // Número de workers (procesos, aka jvm) que ocupará la topología en el clúster
        conf.setNumWorkers(2); // 2 procesos
        // Emite logs a cada emisión de un bolt
        conf.setDebug(true);
        // Si un spout emite una tupla y esta no finaliza su recorrido por la topología
        // antes de un segundo se considerará fail (por defecto, 30)
        conf.setMessageTimeoutSecs(1); 
        // Los ackers son tareas que determinan cuándo una tupla está correctamente
        // procesada. Si el número de tuplas es muy grande debería aumentarse este parámetro
        conf.setNumAckers(1);
        
        // Por muchas tareas que agreguemos al bolt reduce el máximo número de threads a los indicados
        // (util para desarrollar)
        conf.setMaxTaskParallelism(10);
    	
        TopologyBuilder builder = new TopologyBuilder();
        
        // Cada uno de estos set crea un Executor nuevo
        builder.setSpout("wordSpout", new TestWordSpout(), /* hint:dos executors (threads) */ 2);        
        builder.setBolt("starPrefixer", 
        				new PrefixBolt("*"), 
        			    /* paralelism hint: tres executors */ 3)
        		.setNumTasks(3*2) /* Seis tareas, dos tareas por executor */
                .shuffleGrouping("wordSpout");
        builder.setBolt("hashPrefixer", new PrefixBolt("#"), 2).shuffleGrouping("starPrefixer");
        builder.setBolt("consoleBolt", new ConsoleBolt(), 1).shuffleGrouping("hashPrefixer");
        
        
        if(args!=null && args.length > 0) {
            conf.setNumWorkers(3);
            
            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        } else {
        
            LocalCluster cluster = new LocalCluster();
            
            cluster.submitTopology("test", conf, builder.createTopology());
            Utils.sleep(10000);
            cluster.killTopology("test");
            // Hay una excepción con el borrado de un fichero, pero no importa.
            cluster.shutdown();    
        }
    }
}