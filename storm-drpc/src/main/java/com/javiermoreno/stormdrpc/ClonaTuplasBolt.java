package com.javiermoreno.stormdrpc;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class ClonaTuplasBolt extends BaseBasicBolt {
    private static final long serialVersionUID = 1L;
    private static final int WORKERS = 4;

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        
        for (int i=0; i < WORKERS; i++) {
            collector.emit(new Values(input.getValue(0), input.getValue(1)));
        }
        
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("id", "iteraciones"));
        
    }

}
