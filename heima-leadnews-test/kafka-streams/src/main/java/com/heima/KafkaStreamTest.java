package com.heima;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.time.Duration;
import java.util.Properties;

/**
 * @ClassName KafkaStreamTest.java
 * @Author xazhao
 * @Create 2022.09.03
 * @UpdateUser
 * @UpdateDate 2022.09.03
 * @Description
 * @Version 1.0.0
 */
public class KafkaStreamTest {

    public static void main(String[] args) {

        // kafkastream相关配置
        Properties properties = new Properties();
        // 发送消息的地址
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.200.130:9092");
        // 序列化器和反序列化器
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "kafkaStream-01");
        // 拓扑
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        // 从哪个source主题中获取消息
        KStream<String, String> KStream = streamsBuilder.stream("TopicSource");
        // 处理aggregate, 聚合
        KStream.groupByKey().windowedBy(TimeWindows.of(Duration.ofSeconds(5))).aggregate(new Initializer<String>() {
            @Override
            public String apply() {
                // 初始化值
                return "0";
            }
        }, new Aggregator<String, String, String>() {
            // 如何累加, aggregate上次累加的值
            @Override
            public String apply(String key, String value, String aggregate) {
                int parseValue = Integer.parseInt(value);
                int parseAggregate = Integer.parseInt(aggregate);
                return String.valueOf(parseValue + parseAggregate);
            }
        }).toStream().map(new KeyValueMapper<Windowed<String>, String, KeyValue<String, String>>() {
            @Override
            public KeyValue<String, String> apply(Windowed<String> key, String value) {
                return new KeyValue<>(key.key(), value);
            }
        }).to("TopicSink");
        // 将处理完后的消息发送到那个主题
        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), properties);
        kafkaStreams.start();
    }
}
