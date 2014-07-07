package com.javiermoreno.trends.stormcomponents;

import static com.javiermoreno.trends.DemoHelpers.instanceName;
import static java.text.MessageFormat.format;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

import com.javiermoreno.trends.domain.Frecuencia;

public class TrendingAgregadorBolt<T extends Comparable<T>> extends BaseRichBolt{

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(TrendingAgregadorBolt.class);
   
    private OutputCollector collector;
    
    private SortedSet<Frecuencia<T>> trendingValues = new TreeSet<>(new InverterCompartor());
    
    private int tickActual;
    
    public TrendingAgregadorBolt() {
        this.tickActual = -1;
    }
    
    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        log.debug(format("Preparando bolt {0}.", instanceName(this)));
        this.collector = collector;
    }
    
    @Override
    public void execute(Tuple input) {
        int tickTop = input.getInteger(0);
        List<Frecuencia<T>> topParcial = (List<Frecuencia<T>>) input.getValue(1);
        if (tickActual != tickTop) {
            trendingValues.clear();
            tickActual = tickTop;
            log.debug(format("Agregador ha detectado un nuevo tick ({0}). Limpiando datos antiguos.", tickActual));
        }
        trendingValues.addAll(topParcial);        
        collector.ack(input);
        log.info(format("Nuevos trends: {0}", trendingValues));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // Nada que declarar, terminamos aquí de momento
    }
    
    private static class InverterCompartor implements Comparator<Object>, Serializable {
        private static final long serialVersionUID = 1L;

        @SuppressWarnings({"rawtypes", "unchecked"})
        public int compare(Object o1, Object o2) {
            Comparable c1 = (Comparable) o1;
            Comparable c2 = (Comparable) o2;
            return c1.compareTo(c2) * -1;
        }
    }
    
}