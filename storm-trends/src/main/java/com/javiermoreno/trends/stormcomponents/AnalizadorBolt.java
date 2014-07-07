package com.javiermoreno.trends.stormcomponents;

import static com.javiermoreno.trends.DemoHelpers.instanceName;
import static java.text.MessageFormat.format;

import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * Este bolt simula el proceso de análisis del tweet para extraer
 * información contextual basada por ejemplo en datos georeferenciados
 * o en la semántica del mensaje.
 * 
 * En este caso elimina el mensaje y se inventa un supuesto "peso" del tweet: 
 * mientras más alto fuese más impacto podríamos darle en el resultado final.
 * 
 * Es un BaseRichBolt por lo que puede integrarse fácilmente en topologías
 * fiables.
 * 
 * @author ciberado
 *
 */
public class AnalizadorBolt extends BaseRichBolt {
    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(AnalizadorBolt.class);
    private static final Random rnd = new Random();
    
    
    private OutputCollector collector;
    
    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        log.debug(format("Preparando bolt {0}.", instanceName(this)));
        this.collector = collector;
    }
    
    @Override
    public void execute(Tuple input) {
        log.debug(format("{0}*R {1}", input.getString(1), instanceName(this)));
        long timestamp = input.getLong(0);
        String utmSquare = input.getString(2);
        int peso = rnd.nextInt(10);
        // Creamos nueva tupla anclada a la actual y la emitimos
        collector.emit(input, new Values(timestamp, utmSquare, peso));
        // Esta tupla ya ha dado de sí todo lo que necesitábamos, así que la confirmamos
        // como procesada
        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("timestamp", "utmSquare", "peso"));
    }

}
