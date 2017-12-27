package com.dcdt.doctorstation.preIntervenation;

import com.dcdt.cache.CheckResultCache;
import com.dcdt.doctorstation.entity.CheckResults;

/**
 * Created by LiRong on 2017/12/1.
 */
public class IntervenedPresc {
    private CheckResults realPresc;
    //    private int state;
    private long submitTime;

    private IntervenationState state;

    public IntervenedPresc(String presId) {
        realPresc = CheckResultCache.findCheckResult(presId);
        submitTime = System.currentTimeMillis();
        state = IntervenationState.WAIT_TO_BE_INTERVENED;
    }

    public int getState() {
        return state.getState();
    }

    public void setState(IntervenationState state) {
        this.state = state;
    }

    public long getSubmitTime() {
        return submitTime;
    }
}
