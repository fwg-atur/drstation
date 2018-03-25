package com.dcdt.doctorstation.entity;

/**
 * Created by wtwang on 2018/3/17.
 */
public class CheckPharmacist {
    protected String pharmacistCheck;

//    public CheckPharmacist(String pharmacistCheck){
//        this.pharmacistCheck = pharmacistCheck;
//    }
//
//    public CheckPharmacist clone(){
//        CheckPharmacist ret = new CheckPharmacist(pharmacistCheck);
//        return ret;
//    }

    public String getPharmacistCheck() {
        return pharmacistCheck;
    }

    public void setPharmacistCheck(String pharmacistCheck) {
        this.pharmacistCheck = pharmacistCheck;
    }
}
