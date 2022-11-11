package com.heima.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.LoginDto;
import com.heima.model.user.pojos.ApUser;

/**
 * @ClassName UserService.java
 * @Author xazhao
 * @Create 2022.08.17
 * @Description
 */

public interface UserService extends IService<ApUser> {

    /**
     * APP端用户登录接口
     * @Param [loginDto]
     * @Return {@link ResponseResult}
     */
    public ResponseResult userLogin(LoginDto loginDto);
}
