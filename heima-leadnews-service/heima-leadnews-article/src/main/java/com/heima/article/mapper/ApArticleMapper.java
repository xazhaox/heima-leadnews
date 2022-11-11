package com.heima.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @ClassName ApArticleMapper.java
 * @Author xazhao
 * @Create 2022.08.18
 * @Description
 */

@Mapper
public interface ApArticleMapper extends BaseMapper<ApArticle> {

    /**
     * 加载首页, 根据参数加载文章列表, 1为加载更多 2为加载最新
     * @Param [articleHomeDto, load-type]
     * @Return {@link List<ApArticle>}
     */
    public List<ApArticle> loadArticleList(
            @Param("articleHomeDto") ArticleHomeDto articleHomeDto,
            @Param("loadtype") short loadtype
            );

    /**
     * 查询指定大于指定发布时间的文章
     * @Param [date]
     * @Return {@link List<ApArticle>}
     */
    public List<ApArticle> findArticleByPublishTime(Date date);
}
