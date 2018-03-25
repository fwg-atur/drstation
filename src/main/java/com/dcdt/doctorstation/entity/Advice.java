package com.dcdt.doctorstation.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiRong on 2017/7/2.
 */
public class Advice {
    private List<CheckInfo> checkInfoList;
    private String REPEAT;
    private String DRUG_LO_NAME;
    private String DRUG_LO_ID;
    private String drugLinked;    //药品说明书链接
    private String ADMINISTRATION;
    private String DOSAGE;
    private String DOSAGE_UNIT;
    private String FREQ_COUNT;
    private String FREQ_INTERVAL;
    private String FREQ_INTERVAL_UNIT;
    private String START_DAY;
    private String END_DAY;
    private String DEPT_CODE;
    private String DOCTOR_NAME;
    private String ORDER_NO;
    private String ORDER_SUB_NO;
    private String AUTHORITY_LEVELS;
    private String ALERT_LEVELS;
    private String TITLE;
    private String GROUP_ID;
    private String USER_ID;
    private String PRES_ID;
    private String PRES_DATE;
    private String PRES_SEQ_ID;
    private String PK_ORDER_NO;
    private String COURSE;
    private String PKG_COUNT;
    private String PKG_UNIT;
    private String BAK_01;
    private String BAK_02;
    private String BAK_03;
    private String BAK_04;
    private String BAK_05;

    public Advice(){

    }

    public Advice( String REPEAT, String DRUG_LO_NAME, String DRUG_LO_ID, String ADMINISTRATION, String DOSAGE, String DOSAGE_UNIT, String FREQ_COUNT, String FREQ_INTERVAL, String FREQ_INTERVAL_UNIT, String START_DAY, String END_DAY, String DEPT_CODE, String DOCTOR_NAME, String ORDER_NO, String ORDER_SUB_NO, String AUTHORITY_LEVELS, String ALERT_LEVELS, String TITLE, String GROUP_ID, String USER_ID, String PRES_ID, String PRES_DATE, String PRES_SEQ_ID, String PK_ORDER_NO, String COURSE, String PKG_COUNT, String PKG_UNIT, String BAK_01, String BAK_02, String BAK_03, String BAK_04, String BAK_05) {
        this.REPEAT = REPEAT;
        this.DRUG_LO_NAME = DRUG_LO_NAME;
        this.DRUG_LO_ID = DRUG_LO_ID;
        this.ADMINISTRATION = ADMINISTRATION;
        this.DOSAGE = DOSAGE;
        this.DOSAGE_UNIT = DOSAGE_UNIT;
        this.FREQ_COUNT = FREQ_COUNT;
        this.FREQ_INTERVAL = FREQ_INTERVAL;
        this.FREQ_INTERVAL_UNIT = FREQ_INTERVAL_UNIT;
        this.START_DAY = START_DAY;
        this.END_DAY = END_DAY;
        this.DEPT_CODE = DEPT_CODE;
        this.DOCTOR_NAME = DOCTOR_NAME;
        this.ORDER_NO = ORDER_NO;
        this.ORDER_SUB_NO = ORDER_SUB_NO;
        this.AUTHORITY_LEVELS = AUTHORITY_LEVELS;
        this.ALERT_LEVELS = ALERT_LEVELS;
        this.TITLE = TITLE;
        this.GROUP_ID = GROUP_ID;
        this.USER_ID = USER_ID;
        this.PRES_ID = PRES_ID;
        this.PRES_DATE = PRES_DATE;
        this.PRES_SEQ_ID = PRES_SEQ_ID;
        this.PK_ORDER_NO = PK_ORDER_NO;
        this.COURSE = COURSE;
        this.PKG_COUNT = PKG_COUNT;
        this.PKG_UNIT = PKG_UNIT;
        this.BAK_01 = BAK_01;
        this.BAK_02 = BAK_02;
        this.BAK_03 = BAK_03;
        this.BAK_04 = BAK_04;
        this.BAK_05 = BAK_05;
    }

    public Advice clone(){
        Advice ret = new Advice(REPEAT, DRUG_LO_NAME, DRUG_LO_ID, ADMINISTRATION, DOSAGE,
                DOSAGE_UNIT, FREQ_COUNT, FREQ_INTERVAL, FREQ_INTERVAL_UNIT, START_DAY,
                END_DAY, DEPT_CODE, DOCTOR_NAME, ORDER_NO, ORDER_SUB_NO,AUTHORITY_LEVELS,
                ALERT_LEVELS, TITLE, GROUP_ID, USER_ID, PRES_ID, PRES_DATE, PRES_SEQ_ID,
                PK_ORDER_NO, COURSE, PKG_COUNT, PKG_UNIT, BAK_01, BAK_02, BAK_03, BAK_04, BAK_05);
        ret.setCheckInfoList(new ArrayList<CheckInfo>());   //to-do
        return ret;
    }

    public List<CheckInfo> getCheckInfoList() {
        return checkInfoList;
    }

    public void setCheckInfoList(List<CheckInfo> checkInfoList) {
        this.checkInfoList = checkInfoList;
    }

    public String getREPEAT() {
        return REPEAT;
    }

    public void setREPEAT(String REPEAT) {
        this.REPEAT = REPEAT;
    }

    public String getDRUG_LO_NAME() {
        return DRUG_LO_NAME;
    }

    public void setDRUG_LO_NAME(String DRUG_LO_NAME) {
        this.DRUG_LO_NAME = DRUG_LO_NAME;
    }

    public String getDRUG_LO_ID() {
        return DRUG_LO_ID;
    }

    public void setDRUG_LO_ID(String DRUG_LO_ID) {
        this.DRUG_LO_ID = DRUG_LO_ID;
    }

    public String getADMINISTRATION() {
        return ADMINISTRATION;
    }

    public void setADMINISTRATION(String ADMINISTRATION) {
        this.ADMINISTRATION = ADMINISTRATION;
    }

    public String getDOSAGE() {
        return DOSAGE;
    }

    public void setDOSAGE(String DOSAGE) {
        this.DOSAGE = DOSAGE;
    }

    public String getDOSAGE_UNIT() {
        return DOSAGE_UNIT;
    }

    public void setDOSAGE_UNIT(String DOSAGE_UNIT) {
        this.DOSAGE_UNIT = DOSAGE_UNIT;
    }

    public String getFREQ_COUNT() {
        return FREQ_COUNT;
    }

    public void setFREQ_COUNT(String FREQ_COUNT) {
        this.FREQ_COUNT = FREQ_COUNT;
    }

    public String getFREQ_INTERVAL() {
        return FREQ_INTERVAL;
    }

    public void setFREQ_INTERVAL(String FREQ_INTERVAL) {
        this.FREQ_INTERVAL = FREQ_INTERVAL;
    }

    public String getFREQ_INTERVAL_UNIT() {
        return FREQ_INTERVAL_UNIT;
    }

    public void setFREQ_INTERVAL_UNIT(String FREQ_INTERVAL_UNIT) {
        this.FREQ_INTERVAL_UNIT = FREQ_INTERVAL_UNIT;
    }

    public String getSTART_DAY() {
        return START_DAY;
    }

    public void setSTART_DAY(String START_DAY) {
        this.START_DAY = START_DAY;
    }

    public String getEND_DAY() {
        return END_DAY;
    }

    public void setEND_DAY(String END_DAY) {
        this.END_DAY = END_DAY;
    }

    public String getDEPT_CODE() {
        return DEPT_CODE;
    }

    public void setDEPT_CODE(String DEPT_CODE) {
        this.DEPT_CODE = DEPT_CODE;
    }

    public String getDOCTOR_NAME() {
        return DOCTOR_NAME;
    }

    public void setDOCTOR_NAME(String DOCTOR_NAME) {
        this.DOCTOR_NAME = DOCTOR_NAME;
    }

    public String getORDER_NO() {
        return ORDER_NO;
    }

    public void setORDER_NO(String ORDER_NO) {
        this.ORDER_NO = ORDER_NO;
    }

    public String getORDER_SUB_NO() {
        return ORDER_SUB_NO;
    }

    public void setORDER_SUB_NO(String ORDER_SUB_NO) {
        this.ORDER_SUB_NO = ORDER_SUB_NO;
    }

    public String getAUTHORITY_LEVELS() {
        return AUTHORITY_LEVELS;
    }

    public void setAUTHORITY_LEVELS(String AUTHORITY_LEVELS) {
        this.AUTHORITY_LEVELS = AUTHORITY_LEVELS;
    }

    public String getALERT_LEVELS() {
        return ALERT_LEVELS;
    }

    public void setALERT_LEVELS(String ALERT_LEVELS) {
        this.ALERT_LEVELS = ALERT_LEVELS;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getGROUP_ID() {
        return GROUP_ID;
    }

    public void setGROUP_ID(String GROUP_ID) {
        this.GROUP_ID = GROUP_ID;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getPRES_ID() {
        return PRES_ID;
    }

    public void setPRES_ID(String PRES_ID) {
        this.PRES_ID = PRES_ID;
    }

    public String getPRES_DATE() {
        return PRES_DATE;
    }

    public void setPRES_DATE(String PRES_DATE) {
        this.PRES_DATE = PRES_DATE;
    }

    public String getPRES_SEQ_ID() {
        return PRES_SEQ_ID;
    }

    public void setPRES_SEQ_ID(String PRES_SEQ_ID) {
        this.PRES_SEQ_ID = PRES_SEQ_ID;
    }

    public String getPK_ORDER_NO() {
        return PK_ORDER_NO;
    }

    public void setPK_ORDER_NO(String PK_ORDER_NO) {
        this.PK_ORDER_NO = PK_ORDER_NO;
    }

    public String getCOURSE() {
        return COURSE;
    }

    public void setCOURSE(String COURSE) {
        this.COURSE = COURSE;
    }

    public String getPKG_COUNT() {
        return PKG_COUNT;
    }

    public void setPKG_COUNT(String PKG_COUNT) {
        this.PKG_COUNT = PKG_COUNT;
    }

    public String getPKG_UNIT() {
        return PKG_UNIT;
    }

    public void setPKG_UNIT(String PKG_UNIT) {
        this.PKG_UNIT = PKG_UNIT;
    }

    public String getBAK_01() {
        return BAK_01;
    }

    public void setBAK_01(String BAK_01) {
        this.BAK_01 = BAK_01;
    }

    public String getBAK_02() {
        return BAK_02;
    }

    public void setBAK_02(String BAK_02) {
        this.BAK_02 = BAK_02;
    }

    public String getBAK_03() {
        return BAK_03;
    }

    public void setBAK_03(String BAK_03) {
        this.BAK_03 = BAK_03;
    }

    public String getBAK_04() {
        return BAK_04;
    }

    public void setBAK_04(String BAK_04) {
        this.BAK_04 = BAK_04;
    }

    public String getBAK_05() {
        return BAK_05;
    }

    public void setBAK_05(String BAK_05) {
        this.BAK_05 = BAK_05;
    }

    public String getDrugLinked() {
        return drugLinked;
    }

    public void setDrugLinked(String drugLinked) {
        this.drugLinked = drugLinked;
    }
}
