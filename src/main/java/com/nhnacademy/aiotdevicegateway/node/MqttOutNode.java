package com.nhnacademy.aiotdevicegateway.node;

import com.nhnacademy.aiotdevicegateway.device.AcquisitionResponse;
import com.nhnacademy.aiotdevicegateway.device.MQTTMessage;
import com.nhnacademy.aiotdevicegateway.message.Message;
import com.nhnacademy.aiotdevicegateway.message.ResponseMessage;
import com.nhnacademy.aiotdevicegateway.node.base.OutNode;
import com.nhnacademy.aiotdevicegateway.wire.Wire;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;

import java.util.Iterator;
import java.util.Objects;

/**
 * 전달받은 Message가 ResponseMessage이고 AcquisitionResponse를 담고 있다면,
 * Response에 따라 Mqtt 브로커에 수집 데이터를 전달합니다.
 *
 * @author 이수정
 */
@Slf4j
public class MqttOutNode extends OutNode {

    private IMqttClient client;
    private String serverURI = "tcp://localhost:1883";

    public MqttOutNode(NodeInfo info) {
        super(info.getId(), info.getName());
    }

    @Override
    protected void preprocess() {
        try {
            client = new MqttClient(serverURI, getId());

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(1000);
            options.setExecutorServiceTimeout(0);

            client.connect(options);
        } catch (MqttException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    protected void process() {
        try {
            for (int i = 0; i < getInWireLength(); i++) {
                Wire wire = getInWire(i);
                if (wire.hasMessage()) {
                    Message message = wire.get();
                    if (message instanceof ResponseMessage &&
                        ((ResponseMessage) message).getResponse() instanceof AcquisitionResponse) {
                        AcquisitionResponse response = (AcquisitionResponse)((ResponseMessage) message).getResponse();

                        Iterator<MQTTMessage> iterator = response.getMqttMessageIterator();
                        while (iterator.hasNext()) {
                            MQTTMessage mqttMessage = iterator.next();
                            if (Objects.nonNull(mqttMessage)) {
                                client.publish(mqttMessage.getTopic(), new MqttMessage(mqttMessage.getJsonObject().toString().getBytes()));
                            }
                        }
                    }
                }
            }
        } catch (MqttException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    protected synchronized void postprocess() {
        try {
            client.disconnect();
            super.postprocess();
        } catch (MqttException e) {
            log.error(e.getMessage());
        }
    }
}
