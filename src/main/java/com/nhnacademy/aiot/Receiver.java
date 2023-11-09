package com.nhnacademy.aiot;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Receiver extends Thread {
    private final DatagramSocket socket;

    public Receiver(DatagramSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
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
                } else {
                    continue;
                }

                System.out.println(resp);
            }
        } catch (IOException e) {
            Thread.currentThread().interrupt();
        }
    }
}
