package com.heima;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName XxlJobApplication.java
 * @Author xazhao
 * @Create 2022.09.01
 * @UpdateUser
 * @UpdateDate 2022.09.01
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@SpringBootApplication
public class XxlJobApplication {

	public static void main(String[] args) {
        SpringApplication.run(XxlJobApplication.class, args);
		log.info("启动成功...");
	}
}