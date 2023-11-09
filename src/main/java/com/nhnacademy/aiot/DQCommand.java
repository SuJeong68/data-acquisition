package com.nhnacademy.aiot;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DQCommand {
    private int type = 0x31415926;
    private int groupId;
    private int command;
    private int arg0;
    private int arg1;
    private int arg2;
    private String payLoad;

    public DQCommand(int groupId, Command command, String payLoad) {
        this.groupId = groupId;
        this.command = command.number;
        this.payLoad = payLoad;
    }

    // 적용 보류 (리팩...)
    /*public static DQCommand createCommand(int groupId, String payLoad) {
        return new DQCommand(groupId, Command.COMMAND, payLoad);
    }*/

    public byte[] toByteArray() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 * 6 + payLoad.getBytes().length);

        byteBuffer.order(ByteOrder.LITTLE_ENDIAN).putInt(type);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN).putInt(groupId);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN).putInt(command);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN).putInt(arg0);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN).putInt(arg1);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN).putInt(arg2);
        byteBuffer.put(payLoad.getBytes());

        return byteBuffer.array();
    }

    enum Command {
        SYNC_START(1),
        CONNECT(10),
        KEEP_ALIVE(12),
        COMMAND(13);

        final int number;

        Command(int number) {
            this.number = number;
        }
    }
}
