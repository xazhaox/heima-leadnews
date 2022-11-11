package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.NewsAuthDto;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsEnableDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.wemedia.service.WmNewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName WmNewsController.java
 * @Author xazhao
 * @Create 2022.08.20
 * @Description
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/news")
public class WmNewsController {

    @Autowired
    private WmNewsService wmNewsService;

    /**
     * 自媒体文章分页查询
     * @Param [wmNewsPageReqDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/list")
    public ResponseResult pageList(@RequestBody WmNewsPageReqDto wmNewsPageReqDto) {

        return wmNewsService.pageList(wmNewsPageReqDto);
    }

    /**
     * 发布文章
     * @Param []
     * @Return {@link ResponseResult}
     */
    @PostMapping("/submit")
    public ResponseResult postArticle(@RequestBody WmNewsDto wmNewsDto) throws Exception {

        return wmNewsService.postArticle(wmNewsDto);
    }

    /**
     * 根据文章id查询文章
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    @GetMapping("/one/{id}")
    public ResponseResult selectById(@PathVariable("id") Integer id) {

        return wmNewsService.selectById(id);
    }

    /**
     * 删除文章
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    @GetMapping("/del_news/{id}")
    public ResponseResult deleteArticle(@PathVariable("id") Integer id) {

        return wmNewsService.deleteArticle(id);
    }


    /**
     * 上架或下架文章
     * @Param []
     * @Return {@link ResponseResult}
     */
    @PostMapping("/down_or_up")
    public ResponseResult removeArticle(@RequestBody WmNewsEnableDto wmNewsEnableDto) {

        return wmNewsService.removeArticle(wmNewsEnableDto);
    }

    /**
     * 查看文章列表
     * @Param []
     * @Return {@link ResponseResult}
     */
    @PostMapping("/list_vo")
    public ResponseResult viewArticleList(@RequestBody NewsAuthDto newsAuthDto) {

        return wmNewsService.viewArticleList(newsAuthDto);
    }

    /**
     * 查看文章详情
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    @GetMapping("/one_vo/{id}")
    public ResponseResult viewArticleDetails(@PathVariable Integer id) {

        return wmNewsService.viewArticleDetails(id);
    }

    /**
     * 驳回文章审核
     * @Param [newsAuthDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/auth_fail")
    public ResponseResult rejectArticleReview(@RequestBody NewsAuthDto newsAuthDto) {

        return wmNewsService.rejectArticleReview(newsAuthDto);
    }

    /**
     * 文章审核通过
     * @Param [newsAuthDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/auth_pass")
    public ResponseResult articlePassedTheReview(@RequestBody NewsAuthDto newsAuthDto) {

        return wmNewsService.articlePassedTheReview(newsAuthDto);
    }
}
