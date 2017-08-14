package com.gta.zssx.dormitory.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lan.zheng on 2017/6/27.
 * 一个专业或一个宿舍的信息 -- 用于宿舍主页的列表和新增录入
 */

public class DormitoryOrClassSingleInfoBean implements Serializable, Parcelable {
    private int DormitoryOrClassId;  //宿舍或专业部Id
    private String DormitoryOrClassName;  //宿舍或专业部名字

    public int getDormitoryOrClassId() {
        return DormitoryOrClassId;
    }

    public void setDormitoryOrClassId(int Id) {
        this.DormitoryOrClassId = Id;
    }

    public String getDormitoryOrClassName() {
        return DormitoryOrClassName;
    }

    public void setDormitoryOrClassName(String DormitoryOrClassName) {
        this.DormitoryOrClassName = DormitoryOrClassName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.DormitoryOrClassId);
        dest.writeString(this.DormitoryOrClassName);
    }

    public DormitoryOrClassSingleInfoBean() {
    }

    protected DormitoryOrClassSingleInfoBean(Parcel in) {
        this.DormitoryOrClassId = in.readInt();
        this.DormitoryOrClassName = in.readString();
    }

    public static final Creator<DormitoryOrClassSingleInfoBean> CREATOR = new Creator<DormitoryOrClassSingleInfoBean>() {
        @Override
        public DormitoryOrClassSingleInfoBean createFromParcel(Parcel source) {
            return new DormitoryOrClassSingleInfoBean(source);
        }

        @Override
        public DormitoryOrClassSingleInfoBean[] newArray(int size) {
            return new DormitoryOrClassSingleInfoBean[size];
        }
    };
}
