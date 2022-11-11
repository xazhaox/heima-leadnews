package com.heima.article.controller.feign;

import com.heima.article.service.ApArticleService;
import com.heima.model.article.dtos.ArticleFeignDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsAuthorDto;
import com.heima.model.wemedia.pojos.WmNews;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ArticleFeignController.java
 * @Author xazhao
 * @Create 2022.08.21
 * @Description
 */

@RestController
@RequestMapping("/article")
public class ArticleFeignController {

    @Autowired
    private ApArticleService apArticleService;

    /**
     * APP端文章保存
     * @Param []
     * @Return {@link ResponseResult}
     */
    @PostMapping("/insert")
    public ResponseResult articleFeignClient(@RequestBody ArticleFeignDto articleFeignDto) throws TemplateException, IOException {

        return apArticleService.articleFeignClient(articleFeignDto);
    }

    /**
     * 获取作者名, 封装WmNewsAuthorDto
     * @Param []
     * @Return {@link ResponseResult}
     */
    @PostMapping("/add")
    public ResponseResult articleAuthorFeignClient(@RequestBody Map<String, List<WmNews>> wmNewsMap) {

        return apArticleService.articleAuthorFeignClient(wmNewsMap);
    }

    /**
     * 计算热点文章
     * @Param [date]
     * @Return {@link ResponseResult<List<ApArticle>>}
     */
    @GetMapping("findArticleByPublishTime")
    public ResponseResult<List<ApArticle>> findArticleByPublishTime(@RequestParam("date") Date date) {

        return apArticleService.findArticleByPublishTime(date);
    }

}
