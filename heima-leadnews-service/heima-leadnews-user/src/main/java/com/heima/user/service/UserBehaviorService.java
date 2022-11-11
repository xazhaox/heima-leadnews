package com.heima.user.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.UserRelationDto;

/**
 * @ClassName UserBehaviorService.java
 * @Author xazhao
 * @Create 2022.09.01
 * @UpdateUser
 * @UpdateDate 2022.09.01
 * @Description
 * @Version 1.0.0
 */
public interface UserBehaviorService {

    /**
     * 关注与取消关注
     * @Param [userRelationDto]
     * @Return {@link ResponseResult}
     */
    ResponseResult followAndUnfollow(UserRelationDto userRelationDto);
}
