package com.heima.admin.service.impl;

import com.heima.admin.service.NewsService;
import com.heima.feign.wemedia.WemediaFeignClient;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.NewsAuthDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName NewsServiceImpl.java
 * @Author xazhao
 * @Create 2022.08.30
 * @UpdateUser
 * @UpdateDate 2022.08.30
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private WemediaFeignClient wemediaFeignClient;

    /**
     * 查看文章列表
     * @Param []
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult viewArticleList(NewsAuthDto newsAuthDto) {

        return wemediaFeignClient.viewArticleList(newsAuthDto);
    }

    /**
     * 查看文章详情
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult viewArticleDetails(Integer id) {

        return wemediaFeignClient.viewArticleDetails(id);
    }

    /**
     * 驳回文章审核
     * @Param [newsAuthDto]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult rejectArticleReview(NewsAuthDto newsAuthDto) {

        return wemediaFeignClient.rejectArticleReview(newsAuthDto);
    }

    /**
     * 文章审核通过
     * @Param [newsAuthDto]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult articlePassedTheReview(NewsAuthDto newsAuthDto) {

        return wemediaFeignClient.articlePassedTheReview(newsAuthDto);
    }
}
