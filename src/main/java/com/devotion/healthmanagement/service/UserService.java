package com.devotion.healthmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.devotion.healthmanagement.entity.User;
import com.devotion.healthmanagement.entity.dto.UserIllness;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface UserService extends IService<User> {
    User login(User user) throws NoSuchAlgorithmException, InvalidKeySpecException;

    Boolean updateUser(User user);

}
