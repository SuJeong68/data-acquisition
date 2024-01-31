package com.nhnacademy.aiotdevicegateway.wire;

import com.nhnacademy.aiotdevicegateway.message.Message;
import lombok.Getter;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Node 간 Message 객체를 전달하기 위한 class 입니다.
 *
 * @author 이수정
 */
public final class Wire {

    private final String id;
    private final BlockingQueue<Message> queue;

    private final String fromId;
    @Getter
    private final String toId;

    public Wire(String fromId, String toId) {
        this.id = UUID.randomUUID().toString();
        this.queue = new LinkedBlockingQueue<>();

        this.fromId = fromId;
        this.toId = toId;
    }

    /**
     * 전달할 Message를 queue에 추가합니다.
     *
     * @param message 전달할 Message
     * @author 이수정
     */
    public void add(Message message) {
        queue.add(message);
    }

    /**
     * queue에 Message가 존재하는지 확인하여 반환합니다.
     *
     * @return queue 내 Message 존재 여부
     * @author 이수정
     */
    public boolean hasMessage() {
        return !queue.isEmpty();
    }

    /**
     * queue에 존재하는 가장 오래된 Message를 하나 반환받습니다.
     *
     * @return 가장 오래된 Message
     * @author 이수정
     */
    public Message get() {
        return queue.poll();
    }
}
