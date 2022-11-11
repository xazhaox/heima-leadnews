package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.NewsAuthDto;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsEnableDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;

/**
 * @ClassName WmNewsService.java
 * @Author xazhao
 * @Create 2022.08.20
 * @Description
 */
public interface WmNewsService extends IService<WmNews> {

    /**
     * 自媒体文章分页查询
     * @Param [wmNewsPageReqDto]
     * @Return {@link ResponseResult}
     */
    ResponseResult pageList(WmNewsPageReqDto wmNewsPageReqDto);

    /**
     * 发布文章
     * @Param []
     * @Return {@link ResponseResult}
     */
    ResponseResult postArticle(WmNewsDto wmNewsDto) throws Exception;

    /**
     * 根据文章id查询文章
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    ResponseResult selectById(Integer id);

    /**
     * 删除文章
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    ResponseResult deleteArticle(Integer id);

    /**
     * 文章上下架
     * @Param [wmNewsEnableDto]
     * @Return {@link ResponseResult}
     */
    ResponseResult removeArticle(WmNewsEnableDto wmNewsEnableDto);

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
