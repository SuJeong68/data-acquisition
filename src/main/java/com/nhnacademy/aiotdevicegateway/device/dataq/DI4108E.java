package com.nhnacademy.aiotdevicegateway.device.dataq;

import com.nhnacademy.aiotdevicegateway.device.DeviceInfo;
import com.nhnacademy.aiotdevicegateway.device.dataq.DQCommandUtils.Command;
import com.nhnacademy.aiotdevicegateway.node.ReceiveNode;
import com.nhnacademy.aiotdevicegateway.node.UdpReceiveNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;

/**
 * DataQ instruments의 DI4108E 정보를 담으며, 이를 바탕으로 수집 요청을 하는 class 입니다.
 *
 * @author 이수정
 */
@Slf4j
public final class DI4108E implements DataQ {

    private static final int PC_PORT = 1234;
    private static final int DEVICE_PORT = 51235;
    private static final int KEEP_ALIVE_INTERVAL = 4000;

    private final Info info;
    private DatagramSocket socket;
    private InetAddress host;
    private long interval = 300;

    /**
     * DI4108E 기기의 정보를 입력 받아 Instance를 생성합니다.
     *
     * @param info DI4108E의 inner class Info
     * @author 이수정
     */
    public DI4108E(Info info) {
        this.info = info;
        try {
            this.socket = new DatagramSocket(PC_PORT);
            this.host = InetAddress.getByAddress(info.getByteAddress());
        } catch (SocketException | UnknownHostException e) {
            log.error("DI4108E constructor exception msg = {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String getId() {
        return info.getId();
    }

    @Override
    public String getName() {
        return info.getName();
    }

    @Override
    public DeviceInfo getInfo() {
        return info;
    }

    @Override
    public void execute(String[] wires) {
        startReceiveNode(new UdpReceiveNode(wires, this, socket));
        sendRequest();
    }

    @Override
    public void startReceiveNode(ReceiveNode node) {
        node.connectBetweenNodes();
        node.start();
    }

    @Override
    public void sendRequest() {
        connect();
        stopAndReset();
        setting();

        scanStart();
        keepAlive();
    }

    /**
     * DI4108E에 Ethernet Interface CONNECT(10) 설정을 보냅니다.
     *
     * @author 이수정
     */
    private void connect() {
        send(DQCommandUtils.createCommandBytes(info.getGroupId(), Command.CONNECT));
    }

    /**
     * DI4108E에 stop, reset 명령을 보냅니다.
     *
     * @author 이수정
     */
    private void stopAndReset() {
        send(DQCommandUtils.createPayloadBytes(info.getGroupId(), "stop".getBytes()));
        send(DQCommandUtils.createPayloadBytes(info.getGroupId(), "reset 1".getBytes()));
    }

    /**
     * DI4108E의 encode, ps, slist, dec, srate를 설정합니다.
     *
     * @author 이수정
     */
    private void setting() {
        send(DQCommandUtils.createPayloadBytes(info.getGroupId(), String.format("encode %d", info.getEncode()).getBytes()));
        send(DQCommandUtils.createPayloadBytes(info.getGroupId(), String.format("ps %d", info.getPs()).getBytes()));
        configScnList();
        send(DQCommandUtils.createPayloadBytes(info.getGroupId(), String.format("dec %d", info.getDec()).getBytes()));
        send(DQCommandUtils.createPayloadBytes(info.getGroupId(), String.format("srate %d", info.getSrate()).getBytes()));
    }

    /**
     * DI4108E의 채널 0번부터 최대 7번까지 slist를 설정합니다.
     *
     * @author 이수정
     */
    public void configScnList() {
        int[] slist = info.getSlist();
        for (int i = 0; i < slist.length; i++) {
            send(DQCommandUtils.createPayloadBytes(info.getGroupId(), String.format("slist %d %d", i, slist[i]).getBytes()));
        }
    }

    /**
     * DI4108E에 수집 시작 신호를 보냅니다.
     *
     * @author 이수정
     */
    private void scanStart() {
        send(DQCommandUtils.createPayloadBytes(info.getGroupId(), "start".getBytes()));
        send(DQCommandUtils.createCommandBytes(info.getGroupId(), Command.SYNC_START));
    }

    /**
     * 일정주기마다 DI4108E에 KeepAlive 신호를 보냅니다.
     * (최대 8초마다 신호를 보내지 않으면 연결이 끊깁니다.)
     *
     * @author 이수정
     */
    private void keepAlive() {
        new Thread(() -> {
            interval = KEEP_ALIVE_INTERVAL;

            final byte[] bytes = DQCommandUtils.createCommandBytes(info.getGroupId(), Command.KEEP_ALIVE);
            while (!socket.isClosed()) {
                send(bytes);
            }
        }).start();
    }

    /**
     * byte 배열을 바탕으로 DI4108E에 요청을 보냅니다.
     *
     * @param bytes 요청 내용이 담긴 byte array
     * @author 이수정
     */
    private void send(byte[] bytes) {
        try {
            socket.send(new DatagramPacket(bytes, bytes.length, host, DEVICE_PORT));
            Thread.sleep(interval);
        } catch (IOException | InterruptedException e) {
            log.error("DataQ send() name = {}, exception msg = {}", getName(), e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * DI4108E 기기의 정보를 담는 class 입니다.
     * 기기의 id, model, 이름, 위치, 수집을 위한 정보를 담습니다.
     *
     * @author 이수정
     */
    @Getter
    @AllArgsConstructor
    public static final class Info implements DataQInfo {

        private final String id;
        private final String model;
        private final String name;
        private final String branch;
        private final String place;
        private final int groupId;
        private final int[] address;
        private final String[] sensors;
        private final int[] slist;
        private final int encode;
        private final int ps;
        private final int dec;
        private final int srate;

        @Override
        public byte[] getByteAddress() {
            byte[] bytes = new byte[address.length];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) address[i];
            }
            return bytes;
        }

        @Override
        public int getHz() {
            return 60_000_000 / (dec * srate);
        }

        @Override
        public String getSensor(int channel) {
            return sensors[channel];
        }

        @Override
        public int getSensorsLength() {
            return sensors.length;
        }
    }
}
