package com.javiermoreno.tweeteater;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaMessageStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.Message;

public class TweetsKafkaConsumer {

    public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        // zookeper
        props.put("zk.connect", "localhost:2181");
        props.put("zk.connectiontimeout.ms", "1000000");
        props.put("", "");
        // permite identificar consumidores: kafka recuerda el offset del último 
        // mensaje recuperado por un groupid particular
        // Si varios consumidores comparten groupid se repartirán los mensajes
        // round robbin
        props.put("groupid", "tweetsGroup");

        String topicName = "realtimetweets";

        ConsumerConfig consumerConfig = new ConsumerConfig(props);
        ConsumerConnector consumerConnector = kafka.consumer.Consumer
                .createJavaConsumerConnector(consumerConfig);
        
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topicName, new Integer(1));
        Map<String, List<KafkaMessageStream<Message>>> consumerMap =
                consumerConnector.createMessageStreams(topicCountMap);
        KafkaMessageStream<Message> stream = consumerMap.get(topicName).get(0);
        
        ConsumerIterator<Message> it = stream.iterator();
        while (it.hasNext() == true) {
            Message message = it.next();
            ByteBuffer buffer = message.payload();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            String s = new String(data);
            System.out.println("MENSAJE RECIBIDO: " + s);
        }
    }
    
}
