package com.dcdt.doctorstation.entity;

/**
 * Created by LiRong on 2017/7/2.
 */
public class Doctor {
    private String USER_ID;
    private String NAME;
    private String POSITION;
    private String DEPT_CODE;
    private String FKNF;
    private String DEPT_NAME;

    public Doctor(String USER_ID, String NAME, String POSITION, String DEPT_CODE, String FKNF, String DEPT_NAME) {
        this.USER_ID = USER_ID;
        this.NAME = NAME;
        this.POSITION = POSITION;
        this.DEPT_CODE = DEPT_CODE;
        this.FKNF = FKNF;
        this.DEPT_NAME = DEPT_NAME;
    }


    protected Doctor clone(){
        Doctor ret = new Doctor(USER_ID, NAME, POSITION, DEPT_CODE, FKNF, DEPT_NAME);
        return ret;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getPOSITION() {
        return POSITION;
    }

    public void setPOSITION(String POSITION) {
        this.POSITION = POSITION;
    }

    public String getDEPT_CODE() {
        return DEPT_CODE;
    }

    public void setDEPT_CODE(String DEPT_CODE) {
        this.DEPT_CODE = DEPT_CODE;
    }

    public String getFKNF() {
        return FKNF;
    }

    public void setFKNF(String FKNF) {
        this.FKNF = FKNF;
    }

    public String getDEPT_NAME() {
        return DEPT_NAME;
    }

    public void setDEPT_NAME(String DEPT_NAME) {
        this.DEPT_NAME = DEPT_NAME;
    }
}
