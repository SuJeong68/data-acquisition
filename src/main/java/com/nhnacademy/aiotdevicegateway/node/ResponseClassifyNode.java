package com.nhnacademy.aiotdevicegateway.node;

import com.google.gson.JsonObject;
import com.nhnacademy.aiotdevicegateway.SettingObjRepository;
import com.nhnacademy.aiotdevicegateway.device.Device;
import com.nhnacademy.aiotdevicegateway.device.Response;
import com.nhnacademy.aiotdevicegateway.device.ResponseUtils;
import com.nhnacademy.aiotdevicegateway.device.dataq.ADCData;
import com.nhnacademy.aiotdevicegateway.device.dataq.DI4108E;
import com.nhnacademy.aiotdevicegateway.device.dataq.DQResponse;
import com.nhnacademy.aiotdevicegateway.exception.NotExistsResponseException;
import com.nhnacademy.aiotdevicegateway.message.JsonMessage;
import com.nhnacademy.aiotdevicegateway.message.Message;
import com.nhnacademy.aiotdevicegateway.message.ResponseMessage;
import com.nhnacademy.aiotdevicegateway.node.base.InOutNode;
import com.nhnacademy.aiotdevicegateway.wire.Wire;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

/**
 * 전달받은 Message가 ReceivedMessage라면 Device에 맞는 Response로 변환하여 OutAble 노드에 전달합니다.
 *
 * @author 이수정
 */
@Slf4j
public final class ResponseClassifyNode extends InOutNode {

    public ResponseClassifyNode(NodeInfo info) {
        super(info.getId(), info.getName(), info.getWires());
    }

    @Override
    protected void preprocess() {
        //
    }

    @Override
    protected void process() {
        for (int i = 0; i < getInWireLength(); i++) {
            Wire wire = getInWire(i);
            while (wire.hasMessage()) {
                Message message = wire.get();
                try {
                    if (message instanceof JsonMessage) {
                        Response response = transToResponse(((JsonMessage) message).getJsonObject());
                        output(new ResponseMessage(response));
                    }
                } catch (NotExistsResponseException e) {
                    log.error("{}", e.getMessage());
                }
            }
        }
    }

    /**
     * 각 ReceiveMessage의 Device에 맞는 Response가 존재하는지 확인하고 Response 객체를 생성해 반환합니다.
     *
     * @param jsonObject device, data 정보를 담은 JsonObject
     * @throws NotExistsResponseException Response를 생성할 수 없는 Device인 경우
     * @return 생성된 Response 객체
     * @author 이수정
     */
    private Response transToResponse(JsonObject jsonObject) {
        Device device = SettingObjRepository.getDevice(jsonObject.get("deviceId").getAsString());
        byte[] data = Base64.getDecoder().decode(jsonObject.get("data").getAsString());
        long time = jsonObject.get("time").getAsLong();

        if (device instanceof DI4108E) {
            int type = ResponseUtils.getSplitedByteInt(data, 0, 4);
            if (type == ADCData.TYPE) {
                return new ADCData(device, data, time);
            } else if (type == DQResponse.TYPE) {
                return new DQResponse(device, data, time);
            }
        }
        throw new NotExistsResponseException(device.getClass().getName());
    }
}
