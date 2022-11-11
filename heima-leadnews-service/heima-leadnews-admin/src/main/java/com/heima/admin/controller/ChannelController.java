package com.heima.admin.controller;

import com.heima.admin.service.ChannelService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.ChannelDto;
import com.heima.model.wemedia.pojos.WmChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName ChannelController.java
 * @Author xazhao
 * @Create 2022.08.30
 * @UpdateUser
 * @UpdateDate 2022.08.30
 * @Description
 * @Version 1.0.0
 */

@RestController
@RequestMapping("/api/v1/channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    /**
     * 分页查询频道
     * @Param [channelDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/list")
    public ResponseResult queryChannel(@RequestBody ChannelDto channelDto) {

        return channelService.queryChannel(channelDto);
    }

    /**
     * 保存频道
     * @Param [wmChannel]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/save")
    public ResponseResult saveChannel(@RequestBody WmChannel wmChannel) {

        return channelService.saveChannel(wmChannel);
    }

    /**
     * 修改频道
     * @Param [wmChannel]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/api/v1/channel/update")
    public ResponseResult updateChannel(@RequestBody WmChannel wmChannel) {

        return channelService.updateChannel(wmChannel);
    }

    /**
     * 删除频道
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    @GetMapping("/api/v1/channel/del/{id}")
    public ResponseResult deleteChannel(@PathVariable Integer id) {

        return channelService.deleteChannel(id);
    }
}
