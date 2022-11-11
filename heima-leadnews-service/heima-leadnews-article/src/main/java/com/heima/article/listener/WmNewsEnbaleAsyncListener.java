package com.heima.article.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.heima.article.service.ApArticleConfigService;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.wemedia.dtos.NewsSyncDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName WmNewsEnbaleAsyncListener.java
 * @Author xazhao
 * @Create 2022.08.26
 * @UpdateUser
 * @UpdateDate 2022.08.26
 * @Description
 * @Version 1.0.0
 */

@Component
@Slf4j
public class WmNewsEnbaleAsyncListener {

    @Autowired
    private ApArticleConfigService apArticleConfigService;

    @KafkaListener(topics = "EnableAsyncTopic")
    public void recMessage(ConsumerRecord<String, String> consumerRecord) {
        // 获取value数据
        String value = consumerRecord.value();
        log.info(value);
        // 将获取到的NewsSyncDto字符串转为NewsSyncDto对象
        NewsSyncDto newsSyncDto = JSON.parseObject(value, NewsSyncDto.class);
        LambdaUpdateWrapper<ApArticleConfig> updateWrapper = new LambdaUpdateWrapper<>();
        // 设置isDown值
        updateWrapper.set(ApArticleConfig::getIsDown, newsSyncDto.isDown());
        // 根据article_id修改
        updateWrapper.eq(ApArticleConfig::getArticleId, newsSyncDto.getArticleId());
        // 修改数据库
        apArticleConfigService.update(updateWrapper);
    }
}
