package com.argus.redis;

import redis.clients.jedis.Jedis;

/**
 * Created by xingding on 18/6/7.
 */
public class RedisMain {

    public static void main(String[] args) {
       RedisMain main = new RedisMain();
       main.connect();
    }

    private void connect(){
        Jedis jedis = new Jedis("127.0.0.1");
        System.out.println("connect success");
        System.out.println(jedis.ping());
    }
}
