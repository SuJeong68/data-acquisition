package com.nhnacademy.aiot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class AdcData implements DQResponse {
    static final int TYPE = 0x14142135;
    private static AtomicLong scanTime;

    private int groupId;
    private int order;
    private int cumulativeCount;
    private int payLoadSamples;
    private short[] payLoad;
    private double[] converted;
    private long time;

    public AdcData(byte[] data) {
        if (Objects.isNull(scanTime)) {
            scanTime = new AtomicLong(System.currentTimeMillis());
        }
        time = scanTime.getAndAdd(1);

        groupId = DQResponse.getSplitedByteBuffer(data, 4, 8).getInt();
        order = DQResponse.getSplitedByteBuffer(data, 8, 12).getInt();
        cumulativeCount = DQResponse.getSplitedByteBuffer(data, 12, 16).getInt();
        payLoadSamples = DQResponse.getSplitedByteBuffer(data, 16, 20).getInt();

        payLoad = new short[payLoadSamples];
        DQResponse.getSplitedByteBuffer(data, 20, 20 + payLoadSamples * 2).asShortBuffer().get(payLoad);

        converted = new double[payLoadSamples];
        conversion();
    }

    private void conversion() {
        for (int i = 0; i < payLoadSamples; i++) {
            converted[i] = Math.round((payLoad[i] / 32767.0 * 5) * 10000) / 10000.0;
        }
    }

    @Override
    public String getPayLoad() {
        StringBuilder sb = new StringBuilder();
        sb.append("samples = [ ");
        for (int i = 0; i < payLoadSamples; i++) {
            sb.append("[").append(payLoad[i]).append(", ").append(converted[i]).append("]");
            if (i != payLoadSamples - 1) {
                sb.append(", ");
            }
        }
        sb.append(" ]");
        return sb.toString();
    }

    public String getConverted(int i) {
        return Double.toString(converted[i]);
    }

    public String getTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return timeFormat.format(new Date(time));
    }

    public int getPayLoadSamples() {
        return payLoadSamples;
    }

    @Override
    public String toString() {
        return "time = " + getTime() + ", " + getPayLoad();
    }
}
