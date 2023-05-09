package com.devotion.healthmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.devotion.healthmanagement.entity.UserBody;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BodyMapper extends BaseMapper<UserBody> {
}
