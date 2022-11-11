package com.itheima;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName KafkaApplication.java
 * @Author xazhao
 * @Create 2022.08.26
 * @UpdateUser
 * @UpdateDate 2022.08.26
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@SpringBootApplication
public class KafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaApplication.class, args);
        log.info("启动成功...");
    }
}