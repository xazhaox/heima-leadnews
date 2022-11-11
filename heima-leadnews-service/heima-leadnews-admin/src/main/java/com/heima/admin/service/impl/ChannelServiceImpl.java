package com.heima.admin.service.impl;

import com.heima.admin.service.ChannelService;
import com.heima.feign.wemedia.WemediaFeignClient;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.ChannelDto;
import com.heima.model.wemedia.pojos.WmChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName ChannelServiceImpl.java
 * @Author xazhao
 * @Create 2022.08.30
 * @UpdateUser
 * @UpdateDate 2022.08.30
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@Service
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    private WemediaFeignClient wemediaFeignClient;

    /**
     * 分页查询频道
     * @Param [channelDto]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult queryChannel(ChannelDto channelDto) {

        return wemediaFeignClient.queryChannel(channelDto);
    }

    /**
     * 保存频道
     * @Param [wmChannel]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult saveChannel(WmChannel wmChannel) {

        return wemediaFeignClient.saveChannel(wmChannel);
    }

    /**
     * 修改频道信息
     * @Param [wmChannel]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult updateChannel(WmChannel wmChannel) {

        return wemediaFeignClient.updateChannel(wmChannel);
    }

    /**
     * 删除频道
     *
     * @param id
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult deleteChannel(Integer id) {

        return wemediaFeignClient.deleteChannel(id);
    }
}
