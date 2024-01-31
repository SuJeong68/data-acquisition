package com.nhnacademy.aiotdevicegateway.device;

/**
 * Device 응답 객체 interface 입니다.
 *
 * @author 이수정
 */
public interface Response {

    /**
     * 응답 받은 시간을 반환합니다.
     * (모든 Response는 receive된 시간정보를 포합합니다.)
     *
     * @return ReceiveNode에서 응답받은 시간
     * @author 이수정
     */
    long getTime();
}
