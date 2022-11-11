package com.heima.search.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.SearchDto;

/**
 * @ClassName KeywordAssociationService.java
 * @Author xazhao
 * @Create 2022.08.29
 * @UpdateUser
 * @UpdateDate 2022.08.29
 * @Description
 * @Version 1.0.0
 */
public interface KeywordAssociationService {

    /**
     * 自动补全查询
     * @Param []
     * @Return {@link ResponseResult}
     */
    public ResponseResult keywordAssociation(SearchDto searchDto);
}
