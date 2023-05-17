package com.devotion.healthmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Illness {
    @TableId(type = IdType.AUTO)
    private Integer illId;
    private String illName;
    private String info;
    private String advice;
    private Float tempTop;
    private Float tempBtm;
    private Integer hrtrateTop;
    private Integer hrtrateBtm;
    private Integer highPressureTop;
    private Integer highPressureBtm;
    private Integer lowPressureTop;
    private Integer lowPressureBtm;
    private Float fatTop;
    private Float fatBtm;
    private Integer bldsugarTop;
    private Integer bldsugarBtm;

}

