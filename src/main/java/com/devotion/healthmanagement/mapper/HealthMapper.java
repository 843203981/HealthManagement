package com.devotion.healthmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.devotion.healthmanagement.entity.Health;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HealthMapper extends BaseMapper<Health> {
}
