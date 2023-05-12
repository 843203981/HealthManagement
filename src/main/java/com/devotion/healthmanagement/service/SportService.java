package com.devotion.healthmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.devotion.healthmanagement.entity.Sport;
import com.devotion.healthmanagement.entity.dto.UserFood;
import com.devotion.healthmanagement.entity.dto.UserSport;

import java.util.List;

public interface SportService extends IService<Sport> {
    public List<UserSport> list(Integer id, String date);

    Sport match(String sportName);

    void saveUserSport(UserSport userSport);
}
