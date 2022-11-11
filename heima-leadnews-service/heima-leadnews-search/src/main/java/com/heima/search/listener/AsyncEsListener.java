package com.heima.search.listener;

import com.alibaba.fastjson.JSON;
import com.heima.model.common.constants.WmNewsConstant;
import com.heima.model.dataimport.pojos.EsArticle;
import com.heima.utils.common.DataAnalysisUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @ClassName AsyncEsListener.java
 * @Author xazhao
 * @Create 2022.08.28
 * @UpdateUser
 * @UpdateDate 2022.08.28
 * @Description
 * @Version 1.0.0
 */

@Component
public class AsyncEsListener {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * TODO 接收消息, 数据同步
     * @Param [consumerRecord]
     * @Return
     */
    @KafkaListener(topics = WmNewsConstant.ARTICLE_ASYNC_ES_TOPIC)
    public void asyncEsListener(ConsumerRecord<String, String> consumerRecord) throws IOException {
        // 获取消息的value, 为json字符串
        String value = consumerRecord.value();
        // 将value转为EsArticle对象
        EsArticle esArticle = JSON.parseObject(value, EsArticle.class);
        // 处理对象中的文本, 调用工具类解析文章内容数据, 只保留文章文本部分
        String content = DataAnalysisUtils.analysisOfArticleContent(esArticle.getContent());
        // 重写设置文章内容数据
        esArticle.setContent(content);
        // 在将EsArticle对象转为JSON
        String esArticleJson = JSON.toJSONString(esArticle);
        // 创建IndexRequest保存到es
        IndexRequest indexResponse = new IndexRequest("app_info_article")
                .id(esArticle.getId().toString());
        // 构建数据
        indexResponse.source(esArticleJson, XContentType.JSON);
        // 发送请求
        restHighLevelClient.index(indexResponse, RequestOptions.DEFAULT);
    }
}
