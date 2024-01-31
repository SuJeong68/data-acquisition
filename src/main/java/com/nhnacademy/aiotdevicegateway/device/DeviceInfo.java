package com.nhnacademy.aiotdevicegateway.device;

/**
 * Device 별로 존재하는 Info들의 interface 입니다.
 *
 * @author 이수정
 */
public interface DeviceInfo {

    /**
     * Device의 이름을 반환합니다.
     *
     * @return Device 이름
     * @author 이수정
     */
    String getName();

    /**
     * Device의 branch를 반환합니다.
     *
     * @return Device branch
     * @author 이수정
     */
    String getBranch();

    /**
     * Device의 장소를 반환합니다.
     *
     * @return Device 장소
     * @author 이수정
     */
    String getPlace();
}
