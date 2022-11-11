package com.heima;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName FreemarkerTestApplication.java
 * @Author xazhao
 * @Create 2022.08.18
 * @Description
 */

@Slf4j
@SpringBootApplication
public class FreemarkerTestApplication {

    public static void main(String[] args) {

        SpringApplication.run(FreemarkerTestApplication.class, args);
        log.info("启动成功...");
    }
}
