package com.heima.admin.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName AdmiGatewayApplication.java
 * @Author xazhao
 * @Create 2022.08.29
 * @UpdateUser
 * @UpdateDate 2022.08.29
 * @Description
 * @Version 1.0.0
 */


@Slf4j
@SpringBootApplication
@EnableDiscoveryClient // 开启注册中心
public class AdmiGatewayApplication {

    public static void main(String[] args) {

        SpringApplication.run(AdmiGatewayApplication.class, args);
        log.info("Gateway启动成功...");
    }
}
