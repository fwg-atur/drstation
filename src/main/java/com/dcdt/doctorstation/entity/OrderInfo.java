package com.dcdt.doctorstation.entity;

import java.util.ArrayList;
import java.util.List;

public class OrderInfo {

    private List<Order> orderList = new ArrayList<Order>();

    private String BED_NO;

    private String PROBLEM_LEVEL;

    private String PATIENT_NAME;

    // 用于前台显示
    private int orderGroupId;


    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public String getBED_NO() {
        return BED_NO;
    }

    public void setBED_NO(String BED_NO) {
        this.BED_NO = BED_NO;
    }

    public String getPROBLEM_LEVEL() {
        return PROBLEM_LEVEL;
    }

    public void setPROBLEM_LEVEL(String PROBLEM_LEVEL) {
        this.PROBLEM_LEVEL = PROBLEM_LEVEL;
    }

    public String getPATIENT_NAME() {
        return PATIENT_NAME;
    }

    public void setPATIENT_NAME(String PATIENT_NAME) {
        this.PATIENT_NAME = PATIENT_NAME;
    }

    public int getOrderGroupId() {
        return orderGroupId;
    }

    public void setOrderGroupId(int orderGroupId) {
        this.orderGroupId = orderGroupId;
    }
}
