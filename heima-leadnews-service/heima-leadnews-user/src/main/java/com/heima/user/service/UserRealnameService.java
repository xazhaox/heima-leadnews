package com.heima.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.pojos.ApUserRealname;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.UserAuthDto;

/**
 * @ClassName UserRealnameService.java
 * @Author xazhao
 * @Create 2022.08.31
 * @UpdateUser
 * @UpdateDate 2022.08.31
 * @Description
 * @Version 1.0.0
 */
public interface UserRealnameService extends IService<ApUserRealname> {

    /**
     * 用户列表查询
     * @Param [userAuthDto]
     * @Return {@link ResponseResult}
     */
    ResponseResult userListQuery(UserAuthDto userAuthDto);

    /**
     * 驳回用户审核
     * @Param [userAuthDto]
     * @Return {@link ResponseResult}
     */
    ResponseResult rejectUserReview(UserAuthDto userAuthDto);

    /**
     * 用户审核通过
     * @Param [userAuthDto]
     * @Return {@link ResponseResult}
     */
    ResponseResult userApproved(UserAuthDto userAuthDto);
}
