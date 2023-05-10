package com.devotion.healthmanagement.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_sport")
public class UserSport {
    private Integer Id;
    private Integer sportId;
    private Integer cost;
    private String sportName;
    private String type;
    private Integer time;
    private String date;
}
