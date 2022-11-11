package com.heima.wemedia.listener;

import com.heima.model.wemedia.pojos.WmNews;
import com.heima.wemedia.config.RabbitMqConfig;
import com.heima.wemedia.service.ArticleContentReviewService;
import com.heima.wemedia.service.WmNewsService;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName ArticlesAreDelayed.java
 * @Author xazhao
 * @Create 2022.08.25
 * @UpdateUser
 * @UpdateDate 2022.08.25
 * @Description
 * @Version 1.0.0
 */

@Component
public class ArticlesAreDelayed {

    @Autowired
    private WmNewsService wmNewsService;

    @Autowired
    private ArticleContentReviewService articleContentReviewService;

    /**
     * 监听死信队列
     * @Param [id]
     * @Return
     */
    @RabbitListener(queues = RabbitMqConfig.DEADLETTER_QUEUE)
    public void articlesAreDelayed(String id) {
        // 查询文章
        WmNews wmNews = wmNewsService.getById(id);
        // 判断查询出的文章是否为空
        if(wmNews == null) {
            return;
        }
        // 判断文章的状态是否为8
        if (wmNews.getStatus() == WmNews.Status.SUCCESS.getCode()
                // 并且发布时间小于当前时间
                && wmNews.getPublishTime().before(new Date())) {
            // 调用方法, 发布文章
            articleContentReviewService.postArticle(wmNews);
        }
    }
}
