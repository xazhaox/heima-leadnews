package com.heima;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @ClassName ProcuderTest.java
 * @Author xazhao
 * @Create 2022.09.03
 * @UpdateUser
 * @UpdateDate 2022.09.03
 * @Description 生产者
 * @Version 1.0.0
 */
public class ProcuderTest {

    public static void main(String[] args) {

        // kafkastream相关配置
        Properties properties = new Properties();
        // kafka的地址, key和value的序列化器
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.200.130:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<String, String>(properties);

        // 封装发送消息
        ProducerRecord<String, String> producerRecord1 = new ProducerRecord<String, String>("TopicSource", "xiaoan", "1");
        ProducerRecord<String, String> producerRecord2 = new ProducerRecord<String, String>("TopicSource", "xiaoan", "5");
        ProducerRecord<String, String> producerRecord3 = new ProducerRecord<String, String>("TopicSource", "Marry", "2");
        ProducerRecord<String, String> producerRecord4 = new ProducerRecord<String, String>("TopicSource", "Marry", "4");
        ProducerRecord<String, String> producerRecord5 = new ProducerRecord<String, String>("TopicSource", "Tom", "12");

        producer.send(producerRecord1);
        producer.send(producerRecord2);
        producer.send(producerRecord3);
        producer.send(producerRecord4);
        producer.send(producerRecord5);
        // 关闭消息通道, 必须关闭, 否则消息发送失败
        producer.close();
        System.out.println("消息发送结束...");
    }
}
