package com.heima.user.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.LoginDto;
import com.heima.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UserController.java
 * @Author xazhao
 * @Create 2022.08.17
 * @Description
 */

@Api(value = "APP端用户登录", tags = "ap_user", description = "app端用户登录API")
@RestController
@RequestMapping("/api/v1/login")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * APP端用户登录接口
     * @Param [loginDto]
     * @Return {@link ResponseResult}
     */
    @ApiOperation("APP端用户登录接口")
    @PostMapping("/login_auth")
    public ResponseResult login(@RequestBody LoginDto loginDto) {
        // 调用方法
        return userService.userLogin(loginDto);
    }
}
