package com.gta.zssx.dormitory.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by weiye.chen on 2017/7/7.
 */

public class DormitoryOrClassListInfoBean implements Parcelable{
    private int DormitoryOrClassId;
    /**
     * * "DormitoryOrClassId": 111, 宿舍楼/专业部ID
     * "DormitoryOrClassLevelList": [
     * {
     * "LevelName": "1F",
     * "LevelId": 1,
     * " LevelList": [
     * {
     * "Name": "101室",
     * "Id": 101,
     * "Score": 100,
     * "Status": 1
     * “BelongLevel”:1F
     * }
     * ]
     * },
     */
    private List<DormitoryOrClassLevelList> DormitoryOrClassList;


    public int getDormitoryOrClassId() {
        return DormitoryOrClassId;
    }

    public void setDormitoryOrClassId(int dormitoryOrClassId) {
        DormitoryOrClassId = dormitoryOrClassId;
    }

    public List<DormitoryOrClassLevelList> getDormitoryOrClassList() {
        return DormitoryOrClassList;
    }

    public void setDormitoryOrClassList(List<DormitoryOrClassLevelList> dormitoryOrClassList) {
        DormitoryOrClassList = dormitoryOrClassList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.DormitoryOrClassId);
        dest.writeTypedList(this.DormitoryOrClassList);
    }

    public DormitoryOrClassListInfoBean() {
    }

    protected DormitoryOrClassListInfoBean(Parcel in) {
        this.DormitoryOrClassId = in.readInt();
        this.DormitoryOrClassList = in.createTypedArrayList(DormitoryOrClassLevelList.CREATOR);
    }

    public static final Creator<DormitoryOrClassListInfoBean> CREATOR = new Creator<DormitoryOrClassListInfoBean>() {
        @Override
        public DormitoryOrClassListInfoBean createFromParcel(Parcel source) {
            return new DormitoryOrClassListInfoBean(source);
        }

        @Override
        public DormitoryOrClassListInfoBean[] newArray(int size) {
            return new DormitoryOrClassListInfoBean[size];
        }
    };
}
