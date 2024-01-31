package com.nhnacademy.aiotdevicegateway.device;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * MQTT out을 위한 정보를 담는 class 입니다.
 *
 * @author 이수정
 */
@Getter
@AllArgsConstructor
public class MQTTMessage {
    private String topic;
    private JsonObject jsonObject;
}
