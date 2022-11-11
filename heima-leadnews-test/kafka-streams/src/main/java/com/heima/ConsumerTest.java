package com.heima;

import org.apache.kafka.clients.consumer.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * @ClassName ConsumerTest.java
 * @Author xazhao
 * @Create 2022.09.03
 * @UpdateUser
 * @UpdateDate 2022.09.03
 * @Description 消费者
 * @Version 1.0.0
 */
public class ConsumerTest {

    public static void main(String[] args) {

        // kafkastream相关配置
        Properties properties = new Properties();
        // 创建Kafka的配置对象, 发送消息的地址
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.200.130:9092");
        // 消息key的反序列化器
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        // 消息value的反序列化器
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        // 消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "topicMGroup1");

        // 创建消息消费者对象
        Consumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        // 订阅主题
        consumer.subscribe(Arrays.asList("TopicSink"));
        // 死循环让当前线程一直处于监听状态
        while (true) {
            // 拉取消息, 得到一个map
            ConsumerRecords<String, String> consumerRecords = consumer
                    // Duration.ofSeconds(3)表示若没有拉取消息, 等待3秒在拉取一次
                    .poll(Duration.ofSeconds(3));
            // 遍历拉取到的消息
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                System.out.print("Key : " + consumerRecord.key());
                System.out.println(", Value : " + consumerRecord.value() + "\n");
            }
        }
    }
}
