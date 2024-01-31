package com.nhnacademy.aiotdevicegateway;

import com.nhnacademy.aiotdevicegateway.device.Device;
import com.nhnacademy.aiotdevicegateway.node.base.ActiveNode;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 생성된 Device 객체와 Node 객체를 저장하고 관리하는 class 입니다.
 *
 * @author 이수정
 */
public final class SettingObjRepository {

    private SettingObjRepository() { /* Repository class */ }

    private static final Map<String, Device> DEVICES = new ConcurrentHashMap<>();
    private static final Map<String, ActiveNode> NODES = new ConcurrentHashMap<>();

    /**
     * Device 객체를 모은 Map에 새 Device를 추가합니다.
     *
     * @param id     Device ID
     * @param device Device 객체
     * @author 이수정
     */
    public static void add(String id, Device device) {
        DEVICES.put(id, device);
    }

    /**
     * Node 객체를 모은 Map에 새 Node를 추가합니다.
     *
     * @param id   Node ID
     * @param node ActiveNode 객체
     * @author 이수정
     */
    public static void add(String id, ActiveNode node) {
        NODES.put(id, node);
    }

    /**
     * ID에 따라 Device 객체를 반환합니다.
     *
     * @param id 찾고지하는 Device ID
     * @return Device 객체
     * @author 이수정
     */
    public static Device getDevice(String id) {
        return DEVICES.get(id);
    }

    /**
     * ID에 따라 Node 객체를 반환합니다.
     *
     * @param id 찾고지하는 Node ID
     * @return ActiveNode 객체
     * @author 이수정
     */
    public static ActiveNode getNode(String id) {
        return NODES.get(id);
    }

    /**
     * Device 객체들(map의 values())을 반환합니다.
     *
     * @return Device 객체 collection
     * @author 이수정
     */
    public static Collection<Device> getDeviceValues() {
        return DEVICES.values();
    }

    /**
     * ActiveNode 객체들(map의 values())을 반환합니다.
     *
     * @return ActiveNode 객체 collection
     * @author 이수정
     */
    public static Collection<ActiveNode> getNodeValues() {
        return NODES.values();
    }
}
