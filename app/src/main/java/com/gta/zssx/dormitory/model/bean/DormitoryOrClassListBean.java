package com.gta.zssx.dormitory.model.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 宿舍列表/班级列表
 */
public class DormitoryOrClassListBean implements Parcelable {
    private ItemInfo ItemInfo;
    private List<DormitoryOrClassListInfoBean> DormitoryOrClassInfo;

    public ItemInfo getItemInfo() {
        return ItemInfo;
    }
    public void setItemInfo(ItemInfo itemInfo) {
        this.ItemInfo = itemInfo;
    }

    public List<DormitoryOrClassListInfoBean> getDormitoryOrClassInfo() {
        return DormitoryOrClassInfo;
    }

    public void setDormitoryOrClassInfo(List<DormitoryOrClassListInfoBean> dormitoryOrClassInfo) {
        DormitoryOrClassInfo = dormitoryOrClassInfo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.ItemInfo, flags);
        dest.writeTypedList(this.DormitoryOrClassInfo);
    }

    public DormitoryOrClassListBean() {
    }

    protected DormitoryOrClassListBean(Parcel in) {
        this.ItemInfo = in.readParcelable(com.gta.zssx.dormitory.model.bean.ItemInfo.class.getClassLoader());
        this.DormitoryOrClassInfo = in.createTypedArrayList(DormitoryOrClassListInfoBean.CREATOR);
    }

    public static final Creator<DormitoryOrClassListBean> CREATOR = new Creator<DormitoryOrClassListBean>() {
        @Override
        public DormitoryOrClassListBean createFromParcel(Parcel source) {
            return new DormitoryOrClassListBean(source);
        }

        @Override
        public DormitoryOrClassListBean[] newArray(int size) {
            return new DormitoryOrClassListBean[size];
        }
    };
}

