package com.dcdt.doctorstation.entity;

/**
 * Created by LiRong on 2017/7/2.
 */
public class Diagnosis {
    private String DIAGNOSISES;

    public Diagnosis(String DIAGNOSISES) {
        this.DIAGNOSISES = DIAGNOSISES;
    }

    public Diagnosis clone(){
        return new Diagnosis(DIAGNOSISES);
    }

    public String getDIAGNOSISES() {
        return DIAGNOSISES;
    }

    public void setDIAGNOSISES(String DIAGNOSISES) {
        this.DIAGNOSISES = DIAGNOSISES;
    }
}
