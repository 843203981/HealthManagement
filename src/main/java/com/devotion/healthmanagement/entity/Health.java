package com.devotion.healthmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_health")
public class Health {
    private Integer id;
    private Float temperature;
    private Integer heartRate;
    private Integer highPressure;
    private Integer lowPressure;
    private Integer bloodGlucose;
    private Float bloodFat;
    private String updateTime;
}
