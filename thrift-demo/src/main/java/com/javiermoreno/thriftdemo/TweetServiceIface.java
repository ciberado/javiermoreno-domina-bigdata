package com.javiermoreno.thriftdemo;

import org.apache.thrift.TException;

public class TweetServiceIface implements TweetService.Iface{

    @Override
    public TweetDTOThrift tweetById(String id) throws TException {
        TweetDTOThrift dto = new TweetDTOThrift("Usuario " + id, "Mensaje de " + id);
        dto.setId(id);
        return dto;
    }

    @Override
    public void saveTweet(TweetDTOThrift tweet) throws TException {
        System.out.println("SAVE! " + tweet);
        
    }

    @Override
    public void reset() throws TException {
        System.out.println("RESET!");
    }

}
