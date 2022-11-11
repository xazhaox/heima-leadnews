package com.heima.wemedia.controller.feign;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.pojos.ApUser;
import com.heima.wemedia.service.ApUserRealnameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName ApUserRealnameController.java
 * @Author xazhao
 * @Create 2022.08.31
 * @UpdateUser
 * @UpdateDate 2022.08.31
 * @Description
 * @Version 1.0.0
 */

@RestController
@RequestMapping("/api/v1/auth")
public class ApUserRealnameController {

    @Autowired
    private ApUserRealnameService apUserRealnameService;

    /**
     * 开通自媒体账号, 该账号的用户名和密码与app一致
     * @Param []
     * @Return {@link ResponseResult}
     */
    @PostMapping("/authPass")
    public ResponseResult saveAccountPassword(@RequestBody Map<String, ApUser> apUserMap) {
        
        return apUserRealnameService.saveAccountPassword(apUserMap);
    }
}
