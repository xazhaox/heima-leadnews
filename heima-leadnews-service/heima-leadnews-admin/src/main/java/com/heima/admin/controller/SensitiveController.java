package com.heima.admin.controller;

import com.heima.admin.service.SensitiveService;
import com.heima.feign.wemedia.WemediaFeignClient;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmSensitiveDto;
import com.heima.model.wemedia.pojos.WmSensitive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName SensitiveController.java
 * @Author xazhao
 * @Create 2022.08.29
 * @UpdateUser
 * @UpdateDate 2022.08.29
 * @Description
 * @Version 1.0.0
 */

@RestController
@RequestMapping("/api/v1/sensitive")
public class SensitiveController {

    @Autowired
    private SensitiveService sensitiveService;

    /**
     * 添加敏感词
     * @Param [wmSensitiveDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/save")
    public ResponseResult addAensitiveWords(@RequestBody WmSensitive wmSensitive){

        return sensitiveService.addAensitiveWords(wmSensitive);
    }

    /**
     * 修改敏感词
     * @Param []
     * @Return {@link ResponseResult}
     */
    @PostMapping("/update")
    public ResponseResult modifySensitiveWords(@RequestBody WmSensitive wmSensitive){

        return sensitiveService.modifySensitiveWords(wmSensitive);
    }

    /**
     * 删除敏感词
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    @DeleteMapping("/del/{id}")
    public ResponseResult sensitiveWordDeletion(@PathVariable Integer id){

        return sensitiveService.sensitiveWordDeletion(id);
    }

    /**
     * 分页查询敏感词
     * @Param [wmSensitiveDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/list")
    public ResponseResult querySensitiveWords(@RequestBody WmSensitiveDto wmSensitiveDto) {

        return sensitiveService.querySensitiveWords(wmSensitiveDto);
    }
}
