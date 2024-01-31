package com.nhnacademy.aiotdevicegateway.message;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * JsonObject 형태의 메세지를 담는 Message class 입니다.
 *
 * @author 이수정
 */
@Getter
@AllArgsConstructor
public class JsonMessage extends Message {
    private final JsonObject jsonObject;
}
