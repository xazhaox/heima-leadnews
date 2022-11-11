package com.heima.behavior;

import com.heima.feign.config.DefaultFeignConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @ClassName BehaviorApplication.java
 * @Author xazhao
 * @Create 2022.08.31
 * @UpdateUser
 * @UpdateDate 2022.08.31
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
// 服务消费方, feign全局生效
@EnableFeignClients(defaultConfiguration = DefaultFeignConfiguration.class,
        basePackages = {"com.heima.feign.article", "com.heima.feign.wemedia"})
public class TaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class, args);
        log.info("启动成功...");
    }
}