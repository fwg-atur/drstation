package com.dcdt.doctorstation.preIntervenation;

/**
 * Created by LiRong on 2017/12/1.
 */
public class IntervenationState {
    private final int state;

    //等待被干预
    public static final IntervenationState WAIT_TO_BE_INTERVENED = new IntervenationState(0);
    //正在干预
    public static final IntervenationState INTERVENING = new IntervenationState(1);
    //干预结束
    public static final IntervenationState INTERVENED = new IntervenationState(2);
    //超时
    public static final IntervenationState OUT_TIME = new IntervenationState(-1);

    private static IntervenationState[] states = new IntervenationState[]{
            WAIT_TO_BE_INTERVENED, INTERVENING, INTERVENED
    };

    private IntervenationState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public static IntervenationState getState(int stateCode) {
        return states[stateCode];
    }
}
