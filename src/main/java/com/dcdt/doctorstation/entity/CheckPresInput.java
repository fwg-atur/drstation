package com.dcdt.doctorstation.entity;

import java.util.List;

/**
 * Created by wtwang on 2018/3/17.
 */
public class CheckPresInput {
    protected Doctor doctor;
    protected Patient patient;
    protected Diagnosis diagnosis;
    protected List<Advice> advices;

//    public CheckPresInput(Doctor doctor,Patient patient,Diagnosis diagnosis,List<Advice> advices){
//        this.doctor = doctor;
//        this.patient = patient;
//        this.diagnosis = diagnosis;
//        this.advices = advices;
//    }
//
//    public CheckPresInput clone(){
//        CheckPresInput ret = new CheckPresInput(doctor,patient,diagnosis,advices);
//        return ret;
//    }

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
}
