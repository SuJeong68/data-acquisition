package com.nhnacademy.aiotdevicegateway.device.dataq;

import com.google.gson.JsonObject;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.nhnacademy.aiotdevicegateway.device.AcquisitionResponse;
import com.nhnacademy.aiotdevicegateway.device.Device;
import com.nhnacademy.aiotdevicegateway.device.MQTTMessage;
import com.nhnacademy.aiotdevicegateway.device.ResponseUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;

/**
 * DataQ 수집 데이터를 받는 Response 입니다.
 *
 * @author 이수정
 */
@Slf4j
@Getter
public final class ADCData implements AcquisitionResponse {

    public static final int TYPE = 0x14142135;

    private final DataQInfo info;
    private final long time;

    private final int groupID;
    private final int order;
    private final int cumulativeCount;
    private final int payloadSamples;
    private final short[] payload;

    private final double[] converted;

    /**
     * DataQ에서 받은 수집 데이터로 ADCData Instance를 생성합니다.
     *
     * @param device DataQ의 정보를 담은 Device 객체
     * @param data   수집된 데이터
     * @param time   응답 받은 시간
     * @author 이수정
     */
    public ADCData(Device device, byte[] data, long time) {
        info = (DataQInfo) (((DataQ) device).getInfo());
        this.time = time;

        groupID = ResponseUtils.getSplitedByteInt(data, 4, 8);
        order = ResponseUtils.getSplitedByteInt(data, 8, 12);
        cumulativeCount = ResponseUtils.getSplitedByteInt(data, 12, 16);
        payloadSamples = ResponseUtils.getSplitedByteInt(data, 16, 20);
        payload = ResponseUtils.getSplitedShortArray(data, 20, payloadSamples);

        converted = new double[payloadSamples];
        convertPayload();
    }

    /**
     * Digital 값을 Analog 배열로 변환합니다. (5V 기준) <br/>
     * Analog to Digital => 5V (/5) -> 1V (*32_767) -> short 값 <br/>
     * Digital to Analog => short 값 (/32_767) -> 1V (*5) -> 5V
     *
     * @author 이수정
     */
    public void convertPayload() {
        for (int i = 0; i < payloadSamples; i++) {
            converted[i] = Math.round(((payload[i] & 0xffff) / 65_535.0 * 5) * 10_000) / 10_000.0;
        }
    }

    /**
     * DataQ device의 0번부터 7번 채널까지 Point 객체를 생성해 제공하는 Iterator 반환합니다.
     *
     * @return Point 객체를 반환하는 Iterator
     * @author 이수정
     */
    @Override
    public Iterator<Point> getPointIterator() {
        return new Iterator<>() {
            int currentPayloadSample = 0;
            long currentTime = time;
            int channel = 0;
            final int maxChannel = info.getSensorsLength();
            final long interval = info.getHz();

            @Override
            public boolean hasNext() {
                return currentPayloadSample < payloadSamples;
            }

            @Override
            public Point next() {
                if (channel == maxChannel) {
                    channel = 0;
                    currentTime += interval;
                }

                Point point = null;
                if (channel < info.getSensorsLength() && !info.getSensor(channel).isBlank()) {
                    point = Point.measurement(info.getSensor(channel))
                        .addTag("name", info.getName())
                        .addField("value", converted[currentPayloadSample])
                        .time(currentTime, WritePrecision.MS);
                }
                channel++;
                currentPayloadSample++;

                return point;
            }
        };
    }

    /**
     * DataQ device의 0번부터 7번 채널까지 MQTTMessage 객체를 생성해 제공하는 Iterator 반환합니다.
     *
     * @return MQTTMessage 객체를 반환하는 Iterator
     * @author 이수정
     */
    @Override
    public Iterator<MQTTMessage> getMqttMessageIterator() {
        return new Iterator<>() {
            int currentPayloadSample = 0;
            long currentTime = time;
            int channel = 0;
            final int maxChannel = info.getSensorsLength();
            final long interval = info.getHz();

            @Override
            public boolean hasNext() {
                return currentPayloadSample < payloadSamples;
            }

            @Override
            public MQTTMessage next() {
                if (channel == maxChannel) {
                    channel = 0;
                    currentTime += interval;
                }

                MQTTMessage mqttMessage = null;
                if (channel < info.getSensorsLength() && !info.getSensor(channel).isBlank()) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("payload", converted[currentPayloadSample]);
                    mqttMessage = new MQTTMessage(String.format("data/b/%s/p/%s/n/%s/s/%s", info.getBranch(), info.getPlace(), info.getName(), info.getSensor(channel)), jsonObject);
                }
                channel++;
                currentPayloadSample++;

                return mqttMessage;
            }
        };
    }
}
