package com.devotion.healthmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_body")
public class Body {
    private Integer id;
    private Integer weight;
    private Integer height;
    private Integer bmi;
    private Integer age;
    private String sex;
    private String updateTime;
    private Integer heatCount;
    private Integer fatCount;
    private Integer proteinCount;
    private Integer carbonCount;
    private Float intensity;
}
