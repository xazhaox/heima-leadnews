package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.wemedia.service.WmMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @ClassName WmMaterialController.java
 * @Author xazhao
 * @Create 2022.08.20
 * @Description
 */

@RestController
@RequestMapping("/api/v1/material")
public class WmMaterialController {

    @Autowired
    private WmMaterialService wmMaterialService;

    /**
     * 上传图片
     * @Param [multipartFile]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/upload_picture")
    public ResponseResult uploadImage(MultipartFile multipartFile) throws IOException {

        return wmMaterialService.uploadImage(multipartFile);
    }

    /**
     * 素材列表分页查询
     * @Param [wmMaterialDto]
     * @Return {@link ResponseResult}
     */
    @PostMapping("/list")
    public ResponseResult pageList(@RequestBody WmMaterialDto wmMaterialDto) {
        return wmMaterialService.pageList(wmMaterialDto);
    }

    /**
     * 删除素材
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    @GetMapping("/del_picture/{id}")
    public ResponseResult deleteImage(@PathVariable Integer id) {

        return wmMaterialService.deleteImage(id);
    }

    /**
     * 收藏素材
     * @Param []
     * @Return {@link ResponseResult}
     */
    @GetMapping("/collect/{id}")
    public ResponseResult collectionMaterial(@PathVariable Integer id) {

        return wmMaterialService.collectionMaterial(id);
    }

    /**
     * 取消收藏素材
     * @Param []
     * @Return {@link ResponseResult}
     */
    @GetMapping("/cancel_collect/{id}")
    public ResponseResult cancelCollectionMaterial(@PathVariable Integer id) {

        return wmMaterialService.cancelCollectionMaterial(id);
    }
}
