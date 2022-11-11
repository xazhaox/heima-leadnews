package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.mapper.AdUserMapper;
import com.heima.admin.service.AdUserService;
import com.heima.model.admin.dtos.AdUserDto;
import com.heima.model.admin.pojos.AdUser;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.utils.common.AppJwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AdUserServiceImpl.java
 * @Author xazhao
 * @Create 2022.08.29
 * @UpdateUser
 * @UpdateDate 2022.08.29
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@Service
public class AdUserServiceImpl extends ServiceImpl<AdUserMapper, AdUser> implements AdUserService {

    @Autowired
    private AdUserService adUserService;

    /**
     * 登录
     * @Param []
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult userLogin(AdUserDto adUserDto) {
        // 判断用户名密码是否为空
        if (StringUtils.isBlank(adUserDto.getName()) || StringUtils.isBlank(adUserDto.getPassword())) {
            return ResponseResult.errorResult(
                    AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST, "用户名或密码为空");
        }
        LambdaUpdateWrapper<AdUser> queryWrapper = new LambdaUpdateWrapper<>();
        // 构建条件
        queryWrapper.eq(AdUser::getName, adUserDto.getName());
        // 查询用户名密码
        AdUser adUser = adUserService.getOne(queryWrapper);
        // 判断是否查询出的为空
        if (adUser == null) {
            return ResponseResult.errorResult(
                    AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST, "用户名不存在");
        }
        // 获取传回的密码
        String password = adUserDto.getPassword();
        // 获取数据库中的盐
        String adUserSalt = adUser.getSalt();
        // 进行md5加密
        password = DigestUtils.md5DigestAsHex((password + adUserSalt).getBytes());
        // 使用用户名查询能查到说明用户名正确, 比对密码是否正确
        if (!password.equals(adUser.getPassword())) {
            return ResponseResult.errorResult(
                    AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST, "密码错误");
        }
        // 以上条件都不满足, 可以登录, 封装返回数据
        Map<Object, Object> responseMap = new HashMap<>();
        // 根据id生成token
        String token = AppJwtUtil.getToken(adUser.getId().longValue());
        // 返回数据, 不需要返回密码和盐, 置为空
        adUser.setPassword("");
        adUser.setSalt("");
        // 封装返回数据
        responseMap.put("user", adUser);
        responseMap.put("token", token);
        // 返回数据
        return ResponseResult.okResult(responseMap);
    }
}
