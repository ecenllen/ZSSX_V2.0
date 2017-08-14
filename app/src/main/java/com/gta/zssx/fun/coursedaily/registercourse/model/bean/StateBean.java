package com.gta.zssx.fun.coursedaily.registercourse.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/20.
 * @since 1.0.0
 */
public class StateBean implements Serializable {

    //1正常 2迟到 3请假 4旷课 5公休
    String state;
    int stateCode;
    public static List<StateBean> sStateBeen;

    public StateBean(String state, int stateCode) {
        this.state = state;
        this.stateCode = stateCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public static List<StateBean> getStateList() {
        if (sStateBeen == null) {
            sStateBeen = new ArrayList<>();
            sStateBeen.add(new StateBean(NORMAL_S, NORMAL));
            sStateBeen.add(new StateBean(DELAY_S, DELAY));
            sStateBeen.add(new StateBean(LEAVE_S, LEAVE));
            sStateBeen.add(new StateBean(ABSENT_S, ABSENT));
            sStateBeen.add(new StateBean(VOCATION_S, VOCATION));
        }
        return sStateBeen;
    }

    public static String getState(int code) {
        String state = null;
        switch (code) {
            case NORMAL:
                state = NORMAL_S;
                break;
            case DELAY:
                state = DELAY_S;
                break;
            case LEAVE:
                state = LEAVE_S;
                break;
            case ABSENT:
                state = ABSENT_S;
                break;
            case VOCATION:
                state = VOCATION_S;
                break;
            default:
                break;

        }
        return state;
    }

    //正常
    public static final int NORMAL = 1;
    public static final String NORMAL_S = "正常";
    public static final String DELAY_S = "迟到";
    public static final String LEAVE_S = "请假";
    public static final String ABSENT_S = "旷课";
    public static final String VOCATION_S = "公休";
    //迟到
    public static final int DELAY = 2;
    //请假
    public static final int LEAVE = 3;
    //旷课
    public static final int ABSENT = 4;
    //公休
    public static final int VOCATION = 5;
    //未登记
    public static final int UNSIGN = 0;
}
