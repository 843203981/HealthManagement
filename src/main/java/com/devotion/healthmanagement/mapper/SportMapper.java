package com.devotion.healthmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.devotion.healthmanagement.entity.Sport;
import com.devotion.healthmanagement.entity.dto.UserSport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SportMapper extends BaseMapper<Sport>{
    @Select("select user_sport.id,sport.cost,sport.sport_name,sport.type,time,date from user_sport,sport where user_sport.sport_id=sport.sport_id and id=#{id} and date=#{date}")
    List<UserSport> selectUserSportList(Integer id, String date);

}
