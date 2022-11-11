package com.heima.user.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.UserAuthDto;
import com.heima.user.service.UserRealnameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UserRealnameController.java
 * @Author xazhao
 * @Create 2022.08.31
 * @UpdateUser
 * @UpdateDate 2022.08.31
 * @Description
 * @Version 1.0.0
 */

@RestController
@RequestMapping("/api/v1/auth")
public class UserRealnameController {

    @Autowired
    private UserRealnameService userRealnameService;

    /**
     * 用户列表查询
     * @Param [userAuthDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/list")
    public ResponseResult userListQuery(@RequestBody UserAuthDto userAuthDto) {

        return userRealnameService.userListQuery(userAuthDto);
    }

    /**
     * 驳回用户审核
     * @Param [userAuthDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/authFail")
    public ResponseResult rejectUserReview(@RequestBody UserAuthDto userAuthDto) {
        
        return userRealnameService.rejectUserReview(userAuthDto);
    }

    /**
     * 用户审核通过
     * @Param [userAuthDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/authPass")
    public ResponseResult userApproved(@RequestBody UserAuthDto userAuthDto) {

        return userRealnameService.userApproved(userAuthDto);
    }
}
