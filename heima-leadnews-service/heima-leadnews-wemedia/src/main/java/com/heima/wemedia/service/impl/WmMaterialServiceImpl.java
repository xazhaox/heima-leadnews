package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.file.service.FileStorageService;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.model.wemedia.pojos.WmNewsMaterial;
import com.heima.utils.common.ThreadLocalUtils;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.service.WmMaterialService;
import com.heima.wemedia.service.WmNewsMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName WmMaterialServiceImpi.java
 * @Author xazhao
 * @Create 2022.08.20
 * @Description
 */

@Slf4j
@Service
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial>
        implements WmMaterialService {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private WmNewsMaterialService wmNewsMaterialService;

    @Autowired
    private WmMaterialService wmMaterialService;

    /**
     * 上传图片
     * @Param [multipartFile]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult uploadImage(MultipartFile multipartFile) throws IOException {
        // 获取原始文件名
        String originalFilename = multipartFile.getOriginalFilename();
        // 利用工具类获取文件的后缀名
        String extension = FilenameUtils.getExtension(originalFilename);
        // 使用uuid生成文件名
        String imageName = UUID.randomUUID().toString() + "." + extension;
        // 上传文件保存到minio
        String imageUrl = fileStorageService
                .uploadImgFile("", imageName, multipartFile.getInputStream());
        // 文件信息保存到wm_material表
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUserId(ThreadLocalUtils.getUserId());
        wmMaterial.setUrl(imageUrl);
        wmMaterial.setCreatedTime(new Date());
        wmMaterial.setIsCollection((short) 0); // 是否收藏
        wmMaterial.setType((short) 0); // 素材的类型
        this.save(wmMaterial);
        return ResponseResult.okResult(wmMaterial);
    }

    /**
     * 素材列表分页查询
     * @Param [wmMaterialDto]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult pageList(WmMaterialDto wmMaterialDto) {
        // 构建分页查询器
        IPage<WmMaterial> pageInfo = new Page<>(wmMaterialDto.getPage(), wmMaterialDto.getSize());
        // 判断传回的页数和每页长度是否合法
        wmMaterialDto.checkParam();
        LambdaQueryWrapper<WmMaterial> queryWrapper = new LambdaQueryWrapper<>();
        // 查询当前用户的素材
        queryWrapper.eq(WmMaterial::getUserId, ThreadLocalUtils.getUserId());
        // 判断是否是已收藏的素材
        if (wmMaterialDto.getIsCollection() != null && wmMaterialDto.getIsCollection() == 1) {
            // 查询已收藏
            queryWrapper.eq(WmMaterial::getIsCollection, wmMaterialDto.getIsCollection());
        }
        // 排序, 降序
        queryWrapper.orderByDesc(WmMaterial::getCreatedTime);
        // 进行分页查询
        this.page(pageInfo, queryWrapper);
        // 封装返回参数
        PageResponseResult pageResponseResult = new PageResponseResult();
        pageResponseResult.setCurrentPage((int) pageInfo.getCurrent());
        pageResponseResult.setSize((int) pageInfo.getSize());
        pageResponseResult.setTotal((int) pageInfo.getTotal());
        pageResponseResult.setData(pageInfo.getRecords());
        return pageResponseResult;
    }

    /**
     * 删除素材
     * @Param [id]
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult deleteImage(Integer id) {
        // 已发布文章中包含的素材不能被删除, 操做wm_news_material
        LambdaUpdateWrapper<WmNewsMaterial> queryWrapper = new LambdaUpdateWrapper<>();
        // 查询条件
        queryWrapper.eq(WmNewsMaterial::getMaterialId, id);
        int count = wmNewsMaterialService.count(queryWrapper);
        // 判断count是否大于0
        if (count > 0) {
            return ResponseResult.errorResult(
                    AppHttpCodeEnum.PARAM_INVALID, "改素材已被使用不能删除");
        }
        // 查询url
        WmMaterial wmMaterial = wmMaterialService.getById(id);
        String imageUrl = wmMaterial.getUrl();
        // 删除minio中的图片数据
        fileStorageService.delete(imageUrl);
        // 删除wm_material表中的素材数据
        wmMaterialService.removeById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 收藏素材
     * @Param []
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult collectionMaterial(Integer id) {
        // 判断传回的id是否为空
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 根据id查询wm_material表
        WmMaterial wmMaterial = wmMaterialService.getById(id);
        // 将is_collection字段修改为1表示已收藏
        wmMaterial.setIsCollection((short) 1);
        // 修改数据库
        wmMaterialService.updateById(wmMaterial);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 取消收藏素材
     * @Param []
     * @Return {@link ResponseResult}
     */
    @Override
    public ResponseResult cancelCollectionMaterial(Integer id) {
        // 判断传回的id是否为空
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 根据id查询wm_material表
        WmMaterial wmMaterial = wmMaterialService.getById(id);
        // 将is_collection字段修改为1表示已收藏
        wmMaterial.setIsCollection((short) 0);
        // 修改数据库
        wmMaterialService.updateById(wmMaterial);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
