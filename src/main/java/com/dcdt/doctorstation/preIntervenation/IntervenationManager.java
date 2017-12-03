package com.dcdt.doctorstation.preIntervenation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LiRong on 2017/12/1.
 */
@Component
public class IntervenationManager {
    private Map<String, IntervenedDoctor> doctors;

    @Value("${longestWaitTime}")
    private long longestWaitTime;

    @Value("${queryInterval}")
    private long queryInterval;


    public IntervenationManager() {
        this.doctors = new HashMap<String, IntervenedDoctor>();
    }

    /**
     * 点击下一步，向干预管理器注册一个医生
     *
     * @param doctor
     */
    public void registerDoctor(IntervenedDoctor doctor) {
        doctors.put(doctor.getId(), doctor);
    }

    public IntervenedDoctor findDoctorById(String id) {
        return doctors.get(id);
    }

    /**
     * 检查医生/处方状态
     * 如果超时了，则修改状态，并删除该医生
     * 返回干预状态
     *
     * @param id
     * @return
     */
    public int checkDoctorState(String id) {
        IntervenedDoctor doctor = findDoctorById(id);

        if (isOutTime(doctor)) {
            doctor.setState(IntervenationState.OUT_TIME);
            handleOutTimeDoctor(doctor);
        }

        return doctor.getState();
    }

    private IntervenedDoctor handleOutTimeDoctor(IntervenedDoctor doctor) {
        return doctors.remove(doctor.getId());
    }

    private boolean isOutTime(IntervenedDoctor doctor) {
        return (System.currentTimeMillis() - doctor.getSubmitTime()) >= longestWaitTime;
    }

    /**
     * 改变医生/处方状态
     *
     * @param id
     * @param stateCode
     */
    public void changeDoctorState(String id, int stateCode) {
        IntervenedDoctor doctor = findDoctorById(id);
        doctor.setState(stateCode);
    }
}
