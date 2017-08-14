package com.gta.zssx.fun.coursedaily.registerrecord.model.DTO;

import java.io.Serializable;

/**
 * Created by lan.zheng on 2016/7/1.
 *
 */
public class MyClassSubTabInfoDto implements Serializable {
    private String Date;
    private String Week;
    private boolean IsSubTab;
//    private boolean IsFirstGetDateandWeek;

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getWeek() {
        return Week;
    }
    public void setWeek(String Week) {
        this.Week = Week;
    }

    //用于判断是否是多tab
    public void setIsSubTab(boolean IsSubTab) {
        this.IsSubTab = IsSubTab;
    }

    public boolean getIsSubTab() {
        return IsSubTab;
    }

    //用于判断是否是第一次进入获取时间的，第一次赋值后，后面都要置为false，除非再次进入这个页面
    /*public void setIsFirstGetDateandWeek(boolean IsFirstGetDateandWeek) {
        this.IsFirstGetDateandWeek = IsFirstGetDateandWeek;
    }

    public boolean getIsFirstGetDateandWeek() {
        return IsFirstGetDateandWeek;
    }*/

}
