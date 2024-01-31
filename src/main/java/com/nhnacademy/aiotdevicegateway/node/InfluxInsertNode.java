package com.nhnacademy.aiotdevicegateway.node;

import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteOptions;
import com.influxdb.client.write.Point;
import com.nhnacademy.aiotdevicegateway.BeanContext;
import com.nhnacademy.aiotdevicegateway.device.AcquisitionResponse;
import com.nhnacademy.aiotdevicegateway.message.Message;
import com.nhnacademy.aiotdevicegateway.message.ResponseMessage;
import com.nhnacademy.aiotdevicegateway.node.base.OutNode;
import com.nhnacademy.aiotdevicegateway.wire.Wire;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * 전달받은 Message가 ResponseMessage이고 AcquisitionResponse를 담고 있다면,
 * Response에 따라 InfluxDB에 수집된 데이터를 insert 합니다.
 *
 * @author 이수정
 */
@Slf4j
public class InfluxInsertNode extends OutNode {

    private final WriteApi writeApi;
    private final int batchSize;
    private List<Point> points;

    /**
     * 입력받을 와이어, influxDB writeApi, insert될 Point를 담을 List를 초기화합니다.
     *
     * @param info   노드 정보를 담는 객체
     * @author 이수정
     */
    protected InfluxInsertNode(NodeInfo info) {
        super(info.getId(), info.getName());
        writeApi = BeanContext.get("writeApi", WriteApi.class);
        batchSize = BeanContext.get("writeOptions", WriteOptions.class).getBatchSize();
        points = new LinkedList<>();
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
                if (message instanceof ResponseMessage &&
                    ((ResponseMessage) message).getResponse() instanceof AcquisitionResponse) {
                    addToBatchList((AcquisitionResponse)((ResponseMessage) message).getResponse());

                    if (points.size() >= batchSize) {
                        writeApi.writePoints(points.subList(0, batchSize));
                        points = points.subList(batchSize, points.size());
                    }
                }
            }
        }
    }

    /**
     * batch용 list에 response에 따라 지정된 Point를 저장합니다.
     *
     * @param response Device, data 등의 정보를 포함한 ResponseMessage 객체
     * @author 이수정
     */
    private void addToBatchList(AcquisitionResponse response) {
        Iterator<Point> iterator = response.getPointIterator();
        while (iterator.hasNext()) {
            Point point = iterator.next();
            if (Objects.nonNull(point)) {
                points.add(point);
            }
        }
    }
}
