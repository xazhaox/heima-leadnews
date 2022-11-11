package com.heima.admin.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.NewsAuthDto;

/**
 * @ClassName NewsService.java
 * @Author xazhao
 * @Create 2022.08.30
 * @UpdateUser
 * @UpdateDate 2022.08.30
 * @Description
 * @Version 1.0.0
 */
public interface NewsService {

    /**
     * 查看文章列表
     * @Param []
     * @Return {@link ResponseResult}
     */
    ResponseResult viewArticleList(NewsAuthDto newsAuthDto);

    /**
     * 查看文章详情
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    ResponseResult viewArticleDetails(Integer id);

    /**
     * 驳回文章审核
     * @Param [newsAuthDto]
     * @Return {@link ResponseResult}
     */
    ResponseResult rejectArticleReview(NewsAuthDto newsAuthDto);

    /**
     * 文章审核通过
     * @Param [newsAuthDto]
     * @Return {@link ResponseResult}
     */
    ResponseResult articlePassedTheReview(NewsAuthDto newsAuthDto);
}
