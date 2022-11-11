package com.heima.admin.service.impl;

import com.heima.admin.service.SensitiveService;
import com.heima.feign.wemedia.WemediaFeignClient;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmSensitiveDto;
import com.heima.model.wemedia.pojos.WmSensitive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName SensitiveServiceImpl.java
 * @Author xazhao
 * @Create 2022.08.29
 * @UpdateUser
 * @UpdateDate 2022.08.29
 * @Description
 * @Version 1.0.0
 */

@Slf4j
@Service
public class SensitiveServiceImpl implements SensitiveService {

    @Autowired
    private WemediaFeignClient wemediaFeignClient;

    /**
     * 添加敏感词
     *
     * @param wmSensitive
     * @Param [wmSensitiveDto]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult addAensitiveWords(WmSensitive wmSensitive) {

        return wemediaFeignClient.addAensitiveWords(wmSensitive);
    }

    /**
     * 修改敏感词
     *
     * @param wmSensitive
     * @Param []
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult modifySensitiveWords(WmSensitive wmSensitive) {

        return wemediaFeignClient.modifySensitiveWords(wmSensitive);
    }

    /**
     * 删除敏感词
     *
     * @param id
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult sensitiveWordDeletion(Integer id) {

        return wemediaFeignClient.sensitiveWordDeletion(id);
    }

    /**
     * 分页查询敏感词
     *
     * @param wmSensitiveDto
     * @Param [wmSensitiveDto]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult querySensitiveWords(WmSensitiveDto wmSensitiveDto) {

        return wemediaFeignClient.querySensitiveWords(wmSensitiveDto);
    }
}
