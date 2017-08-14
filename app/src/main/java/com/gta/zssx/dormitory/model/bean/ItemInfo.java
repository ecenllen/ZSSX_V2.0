package com.gta.zssx.dormitory.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * ItemInfo:
 * {
 * ItemType:1  1.常规项   2.增分  3.减分
 * ItemMode: 1 1.记分    2.扣分
 * DimensionType: 2  1.宿舍维度   2.班级维度
 * ScoringTemplateType:1  1.录入姓名单项2.录入姓名多项3.不录入姓名有选项设4.不录入姓名无选项设置
 * }
 */
public class ItemInfo implements Parcelable {
    private int ItemType;
    private int ItemMode;
    private int DimensionType;
    private int ScoringTemplateType;

    public int getItemType() {
        return ItemType;
    }

    public void setItemType(int itemType) {
        ItemType = itemType;
    }

    public int getItemMode() {
        return ItemMode;
    }

    public void setItemMode(int itemMode) {
        ItemMode = itemMode;
    }

    public int getDimensionType() {
        return DimensionType;
    }

    public void setDimensionType(int dimensionType) {
        DimensionType = dimensionType;
    }

    public int getScoringTemplateType() {
        return ScoringTemplateType;
    }

    public void setScoringTemplateType(int scoringTemplateType) {
        ScoringTemplateType = scoringTemplateType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ScoringTemplateType);
        dest.writeInt(this.ItemMode);
        dest.writeInt(this.DimensionType);
        dest.writeInt(this.ItemType);
    }

    public ItemInfo() {
    }

    protected ItemInfo(Parcel in) {
        this.ScoringTemplateType = in.readInt();
        this.ItemMode = in.readInt();
        this.DimensionType = in.readInt();
        this.ItemType = in.readInt();
    }

    public static final Parcelable.Creator<ItemInfo> CREATOR = new Parcelable.Creator<ItemInfo>() {
        @Override
        public ItemInfo createFromParcel(Parcel source) {
            return new ItemInfo(source);
        }

        @Override
        public ItemInfo[] newArray(int size) {
            return new ItemInfo[size];
        }
    };
}