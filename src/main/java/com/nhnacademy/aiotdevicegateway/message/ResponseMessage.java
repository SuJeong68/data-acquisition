package com.nhnacademy.aiotdevicegateway.message;

import com.nhnacademy.aiotdevicegateway.device.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response 객체를 담는 Message class 입니다.
 *
 * @author 이수정
 */
@Getter
@AllArgsConstructor
public class ResponseMessage extends Message {
    private final Response response;
}
