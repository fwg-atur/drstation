package com.dcdt.doctorstation.entity;

import java.util.List;

/**
 * Created by wtwang on 2018/3/17.
 */
public class PrescInfo {
    protected String group_id;
    protected String order_id;
    protected String order_sub_id;
    protected String drug_lo_id;
    protected String drug_lo_name;
    protected List<CheckInfo> checkInfos;

    //昌平医院新增字段
    protected String ganyu_state;
    protected String ganyu_info;
    protected String medication_flag;

    protected String kh;

    //在排序时用到的辅助字段，标识order_id是否已经处理过
    private boolean order_id_flag;
    //在排序时用到的辅助字段，标识order_sub_id是否已经处理过
    private boolean order_sub_id_flag;
    //在排序时用到的辅助字段，标识group_id是否已经处理过
    private boolean group_id_flag;

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

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

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

    public String getGanyu_state() {
        return ganyu_state;
    }

    public void setGanyu_state(String ganyu_state) {
        this.ganyu_state = ganyu_state;
    }

    public String getGanyu_info() {
        return ganyu_info;
    }

    public void setGanyu_info(String ganyu_info) {
        this.ganyu_info = ganyu_info;
    }

    public String getMedication_flag() {
        return medication_flag;
    }

    public void setMedication_flag(String medication_flag) {
        this.medication_flag = medication_flag;
    }

    public String getKh() {
        return kh;
    }

    public void setKh(String kh) {
        this.kh = kh;
    }

    public boolean isOrder_id_flag() {
        return order_id_flag;
    }

    public void setOrder_id_flag(boolean order_id_flag) {
        this.order_id_flag = order_id_flag;
    }

    public boolean isOrder_sub_id_flag() {
        return order_sub_id_flag;
    }

    public void setOrder_sub_id_flag(boolean order_sub_id_flag) {
        this.order_sub_id_flag = order_sub_id_flag;
    }

    public boolean isGroup_id_flag() {
        return group_id_flag;
    }

    public void setGroup_id_flag(boolean group_id_flag) {
        this.group_id_flag = group_id_flag;
    }
}
