package com.nhnacademy.aiotdevicegateway.device.dataq;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * DataQ Protocol에 정의된 요청 Command 형식에 맞춰 byte array를 만들어주는 util class 입니다.
 *
 * @author 이수정
 */
public class DQCommandUtils {

    private static final int TYPE = 0x31415926;
    private static final int COMPONENT_SIZE = 6;

    private DQCommandUtils() {}

    /**
     * 요청에 필요한 파라미터를 받아 요청 형식에 맞는 byte array로 변환하여 반환시켜 줍니다.
     *
     * @param groupId connect된 groupId
     * @param command 각 요청에 맞는 Command enum
     * @param payload 요청에 담을 값
     * @param args    요청 옵션
     * @return 요청 형식에 맞게 생성된 byte array
     * @author 이수정
     */
    public static byte[] createRequestBytes(int groupId, Command command, byte[] payload, int... args) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES * COMPONENT_SIZE + payload.length);

        buffer.order(ByteOrder.LITTLE_ENDIAN).putInt(TYPE)
            .order(ByteOrder.LITTLE_ENDIAN).putInt(groupId)
            .order(ByteOrder.LITTLE_ENDIAN).putInt(command.value);
        Arrays.stream(args)
            .forEach(argument -> buffer.order(ByteOrder.LITTLE_ENDIAN).putInt(argument));
        for (int i = 0; i < 3 - args.length; i++) {
            buffer.order(ByteOrder.LITTLE_ENDIAN).putInt(0);
        }
        buffer.put(payload);

        return buffer.array();
    }

    /**
     * device에 command를 직접 payload로 입력하여 보낼 때 사용합니다. COMMAND(13)
     *
     * @param groupId connect된 groupId
     * @param payload 요청에 담을 값
     * @param args    요청 옵션
     * @return 요청 형식에 맞게 생성된 byte array
     * @author 이수정
     */
    public static byte[] createPayloadBytes(int groupId, byte[] payload, int... args) {
        return createRequestBytes(groupId, Command.COMMAND, payload, args);
    }

    /**
     * device에 command를 이용한 요청을 보낼 때 사용합니다.
     *
     * @param groupId connect된 groupId
     * @param command 각 요청에 맞는 Command enum
     * @param args    요청 옵션
     * @return 요청 형식에 맞게 생성된 byte array
     * @author 이수정
     */
    public static byte[] createCommandBytes(int groupId, Command command, int... args) {
        return createRequestBytes(groupId, command, new byte[]{}, args);
    }

    /**
     * DataQ Protocol에 정의된 Command의 값 정보를 담은 enum 입니다.
     *
     * @author 이수정
     */
    enum Command {
        SYNC_START(1),
        CONNECT(10),
        KEEP_ALIVE(12),
        COMMAND(13);

        private final int value;

        Command(int value) {
            this.value = value;
        }
    }
}
