package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.feign.wemedia.WemediaFeignClient;
import com.heima.model.admin.pojos.ApUserRealname;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.UserAuthDto;
import com.heima.model.user.pojos.ApUser;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.user.mapper.UserRealnameMapper;
import com.heima.user.service.UserRealnameService;
import com.heima.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserRealnameServiceImpl.java
 * @Author xazhao
 * @Create 2022.08.31
 * @UpdateUser
 * @UpdateDate 2022.08.31
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@Service
public class UserRealnameServiceImpl extends
        ServiceImpl<UserRealnameMapper, ApUserRealname> implements UserRealnameService {

    @Autowired
    private UserRealnameService userRealnameService;

    @Autowired
    private UserService userService;

    @Autowired
    private WemediaFeignClient wemediaFeignClient;

    /**
     * 用户列表查询
     * @Param [userAuthDto]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult userListQuery(UserAuthDto userAuthDto) {
        // 校验参数
        if (userAuthDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 分页构建器
        IPage<ApUserRealname> pageInfo = new Page<>(userAuthDto.getPage(), userAuthDto.getSize());
        // 构建查询条件
        LambdaQueryWrapper<ApUserRealname> queryWrapper = new LambdaQueryWrapper<>();
        // 创建时间, 降序
        queryWrapper.orderByDesc(ApUserRealname::getCreatedTime);
        // 判断前端是否传回状态
        if (userAuthDto.getStatus() != null) {
            // 添加根据状态查询
            queryWrapper.eq(ApUserRealname::getStatus, userAuthDto.getStatus());
        }
        // 分页查询
        userRealnameService.page(pageInfo, queryWrapper);
        // 获取数据
        List<ApUserRealname> apUserRealnameRecords = pageInfo.getRecords();
        // 返回数据
        return ResponseResult.okResult(apUserRealnameRecords);
    }

    /**
     * 驳回用户审核
     * @Param [userAuthDto]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult rejectUserReview(UserAuthDto userAuthDto) {
        // 校验参数
        if (userAuthDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 校验是否输入驳回理由
        if (StringUtils.isBlank(userAuthDto.getMsg())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "请补充驳回理由");
        }
        // 根据id查询
        ApUserRealname apUserRealname = userRealnameService.getById(userAuthDto.getId());
        // 驳回审核, 将状态修改为2
        apUserRealname.setStatus((short) 2);
        // 设置驳回信息
        apUserRealname.setReason(userAuthDto.getMsg());
        // 修改数据库
        userRealnameService.updateById(apUserRealname);
        // 返回数据
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 用户审核通过
     * @Param [userAuthDto]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult userApproved(UserAuthDto userAuthDto) {
        // 校验参数
        if (userAuthDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        // 根据id查询
        ApUserRealname apUserRealname = userRealnameService.getById(userAuthDto.getId());
        // 修改状态
        apUserRealname.setStatus(userAuthDto.getStatus().shortValue());
        apUserRealname.setReason("审核通过");
        // 修改数据库
        userRealnameService.updateById(apUserRealname);

        // TODO 用户通过审核后需要开通自媒体账号(该账号的用户名和密码与app一致)
        LambdaQueryWrapper<ApUser> queryWrapper = new LambdaQueryWrapper<>();
        // 根据name查询
        queryWrapper.eq(ApUser::getName, apUserRealname.getName());
        // 创建map存放ApUser
        Map<String, ApUser> apUserMap = new HashMap<>();
        // 查询ap_user表
        ApUser apUser = userService.getOne(queryWrapper);
        // 存入map
        apUserMap.put("apUser", apUser);

        // 调用feign
        wemediaFeignClient.saveAccountPassword(apUserMap);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
