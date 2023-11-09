package com.nhnacademy.aiot;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

interface DQResponse {
    static ByteBuffer getSplitedByteBuffer(byte[] data, int from, int to) {
        return ByteBuffer.wrap(Arrays.copyOfRange(data, from, to)).order(ByteOrder.LITTLE_ENDIAN);
    }

    String getPayLoad();
}
