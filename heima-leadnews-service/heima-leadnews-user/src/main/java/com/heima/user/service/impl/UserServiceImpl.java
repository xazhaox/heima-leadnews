package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.LoginDto;
import com.heima.model.user.pojos.ApUser;
import com.heima.user.mapper.UserMapper;
import com.heima.user.service.UserService;
import com.heima.utils.common.AppJwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName UserServiceImpl.java
 * @Author xazhao
 * @Create 2022.08.17
 * @Description
 */

@Slf4j
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, ApUser> implements UserService {

    /**
     * APP端用户登录接口
     * @Param [loginDto]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult userLogin(LoginDto loginDto) {
        // 判断传回的数据是否为空, isBlank判断为空或者空字符串
        if (!StringUtils.isBlank(loginDto.getPhone()) && !StringUtils.isBlank(loginDto.getPassword())) {
            // 传回的数据不为空, 构建查询条件
            LambdaUpdateWrapper<ApUser> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            // 查询条件
            lambdaUpdateWrapper.eq(ApUser::getPhone, loginDto.getPhone());
            // 根据传回的手机号查询数据库该条数据, 查到数据手机号一定正确
            ApUser user = this.getOne(lambdaUpdateWrapper);
            // 判断查询出的数据是否为空
            if (user == null) {
                // 用户不存在, 登录失败
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,
                        "该用户不存在");
            }
            // 获取传回的密码
            String password = loginDto.getPassword();
            // 获取数据库中的盐
            String userSalt = user.getSalt();
            // 进行md5加密
            password = DigestUtils.md5DigestAsHex((password + userSalt).getBytes());
            // 比对加密后的密码和数据库中的密码是否一致
            if (!(password).equals(user.getPassword())) {
                // 不一致, 登录失败
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR,
                        "密码错误");
            }
            // 以上条件都不满足可以登录成功, 封装返回数据
            Map<Object, Object> responseMap = new HashMap<>();
            // 根据id生成token
            String token = AppJwtUtil.getToken(user.getId().longValue());
            // 返回数据需要返回盐和密码, 置为空
            user.setSalt("");
            user.setPassword("");
            // 封装数据返回
            responseMap.put("token", token);
            log.info("user : {}", user);
            responseMap.put("user", user);
            // 返回数据
            return ResponseResult.okResult(responseMap);
        } else {
            // 传回的数据为空, 游客登录
            Map<Object, Object> responseMap = new HashMap<>();
            // 将token设置为0
            String touristsToken = AppJwtUtil.getToken(0L);
            // 封装数据
            responseMap.put("token", touristsToken);
            return ResponseResult.okResult(responseMap);
        }
    }
}
