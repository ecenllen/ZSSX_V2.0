package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lan.zheng on 2016/11/10.  值班列表
 */
public class DutyNoticeInfo implements Serializable {

    private int DutyId;            //值班主表Id
    private int DutyDetailId;      //值班明细id
    private String SwitchUserId;       //申请调班人Id姓名
    private String ToSwitchUserId;       //被申请调班人Id
    private String Time;       //时间
    private String DutyName;       //值班表名
    private List<LeadersEntity> Leaders;  //值班领导
    private int DutyType;      //安排类别
    private int ArrangeType;   //对应安排
    private int ShiftStatus;   //调班状态
    private int DutyOrCheckDuty;  //值班登记或值班检查

    public int getDutyId() {
        return DutyId;
    }

    public void setDutyId(int DutyId) {
        this.DutyId = DutyId;
    }

    public int getDutyDetailId() {
        return DutyDetailId;
    }

    public void setDutyDetailId(int DutyDetailId) {
        this.DutyDetailId = DutyDetailId;
    }

    public String getSwitchUserId() {
        return SwitchUserId;
    }

    public void setSwitchUserId(String SwitchUserId) {
        this.SwitchUserId = SwitchUserId;
    }

    public String getToSwitchUserId() {
        return ToSwitchUserId;
    }

    public void setToSwitchUserId(String ToSwitchUserId) {
        this.ToSwitchUserId = ToSwitchUserId;
    }

    public String getDutyName() {
        return DutyName;
    }

    public void setDutyName(String DutyName) {
        this.DutyName = DutyName;
    }

    public String getTime() {
            return Time;
        }

    public void setTime(String Time) {
            this.Time = Time;
        }

    public List<LeadersEntity> getLeaders() {
        return Leaders;
    }

    public void setLeaders(List<LeadersEntity> Leaders) {
        this.Leaders = Leaders;
    }


    public int getArrangeType() {
        return ArrangeType;
    }

    public void setArrangeType(int ArrangeType) {
        this.ArrangeType = ArrangeType;
    }

    public int getShiftStatus() {
        return ShiftStatus;
    }

    public void setShiftStatus(int ShiftStatus) {
        this.ShiftStatus = ShiftStatus;
    }

    public int getDutyType() {
        return DutyType;
    }

    public void setDutyType(int DutyType) {
        this.DutyType = DutyType;
    }

    public int getDutyOrCheckDuty() {
        return DutyOrCheckDuty;
    }

    public void setDutyOrCheckDuty(int DutyOrCheckDuty) {
        this.DutyOrCheckDuty = DutyOrCheckDuty;
    }

    public static class LeadersEntity {
        /**
         * 领导Id
         */
        private String LeaderId;
        /**
         * 领导姓名
         */
        private String LeaderName;

        public String getLeaderId() {
            return LeaderId;
        }

        public void setLeaderId(String LeaderId) {
            this.LeaderId = LeaderId;
        }

        public String getLeaderName() {
            return LeaderName;
        }

        public void setLeaderName(String LeaderName) {
            this.LeaderName = LeaderName;
        }
    }
}
