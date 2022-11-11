package com.heima.admin.controller;

import com.heima.admin.service.NewsService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.NewsAuthDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName NewsController.java
 * @Author xazhao
 * @Create 2022.08.30
 * @UpdateUser
 * @UpdateDate 2022.08.30
 * @Description
 * @Version 1.0.0
 */

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    /**
     * 查看文章列表
     * @Param []
     * @Return {@link ResponseResult}
     */
    @PostMapping("/list_vo")
    public ResponseResult viewArticleList(@RequestBody NewsAuthDto newsAuthDto) {

        return newsService.viewArticleList(newsAuthDto);
    }

    /**
     * 查看文章详情
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    @GetMapping("/one_vo/{id}")
    public ResponseResult viewArticleDetails(@PathVariable Integer id) {

        return newsService.viewArticleDetails(id);
    }

    /**
     * 驳回文章审核
     * @Param [newsAuthDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/auth_fail")
    public ResponseResult rejectArticleReview(@RequestBody NewsAuthDto newsAuthDto) {

        return newsService.rejectArticleReview(newsAuthDto);
    }

    /**
     * 文章审核通过
     * @Param [newsAuthDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/api/v1/news/auth_pass")
    public ResponseResult articlePassedTheReview(@RequestBody NewsAuthDto newsAuthDto) {

        return newsService.articlePassedTheReview(newsAuthDto);
    }

}
