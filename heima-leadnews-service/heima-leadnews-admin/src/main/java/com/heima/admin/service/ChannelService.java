package com.heima.admin.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.ChannelDto;
import com.heima.model.wemedia.pojos.WmChannel;

/**
 * @ClassName ChannelService.java
 * @Author xazhao
 * @Create 2022.08.30
 * @UpdateUser
 * @UpdateDate 2022.08.30
 * @Description
 * @Version 1.0.0
 */

public interface ChannelService {

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
     * 修改频道信息
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
