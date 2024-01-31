package com.nhnacademy.aiotdevicegateway.device;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.nhnacademy.aiotdevicegateway.device.dataq.DI4108E;

import java.util.function.Function;

/**
 * 사용가능한 Device를 모아놓은 enum 입니다.
 *
 * @author 이수정
 */
public enum DeviceGroup {

    DATAQ_DI_4108_E(info -> new DI4108E(new Gson().fromJson(info, DI4108E.Info.class)));

    private final Function<JsonElement, Device> function;

    DeviceGroup(Function<JsonElement, Device> function) {
        this.function = function;
    }

    public Device makeDeviceInstance(JsonElement deviceInfo) {
        return function.apply(deviceInfo);
    }
}
