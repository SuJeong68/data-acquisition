package com.nhnacademy.aiotdevicegateway.node;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Node의 기본 정보를 담는 class 입니다.
 *
 * @author 이수정
 */
@Getter
@AllArgsConstructor
public final class NodeInfo {

    private final String id;
    private final String type;
    private final String name;
    private final String[] wires;
}
