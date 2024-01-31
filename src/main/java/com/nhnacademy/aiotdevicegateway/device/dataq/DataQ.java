package com.nhnacademy.aiotdevicegateway.device.dataq;

import com.nhnacademy.aiotdevicegateway.device.Device;
import com.nhnacademy.aiotdevicegateway.device.DeviceInfo;

/**
 * DataQ instruments의 Device임을 나타내는 interface 입니다.
 *
 * @author 이수정
 */
public interface DataQ extends Device {

    /**
     * DataQ Device의 정보를 담은 각 Device inner class인 Info 반환합니다.
     *
     * @return Data Device의 정보를 담은 DataQInfo
     * @author 이수정
     */
    DeviceInfo getInfo();
}
