package com.gta.zssx.patrolclass.model.entity;

/**
 * Created by liang.lu on 2017/4/18 16:57.
 */

public class RegisterInterfaceEntity {
    /**
     * String uId,
     * String LineDate,  格式为yyyy-MM-dd的时间
     * String WordsDate, 格式为yyyy年MM月dd日的时间
     * int classId,
     * int sectionId
     * String DeptId
     */
    private String Uid;
    private String LineDate;
    private String WordsDate;
    private int ClassId;
    private int SectionId;
    private String DeptId;

    public String getDeptId () {
        return DeptId;
    }

    public void setDeptId (String deptId) {
        DeptId = deptId;
    }

    public String getLineDate () {
        return LineDate;
    }

    public void setLineDate (String lineDate) {
        LineDate = lineDate;
    }

    public String getWordsDate () {
        return WordsDate;
    }

    public void setWordsDate (String wordsDate) {
        WordsDate = wordsDate;
    }

    public String getUid () {
        return Uid;
    }

    public void setUid (String uid) {
        Uid = uid;
    }

    public int getClassId () {
        return ClassId;
    }

    public void setClassId (int classId) {
        ClassId = classId;
    }

    public int getSectionId () {
        return SectionId;
    }

    public void setSectionId (int sectionId) {
        SectionId = sectionId;
    }
}
