package com.itheima.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * @ClassName ConsumerQuickStart.java 消息的消费者
 * @Author xazhao
 * @Create 2022.08.24
 * @UpdateUser
 * @UpdateDate 2022.08.24
 * @Description
 * @Version 1.0.0
 */
public class ConsumerQuickStart {

    public static void main(String[] args) {

        // kafka配置信息
        Properties properties = new Properties();
        // kafka连接地址
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.200.130:9092");
        // 消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group2");
        // 消息key的反序列化器
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        // 消息value的反序列化器
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        // 消费者对象
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
        // 将topic的itheima-topic转为集合
        List<String> singletonList = Collections.singletonList("itheima-topic");
        // 订阅主题, 需要一个集合
        kafkaConsumer.subscribe(singletonList);

        // 死循环让当前线程一直出去监听状态
        while (true) {
            // 拉取消息, 得到一个map
            ConsumerRecords<String, String> consumerRecords = kafkaConsumer
                    // poll(Duration.ofSeconds(3))表示若没有拉取到消息, 等待秒钟在拉取一次
                    .poll(Duration.ofSeconds(3));
            // 遍历拉取到的消息
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                System.out.print("key : " + consumerRecord.key());
                System.out.println(", value : " + consumerRecord.value());
            }
        }
    }
}
