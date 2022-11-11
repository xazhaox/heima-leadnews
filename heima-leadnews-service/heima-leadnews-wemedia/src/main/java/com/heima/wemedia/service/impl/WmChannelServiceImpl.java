package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.PageRequestDto;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.ChannelDto;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.service.WmChannelService;
import com.heima.wemedia.service.WmNewsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.events.Event;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName WmChannelServiceImpl.java
 * @Author xazhao
 * @Create 2022.08.20
 * @Description
 */

@Slf4j
@Service
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel>
        implements WmChannelService {

    @Autowired
    private WmChannelService wmChannelService;

    @Autowired
    private WmNewsService wmNewsService;

    /**
     * 查询所有频道
     * @Param []
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult<List<WmChannel>> channels() {
        // 构建条件
        LambdaUpdateWrapper<WmChannel> queryWrapper = new LambdaUpdateWrapper<>();
        // 频道是否可用
        queryWrapper.eq(WmChannel::getStatus, 1);
        // 排序, 升序
        queryWrapper.orderByAsc(WmChannel::getOrd);
        // 查询
        List<WmChannel> channelList = this.list(queryWrapper);
        // 返回
        return ResponseResult.okResult(channelList);
    }

    /**
     * 分页查询频道
     * @Param [channelDto]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult queryChannel(ChannelDto channelDto) {
        // 校验数据
        if (channelDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 构建分页查询
        IPage<WmChannel> pageInfo = new Page<>(channelDto.getPage(), channelDto.getSize());
        // 构建条件
        LambdaQueryWrapper<WmChannel> queryWrapper = new LambdaQueryWrapper<>();
        // 按照创建时间倒序查询
        queryWrapper.orderByDesc(WmChannel::getCreatedTime);
        // 按照频道名称模糊查询
        if (channelDto.getName() != null && !channelDto.getName().equals("")) {
            queryWrapper.like(WmChannel::getName, channelDto.getName());
        }

        // TODO 可以按照状态进行精确查找(1: 启用true 0: 禁用false)

        // 分页查询
        IPage<WmChannel> channelIPage = wmChannelService.page(pageInfo, queryWrapper);
        // 获取数据
        List<WmChannel> records = channelIPage.getRecords();
        // 返回数据
        return ResponseResult.okResult(records);
    }

    /**
     * 保存频道
     * @Param [wmChannel]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult saveChannel(WmChannel wmChannel) {
        // 校验数据
        if (wmChannel == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 判断name是否为空
        if (StringUtils.isBlank(wmChannel.getName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "频道名称为空");
        }
        LambdaQueryWrapper<WmChannel> queryWrapper = new LambdaQueryWrapper<>();
        // 根据name查询
        queryWrapper.eq(WmChannel::getName, wmChannel.getName());
        // 查询
        WmChannel channelServiceOne = wmChannelService.getOne(queryWrapper);
        // 若channelServiceOne不为空, 表示name已存在
        if (channelServiceOne != null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "数据已存在");
        }
        // 补全数据
        wmChannel.setCreatedTime(new Date());
        wmChannel.setIsDefault(true);
        // 获取参数保存
        wmChannelService.save(wmChannel);
        // 返回数据
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 修改频道
     * @Param [wmChannel]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult updateChannel(WmChannel wmChannel) {
        // 校验数据
        if (wmChannel == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 判断name是否为空
        if (StringUtils.isBlank(wmChannel.getName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "频道名称为空");
        }

        // 根据id查询
        WmChannel wmChannelOne = wmChannelService.getById(wmChannel.getId());

        // 判断是否被引用
        LambdaQueryWrapper<WmNews> queryWrapper = new LambdaQueryWrapper<>();
        // 构建条件
        queryWrapper.eq(WmNews::getChannelId, wmChannel.getId());
        // 根据channel_id查询wm_news表
        List<WmNews> wmNews = wmNewsService.list(queryWrapper);
        // 判断频道是否已经被引用, wmNews为不空表示已被引用
        if (wmNews.size() > 0) {
            // 集合长度大于0, 表示已被引用不能被禁用
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "该频道已被引用");
        }

        // 点击的是启用或禁用
        if (!wmChannel.getStatus() == wmChannelOne.getStatus()) {
            // 修改启用, 禁用
            wmChannel.setStatus(wmChannel.getStatus());
            // 保存
            wmChannelService.updateById(wmChannel);
            // 返回数据
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }

        // 判断修改的频道名称是否已经存在
        LambdaQueryWrapper<WmChannel> channelQueryWrapper = new LambdaQueryWrapper<>();
        channelQueryWrapper.eq(WmChannel::getName, wmChannel.getName());
        // 未被引用, 判断修改的频道是否已经存在
        WmChannel channelServiceOne = wmChannelService.getOne(channelQueryWrapper);
        // 根据频道名称查出数据, 表示已经存在改频道
        if (channelServiceOne != null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "该频道已存在");
        }

        // 未被引用, 保存传回的数据
        wmChannelService.updateById(wmChannel);

        // 返回数据
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 删除频道
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult deleteChannel(Integer id) {
        // 校验数据
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 根据id查询频道
        WmChannel wmChannel = wmChannelService.getById(id);
        // 判断该字段是否被禁用
        if (wmChannel.getStatus()) {
            // true表示启用, 不能删除
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "该频道已被启用");
        }
        // 判断该频道是否已被引用
        LambdaQueryWrapper<WmNews> queryWrapper = new LambdaQueryWrapper<>();
        // 查询条件
        queryWrapper.eq(WmNews::getChannelId, id);
        // 根据频道id查询wm_news表
        List<WmNews> wmNewsList = wmNewsService.list(queryWrapper);
        // 集合不为空表示已被引用不能删除
        if (wmNewsList.size() > 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "该频道已被引用");
        }
        // 删除频道
        wmChannelService.removeById(id);
        // 返回数据
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
