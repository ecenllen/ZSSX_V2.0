package com.gta.zssx.dormitory.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by weiye.chen on 2017/7/8.
 */

public class DormitoryListArgumentBean implements Parcelable, Serializable {
    /**
     * @param context  context
     * @param recordId 记录ID
     * @param itemId   指标项ID
     * @param itemName 指标项名称
     * @param date     日期
     * @paren dimensionType 1为宿舍维度, 2 为班级维度
     * @param actionType  1新增、2编辑、3查看 状态
     * @param isAll     是否全部宿舍楼/专业部
     * @param dormitoryIdList   宿舍楼/专业部 list ID
     */
    private int recordId;
    private int itemId;
    private String itemName;
    private String date;
    private int actionType;
    private boolean isAll;
    private int dimensionType;
    private ArrayList<DormitoryOrClassSingleInfoBean> dormitoryIdList;

    private int parentPosition;
    private int childPosition;
    private int dormitoryOrClassId;
    private DormitoryOrClassListBean dormitoryOrClassListBean;

    public int getParentPosition() {
        return parentPosition;
    }

    public void setParentPosition(int parentPosition) {
        this.parentPosition = parentPosition;
    }

    public int getChildPosition() {
        return childPosition;
    }

    public void setChildPosition(int childPosition) {
        this.childPosition = childPosition;
    }

    public int getDormitoryOrClassId() {
        return dormitoryOrClassId;
    }

    public void setDormitoryOrClassId(int dormitoryOrClassId) {
        this.dormitoryOrClassId = dormitoryOrClassId;
    }

    public DormitoryOrClassListBean getDormitoryOrClassListBean() {
        return dormitoryOrClassListBean;
    }

    public void setDormitoryOrClassListBean(DormitoryOrClassListBean dormitoryOrClassListBean) {
        this.dormitoryOrClassListBean = dormitoryOrClassListBean;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public boolean isAll() {
        return isAll;
    }

    public void setAll(boolean all) {
        isAll = all;
    }

    public int getDimensionType() {
        return dimensionType;
    }

    public void setDimensionType(int dimensionType) {
        this.dimensionType = dimensionType;
    }

    public ArrayList<DormitoryOrClassSingleInfoBean> getDormitoryIdList() {
        return dormitoryIdList;
    }

    public void setDormitoryIdList(ArrayList<DormitoryOrClassSingleInfoBean> dormitoryIdList) {
        this.dormitoryIdList = dormitoryIdList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.recordId);
        dest.writeInt(this.itemId);
        dest.writeString(this.itemName);
        dest.writeString(this.date);
        dest.writeInt(this.actionType);
        dest.writeByte(this.isAll ? (byte) 1 : (byte) 0);
        dest.writeInt(this.dimensionType);
        dest.writeList(this.dormitoryIdList);
        dest.writeInt(this.parentPosition);
        dest.writeInt(this.childPosition);
        dest.writeInt(this.dormitoryOrClassId);
        dest.writeParcelable(this.dormitoryOrClassListBean, flags);
    }

    public DormitoryListArgumentBean() {
    }

    protected DormitoryListArgumentBean(Parcel in) {
        this.recordId = in.readInt();
        this.itemId = in.readInt();
        this.itemName = in.readString();
        this.date = in.readString();
        this.actionType = in.readInt();
        this.isAll = in.readByte() != 0;
        this.dimensionType = in.readInt();
        this.dormitoryIdList = new ArrayList<DormitoryOrClassSingleInfoBean>();
        in.readList(this.dormitoryIdList, DormitoryOrClassSingleInfoBean.class.getClassLoader());
        this.parentPosition = in.readInt();
        this.childPosition = in.readInt();
        this.dormitoryOrClassId = in.readInt();
        this.dormitoryOrClassListBean = in.readParcelable(DormitoryOrClassListBean.class.getClassLoader());
    }

    public static final Creator<DormitoryListArgumentBean> CREATOR = new Creator<DormitoryListArgumentBean>() {
        @Override
        public DormitoryListArgumentBean createFromParcel(Parcel source) {
            return new DormitoryListArgumentBean(source);
        }

        @Override
        public DormitoryListArgumentBean[] newArray(int size) {
            return new DormitoryListArgumentBean[size];
        }
    };
}
