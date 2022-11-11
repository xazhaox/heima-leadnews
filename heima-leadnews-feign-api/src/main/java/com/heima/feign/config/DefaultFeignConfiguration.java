package com.heima.feign.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @ClassName DefaultFeignConfiguration.java
 * @Author xazhao
 * @Create 2022.08.22
 * @Description
 */

@Component
public class DefaultFeignConfiguration {

    /**
     * 全局配置
     * 在服务消费方的引导类上添加 @EnableFeignClients 注解添加加载配置类
     * 在服务提供方的接口上添加 @FeignClient 注解添加加载配置类
     * @Param []
     * @Return {@link Logger.Level}
     */
    @Bean
    public Logger.Level feignLogger() {
        // 日志级别
        return Logger.Level.FULL;
    }
}
