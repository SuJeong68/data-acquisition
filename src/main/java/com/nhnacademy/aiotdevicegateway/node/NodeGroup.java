package com.nhnacademy.aiotdevicegateway.node;

import com.nhnacademy.aiotdevicegateway.node.base.ActiveNode;

import java.util.function.Function;

/**
 * 사용가능한 Node를 모아놓은 enum 입니다.
 *
 * @author 이수정
 */
public enum NodeGroup {
    DEVICE_MAKE_AND_EXECUTE(DeviceMakeAndExecuteNode::new),
    DEBUG(DebugNode::new),
    RESPONSE_CLASSIFY(ResponseClassifyNode::new),
    INFLUX_INSERT(InfluxInsertNode::new),
    MQTT_OUT(MqttOutNode::new);

    private final Function<NodeInfo, ActiveNode> function;

    NodeGroup(Function<NodeInfo, ActiveNode> function) {
        this.function = function;
    }

    public ActiveNode makeNodeInstance(NodeInfo nodeInfo) {
        return function.apply(nodeInfo);
    }
}
