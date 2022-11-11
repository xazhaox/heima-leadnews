package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.dtos.AdUserDto;
import com.heima.model.admin.pojos.AdUser;
import com.heima.model.common.dtos.ResponseResult;

/**
 * @ClassName AdUserService.java
 * @Author xazhao
 * @Create 2022.08.29
 * @UpdateUser
 * @UpdateDate 2022.08.29
 * @Description
 * @Version 1.0.0
 */
public interface AdUserService extends IService<AdUser> {

    /**
     * 登录
     * @Param []
     * @Return {@link ResponseResult}
     */
    ResponseResult userLogin(AdUserDto adUserDto);
}
