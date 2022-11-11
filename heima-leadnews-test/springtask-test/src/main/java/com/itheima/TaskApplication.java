package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @ClassName TaskApplication.java
 * @Author xazhao
 * @Create 2022.08.25
 * @UpdateUser
 * @UpdateDate 2022.08.25
 * @Description
 * @Version 1.0.0
 */

@EnableScheduling
@SpringBootApplication
public class TaskApplication {

    public static void main(String[] args) {

        SpringApplication.run(TaskApplication.class,args);
    }
}
