package com.nhnacademy.aiot;

import java.nio.charset.StandardCharsets;

public class Response implements DQResponse {
    static final int TYPE = 0x21712818;
    private int groupId;
    private int order;
    private int payLoadLength;
    private String payLoad;

    public Response(byte[] data) {
        groupId = DQResponse.getSplitedByteBuffer(data, 4, 8).getInt();
        order = DQResponse.getSplitedByteBuffer(data, 8, 12).getInt();
        payLoadLength = DQResponse.getSplitedByteBuffer(data, 12, 16).getInt();
        payLoad = new String(StandardCharsets.UTF_8.decode(
            DQResponse.getSplitedByteBuffer(data, 16, 16 + payLoadLength - 1)).array())
            .replace("\r", "").replace("\n", "");
    }

    @Override
    public String getPayLoad() {
        return payLoad;
    }

    @Override
    public String toString() {
        return getPayLoad();
    }
}
