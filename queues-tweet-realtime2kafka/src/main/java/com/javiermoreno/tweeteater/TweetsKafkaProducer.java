package com.javiermoreno.tweeteater;

import static java.util.Arrays.asList;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import kafka.javaapi.producer.Producer;
import kafka.javaapi.producer.ProducerData;
import kafka.message.Message;
import kafka.producer.ProducerConfig;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Constants.FilterLevel;
import com.twitter.hbc.core.endpoint.Location;
import com.twitter.hbc.core.endpoint.Location.Coordinate;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;


public class TweetsKafkaProducer {
    
    private static final Logger log = LoggerFactory.getLogger(TweetsKafkaProducer.class);

    // https://apps.twitter.com/
    private static final String API_KEY = "";
    private static final String SECRET_KEY = "";
    private static final String ACCESS_TOKEN = "";
    private static final String SECRET_ACCESS_TOKEN = "";
    
    
    public static void main(String[] args) throws ParseException {
 //       BasicConfigurator.configure();
        
        // Inicialización de productor
        Properties props = new Properties();
        props.put("zk.connect", "127.0.0.1:2181");
        props.put("serializer.class", "kafka.serializer.DefaultEncoder");
        //props.put("serializer.class", "kafka.serializer.StringEncoder");
        // Configuración de compresión y acumulación (10 mensajes o un segundo) 
        props.put("producer.type", "async");
        props.put("compression.codec", "1");
        props.put("batch.size", "10");
        props.put("queue.time", "1000");
        String topicName = "realtimetweets";

        ProducerConfig config = new ProducerConfig(props);
        Producer<String, Message> producer = new Producer<>(config);

        BlockingQueue<String> processorQueue = new LinkedBlockingQueue<String>(10000);
        BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>(10000);
        // ESTOS DATOS NO DEBERÍAN HARDCODEARSE!!!
        Authentication hosebirdAuth = new OAuth1(API_KEY, SECRET_KEY, ACCESS_TOKEN, SECRET_ACCESS_TOKEN);
        StatusesFilterEndpoint endPoint = new StatusesFilterEndpoint().trackTerms(
                asList(new String[] { "Barcelona", "Catalunya" })).locations(
                asList(new Location[] { new Location(new Coordinate(0.13, 40.49), new Coordinate(
                        3.49, 43.0)) // SWLon, SWLat, NELon, NELat
                }));
        endPoint.filterLevel(FilterLevel.None); // obtener tweets relevantes y
                                                // no relevantes
        endPoint.stallWarnings(true); // Recibir notificaciones avisando sobre
                                      // una posible sanci�n
        // endPoint.followings(asList(new Long[] {})); // Seguir los ids
        // indicados
        // endPoint.languages(asList(new String[] {"ca", "en", "es"})); //
        // Filtrado por idiomas

        ClientBuilder builder = new ClientBuilder()
                .hosts(Constants.STREAM_HOST)
                .authentication(hosebirdAuth)
                // .endpoint(new StatusesSampleEndpoint())
                .endpoint(endPoint).gzipEnabled(true)
                .processor(new StringDelimitedProcessor(processorQueue))
                .eventMessageQueue(eventQueue);
        Client hosebirdClient = builder.build();
        hosebirdClient.connect();
        while (hosebirdClient.isDone() == false) {
            try {
                String jsonTweet = processorQueue.take();
                byte[] bytes = jsonTweet.getBytes(Charset.forName("UTF-8"));
                Message message = new Message(bytes);
                ProducerData<String, Message> data = 
                        new ProducerData<>(topicName, message);
                producer.send(data);
                log.debug(MessageFormat.format("Obtenidos {0}. Perdidos {1}",
                    hosebirdClient.getStatsTracker().getNumMessages(),
                    hosebirdClient.getStatsTracker().getNumMessagesDropped()));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        producer.close();  // Simbólico: Para simplificar el código no se gestionaron en finally
        hosebirdClient.stop();
    }

}
