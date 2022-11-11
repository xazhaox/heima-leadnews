package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.article.dtos.ArticleFeignDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.behavior.dtos.ArticleInfoDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsAuthorDto;
import com.heima.model.wemedia.pojos.WmNews;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ApArticleService.java
 * @Author xazhao
 * @Create 2022.08.18
 * @Description
 */
public interface ApArticleService extends IService<ApArticle> {

    /**
     * 加载首页, 根据参数加载文章列表, 1为加载更多 2为加载最新
     * @Param [articleHomeDto]
     * @Return {@link ResponseResult}
     */
    public ResponseResult articleLoad(ArticleHomeDto articleHomeDto, Short loadtype);

    /**
     * APP端文章保存
     * @Param []
     * @Return {@link ResponseResult}
     */
    ResponseResult articleFeignClient(ArticleFeignDto articleFeignDto) throws IOException, TemplateException;

    /**
     * 获取作者名, 封装WmNewsAuthorDto
     * @Param []
     * @Return {@link ResponseResult}
     */
    ResponseResult articleAuthorFeignClient(Map<String, List<WmNews>> wmNewsMap);

    /**
     * 回显用户行为
     * @Param []
     * @Return {@link ResponseResult}
     */
    ResponseResult loadUserBehavior(ArticleInfoDto articleInfoDto);

    /**
     * 计算热点文章
     * @Param [date]
     * @Return {@link ResponseResult<List<ApArticle>>}
     */
    ResponseResult<List<ApArticle>> findArticleByPublishTime(Date date);
}
