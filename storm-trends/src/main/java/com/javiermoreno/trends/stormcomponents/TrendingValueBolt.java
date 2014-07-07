package com.javiermoreno.trends.stormcomponents;

import static com.javiermoreno.trends.DemoHelpers.instanceName;
import static java.text.MessageFormat.format;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.Config;
import backtype.storm.Constants;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.javiermoreno.trends.domain.Frecuencia;
import com.javiermoreno.trends.domain.Ventana;

public class TrendingValueBolt<T extends Comparable<T>> extends BaseRichBolt{

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(TrendingValueBolt.class);

    private OutputCollector collector;
    
    private Ventana<T> ventana;
    private int trendingFieldIndex;

    public TrendingValueBolt(int trendingFieldIndex) {
        this.trendingFieldIndex = trendingFieldIndex;
    }
    
    @Override
    public Map<String, Object> getComponentConfiguration() {
        Config conf = new Config();
        int tickFrequencyInSeconds = 10;
        conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, tickFrequencyInSeconds);
        return conf;
    }
    
    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        log.debug(format("Preparando bolt {0}.", instanceName(this)));
        this.collector = collector;
        this.ventana = new Ventana<>();
    }
    
    private static boolean isTickTuple(Tuple tuple) {
        return tuple.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID)
            && tuple.getSourceStreamId().equals(Constants.SYSTEM_TICK_STREAM_ID);
    }
    
    @Override
    public void execute(Tuple input) {
        if (isTickTuple(input) == false) {
            log.debug(format("TrendingBolt recibiendo tupla."));
            T trendingValue = (T) input.getValue(trendingFieldIndex);
            ventana.registrar(trendingValue);
            collector.ack(input);            
        } else {
            List<Frecuencia<T>> top = ventana.obtenerTopFrecuenciasAndAvanzarTick();
            log.info(format("Recibida tupla tick: emitiendo top: {0}.", top));
            collector.emit(new Values(ventana.getTickActual(), top));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("tick", "top"));
    }
}
