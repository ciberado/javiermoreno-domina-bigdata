package com.javiermoreno.thriftdemo;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

public class MainServer {
    
    public static void main(String[] args) throws TTransportException {
        System.out.println("Inicializando server.");
        TServerSocket transport = new TServerSocket(8888);
        TweetService.Processor<TweetServiceIface> processor =
                new TweetService.Processor<TweetServiceIface>(
                        new TweetServiceIface());
        
        TServer server = new TThreadPoolServer(
                            new Args(transport).processor(processor));

        server.serve();
        System.out.println("Servidor arrancado en 8888.");
    
    }

}
