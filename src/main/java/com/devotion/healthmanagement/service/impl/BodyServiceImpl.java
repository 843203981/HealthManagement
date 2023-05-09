package com.devotion.healthmanagement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devotion.healthmanagement.entity.UserBody;
import com.devotion.healthmanagement.entity.UserHealth;
import com.devotion.healthmanagement.mapper.BodyMapper;
import com.devotion.healthmanagement.mapper.HealthMapper;
import com.devotion.healthmanagement.service.BodyService;
import org.springframework.stereotype.Service;

@Service
public class BodyServiceImpl extends ServiceImpl<BodyMapper, UserBody>  implements BodyService {
}
