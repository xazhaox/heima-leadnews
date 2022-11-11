package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmSensitiveDto;
import com.heima.model.wemedia.pojos.WmSensitive;

/**
 * @ClassName WmSensitiveService.java
 * @Author xazhao
 * @Create 2022.08.23
 * @UpdateUser
 * @UpdateDate 2022.08.23
 * @Description
 * @Version 1.0.0
 */
public interface WmSensitiveService extends IService<WmSensitive> {

    /**
     * 删除敏感词
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    ResponseResult sensitiveWordDeletion(Integer id);

    /**
     * 分页查询敏感词
     * @Param [wmSensitiveDto]
     * @Return {@link ResponseResult}
     */
    ResponseResult querySensitiveWords(WmSensitiveDto wmSensitiveDto);

    /**
     * 添加敏感词
     * @Param [wmSensitive]
     * @Return {@link ResponseResult}
     */
    ResponseResult addAensitiveWords(WmSensitive wmSensitive);

    /**
     * 修改敏感词
     * @Param []
     * @Return {@link ResponseResult}
     */
    ResponseResult modifySensitiveWords(WmSensitive wmSensitive);
}
