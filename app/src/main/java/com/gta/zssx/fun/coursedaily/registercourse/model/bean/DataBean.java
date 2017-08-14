package com.gta.zssx.fun.coursedaily.registercourse.model.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/7/6.
 * @since 1.0.0  信息存储，内部信息传递使用，非提交数据的Bean
 */
public class DataBean implements Serializable{

    private Set<DetailItemShowBean.TeacherInfoBean> MultipleTeachersList;  //教师列表-new
    private Set<DetailItemShowBean.CourseInfoBean> MultipleCoursesList; //课程列表-new
    private int mClassId;    //班级Id
    private String mSignDate;   //登记日期
    private List<SectionBean> mSectionBeen;  //节次信息
    private boolean isMotify;  //是否是修改
    private String mTitle;  //班级名称
    private String Memo = "";  //备注
    private String ScoreString = ""; //分数-new
    private SectionBean sectionBeanNewSection; //修改进入，换了节次的登记节次，如第一节 --> 第二节

    public void setTeacherInfoBeanList(Set<DetailItemShowBean.TeacherInfoBean> MultipleTeachersList) {
        this.MultipleTeachersList = MultipleTeachersList;
    }

    public Set<DetailItemShowBean.TeacherInfoBean> getTeacherInfoBeanList() {
        return MultipleTeachersList;
    }

    public void setCourseInfoBeanList(Set<DetailItemShowBean.CourseInfoBean> MultipleCoursesList) {
        this.MultipleCoursesList = MultipleCoursesList;
    }

    public Set<DetailItemShowBean.CourseInfoBean> getCourseInfoBeanList() {
        return MultipleCoursesList;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }

    public String getScoreString() {
        return ScoreString;
    }

    public void setScoreString(String scoreString) {
        ScoreString = scoreString;
    }


    private int mOriginalSectionId;
    private String mOriginalSignDate;
    private int mOriginalClassId;


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getOriginalSectionId() {
        return mOriginalSectionId;
    }

    public void setOriginalSectionId(int originalSectionId) {
        mOriginalSectionId = originalSectionId;
    }

    public String getOriginalSignDate() {
        return mOriginalSignDate;
    }

    public void setOriginalSignDate(String originalSignDate) {
        mOriginalSignDate = originalSignDate;
    }

    public int getOriginalClassId() {
        return mOriginalClassId;
    }

    public void setOriginalClassId(int originalClassId) {
        mOriginalClassId = originalClassId;
    }



    public int getClassId() {
        return mClassId;
    }

    public void setClassId(int classId) {
        mClassId = classId;
    }

    public String getSignDate() {
        return mSignDate;
    }

    public void setSignDate(String signDate) {
        mSignDate = signDate;
    }

    public List<SectionBean> getSectionBeen() {
        return mSectionBeen;
    }

    public void setSectionBeen(List<SectionBean> sectionBeen) {
        mSectionBeen = sectionBeen;
    }

    public SectionBean getSectionBeanNewSection(){
        return sectionBeanNewSection;
    }

    public void setSectionBeanNewSection(SectionBean sectionBeanNewSection){
        this.sectionBeanNewSection = sectionBeanNewSection;
    }

    public boolean isMotify() {
        return isMotify;
    }

    public void setMotify(boolean motify) {
        isMotify = motify;
    }


    /**
     * 废弃
     */
    /*private List<PostSignBean.SectionBean> mPostSectionBean;  //提交的登记节次 - old
    public List<PostSignBean.SectionBean> getPostSectionBean() {
        return mPostSectionBean;
    }

    public void setPostSectionBean(List<PostSignBean.SectionBean> postSectionBean) {
        mPostSectionBean = postSectionBean;
    }*/

    /**
     * 废弃
     */
   /* private String mCourseName; //课程名称 - old
    public String getCourseName() {
        return mCourseName;
    }

    public void setCourseName(String courseName) {
        mCourseName = courseName;
    }*/

}
