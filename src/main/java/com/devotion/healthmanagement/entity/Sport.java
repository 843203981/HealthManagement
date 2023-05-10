package com.devotion.healthmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Sport {
    @TableId(type = IdType.AUTO)
    private Integer sportId;
    private String sportName;
    private Integer cost;
    private String type;

}
