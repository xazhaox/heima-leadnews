package com.heima.admin.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmSensitiveDto;
import com.heima.model.wemedia.pojos.WmSensitive;
import org.springframework.stereotype.Service;

/**
 * @ClassName SensitiveService.java
 * @Author xazhao
 * @Create 2022.08.29
 * @UpdateUser
 * @UpdateDate 2022.08.29
 * @Description
 * @Version 1.0.0
 */

public interface SensitiveService {

    /**
     * 添加敏感词
     * @Param [wmSensitiveDto]
     * @Return {@link ResponseResult}
     */
    public ResponseResult addAensitiveWords(WmSensitive wmSensitive);


    /**
     * 修改敏感词
     * @Param []
     * @Return {@link ResponseResult}
     */
    public ResponseResult modifySensitiveWords(WmSensitive wmSensitive);

    /**
     * 删除敏感词
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    public ResponseResult sensitiveWordDeletion(Integer id);

    /**
     * 分页查询敏感词
     * @Param [wmSensitiveDto]
     * @Return {@link ResponseResult}
     */
    public ResponseResult querySensitiveWords(WmSensitiveDto wmSensitiveDto);
}
