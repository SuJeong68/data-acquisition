package com.nhnacademy.aiotdevicegateway.node;

import com.google.gson.JsonObject;
import com.nhnacademy.aiotdevicegateway.device.Device;
import com.nhnacademy.aiotdevicegateway.message.JsonMessage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Base64;

/**
 * UDP 통신으로 응답을 받아오는 ReceiveNode 입니다.
 *
 * @author 이수정
 */
public final class UdpReceiveNode extends ReceiveNode {

    private static final int DATA_SIZE = 1024;
    private final DatagramSocket socket;

    public UdpReceiveNode(String[] wires, Device device, DatagramSocket socket) {
        super(wires, device);
        this.socket = socket;
    }

    @Override
    protected void preprocess() {
        //
    }

    @Override
    protected void process() {
        try {
            byte[] data = new byte[DATA_SIZE];
            DatagramPacket packet = new DatagramPacket(data, data.length);

            socket.receive(packet);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("deviceId", getDevice().getId());
            jsonObject.addProperty("data", Base64.getEncoder().encodeToString(packet.getData()));
            jsonObject.addProperty("time", System.currentTimeMillis());

            output(new JsonMessage(jsonObject));
        } catch (IOException e) {
            Thread.currentThread().interrupt();
        }
    }
}
