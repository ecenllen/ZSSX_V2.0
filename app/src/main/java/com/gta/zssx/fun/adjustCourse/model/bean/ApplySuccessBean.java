package com.gta.zssx.fun.adjustCourse.model.bean;

import java.io.Serializable;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/11/8.
 * @since 1.0.0
 */
public class ApplySuccessBean implements Serializable{

    /**
     * name : 111
     * userId : 78a5bcc7-0a97-44db-a430-0b1a1db422f2
     * deptId : 0
     * deptName : 222
     * phone : 15602333098
     * userName : 高宇星
     * userType : 111
     */

    private String name;
    private String userId;
    private int deptId;
    private String deptName;
    private String phone;
    private String userName;
    private String userType;
    //用于区分调时间、调老师、代课，显示不同的结果页面
    private String mType;

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
