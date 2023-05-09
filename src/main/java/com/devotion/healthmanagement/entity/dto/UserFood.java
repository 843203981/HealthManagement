package com.devotion.healthmanagement.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_food")
public class UserFood{
    private Integer id;
    private Integer foodId;
    private String part;
    private String date;
    private String foodName;
    private Integer fats;
    private Integer proteins;
    private Integer carbons;
    private Integer heat;
}
