package com.dcdt.doctorstation.entity;

/**
 * Created by LiRong on 2017/6/23.
 */
public class CheckMessage {
    private int hasProblem; //0表示没有问题，1表示有问题
    private String presId;  //处方id

    public String getPresId() {
        return presId;
    }

    public void setPresId(String presId) {
        this.presId = presId;
    }

    public int getHasProblem() {
        return hasProblem;
    }

    public void setHasProblem(int hasProblem) {
        this.hasProblem = hasProblem;
    }

}
