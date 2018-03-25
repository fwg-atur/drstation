package com.dcdt.doctorstation.entity;

import java.util.List;

/**
 * Created by wtwang on 2018/3/17.
 */
public class CheckPresOutput {
    protected List<PrescInfo> prescInfos;

//    public CheckPresOutput(List<PrescInfo> prescInfos){
//        this.prescInfos = prescInfos;
//    }
//
//    public CheckPresOutput clone(){
//        CheckPresOutput ret = new CheckPresOutput(prescInfos);
//        return ret;
//    }

    public List<PrescInfo> getPrescInfos() {
        return prescInfos;
    }

    public void setPrescInfos(List<PrescInfo> prescInfos) {
        this.prescInfos = prescInfos;
    }
}
