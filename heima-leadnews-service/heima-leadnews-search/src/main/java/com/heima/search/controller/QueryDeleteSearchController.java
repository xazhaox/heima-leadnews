package com.heima.search.controller;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.search.service.SaveSearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName QueryDeleteSearchController.java
 * @Author xazhao
 * @Create 2022.08.29
 * @UpdateUser
 * @UpdateDate 2022.08.29
 * @Description
 * @Version 1.0.0
 */

@RestController
@RequestMapping("/api/v1/history")
public class QueryDeleteSearchController {

    @Autowired
    private SaveSearchHistoryService saveSearchHistoryService;

    /**
     * 查询搜索记录
     * @Param []
     * @Return {@link ResponseResult}
     */
    @PostMapping("/load")
    public ResponseResult querySearchRecords(@RequestBody Map<String, Integer> SearchSize) {

        return saveSearchHistoryService.querySearchRecords(SearchSize);
    }

    /**
     * 删除搜索记录
     * @Param []
     * @Return {@link ResponseResult}
     */
    @PostMapping("/del")
    public ResponseResult deleteSearchRecords(@RequestBody Map<String, String> apUserSearchId) {

        return saveSearchHistoryService.deleteSearchRecords(apUserSearchId);
    }
}
