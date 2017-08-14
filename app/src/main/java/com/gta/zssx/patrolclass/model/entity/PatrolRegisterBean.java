package com.gta.zssx.patrolclass.model.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/9/1.
 * @since 1.0.0
 */
public class PatrolRegisterBean implements Serializable {

    String autoScore;

    public void setScores (String[] scores) {
        this.scores = scores;
    }

    String[] scores = null;
    String date;

    public String getDate () {
        return date;
    }

    public void setDate (String date) {
        this.date = date;
    }

    String isType;
    int xId;
    int pId;
    String deptId;
    List<SubmitRegisterPatrol.JsonBean.GetScoreListBean> getScoreListBeens;
    private List<Map<String, String>> mEntityMap;
    private String teacherId;
    private int classId;
    private int sectionId;

    public int getSectionId () {
        return sectionId;
    }

    public void setSectionId (int sectionId) {
        this.sectionId = sectionId;
    }

    public int getClassId () {
        return classId;
    }

    public void setClassId (int classId) {
        this.classId = classId;
    }

    public String getTeacherId () {
        return teacherId;
    }

    public void setTeacherId (String teacherId) {
        this.teacherId = teacherId;
    }

    public String getAutoScore () {
        return autoScore;
    }

    public void setAutoScore (String autoScore) {
        this.autoScore = autoScore;
    }

    public String[] getScores () {
        return scores;
    }

    public String getIsType () {
        return isType;
    }

    public void setIsType (String isType) {
        this.isType = isType;
    }

    public int getxId () {
        return xId;
    }

    public void setxId (int xId) {
        this.xId = xId;
    }

    public int getpId () {
        return pId;
    }

    public void setpId (int pId) {
        this.pId = pId;
    }

    public String getDeptId () {
        return deptId;
    }

    public void setDeptId (String deptId) {
        this.deptId = deptId;
    }

    public List<SubmitRegisterPatrol.JsonBean.GetScoreListBean> getGetScoreListBeens () {
        return getScoreListBeens;
    }

    public void setGetScoreListBeens (List<SubmitRegisterPatrol.JsonBean.GetScoreListBean> getScoreListBeens) {
        this.getScoreListBeens = getScoreListBeens;
    }

    public List<Map<String, String>> getEntityMap () {
        return mEntityMap;
    }

    public void setEntityMap (List<Map<String, String>> entityMap) {
        mEntityMap = entityMap;
    }
}
