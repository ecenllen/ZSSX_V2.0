package com.gta.zssx.dormitory.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class LevelList implements Serializable, Parcelable {
    private String Name;
    private int Id;
    private String Score;
    private int Status;
    private String BelongLevel;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getBelongLevel() {
        return BelongLevel;
    }

    public void setBelongLevel(String belongLevel) {
        BelongLevel = belongLevel;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.BelongLevel);
        dest.writeInt(this.Id);
        dest.writeString(this.Score);
        dest.writeInt(this.Status);
        dest.writeString(this.Name);
    }

    public LevelList() {
    }

    protected LevelList(Parcel in) {
        this.BelongLevel = in.readString();
        this.Id = in.readInt();
        this.Score = in.readString();
        this.Status = in.readInt();
        this.Name = in.readString();
    }

    public static final Creator<LevelList> CREATOR = new Creator<LevelList>() {
        @Override
        public LevelList createFromParcel(Parcel source) {
            return new LevelList(source);
        }

        @Override
        public LevelList[] newArray(int size) {
            return new LevelList[size];
        }
    };
}