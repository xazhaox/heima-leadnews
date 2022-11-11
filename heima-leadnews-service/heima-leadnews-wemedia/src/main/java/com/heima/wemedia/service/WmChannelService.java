package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.ChannelDto;
import com.heima.model.wemedia.pojos.WmChannel;

import java.util.List;

/**
 * @ClassName WmChannelService.java
 * @Author xazhao
 * @Create 2022.08.20
 * @Description
 */
public interface WmChannelService extends IService<WmChannel> {
    
    /**
     * 查询所有频道
     * @Param []
     * @Return {@link ResponseResult}
     */
    ResponseResult<List<WmChannel>> channels();

    /**
     * 分页查询频道
     * @Param [channelDto]
     * @Return {@link ResponseResult}
     */
    ResponseResult queryChannel(ChannelDto channelDto);

    /**
     * 保存频道
     * @Param [wmChannel]
     * @Return {@link ResponseResult}
     */
    ResponseResult saveChannel(WmChannel wmChannel);

    /**
     * 修改频道
     * @Param [wmChannel]
     * @Return {@link ResponseResult}
     */
    ResponseResult updateChannel(WmChannel wmChannel);

    /**
     * 删除频道
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    ResponseResult deleteChannel(Integer id);
}
