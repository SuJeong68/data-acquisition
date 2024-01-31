package com.nhnacademy.aiotdevicegateway.exception;

/**
 * DeviceGroup에 등록되지 않은 Device model인 경우 예외를 던집니다.
 *
 * @author 이수정
 */
public class NotExistsDeviceException extends RuntimeException {

    public NotExistsDeviceException(String model) {
        super("Model [" + model + "] is not exists device in DeviceGroup.");
    }
}
