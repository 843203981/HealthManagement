package com.devotion.healthmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.devotion.healthmanagement.entity.Illness;
import com.devotion.healthmanagement.entity.dto.UserIllness;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IllnessMapper extends BaseMapper<Illness> {

    @Select("select * from user_illness where id = #{userId} order by update_time desc")
    List<UserIllness>  selectDiseaseByUserId(Integer userId);

    @Insert("insert into user_illness (id, ill_id, ill_rx, update_time) values (#{id}, #{illId}, #{illRx}, #{updateTime})")
    boolean insertUserIllness(UserIllness userIllness);

    @Delete("delete from user_illness where id = #{id} and ill_id = #{illId}")
    boolean deleteUserIllness(UserIllness userIllness);

}
