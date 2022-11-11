package com.heima.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.user.pojos.ApUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName UserMapper.java
 * @Author xazhao
 * @Create 2022.08.17
 * @Description
 */

@Mapper
public interface UserMapper extends BaseMapper<ApUser> {
}
