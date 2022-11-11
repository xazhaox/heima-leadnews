package com.itheima.listener;

import com.alibaba.fastjson.JSON;
import com.itheima.controller.KafkaController;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName KafkaListener.java
 * @Author xazhao
 * @Create 2022.08.26
 * @UpdateUser
 * @UpdateDate 2022.08.26
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@Component
public class KafkaListenerTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 消息的消费者
     * @Param [message]
     * @Return 
     */
    @KafkaListener(topics = "itcast-topic")
    public void kafkaListener(String message) {
        // 消息不为空接收
        if (message != null) {
            log.info("消息为 : {}", message);
        }
    }

    /**
     * 消息的消费者
     * @Param [message]
     * @Return
     */
    @KafkaListener(topics = "itcast-topic2")
    public void mapKafkaListener(String message) {
        log.info("message消息为 : {}", message);
        // 将传回的string转为map
        Map map = JSON.parseObject(message, Map.class);
        log.info("map消息为 : {}", map);
    }

    @KafkaListener(topics = "itcast-topic1")
    public void mapKafkaListener1(String message) {
        log.info("message消息为 : {}", message);
        // 将传回的string转为对象
        KafkaController kafkaController = JSON.parseObject(message, KafkaController.class);
        log.info("KafkaController消息为 : {}", kafkaController);
    }
}
