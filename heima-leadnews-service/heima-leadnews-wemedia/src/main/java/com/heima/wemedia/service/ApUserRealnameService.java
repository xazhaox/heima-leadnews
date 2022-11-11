package com.heima.wemedia.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.pojos.ApUser;

import java.util.Map;

/**
 * @ClassName ApUserRealnameService.java
 * @Author xazhao
 * @Create 2022.08.31
 * @UpdateUser
 * @UpdateDate 2022.08.31
 * @Description
 * @Version 1.0.0
 */
public interface ApUserRealnameService {

    /**
     * 开通自媒体账号, 该账号的用户名和密码与app一致
     * @Param []
     * @Return {@link ResponseResult}
     */
    ResponseResult saveAccountPassword(Map<String, ApUser> apUserMap);
}
