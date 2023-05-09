package com.devotion.healthmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devotion.healthmanagement.config.ExecutorConfig;
import com.devotion.healthmanagement.entity.Illness;
import com.devotion.healthmanagement.entity.User;
import com.devotion.healthmanagement.entity.dto.UserIllness;
import com.devotion.healthmanagement.mapper.IllnessMapper;
import com.devotion.healthmanagement.service.AsyncService;
import com.devotion.healthmanagement.service.IllnessService;
import com.devotion.healthmanagement.utils.ExcelUtils;
import com.devotion.healthmanagement.utils.MatchUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class IllnessServiceImpl extends ServiceImpl<IllnessMapper, Illness>  implements IllnessService {

    @Autowired
    private IllnessMapper illnessMapper;

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private ExecutorConfig executorConfig;

    @Autowired
    private ExcelUtils excelUtils;

    @Autowired
    private MatchUtils matchUtils;

    @Override
    public List<UserIllness> getDiseaseByUserId(Integer id) {
        return illnessMapper.selectDiseaseByUserId(id);
    }


    @Override
    public boolean saveUserIllness(UserIllness userIllness) {
        return illnessMapper.insertUserIllness( matchUtils.matchIllIdByIllName(userIllness));
    }

    @Override
    public void asyncUpload(File file) throws org.apache.poi.openxml4j.exceptions.InvalidFormatException, IOException {
        List<UserIllness> userIllnesses = excelUtils.toList(file);
        int runSize =executorConfig.getCorePoolSize();//线程池核心线程数量
        int row = userIllnesses.size();
        int count = row/runSize+1;

        List<UserIllness> userIllnessList = null;//存放每个线程的执行数据
        int startIndex = 0;
        int endIndex = 0;

        //数据分割
        for (int i = 0; i < runSize; i++) {
            if((i+1)==runSize){
                startIndex = (i*count);
                endIndex = userIllnesses.size();
                userIllnessList = userIllnesses.subList(startIndex,endIndex);
            }else{
                startIndex = (i*count);
                endIndex = (i+1)*count;
                if( endIndex > userIllnesses.size()){
                    break;
                }
                userIllnessList= userIllnesses.subList(startIndex, endIndex);
            }
            asyncService.asyncUpload(userIllnessList);
        }
    }
}
