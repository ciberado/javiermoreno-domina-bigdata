package com.javiermoreno.jmsdemo.queues;


import java.util.Date;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;


public class MainProducer {

	public static void main(String[] args) throws Exception {
		Properties env = new Properties(); 
		env.setProperty(Context.INITIAL_CONTEXT_FACTORY, 
						"org.apache.activemq.jndi.ActiveMQInitialContextFactory"); 
		env.setProperty(Context.PROVIDER_URL, "tcp://127.0.0.1:61616"); 
		javax.naming.Context ctx = new javax.naming.InitialContext(env);
		javax.jms.QueueConnectionFactory factory = (javax.jms.QueueConnectionFactory)
				ctx.lookup("ConnectionFactory");
		Connection connection = factory.createConnection();
        connection.start();

        // Create a Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createQueue("demo");

        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        for (int i=0; i <100; i++) {
        	String text = "Hello world! From: " + new Date().getTime();
        	TextMessage message = session.createTextMessage(text);
        	producer.send(message);
        	Thread.sleep(1000);
        }
        
        session.close();
        connection.close();
	}
	
}
