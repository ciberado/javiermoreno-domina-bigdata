/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javiermoreno.bigdata.nosql.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Tuple;

/**
 *
 * @author ciberado
 */
public class AppDemo {

    public static void main(String[] args) {
        String[] names = {"Alice", "Bob", "Charly", "Dave", "Evelen", "Frank", "Geani", "Jack", "Kunt"};
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            
            // key / value
            jedis.set("nameCount", String.valueOf(names.length));
            int nameCount = Integer.valueOf(jedis.get("nameCount"));
            System.out.format("Número de nombres diferentes: %s.%n%n", nameCount);
            
            
            // key / sorted set
            for (int i=0; i < 100; i++) {
                String runnerName = names[(int) (Math.random()*names.length)] + "_" + (int) (Math.random()*10);
                double weight = Math.ceil(Math.random() * 1000);
                jedis.zadd("runnersRank", weight, runnerName);
            }
            Set<Tuple> top5 = jedis.zrangeWithScores("runnersRank", -5, -1);
            System.out.format("Top 5: ");
            for (Tuple currentRunnerScore : top5) {
                System.out.format("%s (%s) ", currentRunnerScore.getElement(), currentRunnerScore.getScore());
            }
            System.out.format(".%n%n");
            
            // key / hash
            Map<String, String> alice = new HashMap<String, String>();
            alice.put("nif", "12345678A");
            alice.put("nombre", "Alice");
            alice.put("apellidos", "Wonderland");
            jedis.hmset(alice.get("nif"), alice);
            
            Map<String, String> persona = jedis.hgetAll("12345678A");
            System.out.println("Alice: " + persona + "\n");
            
            
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }

        
        pool.destroy();

    }
}
