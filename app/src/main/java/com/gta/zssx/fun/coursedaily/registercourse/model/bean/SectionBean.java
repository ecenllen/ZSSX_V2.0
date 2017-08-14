package com.gta.zssx.fun.coursedaily.registercourse.model.bean;

import com.gta.utils.resource.L;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/20.
 * @since 1.0.0  用于内部存单个节次考勤学生状态数据,内部信息传递使用，非提交数据的Bean
 */
public class SectionBean implements Serializable {

//    private String sectionName;
//
//    private String flag;
//    private boolean isUse;
    /**
     * Lesson : “第1节”
     * SignStatus : 0
     * SectionID : 0
     */
    //登记节次状况相关：接口7
    public static int SIGN = 1;
    public static int UNSIGN = 0;
    private String Lesson;    //节次名称
    private String BeginTime; //开始时间
    private String EndTime;   //结束时间
    private int SignStatus;   //后台返回，登记状态：0代表未登记 1代表已登记
    private int SectionId;    //节次Id
    private int SectionOriginalId; //课表真实节次Id--新
    private DateTime BeginDateTime;   //日期
    private DateTime EndDateTime;     //日期

    //登记页面需要存储的数据--同步上一节课或者取消同步时使用，方便还原成没有同步的状态
    private String scoreString; //分数字符串--新
    private int score;  //分数
    private int delayCount; //迟到 --新
    private int leaveCount; //请假 -- 新
    private int absentCount;// 旷课 -- 新
    private int vocationCount;//公假 --新
    private String remark;  //备注--新
    private boolean haveBeenSignFlag;  //点击下一步时是否标记为被登记过

//    private String flag;
    private List<StudentListNewBean> mStudentListBeen ;   //该节次的学生信息

    public int getDelayCount() {
        return delayCount;
    }

    public void setDelayCount(int delayCount) {
        this.delayCount = delayCount;
    }

    public int getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(int leaveCount) {
        this.leaveCount = leaveCount;
    }

    public int getAbsentCount() {
        return absentCount;
    }

    public void setAbsentCount(int absentCount) {
        this.absentCount = absentCount;
    }

    public int getVocationCount() {
        return vocationCount;
    }

    public void setVocationCount(int vocationCount) {
        this.vocationCount = vocationCount;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean getHaveBeenSignFlag() {
        return haveBeenSignFlag;
    }

    public void setHaveBeenSignFlag(boolean haveBeenSignFlag) {
        this.haveBeenSignFlag = haveBeenSignFlag;
    }

    public DateTime getBeginDateTime() {
        return BeginDateTime;
    }

    public void setBeginDateTime(DateTime beginDateTime) {
        BeginDateTime = beginDateTime;
    }

    public DateTime getEndDateTime() {
        return EndDateTime;
    }

    public void setEndDateTime(DateTime endDateTime) {
        EndDateTime = endDateTime;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String beginTime) {
        BeginTime = beginTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getLesson() {
        return Lesson;
    }

    public void setLesson(String Lesson) {
        this.Lesson = Lesson;
    }

    public int getSignStatus() {
        return SignStatus;
    }

    public void setSignStatus(int SignStatus) {
        this.SignStatus = SignStatus;
    }

    public int getSectionId() {
        return SectionId;
    }
    public void setSectionId(int SectionId) {
        this.SectionId = SectionId;
    }

    public String getScoreString() {
        return scoreString;
    }
    public void setScoreString(String scoreString) {
        this.scoreString = scoreString;
    }

    public int getSectionOriginalId() {
        return SectionOriginalId;
    }

    public void setSectionOriginalId(int SectionOriginalId) {
        this.SectionOriginalId = SectionOriginalId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

   /* public boolean getIsSameWithPrevious(){
        return isSameWithPrevious;
    }

    public void setIsSameWithPrevious(boolean isSameWithPrevious){
        this.isSameWithPrevious = isSameWithPrevious;
    }*/

    public List<StudentListNewBean> getStudentListBeen() {
        return mStudentListBeen;
    }

    public void setStudentListBeen(List<StudentListNewBean> studentListBeen) {
        mStudentListBeen = studentListBeen;
    }

    //节次 -- 废弃
    public static final int SECTION_1 = 1;
    public static final int SECTION_2 = 2;
    public static final int SECTION_3 = 3;
    public static final int SECTION_4 = 4;
    public static final int SECTION_5 = 5;
    public static final int SECTION_6 = 6;
    public static final int SECTION_7 = 7;

}
