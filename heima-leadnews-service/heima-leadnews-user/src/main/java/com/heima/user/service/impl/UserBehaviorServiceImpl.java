package com.heima.user.service.impl;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.UserRelationDto;
import com.heima.user.service.UserBehaviorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserBehaviorServiceImpl.java
 * @Author xazhao
 * @Create 2022.09.01
 * @UpdateUser
 * @UpdateDate 2022.09.01
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@Service
public class UserBehaviorServiceImpl implements UserBehaviorService {

    @Autowired
    private UserBehaviorService userBehaviorService;

    /**
     * 关注与取消关注
     * @Param [userRelationDto]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult followAndUnfollow(UserRelationDto userRelationDto) {
        // TODO 关注与取消关注
        return null;
    }
}
