package com.javiermoreno.thriftdemo;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class MainClient {
    
    public static void main(String[] args) throws TException {
        
        TTransport transport = new TSocket("127.0.0.1", 8888);
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        TweetService.Client cliente = new TweetService.Client(protocol);
        TweetDTOThrift dto = cliente.tweetById("1111");
        dto.setMessage(dto.getMessage().toUpperCase());
        cliente.saveTweet(dto);
        cliente.reset();
        transport.close();
    }

}
