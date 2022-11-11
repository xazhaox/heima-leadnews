package com.heima;

import com.alibaba.fastjson.JSON;
import com.heima.mapper.EsArticleMapper;
import com.heima.model.dataimport.pojos.EsArticle;
import com.heima.model.wemedia.dtos.Content;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName ImportData.java
 * @Author xazhao
 * @Create 2022.08.27
 * @UpdateUser
 * @UpdateDate 2022.08.27
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class ImportDataTest {

    @Autowired
    private EsArticleMapper esArticleMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void uploadArticleInformation() throws IOException {
        // 创建BulkRequest
        BulkRequest bulkRequest = new BulkRequest();
        // 查询数据
        List<EsArticle> esArticleList = esArticleMapper.uploadArticleInformation();
        for (EsArticle esArticle : esArticleList) {
            // 获取文章内容
            String content = esArticle.getContent();
            // 解析文本
            String contentText = analysisOfArticleContent(content);
            // 设置内容部分只显示text
            esArticle.setContent(contentText);
            // 创建IndexRequest
            IndexRequest indexRequest = new IndexRequest("app_info_article")
                    .id(esArticle.getId().toString());
            // 准备数据, 指定传输的数据为JSON
            indexRequest.source(JSON.toJSONString(esArticle), XContentType.JSON);
            // 将indexRequest对象存入bulkRequest与es只做一次交互
            bulkRequest.add(indexRequest);
        }
        // 批量导入, 发送请求
        restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        log.info("上传成功...");
    }

    /**
     * 解析文章中的文本部分
     * @Param [content]
     * @Return {@link String}
     */
    public static String analysisOfArticleContent(String content) {
        StringBuilder contentText = new StringBuilder();
        // 将字符传转为集合
        List<Content> contentsList = JSON.parseArray(content, Content.class);
        for (Content contents : contentsList) {
            // 判断type为text的
            if (contents.getType().equals("text")) {
                // 将type为text的值存入contentText
                contentText.append(contents.getValue());
            }
        }
        // 返回内容中文本部分
        return contentText.toString();
    }
}
