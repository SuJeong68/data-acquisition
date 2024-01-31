package com.nhnacademy.aiotdevicegateway;

import org.springframework.context.ApplicationContext;

/**
 * 일반 객체에서 Bean 객체를 사용하기 위한 class 입니다.
 * 
 * @author 이수정
 */
public class BeanContext {
    
    private static ApplicationContext context;

    /**
     * ApplicationContext를 주입받습니다.
     * 
     * @param context 스프링 컨테이너
     * @author 이수정
     */
    public static void init(ApplicationContext context) {
        BeanContext.context = context;
    }

    /**
     * 접근하고자 하는 Bean의 이름과 Class type을 이용해 Bean을 반환합니다.
     * 
     * @param <T>   Class<T>의 값
     * @param name  접근하고자 하는 Bean 이름
     * @param clazz 접근하고자 하는 Bean의 Class
     * @return 스프링 Bean
     * @author 이수정
     */
    public static <T> T get(String name, Class<T> clazz) {
        return context.getBean(name, clazz);
    }
}
