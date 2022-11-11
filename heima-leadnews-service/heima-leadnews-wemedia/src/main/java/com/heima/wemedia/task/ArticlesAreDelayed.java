package com.heima.wemedia.task;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.wemedia.service.ArticleContentReviewService;
import com.heima.wemedia.service.WmNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @ClassName ArticlesAreDelayed.java
 * @Author xazhao
 * @Create 2022.08.25
 * @UpdateUser
 * @UpdateDate 2022.08.25
 * @Description
 * @Version 1.0.0
 */

// @Component
public class ArticlesAreDelayed {

    @Autowired
    private WmNewsService wmNewsService;

    @Autowired
    private ArticleContentReviewService articleContentReviewService;

    /**
     * 每隔一分钟查看有没有状态为8以及发布时间小于当前时间的文章
     * @Param []
     * @Return
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void articlesAreDelayed() {
        LambdaQueryWrapper<WmNews> queryWrapper = new LambdaQueryWrapper<>();
        // 判断文章状态是否为8
        queryWrapper.eq(WmNews::getStatus, WmNews.Status.SUCCESS.getCode());
        // 判断发布时间是否小于当前时间
        queryWrapper.le(WmNews::getPublishTime, new Date());
        // 查询文章
        List<WmNews> wmNews = wmNewsService.list(queryWrapper);
        // 判断查到的文章是否为空
        if (wmNews != null && !wmNews.isEmpty()) {
            // 不为空, 遍历集合中的每一个文章
            for (WmNews wmNew : wmNews) {
                // 调用方法发布文章
                articleContentReviewService.postArticle(wmNew);
            }
        }
    }
}
