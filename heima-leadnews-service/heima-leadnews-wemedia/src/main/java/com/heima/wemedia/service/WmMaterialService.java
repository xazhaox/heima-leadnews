package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @ClassName WmMaterialService.java
 * @Author xazhao
 * @Create 2022.08.20
 * @Description
 */
public interface WmMaterialService extends IService<WmMaterial> {

    /**
     * 上传图片
     * @Param [multipartFile]
     * @Return {@link ResponseResult}
     */
    ResponseResult uploadImage(MultipartFile multipartFile) throws IOException;

    /**
     * 素材列表分页查询
     * @Param [wmMaterialDto]
     * @Return {@link ResponseResult}
     */
    ResponseResult pageList(WmMaterialDto wmMaterialDto);

    /**
     * 删除素材
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    ResponseResult deleteImage(Integer id);

    /**
     * 收藏素材
     * @Param []
     * @Return {@link ResponseResult}
     */
    ResponseResult collectionMaterial(Integer id);

    /**
     * 取消收藏素材
     * @Param []
     * @Return {@link ResponseResult}
     */
    ResponseResult cancelCollectionMaterial(Integer id);
}
