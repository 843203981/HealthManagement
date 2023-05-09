package com.devotion.healthmanagement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.devotion.healthmanagement.entity.User;
import com.devotion.healthmanagement.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MybatisPlusTest {
    @Autowired
    UserMapper userMapper;
    @Test
    public User userLogin() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("uname","qianyujie").eq("password","123456");
        User userResult = userMapper.selectOne(wrapper);
        System.out.println(userResult);
        return userResult;
    }
}
