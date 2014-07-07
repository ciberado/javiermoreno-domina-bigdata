package com.javiermoreno.stormdrpc;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.coordination.BatchOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBatchBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class PIAgregadorBolt extends BaseBatchBolt {
    
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(PIAgregadorBolt.class);
    
    private Object requestId;
    private BatchOutputCollector collector;
    private List<Double> resultados = new ArrayList<>();
    
    public void prepare(Map conf, TopologyContext context, BatchOutputCollector collector, Object id) {
        System.out.println(this);
        log.info(MessageFormat.format("Preparando agregador para request {0}", id));
        this.requestId = id;
        this.collector = collector;        
    }

    public void execute(Tuple tuple) {
        double resultadoActual = tuple.getDouble(1);
        resultados.add(resultadoActual);
        log.debug(MessageFormat.format("Obtenido resultado número {0}: {1}.", resultados.size(), resultadoActual));
    }

    public void finishBatch() {
        double suma = 0.0;
        for (double resultado : resultados) {
            suma = suma + resultado;
        }
        double pi = suma / resultados.size(); 
        
        collector.emit(new Values(requestId, pi));
        System.out.println("************ " + resultados);
        log.info(MessageFormat.format("Calculado PI con {0} resultados parciales: {1}.", resultados.size(), pi));
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("id", "pi"));
    }

}
