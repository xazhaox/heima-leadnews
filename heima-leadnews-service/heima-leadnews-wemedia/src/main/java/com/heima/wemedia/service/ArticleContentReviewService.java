package com.heima.wemedia.service;

import com.heima.model.article.dtos.ArticleFeignDto;
import com.heima.model.wemedia.pojos.WmNews;

/**
 * @ClassName ArticleContentReviewService.java
 * @Author xazhao
 * @Create 2022.08.22
 * @Description
 */
public interface ArticleContentReviewService {

    /**
     * 审核文章内容
     * @Param [wmNews]
     * @Return
     */
    public void articleContentReview(WmNews wmNews) throws Exception;

    /**
     * 发布文章
     * @Param [wmNews]
     * @Return
     */
    public void postArticle(WmNews wmNews);

    /**
     * 补全字段
     * @Param [wmNews, articleFeignDto]
     * @Return
     */
    public void fillField(WmNews wmNews, ArticleFeignDto articleFeignDto);
}
