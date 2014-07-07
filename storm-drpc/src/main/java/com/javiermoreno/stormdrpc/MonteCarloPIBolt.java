package com.javiermoreno.stormdrpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class MonteCarloPIBolt extends BaseBasicBolt {
    private static final long serialVersionUID = 1L;
    
    private static final Logger log = LoggerFactory.getLogger(MonteCarloPIBolt.class);

    public void execute(Tuple input, BasicOutputCollector collector) {
        Object requestId = input.getValue(0);
        int iteraciones = Integer.parseInt(input.getString(1));
        
        double resultado = PIMonteCarlo.calcularPI(iteraciones);
        log.debug("Calculando PI con {0} iteraciones el resultado ha sido {1}.", iteraciones, resultado);
        
        collector.emit(new Values(requestId, resultado));
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("id", "resultado"));        
    }

}
