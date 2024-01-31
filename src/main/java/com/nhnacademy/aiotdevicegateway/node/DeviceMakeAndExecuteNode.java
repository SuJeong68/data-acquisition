package com.nhnacademy.aiotdevicegateway.node;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.nhnacademy.aiotdevicegateway.SettingObjRepository;
import com.nhnacademy.aiotdevicegateway.device.Device;
import com.nhnacademy.aiotdevicegateway.device.DeviceGroup;
import com.nhnacademy.aiotdevicegateway.exception.NotExistsDeviceException;
import com.nhnacademy.aiotdevicegateway.node.base.InOutNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;

/**
 * settings/devices.json 리소스를 읽어 각 Device를 생성하고,
 * Device마다 ReceiveNode를 생성해 와이어를 설정하고 노드를 실행시킵니다.
 * !! nodes.json 설정 시 ReceiveNode에 연결될 노드를 DeviceMakeAndExecuteNode에 설정합니다.
 *
 * @author 이수정
 */
@Slf4j
public final class DeviceMakeAndExecuteNode extends InOutNode {

    private static final String PATH = "settings/devices.json";
    private final String[] transWires;

    public DeviceMakeAndExecuteNode(NodeInfo info) {
        super(info.getId(), info.getName(), info.getWires());
        this.transWires = info.getWires();
    }

    @Override
    protected void preprocess() {
        try {
            ClassPathResource resource = new ClassPathResource(PATH);
            JsonArray jsonArray = new Gson().fromJson(Files.readString(resource.getFile().toPath()), JsonArray.class);

            for (int i = 0; i < jsonArray.size(); i++) {
                makeDeviceInstance(jsonArray.get(i));
            }

            for (Device device : SettingObjRepository.getDeviceValues()) {
                device.execute(transWires);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    protected void process() {
        //
    }

    /**
     * 각 jsonElement(device 1개)의 model이 DeviceGroup에 존재한다면,
     * Device 객체를 생성하여 DEVICES에 추가합니다.
     *
     * @param deviceInfo jsonElement인 device 정보
     * @throws NotExistsDeviceException DeviceGroup에 존재하지 않는 model인 경우
     * @author 이수정
     */
    private void makeDeviceInstance(JsonElement deviceInfo) {
        String model = deviceInfo.getAsJsonObject().get("model").getAsString();
        for (DeviceGroup device : DeviceGroup.values()) {
            if (model.equals(device.name())) {
                SettingObjRepository.add(deviceInfo.getAsJsonObject().get("id").getAsString(), device.makeDeviceInstance(deviceInfo));
                return;
            }
        }
        throw new NotExistsDeviceException(model);
    }
}
