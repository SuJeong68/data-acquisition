package com.nhnacademy.aiotdevicegateway.node.base;

import com.nhnacademy.aiotdevicegateway.exception.AlreadyStartedException;

import java.util.Objects;

/**
 * Runnable 노드 추상 class 입니다.
 * 노드별로 쓰레드를 생성해 실행시킬 수 있습니다.
 *
 * @author 이수정
 */
public abstract class ActiveNode extends Node implements Runnable {

    private Thread thread;

    protected ActiveNode(String id, String name) {
        super(id, name);
    }

    /**
     * thread가 null이 아니라면, 이미 실행된 노드로 AlreadyStartedException을 던집니다.
     * thread가 null이라면, 실행된 노드가 없음을 의미하며 새 쓰레드를 생성해 실행시킵니다.
     *
     * @author 이수정
     */
    public synchronized void start() {
        if (Objects.nonNull(thread)) {
            throw new AlreadyStartedException(getName());
        }

        thread = new Thread(this, getId());
        thread.start();
    }

    /**
     * thread가 실행중이라면 interrupt를 발생시킵니다.
     *
     * @author 이수정
     */
    public synchronized void stop() {
        if (isAlive()) {
            thread.interrupt();
        }
    }

    /**
     * thread가 null인지 확인 후, thread의 alive 여부를 반환합니다.
     *
     * @return thread의 alive 여부
     * @author 이수정
     */
    protected synchronized boolean isAlive() {
        return Objects.nonNull(thread) && thread.isAlive();
    }

    @Override
    public void run() {
        preprocess();

        while (isAlive()) {
            process();
        }

        postprocess();
    }

    protected abstract void preprocess();

    protected abstract void process();

    /**
     * 작업이 끝난 후, thread를 초기화 시킵니다.
     *
     * @author 이수정
     */
    protected synchronized void postprocess() {
        thread = null;
    }
}
