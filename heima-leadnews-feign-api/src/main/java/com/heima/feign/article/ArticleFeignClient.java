package com.heima.feign.article;

import com.heima.model.article.dtos.ArticleFeignDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsAuthorDto;
import com.heima.model.wemedia.pojos.WmNews;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName Article.java
 * @Author xazhao
 * @Create 2022.08.22
 * @Description
 */

// 服务提供方的服务名称
@FeignClient(name = "leadnews-article")
public interface ArticleFeignClient {

    /**
     * APP端文章保存
     * @Param [articleFeignDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/article/insert")
    public ResponseResult<Map<String, String>> articleFeignClient(@RequestBody ArticleFeignDto articleFeignDto);

    /**
     * 获取作者名, 封装WmNewsAuthorDto
     * @Param []
     * @Return {@link ResponseResult}
     */
    @PostMapping("/article/add")
    public ResponseResult articleAuthorFeignClient(@RequestBody Map<String, List<WmNews>> wmNewsMap);

    /**
     * 计算热点文章
     * @Param [date]
     * @Return {@link ResponseResult<List<ApArticle>>}
     */
    @GetMapping("/article/findArticleByPublishTime")
    public ResponseResult<List<ApArticle>> findArticleByPublishTime(@RequestParam("date") Date date);

}
