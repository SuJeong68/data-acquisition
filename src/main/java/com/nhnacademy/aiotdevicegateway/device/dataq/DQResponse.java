package com.nhnacademy.aiotdevicegateway.device.dataq;

import com.nhnacademy.aiotdevicegateway.device.Device;
import com.nhnacademy.aiotdevicegateway.device.DeviceInfo;
import com.nhnacademy.aiotdevicegateway.device.Response;
import com.nhnacademy.aiotdevicegateway.device.ResponseUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * DataQ 요청에 대한 응답을 받는 Response 입니다.
 *
 * @author 이수정
 */
@Slf4j
@Getter
public final class DQResponse implements Response {

    public static final int TYPE = 0x21712818;

    private final DeviceInfo info;
    private final long time;

    private final int groupID;
    private final int order;
    private final int payloadLength;
    private final byte[] payload;

    /**
     * DataQ에서 받은 요청에 대한 응답으로 DQResponse Instance로 생성합니다.
     *
     * @param device DataQ의 정보를 담은 Device 객체
     * @param data   수집된 데이터
     * @param time   응답 받은 시간
     * @author 이수정
     */
    public DQResponse(Device device, byte[] data, long time) {
        info = ((DataQ) device).getInfo();
        this.time = time;

        groupID = ResponseUtils.getSplitedByteInt(data, 4, 8);
        order = ResponseUtils.getSplitedByteInt(data, 8, 12);
        payloadLength = ResponseUtils.getSplitedByteInt(data, 12, 16);
        payload = ResponseUtils.getSplitedByteArray(data, 16, 16 + payloadLength - 1);
    }
}
