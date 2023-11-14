package com.nhnacademy.aiot;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisHashCommands;
import io.lettuce.core.api.sync.RedisListCommands;
import java.util.HashMap;
import java.util.Map;

public class LettuceTest {

    public static void main(String[] args) {
        RedisClient redisClient = RedisClient.create("redis://password@localhost:6379/0");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
//        RedisListCommands<String, String> listCommands = connection.sync();
//
//        listCommands.rpush("listKey", "hello", "hi");
//        listCommands.rpush("listKey", "hello2", "hi2");

        Map<String, String> map = new HashMap<>();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k4", "v4");

        RedisHashCommands<String, String> redisHashCommands = connection.sync();
        redisHashCommands.hset("hash", map);

        connection.close();
        redisClient.shutdown();
    }
}
