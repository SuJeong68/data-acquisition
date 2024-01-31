package com.nhnacademy.aiotdevicegateway.node;

import com.nhnacademy.aiotdevicegateway.device.Device;
import com.nhnacademy.aiotdevicegateway.node.base.InNode;
import lombok.Getter;

/**
 * Device별로 생성되는 노드로 요청에 따른 응답을 받아오는 역할을 합니다.
 * (통신 방식에 따라 나누어 구현합니다.)
 *
 * @author 이수정
 */
public abstract class ReceiveNode extends InNode {

    @Getter
    private final Device device;

    protected ReceiveNode(String[] wires, Device device) {
        super(device.getId(), String.format("%s_receiver", device.getName()), wires);
        this.device = device;
    }
}
