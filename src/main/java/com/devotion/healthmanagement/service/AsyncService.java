package com.devotion.healthmanagement.service;

import com.devotion.healthmanagement.entity.dto.UserIllness;

import java.util.List;

public interface AsyncService {
    public void asyncUpload(List<UserIllness> userIllnessList);
}
