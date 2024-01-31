package com.nhnacademy.aiotdevicegateway.exception;

/**
 * 존재하지 않는 Response인 경우 예외를 던집니다.
 *
 * @author 이수정
 */
public class NotExistsResponseException extends RuntimeException {

    public NotExistsResponseException(String name) {
        super("Response [" + name + "] is not exists.");
    }
}
