package com.dcdt.doctorstation.preIntervenation;

import java.util.List;

/**
 * Created by LiRong on 2017/12/1.
 */
public class IntervenedDoctor {
    private String id;
    private IntervenedPresc intervenedPresc;
    private List<TalkMessage> messageList;

    public IntervenedDoctor(String presId) {
        id = presId;
        intervenedPresc = new IntervenedPresc(presId);
    }

    public long getSubmitTime() {
        return intervenedPresc.getSubmitTime();
    }


    public String getId() {
        return id;
    }

    public int getState() {
        return intervenedPresc.getState();
    }

    public void setState(int state) {
        intervenedPresc.setState(IntervenationState.getState(state));
    }

    public void setState(IntervenationState outTime) {
        intervenedPresc.setState(outTime);
    }
}
