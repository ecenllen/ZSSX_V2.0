package com.gta.zssx.dormitory.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 一栋宿舍楼或者一个专业部的一层级
 */
public class DormitoryOrClassLevelList implements Serializable, Parcelable {

    private String LevelName;  //层级名
    private int LevelId; //层级Id
    private List<LevelList> LevelList;  //层级里面的具体宿舍或者班级

    public String getLevelName() {
        return LevelName;
    }

    public void setLevelName(String levelName) {
        LevelName = levelName;
    }

    public int getLevelId() {
        return LevelId;
    }

    public void setLevelId(int levelId) {
        LevelId = levelId;
    }

    public void setLevelList(List<LevelList> levelList) {
        this.LevelList = levelList;
    }

    public List<LevelList> getLevelList() {
        return LevelList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.LevelName);
        dest.writeInt(this.LevelId);
        dest.writeList(this.LevelList);
    }

    public DormitoryOrClassLevelList() {
    }

    protected DormitoryOrClassLevelList(Parcel in) {
        this.LevelName = in.readString();
        this.LevelId = in.readInt();
        this.LevelList = new ArrayList<LevelList>();
        in.readList(this.LevelList, LevelList.class.getClassLoader());
    }

    public static final Parcelable.Creator<DormitoryOrClassLevelList> CREATOR = new Parcelable.Creator<DormitoryOrClassLevelList>() {
        @Override
        public DormitoryOrClassLevelList createFromParcel(Parcel source) {
            return new DormitoryOrClassLevelList(source);
        }

        @Override
        public DormitoryOrClassLevelList[] newArray(int size) {
            return new DormitoryOrClassLevelList[size];
        }
    };
}