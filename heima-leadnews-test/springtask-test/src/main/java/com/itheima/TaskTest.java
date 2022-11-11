package com.itheima;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ClassName TaskTest.java
 * @Author xazhao
 * @Create 2022.08.25
 * @UpdateUser
 * @UpdateDate 2022.08.25
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@Component
public class TaskTest {

    @Scheduled(cron = "* * * * * ?")
    public void Tasktest() {
        log.info("定时任务...");
    }
}
