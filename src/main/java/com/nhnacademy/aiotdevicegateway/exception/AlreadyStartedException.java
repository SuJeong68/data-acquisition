package com.nhnacademy.aiotdevicegateway.exception;

/**
 * 이미 실행된 ActiveNode인 경우 예외를 던집니다.
 *
 * @author 이수정
 */
public class AlreadyStartedException extends RuntimeException {
    public AlreadyStartedException(String name) {
        super("ActiveNode [" + name + "] is already started.");
    }
}
