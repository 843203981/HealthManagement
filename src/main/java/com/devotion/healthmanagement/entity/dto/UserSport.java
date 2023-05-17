package com.devotion.healthmanagement.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_sport")
public class UserSport {
    private Integer Id;
    private Integer sportId;
    @TableField(exist = false)
    private Integer cost;
    @TableField(exist = false)
    private String sportName;
    @TableField(exist = false)
    private String type;
    private Integer time;
    private String date;
}
