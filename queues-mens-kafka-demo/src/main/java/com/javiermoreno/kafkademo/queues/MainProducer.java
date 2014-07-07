package com.javiermoreno.kafkademo.queues;


import static java.text.MessageFormat.format;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.javaapi.producer.ProducerData;
import kafka.producer.ProducerConfig;

public class MainProducer {

	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.put("zk.connect", "127.0.0.1:2181");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		// Configuración de compresión y acumulación (10 mensajes o un segundo)
		props.put("producer.type", "async");
		props.put("compression.codec", "1");
		props.put("batch.size", "10");
		props.put("queue.time", "1000");
		
		String topicName = "tweets";
		
		ProducerConfig config = new ProducerConfig(props);
		Producer<String, String> producer = 
				new Producer<String, String>(config); 
		System.out.println("Iniciando envío.");
		for (int i = 0; i < 10; i++) {
			String texto = format("Mensaje {0} a topic {1} en ts {2}.", i, topicName, System.currentTimeMillis());
			ProducerData<String, String> data = 
					new ProducerData<String, String>(topicName, texto);
			System.out.println(texto);
			producer.send(data);
			Thread.sleep(1000);
		}
		System.out.println("Envío finalizado.");
//		String[] sentencias = {"a", "b", "c", "d"};
//		List<String> mensajes = Arrays.asList(sentencias);
//		ProducerData<String, String> data1 = 
//				new ProducerData<String, String>("test-topic1", mensajes);
//		ProducerData<String, String> data2 = 
//				new ProducerData<String, String>("test-topic2", mensajes);
//		// Enviar a dos topics en una única request
//		producer.send(Arrays.asList(new ProducerData[] {data1, data2}));
		
		producer.close();
		
	}
	
}
