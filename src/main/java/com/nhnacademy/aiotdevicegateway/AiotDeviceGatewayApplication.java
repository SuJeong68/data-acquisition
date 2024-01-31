package com.nhnacademy.aiotdevicegateway;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@SpringBootApplication
public class AiotDeviceGatewayApplication {

    private final ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(AiotDeviceGatewayApplication.class, args);
    }

    /**
     * 스프링이 정상적으로 작동된 후 BeanContext에 ApplicationContext를 주입하고, 노드를 생성 후 실행합니다.
     * 
     * @author 이수정
     */
    @PostConstruct
    public void init() {
        BeanContext.init(context);
        NodeMaker.makeNodes();
    }
}
