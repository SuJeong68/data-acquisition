package com.nhnacademy.aiotdevicegateway.device;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Response 클래스들이 공통으로 사용하는 util을 모은 class 입니다.
 *
 * @author 이수정
 */
public final class ResponseUtils {

    private ResponseUtils() {}

    /**
     * data에서 from부터 to까지 byte 배열을 LITTLE_ENDIAN으로 정렬한 ByteBuffer 객체로 반환합니다.
     *
     * @param data ReceiveNode가 응답으로 받은 byte 배열
     * @param from 시작 index
     * @param to   끝 index (해당 index는 copy 대상에 포함되지 않습니다.)
     * @return 일정 범위의 정렬된 ByteBuffer 객체
     * @author 이수정
     */
    public static ByteBuffer getSplitedByteBuffer(byte[] data, int from, int to) {
        return ByteBuffer.wrap(Arrays.copyOfRange(data, from, to)).order(ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * data에서 from부터 to까지 byte 배열을 LITTLE_ENDIAN으로 정렬한 ByteBuffer 객체를 통해 int를 반환합니다.
     *
     * @param data ReceiveNode가 응답으로 받은 byte 배열
     * @param from 시작 index
     * @param to   끝 index (해당 index는 copy 대상에 포함되지 않습니다.)
     * @return ByteBuffer 객체를 통해 얻은 int 값
     * @author 이수정
     */
    public static int getSplitedByteInt(byte[] data, int from, int to) {
        return getSplitedByteBuffer(data, from, to).getInt();
    }

    /**
     * data에서 from부터 to까지 byte 배열을 LITTLE_ENDIAN으로 정렬한 ByteBuffer 객체를 통해 byteArray를 반환합니다.
     *
     * @param data ReceiveNode가 응답으로 받은 byte 배열
     * @param from 시작 index
     * @param to   끝 index (해당 index는 copy 대상에 포함되지 않습니다.)
     * @return ByteBuffer 객체를 통해 얻은 byteArray 값
     * @author 이수정
     */
    public static byte[] getSplitedByteArray(byte[] data, int from, int to) {
        return getSplitedByteBuffer(data, from, to).array();
    }

    /**
     * data에서 from부터 to까지 byte 배열을 LITTLE_ENDIAN으로 정렬한 ByteBuffer 객체를 통해 shortArray를 반환합니다.
     *
     * @param data ReceiveNode가 응답으로 받은 byte 배열
     * @param from 시작 index
     * @return ByteBuffer 객체를 통해 얻은 shortArray 값
     * @author 이수정
     */
    public static short[] getSplitedShortArray(byte[] data, int from, int size) {
        short[] shorts = new short[size];
        for (int i = 0; i < size; i++, from += 2) {
            shorts[i] = getSplitedByteBuffer(data, from, from + 2).getShort();
        }
        return shorts;
    }

}
