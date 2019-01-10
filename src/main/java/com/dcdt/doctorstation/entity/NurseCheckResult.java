package com.dcdt.doctorstation.entity;

import java.util.List;

/**
 * Created by wtwang on 2019/1/2.
 */
public class NurseCheckResult {
    private Patient patient;
    private List<Advice> advices;
    private boolean bed_no_flag;
    private boolean serious_flag;
    private int warning_level;

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<Advice> getAdvices() {
        return advices;
    }

    public void setAdvices(List<Advice> advices) {
        this.advices = advices;
    }

    public boolean isBed_no_flag() {
        return bed_no_flag;
    }

    public void setBed_no_flag(boolean bed_no_flag) {
        this.bed_no_flag = bed_no_flag;
    }

    public boolean isSerious_flag() {
        return serious_flag;
    }

    public void setSerious_flag(boolean serious_flag) {
        this.serious_flag = serious_flag;
    }

    public int getWarning_level() {
        return warning_level;
    }

    public void setWarning_level(int warning_level) {
        this.warning_level = warning_level;
    }
}
