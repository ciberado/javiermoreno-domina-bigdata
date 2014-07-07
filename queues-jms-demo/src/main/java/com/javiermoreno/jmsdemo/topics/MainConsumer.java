package com.javiermoreno.jmsdemo.topics;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;

public class MainConsumer {

    public static void main(String[] args) throws Exception {
        Properties env = new Properties();
        env.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        env.setProperty(Context.PROVIDER_URL, "tcp://127.0.0.1:61616");
        javax.naming.Context ctx = new javax.naming.InitialContext(env);
        javax.jms.TopicConnectionFactory factory = (javax.jms.TopicConnectionFactory) ctx.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.setClientID("tweetClient4");
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createTopic("JavierMoreno.Tweets.Entrantes?consumer.retroactive=true");
        MessageConsumer consumer = session.createConsumer(destination);
        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message msg) {
                try {
                    TextMessage textMessage = (TextMessage) msg;
                    String text = textMessage.getText();
                    System.out.println("Received: " + text);
                } catch (JMSException exc) {
                    System.err.println(exc);
                }
            }
        });

        Thread.sleep(100000);
        session.close();
        connection.close();
        System.out.println("Fin.");
    }

}
