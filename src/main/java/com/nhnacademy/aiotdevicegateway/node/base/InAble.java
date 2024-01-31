package com.nhnacademy.aiotdevicegateway.node.base;

import com.nhnacademy.aiotdevicegateway.message.Message;
import com.nhnacademy.aiotdevicegateway.wire.Wire;

/**
 * 입력 노드의 공통 작업을 위한 interface 입니다.
 *
 * @author 이수정
 */
public interface InAble {

    /**
     * 출력될 wire를 outWire에 추가합니다.
     *
     * @param wire 출력될 wire
     * @author 이수정
     */
    void connectOutWire(Wire wire);

    /**
     * outWire 특정 index의 wire를 입력받은 새 wire로 교체합니다.
     *
     * @param index 교체할 index
     * @param wire  교체할 새 wire
     * @author 이수정
     */
    void connectOutWire(int index, Wire wire);

    /**
     * 특정 index의 wire를 반환합니다.
     *
     * @param index 반환받을 wire의 index
     * @return 특정 index의 wire
     * @author 이수정
     */
    Wire getOutWire(int index);

    /**
     * outWire의 길이를 반환합니다.
     *
     * @return outWire의 길이
     * @author 이수정
     */
    int getOutWireLength();

    /**
     * InAble의 wire 정보를 바탕으로 OutAble 노드에 wire를 연결합니다.
     *
     * @author 이수정
     */
    void connectBetweenNodes();

    /**
     * InAble 노드에서 OutAble 노드로 Message를 전달합니다.
     *
     * @param message 전달할 데이터를 담은 Message
     * @author 이수정
     */
    void output(Message message);
}
