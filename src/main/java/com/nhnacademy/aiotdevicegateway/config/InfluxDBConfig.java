package com.nhnacademy.aiotdevicegateway.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * InfluxDB 설정 클래스 입니다.
 *
 * @author 이수정
 */
@Configuration
public class InfluxDBConfig {

    @Value("${aiot.influx.url}")
    private String url;

    @Value("${aiot.influx.token}")
    private String token;

    @Value("${aiot.influx.org}")
    private String org;

    @Value("${aiot.influx.bucket}")
    private String bucket;

    @Value("${aiot.influx.batchsize}")
    private int batchSize;

    /**
     * InfluxDBClient 설정 Bean 입니다.
     *
     * @return 설정된 InfluxDBClient
     * @author 이수정
     */
    @Bean
    public InfluxDBClient influxDBClient() {
        return InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
    }

    /**
     * WriteOptions 설정하고 생성하는 Bean 입니다.
     *
     * @return 생성된 WriteOptions
     * @author 이수정
     */
    @Bean
    public WriteOptions writeOptions() {
        return new WriteOptions.Builder().batchSize(batchSize).build();
    }

    /**
     * non-blocking writeApi를 생성하는 Bean 입니다.
     * BatchSize, BufferSize를 설정합니다.
     *
     * @return 생성된 WriteApi
     * @author 이수정
     */
    @Bean
    public WriteApi writeApi() {
        return influxDBClient().makeWriteApi(writeOptions());
    }

}
