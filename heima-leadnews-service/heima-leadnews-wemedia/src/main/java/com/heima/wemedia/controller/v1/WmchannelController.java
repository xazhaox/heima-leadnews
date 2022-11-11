package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.ChannelDto;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.wemedia.service.WmChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName WmchannelController.java
 * @Author xazhao
 * @Create 2022.08.20
 * @Description
 */

@RestController
@RequestMapping("/api/v1/channel")
public class WmchannelController {

    @Autowired
    private WmChannelService wmChannelService;

    /**
     * 查询所有频道
     * @Param []
     * @Return {@link ResponseResult}
     */
    @GetMapping("/channels")
    public ResponseResult<List<WmChannel>> channels() {

        return wmChannelService.channels();
    }

    /**
     * 分页查询频道
     * @Param [channelDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/list")
    public ResponseResult queryChannel(@RequestBody ChannelDto channelDto) {

        return wmChannelService.queryChannel(channelDto);
    }

    /**
     * 保存频道
     * @Param [wmChannel]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/save")
    public ResponseResult saveChannel(@RequestBody WmChannel wmChannel) {

        return wmChannelService.saveChannel(wmChannel);
    }

    /**
     * 修改频道
     * @Param [wmChannel]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/update")
    public ResponseResult updateChannel(@RequestBody WmChannel wmChannel) {

        return wmChannelService.updateChannel(wmChannel);
    }

    /**
     * 删除频道
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    @GetMapping("/del/{id}")
    public ResponseResult deleteChannel(@PathVariable Integer id) {

        return wmChannelService.deleteChannel(id);
    }
}
