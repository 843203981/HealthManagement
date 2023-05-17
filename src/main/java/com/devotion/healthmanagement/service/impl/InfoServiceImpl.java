package com.devotion.healthmanagement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devotion.healthmanagement.entity.Info;
import com.devotion.healthmanagement.mapper.InfoMapper;
import com.devotion.healthmanagement.service.InfoService;
import org.springframework.stereotype.Service;

@Service
public class InfoServiceImpl extends ServiceImpl<InfoMapper, Info> implements InfoService {
}
