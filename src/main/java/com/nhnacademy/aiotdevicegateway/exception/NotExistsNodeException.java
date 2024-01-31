package com.nhnacademy.aiotdevicegateway.exception;

/**
 * NodeGroup에 등록되지 않은 Node type인 경우 예외를 던집니다.
 *
 * @author 이수정
 */
public class NotExistsNodeException extends RuntimeException {

    public NotExistsNodeException(String type) {
        super("Type [" + type + "] is not exists node in NodeGroup.");
    }
}
