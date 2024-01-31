package com.nhnacademy.aiotdevicegateway.device.dataq;

import com.nhnacademy.aiotdevicegateway.device.DeviceInfo;

/**
 * DataQ device 별 Info를 위한 interface 입니다.
 *
 * @author 이수정
 */
public interface DataQInfo extends DeviceInfo {

    /**
     * int 배열로 받은 address를 byte 배열로 변환하여 반환합니다.
     *
     * @return 변환된 address
     * @author 이수정
     */
    byte[] getByteAddress();

    /**
     * dec, srate에 의해 계산된 hz 값을 반환합니다.
     *
     * @return hz
     * @author 이수정
     */
    int getHz();

    /**
     * channel에 해당하는 sensor 이름을 반환합니다.
     *
     * @param channel 채널 번호
     * @return channel에 존재하는 sensor 이름
     */
    String getSensor(int channel);

    /**
     * Device에 등록된 sensor의 개수를 반환합니다.
     *
     * @return device에 등록된 sensor 개수
     * @author 이수정
     */
    int getSensorsLength();
}
