package com.dcdt.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by LiRong on 2017/12/8.
 */
@Component
public class Config {
    @Value("${drStationServerIp}")
    private String drStationServerIp;

    @Value("${drStationServerPort}")
    private String drStationServerPort;

    @Value("${drugDescriptionURL}")
    private String drugDescriptionURL;

    @Value("${checkInterval}")
    private long checkInterval;

    @Value("${longestWaitTime}")
    private long longestWaitTime;

    @Value("${checkInterveneStateURL}")
    private String checkInterveneStateURL;

    @Value("${checkInterveneMessageURL}")
    private String checkInterveneMessageURL;

    @Value("${interveneFlag}")
    private int interveneFlag;

    @Value("${phoneNumber}")
    private String phoneNumber;

    @Value("${email}")
    private String email;

    @Value("${inHosFlag}")
    private int inHosFlag;

    @Value("${antiCheckInFlag}")
    private int antiCheckInFlag;

    @Value("${antiCheckInURL}")
    private String antiCheckInURL;

    @Value("${browserFlag}")
    private String browserFlag;

    @Value("${groupFlag}")
    private String groupFlag;

    @Value("${bz_flag}")
    private int bz_flag;

    @Value("${medicalRecordURL}")
    private String medicalRecordURL;


    public String getDrStationServerIp() {
        return drStationServerIp;
    }

    public String getDrStationServerPort() {
        return drStationServerPort;
    }

    public String getDrugDescriptionURL() {
        return drugDescriptionURL;
    }

    public long getCheckInterval() {
        return checkInterval;
    }

    public long getLongestWaitTime() {
        return longestWaitTime;
    }

    public String getCheckInterveneStateURL() {
        return checkInterveneStateURL;
    }

    public String getCheckInterveneMessageURL() {
        return checkInterveneMessageURL;
    }

    public int getInterveneFlag() {
        return interveneFlag;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public int getInHosFlag() {
        return inHosFlag;
    }

    public int getAntiCheckInFlag() {
        return antiCheckInFlag;
    }

    public String getAntiCheckInURL() {
        return antiCheckInURL;
    }

    public String getBrowserFlag() {
        return browserFlag;
    }

    public String getGroupFlag() {
        return groupFlag;
    }

    public void setGroupFlag(String groupFlag) {
        this.groupFlag = groupFlag;
    }

    public int getBz_flag() {
        return bz_flag;
    }

    public void setBz_flag(int bz_flag) {
        this.bz_flag = bz_flag;
    }

    public String getMedicalRecordURL() {
        return medicalRecordURL;
    }

    public void setMedicalRecordURL(String medicalRecordURL) {
        this.medicalRecordURL = medicalRecordURL;
    }
}
