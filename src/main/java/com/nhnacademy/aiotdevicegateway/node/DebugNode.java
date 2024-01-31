package com.nhnacademy.aiotdevicegateway.node;

import com.nhnacademy.aiotdevicegateway.message.Message;
import com.nhnacademy.aiotdevicegateway.node.base.OutNode;
import com.nhnacademy.aiotdevicegateway.wire.Wire;
import lombok.extern.slf4j.Slf4j;

/**
 * 전달받은 Message를 바탕으로 debug 로그를 남깁니다.
 *
 * @author 이수정
 */
@Slf4j
public class DebugNode extends OutNode {

    protected DebugNode(NodeInfo info) {
        super(info.getId(), info.getName());
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
//                if (message instanceof ReceivedMessage) {
//                    log.info("data = {}", ((ReceivedMessage) message).getData());
//                }
//                if (message instanceof ResponseMessage) {
//                    Response response = ((ResponseMessage) message).getResponse();
//                    if (response instanceof ADCData) {
//                        log.info("time = {}, converted = {}",
//                            response.getTime(), Arrays.toString(((ADCData) response).getConverted()));
//                    } else if (response instanceof DQResponse) {
//                        log.info("time = {}, payload = {}", response.getTime(), new String(((DQResponse) response).getPayload()));
//                    }
//                }
            }
        }
    }
}
