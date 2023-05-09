package com.devotion.healthmanagement.entity;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class UserBody {
    private Integer id;
    private Integer weight;
    private Integer height;
    private Integer bmi;
    private String updateTime;
    private Integer heatCount;
    private Integer fatCount;
    private Integer proteinCount;
    private Integer carbonCount;
}
