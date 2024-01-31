package com.nhnacademy.aiotdevicegateway.node.base;

import com.nhnacademy.aiotdevicegateway.wire.Wire;

/**
 * 출력 노드의 공통 작업을 위한 interface 입니다.
 *
 * @author 이수정
 */
public interface OutAble {

    /**
     * 출력될 wire를 inWire에 추가합니다.
     *
     * @param wire 출력될 wire
     * @author 이수정
     */
    void connectInWire(Wire wire);

    /**
     * inWire 특정 index의 wire를 입력받은 새 wire로 교체합니다.
     *
     * @param index 교체할 index
     * @param wire  교체할 새 wire
     * @author 이수정
     */
    void connectInWire(int index, Wire wire);

    /**
     * 특정 index의 wire를 반환합니다.
     *
     * @param index 반환받을 wire의 index
     * @return 특정 index의 wire
     * @author 이수정
     */
    Wire getInWire(int index);

    /**
     * inWire의 길이를 반환합니다.
     *
     * @return inWire의 길이
     * @author 이수정
     */
    int getInWireLength();
}
