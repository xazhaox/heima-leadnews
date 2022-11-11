package com.heima.article.controller.v1;

import com.heima.article.service.ApArticleService;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.behavior.dtos.ArticleInfoDto;
import com.heima.model.common.constants.ArticleConstants;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ArticleHomeController.java
 * @Author xazhao
 * @Create 2022.08.18
 * @Description
 */

@RestController
@RequestMapping("/api/v1/article")
public class ArticleHomeController {

    @Autowired
    private ApArticleService apArticleService;

    /**
     * 加载首页, 根据参数加载文章列表, 1为加载更多 2为加载最新
     * @Param [articleHomeDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/load")
    public ResponseResult articleLoad(@RequestBody ArticleHomeDto articleHomeDto) {

        return apArticleService.articleLoad(articleHomeDto, ArticleConstants.LOADTYPE_LOAD_MORE);
    }

    /**
     * 加载更多, 根据参数加载文章列表, 1为加载更多 2为加载最新
     * @Param [articleHomeDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/loadmore")
    public ResponseResult articleLoadmore(@RequestBody ArticleHomeDto articleHomeDto) {

        return apArticleService.articleLoad(articleHomeDto, ArticleConstants.LOADTYPE_LOAD_MORE);
    }

    /**
     * 加载最新, 根据参数加载文章列表, 1为加载更多 2为加载最新
     * @Param [articleHomeDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/loadnew")
    public ResponseResult articleLoadnew(@RequestBody ArticleHomeDto articleHomeDto) {

        return apArticleService.articleLoad(articleHomeDto, ArticleConstants.LOADTYPE_LOAD_NEW);
    }

    /**
     * 回显用户行为
     * @Param []
     * @Return {@link ResponseResult}
     */
    @PostMapping("/load_article_behavior")
    public ResponseResult loadUserBehavior(@RequestBody ArticleInfoDto articleInfoDto) {

        return apArticleService.loadUserBehavior(articleInfoDto);
    }
}
