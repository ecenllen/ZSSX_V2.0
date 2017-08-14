package com.gta.zssx.dormitory.model.bean;

/**
 * Created by lan.zheng on 2017/7/5.
 * 当执行送审操作，保存操作后已送审或未送审列表要进行刷新
 */

public class RxDormitoryRankingListUpdateBean {
    private boolean SpecialRefresh;  //是否是送审成功做特别操作，定位页面到已送审页面
    private int IsNeedToUpdatePage; //如果是2表示已送审和未送审都要更新，如果是0表示更新未送审，1表示更新已送审

    public boolean getSpecialRefresh() {
        return SpecialRefresh;
    }

    public void setSpecialRefresh(boolean SpecialRefresh) {
        this.SpecialRefresh = SpecialRefresh;
    }

    public int getNeedToUpdatePage(){
        return IsNeedToUpdatePage;
    }

    public void setNeedToUpdatePage(int IsNeedToUpdatePage){
        this.IsNeedToUpdatePage = IsNeedToUpdatePage;
    }

    public static final int REFRESH_NOT_SUBMIT_LIST = 0;   //0表示更新未送审页面
    public static final int REFRESH_HAVE_BEEN_SUBMIT_LIST = 1; //1表示更新已送审页面
    public static final int REFRESH_ALL_LIST = 2;//2表示已送审和未送审页面都要更新
}
