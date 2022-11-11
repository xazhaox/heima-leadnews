package com.heima.mapper;

import com.heima.model.dataimport.pojos.EsArticle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName EsArticleMapper.java
 * @Author xazhao
 * @Create 2022.08.27
 * @UpdateUser
 * @UpdateDate 2022.08.27
 * @Description
 * @Version 1.0.0
 */

@Mapper
public interface EsArticleMapper {

    /**
     * 查询需要上传的文章
     * @Param []
     * @Return {@link List<EsArticle>}
     */
    public List<EsArticle> uploadArticleInformation();
}
