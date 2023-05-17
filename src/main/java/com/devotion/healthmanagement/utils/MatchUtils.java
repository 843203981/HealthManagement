package com.devotion.healthmanagement.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.devotion.healthmanagement.entity.Health;
import com.devotion.healthmanagement.entity.Illness;
import com.devotion.healthmanagement.entity.dto.Msg;
import com.devotion.healthmanagement.entity.dto.UserIllness;
import com.devotion.healthmanagement.mapper.IllnessMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MatchUtils {

    @Autowired
    IllnessMapper illnessMapper;
    public Msg matchMsgByIllness (Illness illness){
            Msg msg = new Msg();
            msg.setName(illness.getIllName());
            msg.setHead("bg-warning");
            msg.setInfo(illness.getInfo());
            msg.setAdvice(illness.getAdvice());
            return msg;
    }

    public UserIllness matchIllIdByIllName(UserIllness userIllness){
        QueryWrapper<Illness> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ill_name", userIllness.getIllName());
        Illness illness = illnessMapper.selectOne(queryWrapper);
        if(illness == null){
            illness = new Illness();
            illness.setIllName(userIllness.getIllName());
            illnessMapper.insert(illness);
        }
        userIllness.setIllId(illnessMapper.selectOne(queryWrapper).getIllId());
        return userIllness;
    }

    public List<Msg> matchUserHealthByHealth(Health userHealth){
        List<Msg> msgs = new ArrayList<>();
        Illness health = illnessMapper.selectById(1);
        if(userHealth.getTemperature() == null){
            userHealth.setTemperature(36.5F);
        }
        if(userHealth.getHeartRate() == null){
            userHealth.setHeartRate(75);
        }
        if(userHealth.getHighPressure() == null){
            userHealth.setHighPressure(100);
        }
        if(userHealth.getLowPressure() == null){
            userHealth.setLowPressure(60);
        }
        if(userHealth.getBloodGlucose() == null){
            userHealth.setBloodGlucose(5);
        }
        if(userHealth.getBloodFat() == null){
            userHealth.setBloodFat(5F);
        }

        if(userHealth.getTemperature() > health.getTempTop()){
            illnessMapper.selectList(new QueryWrapper<Illness>().isNotNull("temp_top").ne("ill_id", 1)).forEach(illness -> {
                msgs.add(matchMsgByIllness(illness));
            });
            if(msgs.size() == 0){
                Msg msg = new Msg();
                msg.setName("体温偏高");
                msg.setHead("bg-warning");
                msg.setInfo("您的体温偏高");
                msg.setAdvice("请注意休息");
                msgs.add(msg);
            }
        }
        if(userHealth.getTemperature() < health.getTempBtm()){
            int num = msgs.size();
            illnessMapper.selectList(new QueryWrapper<Illness>().isNotNull("temp_btm").ne("ill_id", 1)).forEach(illness -> {
                msgs.add(matchMsgByIllness(illness));
            });
            if(msgs.size() == num){
                Msg msg = new Msg();
                msg.setName("体温偏低");
                msg.setHead("bg-warning");
                msg.setInfo("您的体温偏低");
                msg.setAdvice("请注意保暖");
                msgs.add(msg);
            }
        }
        if(userHealth.getHeartRate() > health.getHrtrateTop()){
            int num = msgs.size();
            illnessMapper.selectList(new QueryWrapper<Illness>().isNotNull("hrtrate_top").ne("ill_id", 1)).forEach(illness -> {
                msgs.add(matchMsgByIllness(illness));
            });
            if(msgs.size() == num){
                Msg msg = new Msg();
                msg.setName("心率偏高");
                msg.setHead("bg-warning");
                msg.setInfo("您的心率偏高");
                msg.setAdvice("请注意休息");
                msgs.add(msg);
            }
        }
        if(userHealth.getHeartRate() < health.getHrtrateBtm()){
            int num = msgs.size();
            illnessMapper.selectList(new QueryWrapper<Illness>().isNotNull("hrtrate_btm").ne("ill_id", 1)).forEach(illness -> {
                msgs.add(matchMsgByIllness(illness));
            });
            if(msgs.size() == num){
                Msg msg = new Msg();
                msg.setName("心率偏低");
                msg.setHead("bg-warning");
                msg.setInfo("您的心率偏低");
                msg.setAdvice("请注意休息");
                msgs.add(msg);
            }
        }
        if(userHealth.getHighPressure() > health.getHighPressureTop()){
            int num = msgs.size();
            illnessMapper.selectList(new QueryWrapper<Illness>().isNotNull("high_pressure_top").ne("ill_id", 1)).forEach(illness -> {
                msgs.add(matchMsgByIllness(illness));
            });
            if(msgs.size() == num){
                Msg msg = new Msg();
                msg.setName("血压(收缩压)偏高");
                msg.setHead("bg-warning");
                msg.setInfo("您的血压(收缩压)偏高");
                msg.setAdvice("请注意休息");
                msgs.add(msg);
            }
        }
        if(userHealth.getHighPressure() < health.getHighPressureBtm()){
            int num = msgs.size();
            illnessMapper.selectList(new QueryWrapper<Illness>().isNotNull("high_pressure_btm").ne("ill_id", 1)).forEach(illness -> {
                msgs.add(matchMsgByIllness(illness));
            });
            if(msgs.size() == num){
                Msg msg = new Msg();
                msg.setName("血压(收缩压)偏低");
                msg.setHead("bg-warning");
                msg.setInfo("您的血压(收缩压)偏低");
                msg.setAdvice("请注意休息");
                msgs.add(msg);
            }
        }
        if(userHealth.getLowPressure() > health.getLowPressureTop()){
            int num = msgs.size();
            illnessMapper.selectList(new QueryWrapper<Illness>().isNotNull("low_pressure_top").ne("ill_id", 1)).forEach(illness -> {
                msgs.add(matchMsgByIllness(illness));
            });
            if(msgs.size() == num){
                Msg msg = new Msg();
                msg.setName("血压(舒张压)偏高");
                msg.setHead("bg-warning");
                msg.setInfo("您的血压(舒张压)偏高");
                msg.setAdvice("请注意休息");
                msgs.add(msg);
            }
        }
        if(userHealth.getLowPressure() < health.getLowPressureBtm()){
            int num = msgs.size();
            illnessMapper.selectList(new QueryWrapper<Illness>().isNotNull("low_pressure_btm").ne("ill_id", 1)).forEach(illness -> {
                msgs.add(matchMsgByIllness(illness));
            });
            if(msgs.size() == num){
                Msg msg = new Msg();
                msg.setName("血压(舒张压)偏低");
                msg.setHead("bg-warning");
                msg.setInfo("您的血压(舒张压)偏低");
                msg.setAdvice("请注意休息");
                msgs.add(msg);
            }
        }
        if(userHealth.getBloodFat() > health.getFatTop()){
            int num = msgs.size();
            illnessMapper.selectList(new QueryWrapper<Illness>().isNotNull("fat_top").ne("ill_id", 1)).forEach(illness -> {
                msgs.add(matchMsgByIllness(illness));
            });
            if(msgs.size() == num){
                Msg msg = new Msg();
                msg.setName("血脂偏高");
                msg.setHead("bg-danger");
                msg.setInfo("您的血脂偏高");
                msg.setAdvice("请注意饮食");
                msgs.add(msg);
            }
        }
        if(userHealth.getBloodFat() < health.getFatBtm()){
            int num = msgs.size();
            illnessMapper.selectList(new QueryWrapper<Illness>().isNotNull("fat_btm").ne("ill_id", 1)).forEach(illness -> {
                msgs.add(matchMsgByIllness(illness));
            });
            if(msgs.size() == num){
                Msg msg = new Msg();
                msg.setName("血脂偏低");
                msg.setHead("bg-warning");
                msg.setInfo("您的血脂偏低");
                msg.setAdvice("请注意饮食");
                msgs.add(msg);
            }
        }

        if(userHealth.getBloodGlucose() > health.getBldsugarTop()){
            int num = msgs.size();
            illnessMapper.selectList(new QueryWrapper<Illness>().isNotNull("bldsugar_top").ne("ill_id", 1)).forEach(illness -> {
                msgs.add(matchMsgByIllness(illness));
            });
            if(msgs.size() == num){
                Msg msg = new Msg();
                msg.setName("血糖偏高");
                msg.setHead("bg-warning");
                msg.setInfo("您的血糖偏高");
                msg.setAdvice("请注意饮食");
                msgs.add(msg);
            }
        }
        if(userHealth.getBloodGlucose() < health.getBldsugarBtm()){
            int num = msgs.size();
            illnessMapper.selectList(new QueryWrapper<Illness>().isNotNull("bldsugar_btm").ne("ill_id", 1)).forEach(illness -> {
                msgs.add(matchMsgByIllness(illness));
            });
            if(msgs.size() == num){
                Msg msg = new Msg();
                msg.setName("血糖偏低");
                msg.setHead("bg-warning");
                msg.setInfo("您的血糖偏低");
                msg.setAdvice("请注意饮食");
                msgs.add(msg);
            }
        }
        if(msgs.size() == 0){
            Msg msg = new Msg();
            msg.setName("健康");
            msg.setHead("bg-success");
            msg.setInfo("您的健康状况良好");
            msg.setAdvice("请继续保持");
            msgs.add(msg);
        }
        return msgs;
    }
}
