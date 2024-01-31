package com.nhnacademy.aiotdevicegateway.device;

import com.nhnacademy.aiotdevicegateway.node.ReceiveNode;

/**
 * Device임을 나타내는 interface 입니다.
 *
 * @author 이수정
 */
public interface Device {

    /**
     * Device의 ID를 반환합니다.
     *
     * @return Device ID
     * @author 이수정
     */
    String getId();
    /**
     * Device의 이름를 반환합니다.
     *
     * @return Device 이름
     * @author 이수정
     */
    String getName();

    /**
     * Device의 ReceiveNode를 생성해 실행시키고, 수집 요청 작업을 실행합니다.
     *
     * @param wires ReceiveNode에 연결될 wires
     * @author 이수정
     */
    void execute(String[] wires);

    /**
     * Device 정보를 바탕으로 응답을 받아오는 ReceiveNode에 wire를 연결하고 실행시킵니다.
     *
     * @param node Device 정보를 바탕으로 응답을 받아오는 ReceiveNode
     * @author 이수정
     */
    void startReceiveNode(ReceiveNode node);

    /**
     * device에 수집 요청을 보냅니다.
     *
     * @author 이수정
     */
    void sendRequest();

}
