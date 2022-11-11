package com.heima.admin.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName AppGatewayApplication.java
 * @Author xazhao
 * @Create 2022.08.17
 * @Description
 */

@Slf4j
@SpringBootApplication
@EnableDiscoveryClient // 开启注册中心
public class AppGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppGatewayApplication.class, args);
        log.info("Gateway启动成功...");
    }
}
