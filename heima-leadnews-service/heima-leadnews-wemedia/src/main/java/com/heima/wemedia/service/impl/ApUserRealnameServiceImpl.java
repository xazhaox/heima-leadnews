package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.pojos.ApUser;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.wemedia.service.ApUserRealnameService;
import com.heima.wemedia.service.WmUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName ApUserRealnameServiceImpl.java
 * @Author xazhao
 * @Create 2022.08.31
 * @UpdateUser
 * @UpdateDate 2022.08.31
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@Service
public class ApUserRealnameServiceImpl implements ApUserRealnameService {

    @Autowired
    private ApUserRealnameService apUserRealnameService;

    @Autowired
    private WmUserService wmUserService;

    /**
     * 开通自媒体账号, 该账号的用户名和密码与app一致
     * @Param []
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult saveAccountPassword(Map<String, ApUser> apUserMap) {
        // 获取map中的数据
        ApUser apUser = apUserMap.get("apUser");
        WmUser wmUser = new WmUser();
        // 设置用户名
        wmUser.setName(apUser.getName());
        // 设置密码
        wmUser.setPassword(apUser.getPassword());
        // 补全字段
        wmUser.setSalt(apUser.getSalt());
        wmUser.setApUserId(apUser.getId());
        // 设置未ap_author_id用户id
        wmUser.setApAuthorId(apUser.getId());
        wmUser.setNickname("小安");
        wmUser.setPhone(apUser.getPhone());
        wmUser.setStatus(1);
        wmUser.setLoginTime(new Date());
        wmUser.setCreatedTime(new Date());
        // 保存数据
        wmUserService.save(wmUser);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
