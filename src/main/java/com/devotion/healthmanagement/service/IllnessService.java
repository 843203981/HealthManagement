package com.devotion.healthmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.devotion.healthmanagement.entity.Illness;
import com.devotion.healthmanagement.entity.dto.UserIllness;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface IllnessService extends IService<Illness> {
    public List<UserIllness> getDiseaseByUserId(Integer id);
    boolean saveUserIllness(UserIllness userIllness);

    void asyncUpload(File file) throws InvalidFormatException, IOException;
}


