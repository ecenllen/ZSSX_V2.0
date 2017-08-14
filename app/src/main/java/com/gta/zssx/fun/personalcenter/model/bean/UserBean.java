package com.gta.zssx.fun.personalcenter.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/14.
 * @since 1.0.0
 */
public class UserBean implements Serializable {


    /**
     * UserId :
     * UserName :
     * EchoName :
     * Sex : 0
     * Photo :
     * Phone :
     * WorkNo :
     */

    private String UserId;
    private int ClassId;
    private String UserName;
    private String EchoName;
    private int Sex;
    private String Photo;
    private String Phone;
    private String WorkNo;
    private int TotalCount;
    private boolean IsTeacherName;
    private String serverAddress;
    private String Account;
    private String MD5Password;
    private String Password;
    private List<ClassList> ClassName;
    private int uType;
    //量化考核地址
    private String TqaHostUrl;
    //统一权限地址
    private String PowerHostUrl;
    //Oa地址
    private String OaHostUrl;
    //调代课
    private String CourseHostUrl;
    //资产地址
    private String EamHostUrl;
    //bpm地址
    private String BpmHostUrl;
    private String ticket;
    //是否管理员
    private boolean IsSetAssetAdministrator;
    /**
     * 是否资产管理员，分配权限控制
     */
    private boolean IsAddAllocation;
    private List<MenuItemListBean> MenuItemList;
    /**
     * AddPageActivity 中的小icon集合
     */
    private List<UserBean.MenuItemListBean> SmallIconsList;

    public List<MenuItemListBean> getSmallIconsList () {
        return SmallIconsList;
    }

    public void setSmallIconsList (List<MenuItemListBean> smallIconsList) {
        SmallIconsList = smallIconsList;
    }

    public List<MenuItemListBean> getMenuItemList () {
        return MenuItemList;
    }

    public void setMenuItemList (List<MenuItemListBean> menuItemList) {
        MenuItemList = menuItemList;
    }

    public int getClassId () {
        return ClassId;
    }

    public void setClassId (int classId) {
        ClassId = classId;
    }

    public boolean isAddAllocation () {
        return IsAddAllocation;
    }

    public void setAddAllocation (boolean addAllocation) {
        IsAddAllocation = addAllocation;
    }

    public boolean isSetAssetAdministrator () {
        return IsSetAssetAdministrator;
    }

    public void setSetAssetAdministrator (boolean setAssetAdministrator) {
        IsSetAssetAdministrator = setAssetAdministrator;
    }

    public String getPassword () {
        return Password;
    }

    public void setPassword (String password) {
        Password = password;
    }

    public String getBpmHostUrl () {
        return BpmHostUrl;
    }

    public void setBpmHostUrl (String bpmHostUrl) {
        BpmHostUrl = bpmHostUrl;
    }

    public String getTicket () {
        return ticket;
    }

    public void setTicket (String ticket) {
        this.ticket = ticket;
    }

    public String getCourseHostUrl () {
        return CourseHostUrl;
    }

    public void setCourseHostUrl (String courseHostUrl) {
        CourseHostUrl = courseHostUrl;
    }

    public String getEamHostUrl () {
        return EamHostUrl;
    }

    public void setEamHostUrl (String eamHostUrl) {
        EamHostUrl = eamHostUrl;
    }

    public String getTqaHostUrl () {
        return TqaHostUrl;
    }

    public void setTqaHostUrl (String tqaHostUrl) {
        TqaHostUrl = tqaHostUrl;
    }

    public String getPowerHostUrl () {
        return PowerHostUrl;
    }

    public void setPowerHostUrl (String powerHostUrl) {
        PowerHostUrl = powerHostUrl;
    }

    public String getOaHostUrl () {
        return OaHostUrl;
    }

    public void setOaHostUrl (String oaHostUrl) {
        OaHostUrl = oaHostUrl;
    }

    public int getuType () {
        return uType;
    }

    public void setuType (int uType) {
        this.uType = uType;
    }

    public String getAccount () {
        return Account;
    }

    public void setAccount (String account) {
        Account = account;
    }

    public String getMD5Password () {
        return MD5Password;
    }

    public void setMD5Password (String MD5Password) {
        this.MD5Password = MD5Password;
    }

    public String getServerAddress () {
        return serverAddress;
    }

    public void setServerAddress (String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public List<ClassList> getClassName () {
        return ClassName;
    }

    public void setClassName (List<ClassList> className) {
        ClassName = className;
    }

    public int getTotalCount () {
        return TotalCount;
    }

    public void setTotalCount (int totalCount) {
        TotalCount = totalCount;
    }

    public String getUserId () {
        return UserId;
    }

    public void setUserId (String UserId) {
        this.UserId = UserId;
    }

    public String getUserName () {
        return UserName;
    }

    public void setUserName (String UserName) {
        this.UserName = UserName;
    }

    public String getEchoName () {
        return EchoName;
    }

    public void setEchoName (String EchoName) {
        this.EchoName = EchoName;
    }

    public int getSex () {
        return Sex;
    }

    public void setSex (int Sex) {
        this.Sex = Sex;
    }

    public String getPhoto () {
        return Photo;
    }

    public void setPhoto (String Photo) {
        this.Photo = Photo;
    }

    public String getPhone () {
        return Phone;
    }

    public void setPhone (String Phone) {
        this.Phone = Phone;
    }

    public String getWorkNo () {
        return WorkNo;
    }

    public void setWorkNo (String WorkNo) {
        this.WorkNo = WorkNo;
    }

    public boolean isTeacherName () {
        return IsTeacherName;
    }

    public void setTeacherName (boolean teacherName) {
        IsTeacherName = teacherName;
    }

    public static class ClassList implements Serializable {
        private String ClassName;
        private int Id;
        private int Grade;

        public String getClassName () {
            return ClassName;
        }

        public void setClassName (String ClassName) {
            this.ClassName = ClassName;
        }

        public int getId () {
            return Id;
        }

        public void setId (int Id) {
            this.Id = Id;
        }

        public int getGrade () {
            return Grade;
        }

        public void setGrade (int Grade) {
            this.Grade = Grade;
        }
    }

    public static class MenuItemListBean implements Serializable {
        private String powerName;
        private String powerCode;
        private String pushCount = "0";

        public String getPowerName () {
            return powerName;
        }

        public void setPowerName (String powerName) {
            this.powerName = powerName;
        }

        public String getPowerCode () {
            return powerCode;
        }

        public void setPowerCode (String powerCode) {
            this.powerCode = powerCode;
        }

        public String getPushCount () {
            return pushCount;
        }

        public void setPushCount (String pushCount) {
            this.pushCount = pushCount;
        }
    }
}
