package com.javiermoreno.stormcluster;

import static java.text.MessageFormat.format;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;


/*
 *  ¡¡BaseBasicBolt automáticamente gestiona los ack!!! 
 * 
 */
public class EchoConsoleBolt extends BaseRichBolt  /* extends BaseBasicBolt para autoack */{
    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(EchoConsoleBolt.class);

    private OutputCollector collector;
    
    @Override
    public void execute(Tuple input) {
        // el índice 2 contiene el cuadrado mgrs
        log.debug(format("{0}*R {1}", input.getString(0), this));
        if (Math.random() < 0.9) {
            collector.ack(input);
        }
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;        
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        
    }


}
