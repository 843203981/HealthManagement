package com.devotion.healthmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.devotion.healthmanagement.entity.Body;

public interface BodyService extends IService<Body> {
    boolean saveUpdate(Body userBody);
}
