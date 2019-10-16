package com.dcdt.doctorstation.entity;


import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.List;

/**
 * Created by Yikmat on 2019/10/09.
 */
public class Order {


    private String BED_NO;
    private String PATIENT_ID;
    private String VISIT_ID;
    private String PATIENT_NAME;
    private String DOCTOR_ID;
    private String DOCTOR_NAME;
    private String DRUG_NAME;
    private String DRUG_SPEC;
    private String ORDER_TYPE;
    private String START_TIME;
    private String ADMINISTRATION;
    private String FREQUENCY;
    private String DOSAGE;
    private String USE_TIME;
    private String PROBLEM_TYPE;
    private String PROBLEM_LEVEL;
    private String DRUG_STATE;
    private String GROUP_ID;
    private String INPATIENT_NO;


    protected PrescInfo prescInfo;



    public PrescInfo getPrescInfo() {
        return prescInfo;
    }

    public void setPrescInfo(PrescInfo prescInfo) {
        this.prescInfo = prescInfo;
    }

    public String getBED_NO() {
        return BED_NO;
    }

    public void setBED_NO(String BED_NO) {
        this.BED_NO = BED_NO;
    }

    public String getPATIENT_ID() {
        return PATIENT_ID;
    }

    public void setPATIENT_ID(String PATIENT_ID) {
        this.PATIENT_ID = PATIENT_ID;
    }

    public String getVISIT_ID() {
        return VISIT_ID;
    }

    public void setVISIT_ID(String VISIT_ID) {
        this.VISIT_ID = VISIT_ID;
    }

    public String getPATIENT_NAME() {
        return PATIENT_NAME;
    }

    public void setPATIENT_NAME(String PATIENT_NAME) {
        this.PATIENT_NAME = PATIENT_NAME;
    }

    public String getDOCTOR_ID() {
        return DOCTOR_ID;
    }

    public void setDOCTOR_ID(String DOCTOR_ID) {
        this.DOCTOR_ID = DOCTOR_ID;
    }

    public String getDOCTOR_NAME() {
        return DOCTOR_NAME;
    }

    public void setDOCTOR_NAME(String DOCTOR_NAME) {
        this.DOCTOR_NAME = DOCTOR_NAME;
    }

    public String getDRUG_NAME() {
        return DRUG_NAME;
    }

    public void setDRUG_NAME(String DRUG_NAME) {
        this.DRUG_NAME = DRUG_NAME;
    }

    public String getDRUG_SPEC() {
        return DRUG_SPEC;
    }

    public void setDRUG_SPEC(String DRUG_SPEC) {
        this.DRUG_SPEC = DRUG_SPEC;
    }

    public String getORDER_TYPE() {
        return ORDER_TYPE;
    }

    public void setORDER_TYPE(String ORDER_TYPE) {
        this.ORDER_TYPE = ORDER_TYPE;
    }

    public String getSTART_TIME() {
        return START_TIME;
    }

    public void setSTART_TIME(String START_TIME) {
        this.START_TIME = START_TIME;
    }

    public String getADMINISTRATION() {
        return ADMINISTRATION;
    }

    public void setADMINISTRATION(String ADMINISTRATION) {
        this.ADMINISTRATION = ADMINISTRATION;
    }

    public String getFREQUENCY() {
        return FREQUENCY;
    }

    public void setFREQUENCY(String FREQUENCY) {
        this.FREQUENCY = FREQUENCY;
    }

    public String getDOSAGE() {
        return DOSAGE;
    }

    public void setDOSAGE(String DOSAGE) {
        this.DOSAGE = DOSAGE;
    }

    public String getUSE_TIME() {
        return USE_TIME;
    }

    public void setUSE_TIME(String USE_TIME) {
        this.USE_TIME = USE_TIME;
    }

    public String getPROBLEM_TYPE() {
        return PROBLEM_TYPE;
    }

    public void setPROBLEM_TYPE(String PROBLEM_TYPE) {
        this.PROBLEM_TYPE = PROBLEM_TYPE;
    }

    public String getPROBLEM_LEVEL() {
        return PROBLEM_LEVEL;
    }

    public void setPROBLEM_LEVEL(String PROBLEM_LEVEL) {
        this.PROBLEM_LEVEL = PROBLEM_LEVEL;
    }

    public String getDRUG_STATE() {
        return DRUG_STATE;
    }

    public void setDRUG_STATE(String DRUG_STATE) {
        this.DRUG_STATE = DRUG_STATE;
    }

    public String getGROUP_ID() {
        return GROUP_ID;
    }

    public void setGROUP_ID(String GROUP_ID) {
        this.GROUP_ID = GROUP_ID;
    }

    public String getINPATIENT_NO() {
        return INPATIENT_NO;
    }

    public void setINPATIENT_NO(String INPATIENT_NO) {
        this.INPATIENT_NO = INPATIENT_NO;
    }
}
