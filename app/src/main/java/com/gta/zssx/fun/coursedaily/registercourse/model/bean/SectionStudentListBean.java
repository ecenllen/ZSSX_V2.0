package com.gta.zssx.fun.coursedaily.registercourse.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lan.zheng on 2017/3/21.
 * 一个节次对应的学生列表-接口8修改
 */
public class SectionStudentListBean implements Serializable{
    private int SectionId;    //节次Id1,2,3,4,5
    private int SectionOriginalId; //真实的节次Id
    private List<StudentListNewBean> StudentMarked;  //学生状态列表

    public int getSectionId() {
        return SectionId;
    }

    public void setSectionId(int SectionId) {
        this.SectionId = SectionId;
    }

    public int getSectionOriginalId() {
        return SectionOriginalId;
    }

    public void setSectionOriginalId(int SectionOriginalId) {
        this.SectionOriginalId = SectionOriginalId;
    }

    public List<StudentListNewBean> getStudentListNewBeen(){
        return StudentMarked;
    }

    public void setStudentListNewBeenList(List<StudentListNewBean> studentListNewBeenList){
        this.StudentMarked = studentListNewBeenList;
    }
}
