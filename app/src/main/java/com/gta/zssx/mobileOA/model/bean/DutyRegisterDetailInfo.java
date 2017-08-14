package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;

/**
 * Created by lan.zheng on 2016/11/22.
 */
public class DutyRegisterDetailInfo implements Serializable{
    private int DutyDetailId;
    private int AddressId;
    private String Address;
    private String LateStudent;
    private String IllegalList ;
    private String Accident;

    public int getDutyDetailId() {
        return DutyDetailId;
    }

    public void setDutyDetailId(int DutyDetailId) {
        this.DutyDetailId = DutyDetailId;
    }

    public int getAddressId() {
        return AddressId;
    }

    public void setAddressId(int AddressId) {
        this.AddressId = AddressId;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getLateStudent() {
        return LateStudent;
    }

    public void setLateStudent(String LateStudent) {
        this.LateStudent = LateStudent;
    }

    public String getIllegalList() {
        return IllegalList;
    }

    public void setIllegalList(String IllegalList) {
        this.IllegalList = IllegalList;
    }

    public String getAccident() {
        return Accident;
    }

    public void setAccident(String Accident) {
        this.Accident = Accident;
    }
}
