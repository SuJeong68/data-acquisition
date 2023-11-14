package com.nhnacademy.aiot;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisHashCommands;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

public class Receiver extends Thread {
    private final DatagramSocket socket;
    private StatefulRedisConnection<String, String> connection;

    public Receiver(DatagramSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (RedisClient redisClient = RedisClient.create("redis://password@localhost:6379/0")) {
            connection = redisClient.connect();

            byte[] data = new byte[1024];
            DatagramPacket response = new DatagramPacket(data, data.length);

            while (!socket.isClosed()) {
                socket.receive(response);

                int type = DQResponse.getSplitedByteBuffer(data, 0, 4).getInt();

                DQResponse resp;
                if (Response.TYPE == type) {
                    resp = new Response(data);
                } else if (AdcData.TYPE == type) {
                    resp = new AdcData(data);
                    AdcData adc = (AdcData)resp;
                    for (int i = 0; i < adc.getPayLoadSamples(); i++) {
                        redisPut(adc.getTime(), adc.getConverted(i));
                    }
                } else {
                    continue;
                }

                System.out.println(resp);
            }
        } catch (IOException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void redisPut(String time, String str) {
        Map<String, String> map = new HashMap<>();
        map.put(time, str);

        RedisHashCommands<String, String> redisHashCommands = connection.sync();
        redisHashCommands.hset("dataq", map);
    }
}
