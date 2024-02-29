package com.nhnacademy.aiot;

import com.nhnacademy.aiot.DQCommand.Command;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

public class DataQ extends Thread {
    private static final int PC_PORT = 1234;
    private static final int DEVICE_PORT = 51235;
    private static final List<Integer> SLIST = List.of(0x0100, 0x0101, 0x0102, 0x0103, 0x0104, 0x0105, 0x0106, 0x0107);

    private DatagramSocket socket;
    private InetAddress host;
    private long interval = 500;
    private final int groupId = 1;

    public DataQ(byte[] hostAddress) {
        try {
            socket = new DatagramSocket(PC_PORT);

            host = InetAddress.getByAddress(hostAddress);
        } catch (SocketException | UnknownHostException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        try {
            receive();

            connect();
            stopAndReset();

            setting();

            scanStart();

            interval = 6000;
            while (!socket.isClosed()) {
                keepAlive();
            }
        } catch (Exception e) {
            //
        }
    }

    private void scanStart() {
        send(new DQCommand(groupId, Command.COMMAND, "start"));
        send(new DQCommand(groupId, Command.SYNC_START, ""));
    }

    public void configScnList() {
        int position = 0;
        for (int item : SLIST) {
            send(new DQCommand(groupId, Command.COMMAND, String.format("slist %d %d", position, item)));
            position++;
        }
        send(new DQCommand(groupId, Command.COMMAND, "slist"));
    }

    private void keepAlive() {
        send(new DQCommand(groupId, Command.KEEP_ALIVE, ""));
    }

    private void setting() {
        send(new DQCommand(groupId, Command.COMMAND, "encode 0"));
        send(new DQCommand(groupId, Command.COMMAND, "ps 0"));
        configScnList();
        send(new DQCommand(groupId, Command.COMMAND, "dec 60"));
        send(new DQCommand(groupId, Command.COMMAND, "srate 1000"));
    }

    private void stopAndReset() {
        send(new DQCommand(groupId, Command.COMMAND, "stop"));
        send(new DQCommand(groupId, Command.COMMAND, "reset 1"));
    }

    private void receive() {
        Receiver receiver = new Receiver(socket);
        receiver.start();
    }

    private void connect() {
        send(new DQCommand(groupId, Command.CONNECT, ""));
    }

    private void send(DQCommand dqCommand) {
        try {
            byte[] message = dqCommand.toByteArray();

            DatagramPacket request = new DatagramPacket(message, message.length, host, DEVICE_PORT);
            socket.send(request);

            Thread.sleep(interval);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        DataQ dataQ = new DataQ(new byte[]{(byte) 192, (byte) 168, 70, 28});
        dataQ.start();
    }
}
