package com.devotion.healthmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class UserHealth {
    private Integer id;
    private Float temperature;
    private Integer heartRate;
    private Integer highPressure;
    private Integer lowPressure;
    private Integer bloodGlucose;
    private Integer fatPercentage;
    private String updateTime;
}
