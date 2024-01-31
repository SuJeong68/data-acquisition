package com.nhnacademy.aiotdevicegateway.device;

import com.influxdb.client.write.Point;

import java.util.Iterator;

/**
 * Device 응답 객체 중 수집 데이터인 응답을 위한 interface 입니다.
 *
 * @author 이수정
 */
public interface AcquisitionResponse extends Response {

    /**
     * InfluxClient point Iterator를 반환합니다. InfluxInsertNode에서 사용됩니다.
     *
     * @return InfluxDB Client Point를 반환하는 Iterator
     * @author 이수정
     */
    Iterator<Point> getPointIterator();

    /**
     * MQTTMessage Iterator를 반환합니다. MqttOutNode에서 사용됩니다.
     *
     * @return MQTTMessage를 반환하는 Iterator
     * @author 이수정
     */
    Iterator<MQTTMessage> getMqttMessageIterator();
}
