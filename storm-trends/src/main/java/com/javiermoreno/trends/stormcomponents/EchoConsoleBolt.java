package com.javiermoreno.trends.stormcomponents;

import static java.text.MessageFormat.format;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import static com.javiermoreno.trends.DemoHelpers.instanceName;


/*
 *  ¡¡BaseBasicBolt automáticamente gestiona los ack!!! 
 * 
 */
public class EchoConsoleBolt extends BaseBasicBolt {
    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(EchoConsoleBolt.class);
    
    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        log.info(format("Preparando bolt {0}.", instanceName(this)));
    }
    
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        // el índice 2 contiene el cuadrado mgrs
        log.info(format("{0}*R {1}", input.getString(2), instanceName(this)));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // Dead end, aquí no emitimos
    }

}
