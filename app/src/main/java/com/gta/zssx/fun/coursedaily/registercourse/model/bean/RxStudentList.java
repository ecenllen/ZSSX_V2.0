package com.gta.zssx.fun.coursedaily.registercourse.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/7/8.
 * @since 1.0.0
 */
public class RxStudentList implements Serializable {
    private int mOrderPosition;  //0,1,2,3,4,5顺序
    private List<StudentListNewBean> mStudentListBeen;

    public int getmOrderPosition(){
        return mOrderPosition;
    }

    public void setmOrderPosition(int mOrderPosition){
        this.mOrderPosition = mOrderPosition;
    }

    public List<StudentListNewBean> getStudentListBeen() {
        return mStudentListBeen;
    }

    public void setStudentListBeen(List<StudentListNewBean> studentListBeen) {
        mStudentListBeen = studentListBeen;
    }
}
