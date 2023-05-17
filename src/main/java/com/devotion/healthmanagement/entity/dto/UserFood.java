package com.devotion.healthmanagement.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_food")
public class UserFood{
    private Integer id;
    private Integer foodId;
    private String part;
    private String date;
    @TableField(exist = false)
    private String foodName;
    @TableField(exist = false)
    private Integer fats;
    @TableField(exist = false)
    private Integer proteins;
    @TableField(exist = false)
    private Integer carbons;
    @TableField(exist = false)
    private Integer heat;
}
