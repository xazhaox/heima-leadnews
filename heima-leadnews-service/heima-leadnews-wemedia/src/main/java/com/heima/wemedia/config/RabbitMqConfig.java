package com.heima.wemedia.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @ClassName MqConfig.java
 * @Author xazhao
 * @Create 2022.08.25
 * @UpdateUser
 * @UpdateDate 2022.08.25
 * @Description
 * @Version 1.0.0
 */

@Configuration
public class RabbitMqConfig {

    public static final String SEND_EXCHANGE = "send_exchange";

    public static final String SEND_QUEUE = "send_queue";

    public static final String SEND_KEY = "send_key";

    public static final String DEADLETTER_EXCHANGE = "deadletter_exchange";

    public static final String DEADLETTER_QUEUE = "deadletter_queue";

    public static final String DEADLETTER_KEY = "deadletter_key";

    /**
     * 声明普通交换机
     * @Param []
     * @Return {@link Exchange}
     */
    @Bean
    public Exchange createSendExchange() {
        return ExchangeBuilder
                .directExchange(SEND_EXCHANGE).build();
    }

    /**
     * 声明普通队列绑定死信交换机
     * @Param []
     * @Return {@link Queue}
     */
    @Bean
    public Queue createSendQueue() {
        return QueueBuilder
                .durable(SEND_QUEUE)
                // 指定死信交换机
                .deadLetterExchange(DEADLETTER_EXCHANGE)
                // 指定死信routingKey
                .deadLetterRoutingKey(DEADLETTER_KEY)
                .build();
    }

    /**
     * 绑定普通交换机和普通队列
     * @Param [exchange, queue]
     * @Return {@link Binding}
     */
    @Bean
    public Binding createSendQueueIsExchange(
            @Qualifier("createSendExchange") Exchange exchange,
            @Qualifier("createSendQueue")Queue queue) {

        return BindingBuilder
                // 指定队列
                .bind(queue)
                // 指定交换机
                .to(exchange)
                // 指定routingKey
                .with(SEND_KEY).noargs();
    }

    /**
     * 声明死信交换机
     * @Param []
     * @Return {@link Exchange}
     */
    @Bean
    public Exchange createdeadletterExchange() {
        return ExchangeBuilder
                .directExchange(DEADLETTER_EXCHANGE).build();
    }

    /**
     * 声明死信队列
     * @Param []
     * @Return {@link Queue}
     */
    @Bean
    public Queue createdeadletterQueue() {
        return QueueBuilder
                .durable(DEADLETTER_QUEUE).build();
    }

    /**
     * 绑定死信交换机和死信队列
     * @Param [exchange, queue]
     * @Return {@link Binding}
     */
    @Bean
    public Binding createdeadletterQueueIsExchange(
            @Qualifier("createdeadletterExchange") Exchange exchange,
            @Qualifier("createdeadletterQueue")Queue queue) {

        return BindingBuilder
                // 指定队列
                .bind(queue)
                // 指定交换机
                .to(exchange)
                // 指定routingKey
                .with(DEADLETTER_KEY).noargs();
    }
}
