package com.heima.feign.wemedia;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.pojos.ApUser;
import com.heima.model.wemedia.dtos.ChannelDto;
import com.heima.model.wemedia.dtos.NewsAuthDto;
import com.heima.model.wemedia.dtos.WmSensitiveDto;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.model.wemedia.pojos.WmSensitive;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName WemediaFeignClient.java
 * @Author xazhao
 * @Create 2022.08.29
 * @UpdateUser
 * @UpdateDate 2022.08.29
 * @Description
 * @Version 1.0.0
 */

// 服务提供方的名称
@FeignClient("leadnews-wemedia")
public interface WemediaFeignClient {

    // TODO 自媒体文章

    /**
     * 文章审核通过
     * @Param [newsAuthDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/api/v1/news/auth_pass")
    public ResponseResult articlePassedTheReview(@RequestBody NewsAuthDto newsAuthDto);

    /**
     * 驳回文章审核
     * @Param [newsAuthDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/api/v1/news/auth_fail")
    public ResponseResult rejectArticleReview(@RequestBody NewsAuthDto newsAuthDto);

    /**
     * 查看文章详情
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    @GetMapping("/api/v1/news/one_vo/{id}")
    public ResponseResult viewArticleDetails(@PathVariable Integer id);

    /**
     * 查看文章列表
     * @Param []
     * @Return {@link ResponseResult}
     */
    @PostMapping("/api/v1/news/list_vo")
    public ResponseResult viewArticleList(@RequestBody NewsAuthDto newsAuthDto);

    // TODO 频道

    /**
     * 查询所有频道
     * @Param []
     * @Return {@link ResponseResult}
     */
    @GetMapping("/api/v1/channel/channels")
    public ResponseResult<List<WmChannel>> channels();

    /**
     * 删除频道
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    @GetMapping("/api/v1/channel/del/{id}")
    public ResponseResult deleteChannel(@PathVariable Integer id);

    /**
     * 修改频道
     * @Param [wmChannel]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/api/v1/channel/update")
    public ResponseResult updateChannel(@RequestBody WmChannel wmChannel);

    /**
     * 保存频道
     * @Param [wmChannel]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/api/v1/channel/save")
    public ResponseResult saveChannel(@RequestBody WmChannel wmChannel);

    /**
     * 分页查询频道
     * @Param [channelDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/api/v1/channel/list")
    public ResponseResult queryChannel(@RequestBody ChannelDto channelDto);


    // TODO 敏感词

    /**
     * 添加敏感词
     * @Param [wmSensitiveDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/api/v1/sensitive/save")
    public ResponseResult addAensitiveWords(@RequestBody WmSensitive wmSensitive);

    /**
     * 修改敏感词
     * @Param []
     * @Return {@link ResponseResult}
     */
    @PostMapping("/api/v1/sensitive/update")
    public ResponseResult modifySensitiveWords(@RequestBody WmSensitive wmSensitive);

    /**
     * 删除敏感词
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    @DeleteMapping("/api/v1/sensitive/del/{id}")
    public ResponseResult sensitiveWordDeletion(@PathVariable Integer id);

    /**
     * 分页查询敏感词
     * @Param [wmSensitiveDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/api/v1/sensitive/list")
    public ResponseResult querySensitiveWords(@RequestBody WmSensitiveDto wmSensitiveDto);

    /**
     * 开通自媒体账号, 该账号的用户名和密码与app一致
     * @Param []
     * @Return {@link ResponseResult}
     */
    @PostMapping("/api/v1/auth/authPass")
    public ResponseResult saveAccountPassword(@RequestBody Map<String, ApUser> apUserMap);
}
