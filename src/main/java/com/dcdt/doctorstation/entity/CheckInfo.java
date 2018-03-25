package com.dcdt.doctorstation.entity;

/**
 * Created by LiRong on 2017/7/2.
 */
public class CheckInfo {
    private String COLOR;
    private String NAME;
    private String WARNING_LEVEL;
    private String WARNING_INFO;
    private String REF_SOURCE;
    private String YPMC;
    private String JSXX;
    private String ZYJL;
    private String TYSM;
    private String LCSY;
    private String REGULAR_WARNING_LEVEL;

    public CheckInfo(){

    }

    public CheckInfo(String COLOR, String NAME, String WARNING_LEVEL, String WARNING_INFO, String REF_SOURCE, String YPMC, String JSXX, String ZYJL, String TYSM, String LCSY, String REGULAR_WARNING_LEVEL) {
        this.COLOR = COLOR;
        this.NAME = NAME;
        this.WARNING_LEVEL = WARNING_LEVEL;
        this.WARNING_INFO = WARNING_INFO;
        this.REF_SOURCE = REF_SOURCE;
        this.YPMC = YPMC;
        this.JSXX = JSXX;
        this.ZYJL = ZYJL;
        this.TYSM = TYSM;
        this.LCSY = LCSY;
        this.REGULAR_WARNING_LEVEL = REGULAR_WARNING_LEVEL;
    }

    public CheckInfo clone(){
        CheckInfo ret = new CheckInfo(COLOR, NAME, WARNING_LEVEL, WARNING_INFO, REF_SOURCE,
                YPMC, JSXX, ZYJL, TYSM, LCSY, REGULAR_WARNING_LEVEL);
        return ret;
    }

    public String getCOLOR() {
        return COLOR;
    }

    public void setCOLOR(String COLOR) {
        this.COLOR = COLOR;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getWARNING_LEVEL() {
        return WARNING_LEVEL;
    }

    public void setWARNING_LEVEL(String WARNING_LEVEL) {
        this.WARNING_LEVEL = WARNING_LEVEL;
    }

    public String getWARNING_INFO() {
        return WARNING_INFO;
    }

    public void setWARNING_INFO(String WARNING_INFO) {
        this.WARNING_INFO = WARNING_INFO;
    }

    public String getREF_SOURCE() {
        return REF_SOURCE;
    }

    public void setREF_SOURCE(String REF_SOURCE) {
        this.REF_SOURCE = REF_SOURCE;
    }

    public String getYPMC() {
        return YPMC;
    }

    public void setYPMC(String YPMC) {
        this.YPMC = YPMC;
    }

    public String getJSXX() {
        return JSXX;
    }

    public void setJSXX(String JSXX) {
        this.JSXX = JSXX;
    }

    public String getZYJL() {
        return ZYJL;
    }

    public void setZYJL(String ZYJL) {
        this.ZYJL = ZYJL;
    }

    public String getTYSM() {
        return TYSM;
    }

    public void setTYSM(String TYSM) {
        this.TYSM = TYSM;
    }

    public String getLCSY() {
        return LCSY;
    }

    public void setLCSY(String LCSY) {
        this.LCSY = LCSY;
    }

    public String getREGULAR_WARNING_LEVEL() {
        return REGULAR_WARNING_LEVEL;
    }

    public void setREGULAR_WARNING_LEVEL(String REGULAR_WARNING_LEVEL) {
        this.REGULAR_WARNING_LEVEL = REGULAR_WARNING_LEVEL;
    }
}
