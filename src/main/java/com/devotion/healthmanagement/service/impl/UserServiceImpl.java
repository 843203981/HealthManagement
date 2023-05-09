package com.devotion.healthmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devotion.healthmanagement.entity.User;
import com.devotion.healthmanagement.mapper.UserMapper;
import com.devotion.healthmanagement.service.UserService;
import com.devotion.healthmanagement.utils.RSA;
import com.devotion.healthmanagement.utils.RSAUtils;
import com.devotion.healthmanagement.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    RSA rsa;

    @Override
    public User login(User user) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //获取用户名
        String unameLogin = user.getUname();
        //获取密码
        String passwordEncrypt = user.getPassword();

        //取加密密码，Redis中有从Redis取，没有从数据库取然后写入Redis
        User userCheck = (User) redisUtil.get(unameLogin);
        if (userCheck == null) {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("uname",user.getUname());
            userCheck = userMapper.selectOne(wrapper);
            log.info("数据库获取User信息");
            if(userCheck == null){
                userCheck.setId(0);
                userCheck.setUname("不存在该账号");
                return userCheck;
            }
        } else {
            log.info("Redis缓存获取User信息");
        }

        String passwordCheck = userCheck.getPassword();

        //Todo:加密密码
        // 将原文密码和密码进行对比，如果返回true，说明两者一样

        String privateKey = rsa.privateKey;
        // 获得RSA类型的私钥
        RSAPrivateKey rsaPrivateKey = RSAUtils.getPrivateKey(privateKey);
        // 使用私钥解密select_Password
        //String Decrypt_database = RSAUtils.privateDecrypt(select_Password, rsaPrivateKey);
        // 使用私钥解密经过前端加密用户输入的密文
        String passwordLogin = RSAUtils.privateDecrypt(passwordEncrypt, rsaPrivateKey);
        log.info("数据库密码:"+passwordCheck+",用户登录密码:"+passwordLogin);

        //如果能够比对成功，说明登录成功
        if (passwordCheck.equals(passwordLogin)) {
            redisUtil.set(unameLogin, userCheck, 1800);
            log.info("用户登录" + redisUtil.get(unameLogin));
        } else {
            userCheck.setId(0);
            userCheck.setUname("密码错误");
        }
        return userCheck;
    }

    @Override
    public Boolean updateUser(User user) {
        return userMapper.updateById(user) == 1;
    }

}
