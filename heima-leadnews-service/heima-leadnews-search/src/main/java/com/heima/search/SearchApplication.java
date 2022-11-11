package com.heima.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @ClassName SearchApplication.java
 * @Author xazhao
 * @Create 2022.08.27
 * @UpdateUser
 * @UpdateDate 2022.08.27
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@SpringBootApplication
// 对那些不进行自动装配
// @EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@EnableAsync
public class SearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
        log.info("启动成功...");

    }
}
