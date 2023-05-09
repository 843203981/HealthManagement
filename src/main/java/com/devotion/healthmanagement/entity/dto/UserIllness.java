package com.devotion.healthmanagement.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_illness")
public class UserIllness {
    private Integer id;
    private Integer illId;
    private String illRx;
    private String updateTime;

    @TableField(exist = false)
    private String uname;
    @TableField(exist = false)
    private String name;
    @TableField(exist = false)
    private String illName;
}
