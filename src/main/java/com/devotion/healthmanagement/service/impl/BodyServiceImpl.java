package com.devotion.healthmanagement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devotion.healthmanagement.entity.Body;
import com.devotion.healthmanagement.mapper.BodyMapper;
import com.devotion.healthmanagement.service.BodyService;
import org.springframework.stereotype.Service;

@Service
public class BodyServiceImpl extends ServiceImpl<BodyMapper, Body>  implements BodyService {
}
