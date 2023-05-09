package com.devotion.healthmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Food {
    @TableId(type = IdType.AUTO)
    Integer foodId;
    String foodName;
    String info;
    Integer carbons;
    Integer proteins;
    Integer fats;
    Integer heat;

}
