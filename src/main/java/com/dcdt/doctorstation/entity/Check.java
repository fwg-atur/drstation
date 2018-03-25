package com.dcdt.doctorstation.entity;

/**
 * Created by wtwang on 2018/3/17.
 */
public class Check {
    protected CheckPresInput checkPresInput;
    protected CheckPresOutput checkPresOutput;
    protected CheckPharmacist checkPharmacist;

//    public Check(CheckPresInput checkPresInput,CheckPresOutput checkPresOutput,CheckPharmacist checkPharmacist){
//        this.checkPresInput = checkPresInput;
//        this.checkPresOutput = checkPresOutput;
//        this.checkPharmacist = checkPharmacist;
//    }
//
//    public Check clone(){
//        Check ret = new Check(checkPresInput,checkPresOutput,checkPharmacist);
//        return ret;
//    }

    public CheckPresInput getCheckPresInput() {
        return checkPresInput;
    }

    public void setCheckPresInput(CheckPresInput checkPresInput) {
        this.checkPresInput = checkPresInput;
    }

    public CheckPresOutput getCheckPresOutput() {
        return checkPresOutput;
    }

    public void setCheckPresOutput(CheckPresOutput checkPresOutput) {
        this.checkPresOutput = checkPresOutput;
    }

    public CheckPharmacist getCheckPharmacist() {
        return checkPharmacist;
    }

    public void setCheckPharmacist(CheckPharmacist checkPharmacist) {
        this.checkPharmacist = checkPharmacist;
    }
}
