package com.devotion.healthmanagement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devotion.healthmanagement.entity.UserHealth;
import com.devotion.healthmanagement.mapper.HealthMapper;
import com.devotion.healthmanagement.service.HealthService;
import org.springframework.stereotype.Service;

@Service
public class HealthServiceImpl extends ServiceImpl<HealthMapper, UserHealth> implements HealthService {
}