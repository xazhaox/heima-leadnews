package com.itheima.controller;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName KafkaController.java
 * @Author xazhao
 * @Create 2022.08.26
 * @UpdateUser
 * @UpdateDate 2022.08.26
 * @Description
 * @Version 1.0.0
 */

@Data
@RestController
public class KafkaController {

    private String name;

    private Integer age;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    /**
     * 发送消息, 消息生产者
     * @Param []
     * @Return {@link String}
     */
    @GetMapping("/kafka")
    public String kafkaTest() {
        // 发送消息
        kafkaTemplate.send("itcast-topic", "xazhao");
        return "OK";
    }

    /**
     * 发送对象
     * @Param []
     * @Return {@link String}
     */
    @GetMapping("/mapkafka")
    public String mapKafkaTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "小安");
        map.put("age", 23);
        // 将map转换为string
        String toJSONString = JSON.toJSONString(map);
        // 发送消息
        kafkaTemplate.send("itcast-topic2", toJSONString);

        return "ok";
    }


    @GetMapping("/KafkaTest1")
    public String KafkaTest1() {
        KafkaController kafkaController = new KafkaController();
        kafkaController.name = "kafka";
        kafkaController.age = 23;

        // 将对象转换为string
        String jsonString = JSON.toJSONString(kafkaController);
        // 发送消息
        kafkaTemplate.send("itcast-topic1", jsonString);

        return "ok";
    }
}
