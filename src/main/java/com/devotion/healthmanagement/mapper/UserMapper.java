package com.devotion.healthmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.devotion.healthmanagement.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
