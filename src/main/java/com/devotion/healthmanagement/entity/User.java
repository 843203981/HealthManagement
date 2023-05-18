package com.devotion.healthmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    @TableId
    private Integer id;
    private String uname;
    private String name;
    private String password;
    private Integer admin;
}
