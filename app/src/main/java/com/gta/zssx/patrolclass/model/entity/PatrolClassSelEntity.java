package com.gta.zssx.patrolclass.model.entity;

import android.content.Context;
import android.support.annotation.Nullable;

import com.gta.zssx.pub.util.JsonHelper;
import com.gta.zssx.pub.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by liang.lu1 on 2016/7/13.
 */
public class PatrolClassSelEntity {


    /**
     * pId : 1
     * className : 13会计1
     * section : 第一节
     * status : 1
     * teacher : 张小三
     * deptName : 财经部
     * type : 1
     * getScoreList : [{"name":"教师分","score":"50"},{"name":"学生分","score":"75"},{"name":"卫生分","score":"80"}]
     */

    private String pId;
    private String className;
    private String section;
    private int status;
    private String teacher;
    private String deptName;
    private String type;
    private int classId;
    private String deptId;
    private int sectionId;
    /**
     * name : 教师分
     * score : 50
     */

    private List<GetScoreListBean> getScoreList;

    public String getDeptId () {
        return deptId;
    }

    public void setDeptId (String deptId) {
        this.deptId = deptId;
    }

    public int getSectionId () {
        return sectionId;
    }

    public void setSectionId (int sectionId) {
        this.sectionId = sectionId;
    }

    public String getpId () {
        return pId;
    }

    public void setpId (String pId) {
        this.pId = pId;
    }

    public int getClassId () {
        return classId;
    }

    public void setClassId (int classId) {
        this.classId = classId;
    }

    public String getPId () {
        return pId;
    }

    public void setPId (String pId) {
        this.pId = pId;
    }

    public String getClassName () {
        return className;
    }

    public void setClassName (String className) {
        this.className = className;
    }

    public String getSection () {
        return section;
    }

    public void setSection (String section) {
        this.section = section;
    }

    public int getStatus () {
        return status;
    }

    public void setStatus (int status) {
        this.status = status;
    }

    public String getTeacher () {
        return teacher;
    }

    public void setTeacher (String teacher) {
        this.teacher = teacher;
    }

    public String getDeptName () {
        return deptName;
    }

    public void setDeptName (String deptName) {
        this.deptName = deptName;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public List<GetScoreListBean> getGetScoreList () {
        return getScoreList;
    }

    public void setGetScoreList (List<GetScoreListBean> getScoreList) {
        this.getScoreList = getScoreList;
    }

    public static class GetScoreListBean {
        private String name;
        private String score;

        public String getName () {
            return name;
        }

        public void setName (String name) {
            this.name = name;
        }

        public String getScore () {
            return score;
        }

        public void setScore (String score) {
            this.score = score;
        }
    }

    @Nullable
    public static List<PatrolClassSelEntity> getDatas (Context context) {

        try {
            InputStream inputStream = context.getAssets ().open ("classsel.json");

            InputStreamReader reader = new InputStreamReader (inputStream);

            BufferedReader bufferedReader = new BufferedReader (reader);

            String readerData = "";
            StringBuffer stringBuffer = new StringBuffer ();
            while ((readerData = bufferedReader.readLine ()) != null) {
                stringBuffer.append (readerData);
            }

            reader.close ();
            bufferedReader.close ();
            inputStream.close ();
            LogUtil.e ("解析成功：" + stringBuffer.toString ());
            JSONObject jsonObject = new JSONObject (stringBuffer.toString ());
            JSONArray jsonArray = jsonObject.getJSONArray ("data");
            List<PatrolClassSelEntity> models = JsonHelper.
                    parseArray (jsonArray.toString (), PatrolClassSelEntity.class);

            return models;

        } catch (Exception e) {
            e.printStackTrace ();
        }
        return null;
    }
}
