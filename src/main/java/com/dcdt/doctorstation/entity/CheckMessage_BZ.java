package com.dcdt.doctorstation.entity;

/**
 * Created by wtwang on 2019/8/16.
 */
public class CheckMessage_BZ {
    private int state;      //标记状态，取值为1-8，分别对应8种情况的返回值
    private int hasProblem;     //是否有问题
    private String presId;
    private String retXml;      //根据标记状态构造返回xml串

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getHasProblem() {
        return hasProblem;
    }

    public void setHasProblem(int hasProblem) {
        this.hasProblem = hasProblem;
    }

    public String getPresId() {
        return presId;
    }

    public void setPresId(String presId) {
        this.presId = presId;
    }

    public String getRetXml() {
        return retXml;
    }

    public void setRetXml(String retXml) {
        this.retXml = retXml;
    }
}
