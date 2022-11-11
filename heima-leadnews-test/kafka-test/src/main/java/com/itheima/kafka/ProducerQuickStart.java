package com.itheima.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @ClassName ProducerQuickStart.java 消息生产者
 * @Author xazhao
 * @Create 2022.08.24
 * @UpdateUser
 * @UpdateDate 2022.08.24
 * @Description
 * @Version 1.0.0
 */
public class ProducerQuickStart {

    public static void main(String[] args) {

        // kafka配置信息
        Properties properties = new Properties();
        // kafka连接地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.200.130:9092");
        // 发送失败, 失败的重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG, 5);
        // 消息key的序列化器
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        // 消息value的序列化器
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        // 生产者对象
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);
        // 封装发送的消息
        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>
                ("itheima-topic","test001","kafkaTest.");
        // 发送消息
        kafkaProducer.send(producerRecord);
        // 关闭消息通道, 必须关闭, 否则消息发送失败
        kafkaProducer.close();
        System.out.println("消息发送成功...");
    }
}
