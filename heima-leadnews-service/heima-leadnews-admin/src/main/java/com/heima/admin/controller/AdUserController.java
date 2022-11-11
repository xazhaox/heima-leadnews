package com.heima.admin.controller;

import com.heima.admin.service.AdUserService;
import com.heima.model.admin.dtos.AdUserDto;
import com.heima.model.common.dtos.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName AdUserController.java
 * @Author xazhao
 * @Create 2022.08.29
 * @UpdateUser
 * @UpdateDate 2022.08.29
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@RestController
@RequestMapping("/login")
public class AdUserController {

    @Autowired
    private AdUserService adUserService;

    /**
     * 登录
     * @Param []
     * @Return {@link ResponseResult}
     */
    @PostMapping("/in")
    public ResponseResult userLogin(@RequestBody AdUserDto adUserDto) {

        return adUserService.userLogin(adUserDto);
    }
}
