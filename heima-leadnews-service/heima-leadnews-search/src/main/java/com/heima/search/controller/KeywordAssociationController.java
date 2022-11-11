package com.heima.search.controller;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.SearchDto;
import com.heima.search.service.KeywordAssociationService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName KeywordAssociationController.java
 * @Author xazhao
 * @Create 2022.08.29
 * @UpdateUser
 * @UpdateDate 2022.08.29
 * @Description
 * @Version 1.0.0
 */

@RestController
@RequestMapping("/api/v1/associate")
public class KeywordAssociationController {

    @Autowired
    private KeywordAssociationService keywordAssociationService;

    /**
     * 自动补全查询
     * @Param []
     * @Return {@link ResponseResult}
     */
    @PostMapping("/search")
    public ResponseResult keywordAssociation(@RequestBody SearchDto searchDto) {

        return keywordAssociationService.keywordAssociation(searchDto);
    }
}
