package com.dcdt.doctorstation.entity;

import java.util.List;

/**
 * Created by LiRong on 2017/7/2.
 */
public class CheckResults {
    private Doctor doctor;
    private Patient patient;
    private Diagnosis diagnosis;
    private List<Advice> advices;
    private int HIGHEST_WARNING_LEVEL;
    private int WARNING_COUNT;

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
    }

    public List<Advice> getAdvices() {
        return advices;
    }

    public void setAdvices(List<Advice> advices) {
        this.advices = advices;
    }

    public int getHIGHEST_WARNING_LEVEL() {
        return HIGHEST_WARNING_LEVEL;
    }

    public void setHIGHEST_WARNING_LEVEL(int HIGHEST_WARNING_LEVEL) {
        this.HIGHEST_WARNING_LEVEL = HIGHEST_WARNING_LEVEL;
    }

    public int getWARNING_COUNT() {
        return WARNING_COUNT;
    }

    public void setWARNING_COUNT(int WARNING_COUNT) {
        this.WARNING_COUNT = WARNING_COUNT;
    }
}
