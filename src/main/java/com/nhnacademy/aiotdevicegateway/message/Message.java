package com.nhnacademy.aiotdevicegateway.message;

import java.util.UUID;

/**
 * Wire를 통해 node 간 데이터를 전달하기 위한 추상 class 입니다.
 *
 * @author 이수정
 */
public abstract class Message {

    private final String id;

    /**
     * 랜덤 UUID로 id를 생성하고, 새 Message 객체를 반환합니다.
     *
     * @author 이수정
     */
    protected Message() {
        this.id = UUID.randomUUID().toString();
    }
}
