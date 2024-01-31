package com.nhnacademy.aiotdevicegateway.node.base;

import lombok.Getter;

/**
 * id, name 등 기본 정보를 담는 Node class 입니다.
 *
 * @author 이수정
 */
@Getter
public abstract class Node {

    private final String id;
    private final String name;

    protected Node(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
