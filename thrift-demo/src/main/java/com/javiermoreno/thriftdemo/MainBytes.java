package com.javiermoreno.thriftdemo;

import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;

public class MainBytes {

    public static void main(String[] args) throws TException {
        TweetDTOThrift dto = new TweetDTOThrift("Usuario", "Mensaje");
        dto.setId("10000000");
        
        
        TSerializer serializer = new TSerializer(new TBinaryProtocol.Factory());
        byte[] bytes = serializer.serialize(dto);
        
        TDeserializer deserializer = new TDeserializer(new TBinaryProtocol.Factory());
        TweetDTOThrift dto2 = new TweetDTOThrift();
        deserializer.deserialize(dto2, bytes);
        
        System.out.println(dto);
        System.out.println(dto2);
    }
    
}
