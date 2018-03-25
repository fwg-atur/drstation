package com.dcdt.doctorstation.entity;

import java.util.List;

/**
 * Created by wtwang on 2018/3/17.
 */
public class PrescInfo {
    protected String order_id;
    protected String order_sub_id;
    protected String drug_lo_id;
    protected String drug_lo_name;
    protected List<CheckInfo> checkInfos;

//    public PrescInfo(String order_id,String order_sub_id,String drug_lo_id,String drug_lo_name,List<CheckInfo> checkInfos){
//        this.order_id = order_id;
//        this.order_sub_id = order_sub_id;
//        this.drug_lo_id = drug_lo_id;
//        this.drug_lo_name = drug_lo_name;
//        this.checkInfos = checkInfos;
//    }
//
//    public PrescInfo clone(){
//        PrescInfo ret = new PrescInfo(order_id,order_sub_id,drug_lo_id,drug_lo_name,checkInfos);
//        return ret;
//    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_sub_id() {
        return order_sub_id;
    }

    public void setOrder_sub_id(String order_sub_id) {
        this.order_sub_id = order_sub_id;
    }

    public String getDrug_lo_id() {
        return drug_lo_id;
    }

    public void setDrug_lo_id(String drug_lo_id) {
        this.drug_lo_id = drug_lo_id;
    }

    public String getDrug_lo_name() {
        return drug_lo_name;
    }

    public void setDrug_lo_name(String drug_lo_name) {
        this.drug_lo_name = drug_lo_name;
    }

    public List<CheckInfo> getCheckInfos() {
        return checkInfos;
    }

    public void setCheckInfos(List<CheckInfo> checkInfos) {
        this.checkInfos = checkInfos;
    }
}
