package com.dcdt.doctorstation.entity;

/**
 * Created by LiRong on 2017/7/2.
 */
public class Patient {
    private String NAME;
    private String ID;
    private String GENDER;
    private String BIRTH;
    private String WEIGHT;
    private String HEIGHT;
    private String ALERGY_DRUGS;
    private String PREGNANT;
    private String LACT;
    private String HEPATICAL;
    private String RENAL;
    private String PANCREAS;
    private String VISIT_ID;    //住院次数
    private String PATIENT_PRES_ID;
    private String IDENTITY_TYPE;
    private String FEE_TYPE;
    private String SCR;
    private String SCR_UNIT;
    private String GESTATION_AGE;
    private String PRETERM_BIRTH;
    private String DRUG_HISTORY;
    private String FAMILY_DISEASE_HISTORY;
    private String GENETIC_DISEASE;
    private String MEDICARE_01;
    private String MEDICARE_02;
    private String MEDICARE_03;
    private String MEDICARE_04;
    private String MEDICARE_05;

    private String BED_NO;

    //鄱阳
    private String patient_id;

    public Patient(){

    }

    public Patient(String NAME, String ID, String GENDER, String BIRTH, String WEIGHT, String HEIGHT, String ALERGY_DRUGS, String PREGNANT, String LACT, String HEPATICAL, String RENAL, String PANCREAS, String VISIT_ID, String PATIENT_PRES_ID, String IDENTITY_TYPE, String FEE_TYPE, String SCR, String SCR_UNIT, String GESTATION_AGE, String PRETERM_BIRTH, String DRUG_HISTORY, String FAMILY_DISEASE_HISTORY, String GENETIC_DISEASE, String MEDICARE_01, String MEDICARE_02, String MEDICARE_03, String MEDICARE_04, String MEDICARE_05, String BED_NO) {
        this.NAME = NAME;
        this.ID = ID;
        this.GENDER = GENDER;
        this.BIRTH = BIRTH;
        this.WEIGHT = WEIGHT;
        this.HEIGHT = HEIGHT;
        this.ALERGY_DRUGS = ALERGY_DRUGS;
        this.PREGNANT = PREGNANT;
        this.LACT = LACT;
        this.HEPATICAL = HEPATICAL;
        this.RENAL = RENAL;
        this.PANCREAS = PANCREAS;
        this.VISIT_ID = VISIT_ID;
        this.PATIENT_PRES_ID = PATIENT_PRES_ID;
        this.IDENTITY_TYPE = IDENTITY_TYPE;
        this.FEE_TYPE = FEE_TYPE;
        this.SCR = SCR;
        this.SCR_UNIT = SCR_UNIT;
        this.GESTATION_AGE = GESTATION_AGE;
        this.PRETERM_BIRTH = PRETERM_BIRTH;
        this.DRUG_HISTORY = DRUG_HISTORY;
        this.FAMILY_DISEASE_HISTORY = FAMILY_DISEASE_HISTORY;
        this.GENETIC_DISEASE = GENETIC_DISEASE;
        this.MEDICARE_01 = MEDICARE_01;
        this.MEDICARE_02 = MEDICARE_02;
        this.MEDICARE_03 = MEDICARE_03;
        this.MEDICARE_04 = MEDICARE_04;
        this.MEDICARE_05 = MEDICARE_05;

        this.BED_NO = BED_NO;
    }

    public Patient clone(){
        Patient ret = new Patient(NAME, ID, GENDER, BIRTH, WEIGHT, HEIGHT,
                ALERGY_DRUGS, PREGNANT, LACT, HEPATICAL, RENAL, PANCREAS,
                VISIT_ID, PATIENT_PRES_ID, IDENTITY_TYPE, FEE_TYPE, SCR,
                SCR_UNIT, GESTATION_AGE, PRETERM_BIRTH, DRUG_HISTORY,
                FAMILY_DISEASE_HISTORY, GENETIC_DISEASE, MEDICARE_01,
                MEDICARE_02, MEDICARE_03, MEDICARE_04, MEDICARE_05, BED_NO);
        return ret;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getGENDER() {
        return GENDER;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public String getBIRTH() {
        return BIRTH;
    }

    public void setBIRTH(String BIRTH) {
        this.BIRTH = BIRTH;
    }

    public String getWEIGHT() {
        return WEIGHT;
    }

    public void setWEIGHT(String WEIGHT) {
        this.WEIGHT = WEIGHT;
    }

    public String getHEIGHT() {
        return HEIGHT;
    }

    public void setHEIGHT(String HEIGHT) {
        this.HEIGHT = HEIGHT;
    }

    public String getALERGY_DRUGS() {
        return ALERGY_DRUGS;
    }

    public void setALERGY_DRUGS(String ALERGY_DRUGS) {
        this.ALERGY_DRUGS = ALERGY_DRUGS;
    }

    public String getPREGNANT() {
        return PREGNANT;
    }

    public void setPREGNANT(String PREGNANT) {
        this.PREGNANT = PREGNANT;
    }

    public String getLACT() {
        return LACT;
    }

    public void setLACT(String LACT) {
        this.LACT = LACT;
    }

    public String getHEPATICAL() {
        return HEPATICAL;
    }

    public void setHEPATICAL(String HEPATICAL) {
        this.HEPATICAL = HEPATICAL;
    }

    public String getRENAL() {
        return RENAL;
    }

    public void setRENAL(String RENAL) {
        this.RENAL = RENAL;
    }

    public String getPANCREAS() {
        return PANCREAS;
    }

    public void setPANCREAS(String PANCREAS) {
        this.PANCREAS = PANCREAS;
    }

    public String getVISIT_ID() {
        return VISIT_ID;
    }

    public void setVISIT_ID(String VISIT_ID) {
        this.VISIT_ID = VISIT_ID;
    }

    public String getPATIENT_PRES_ID() {
        return PATIENT_PRES_ID;
    }

    public void setPATIENT_PRES_ID(String PATIENT_PRES_ID) {
        this.PATIENT_PRES_ID = PATIENT_PRES_ID;
    }

    public String getIDENTITY_TYPE() {
        return IDENTITY_TYPE;
    }

    public void setIDENTITY_TYPE(String IDENTITY_TYPE) {
        this.IDENTITY_TYPE = IDENTITY_TYPE;
    }

    public String getFEE_TYPE() {
        return FEE_TYPE;
    }

    public void setFEE_TYPE(String FEE_TYPE) {
        this.FEE_TYPE = FEE_TYPE;
    }

    public String getSCR() {
        return SCR;
    }

    public void setSCR(String SCR) {
        this.SCR = SCR;
    }

    public String getSCR_UNIT() {
        return SCR_UNIT;
    }

    public void setSCR_UNIT(String SCR_UNIT) {
        this.SCR_UNIT = SCR_UNIT;
    }

    public String getGESTATION_AGE() {
        return GESTATION_AGE;
    }

    public void setGESTATION_AGE(String GESTATION_AGE) {
        this.GESTATION_AGE = GESTATION_AGE;
    }

    public String getPRETERM_BIRTH() {
        return PRETERM_BIRTH;
    }

    public void setPRETERM_BIRTH(String PRETERM_BIRTH) {
        this.PRETERM_BIRTH = PRETERM_BIRTH;
    }

    public String getDRUG_HISTORY() {
        return DRUG_HISTORY;
    }

    public void setDRUG_HISTORY(String DRUG_HISTORY) {
        this.DRUG_HISTORY = DRUG_HISTORY;
    }

    public String getFAMILY_DISEASE_HISTORY() {
        return FAMILY_DISEASE_HISTORY;
    }

    public void setFAMILY_DISEASE_HISTORY(String FAMILY_DISEASE_HISTORY) {
        this.FAMILY_DISEASE_HISTORY = FAMILY_DISEASE_HISTORY;
    }

    public String getGENETIC_DISEASE() {
        return GENETIC_DISEASE;
    }

    public void setGENETIC_DISEASE(String GENETIC_DISEASE) {
        this.GENETIC_DISEASE = GENETIC_DISEASE;
    }

    public String getMEDICARE_01() {
        return MEDICARE_01;
    }

    public void setMEDICARE_01(String MEDICARE_01) {
        this.MEDICARE_01 = MEDICARE_01;
    }

    public String getMEDICARE_02() {
        return MEDICARE_02;
    }

    public void setMEDICARE_02(String MEDICARE_02) {
        this.MEDICARE_02 = MEDICARE_02;
    }

    public String getMEDICARE_03() {
        return MEDICARE_03;
    }

    public void setMEDICARE_03(String MEDICARE_03) {
        this.MEDICARE_03 = MEDICARE_03;
    }

    public String getMEDICARE_04() {
        return MEDICARE_04;
    }

    public void setMEDICARE_04(String MEDICARE_04) {
        this.MEDICARE_04 = MEDICARE_04;
    }

    public String getMEDICARE_05() {
        return MEDICARE_05;
    }

    public void setMEDICARE_05(String MEDICARE_05) {
        this.MEDICARE_05 = MEDICARE_05;
    }

    public String getBED_NO() {
        return BED_NO;
    }

    public void setBED_NO(String BED_NO) {
        this.BED_NO = BED_NO;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }
}
