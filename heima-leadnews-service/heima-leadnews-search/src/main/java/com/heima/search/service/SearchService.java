package com.heima.search.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.SearchDto;

import java.io.IOException;

/**
 * @ClassName SearchService.java
 * @Author xazhao
 * @Create 2022.08.27
 * @UpdateUser
 * @UpdateDate 2022.08.27
 * @Description
 * @Version 1.0.0
 */
public interface SearchService {

    /**
     * 搜索功能
     * @Param [searchDto]
     * @Return {@link ResponseResult}
     */
    ResponseResult search(SearchDto searchDto) throws IOException;
}
