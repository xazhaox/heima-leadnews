package com.heima.search.controller;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.SearchDto;
import com.heima.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @ClassName SearcController.java
 * @Author xazhao
 * @Create 2022.08.27
 * @UpdateUser
 * @UpdateDate 2022.08.27
 * @Description
 * @Version 1.0.0
 */

@RestController
@RequestMapping("/api/v1/article/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * 搜索功能
     * @Param []
     * @Return {@link ResponseResult}
     */
    @PostMapping("/search")
    public ResponseResult search(@RequestBody SearchDto searchDto) throws IOException {

        return searchService.search(searchDto);
    }
}
