package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmSensitiveDto;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.wemedia.mapper.WmSensitiveMapper;
import com.heima.wemedia.service.WmSensitiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName WmSensitiveService.java
 * @Author xazhao
 * @Create 2022.08.23
 * @UpdateUser
 * @UpdateDate 2022.08.23
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@Service
public class WmSensitiveServiceImpl extends
        ServiceImpl<WmSensitiveMapper, WmSensitive> implements WmSensitiveService {

    @Autowired
    private WmSensitiveService wmSensitiveService;

    /**
     * 删除敏感词
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult sensitiveWordDeletion(Integer id) {
        // 判断id是否传回
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 根据id删除
        wmSensitiveService.removeById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 分页查询敏感词
     * @Param [wmSensitiveDto]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult querySensitiveWords(WmSensitiveDto wmSensitiveDto) {
        // 校验参数
        if (wmSensitiveDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        IPage<WmSensitive> pageInfo = new Page<>(wmSensitiveDto.getPage(), wmSensitiveDto.getSize());
        // 构建条件
        LambdaQueryWrapper<WmSensitive> queryWrapper = new LambdaQueryWrapper<>();
        // 判断是否传回name
        if (wmSensitiveDto.getName() != null && !wmSensitiveDto.getName().equals("")) {
            // 根据name查询
            queryWrapper.like(WmSensitive::getSensitives, wmSensitiveDto.getName());
        }
        // 按照创建时间倒序查询
        queryWrapper.orderByDesc(WmSensitive::getCreatedTime);
        // 查询
        wmSensitiveService.page(pageInfo, queryWrapper);
        // 获取数据
        List<WmSensitive> records = pageInfo.getRecords();
        // 封装返回数据
        PageResponseResult pageResponseResult = new PageResponseResult();
        pageResponseResult.setCurrentPage((int) pageInfo.getCurrent());
        pageResponseResult.setSize((int) pageInfo.getSize());
        pageResponseResult.setTotal((int) pageInfo.getTotal());
        pageResponseResult.setData(records);
        // 返回数据
        return pageResponseResult;
    }

    /**
     * 添加敏感词
     * @Param [wmSensitive]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult addAensitiveWords(WmSensitive wmSensitive) {
        // 校验参数
        if (wmSensitive == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 判断传回的sensitives是否为空
        if (wmSensitive.getSensitives() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "敏感词为空");
        }
        // 查询
        List<WmSensitive> wmSensitiveList = wmSensitiveService.list();
        // 遍历集合
        for (WmSensitive sensitive : wmSensitiveList) {
            // 判断敏感词是否已经存在
            if (wmSensitive.getSensitives().equals(sensitive.getSensitives())) {
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "数据已存在");
            }
        }
        // 设置时间
        wmSensitive.setCreatedTime(new Date());
        // 保存数据
        wmSensitiveService.save(wmSensitive);
        // 返回结果
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 修改敏感词
     * @Param []
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult modifySensitiveWords(WmSensitive wmSensitive) {
        // 校验参数
        if (wmSensitive == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 判断传回的sensitives是否为空
        if (wmSensitive.getSensitives() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "敏感词为空");
        }
        LambdaQueryWrapper<WmSensitive> queryWrapper = new LambdaQueryWrapper<>();
        // 查询条件
        queryWrapper.eq(WmSensitive::getSensitives, wmSensitive.getSensitives());
        // 查询
        WmSensitive sensitiveServiceOne = wmSensitiveService.getOne(queryWrapper);
        // 判断sensitiveServiceOne是否为空, 不为空表示敏感词存在
        if (sensitiveServiceOne != null && !Objects.equals(sensitiveServiceOne.getId(), wmSensitive.getId())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "数据已存在");
        }
        // 设置时间
        wmSensitive.setCreatedTime(new Date());
        // 保存数据
        wmSensitiveService.updateById(wmSensitive);
        // 返回结果
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
