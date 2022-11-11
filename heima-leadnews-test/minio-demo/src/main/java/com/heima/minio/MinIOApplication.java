package com.heima.minio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName MinIOApplication.java
 * @Author xazhao
 * @Create 2022.08.18
 * @Description
 */

@Slf4j
@SpringBootApplication
public class MinIOApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinIOApplication.class,args);
        log.info("启动成功...");
    }
}
