package com.gta.zssx.patrolclass.model.entity;

import android.content.Context;

import com.gta.zssx.pub.util.JsonHelper;
import com.gta.zssx.pub.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.List;

/**
 * Created by liang.lu on 2016/8/16 14:18.
 */
public class ChooseTeacherEntity implements Serializable{


    /**
     * name : 财经部
     * deptList : [{"deptId":1,"deptName":"财经部1","teacherCode":"","type":1},{"deptId":2,"deptName":"财经部2","teacherCode":"","type":1},{"deptId":3,"deptName":"财经部3","teacherCode":"","type":1},{"deptId":4,"deptName":"财经部4","teacherCode":"","type":1}]
     */

    private String name;
    /**
     * deptId : 1
     * deptName : 财经部1
     * teacherCode :
     * type : 1
     */

    private List<DeptListBean> deptList;

    public static List<ChooseTeacherEntity> getChooseTeacherDatas(Context context){
        try {
            InputStream inputStream = context.getAssets ().open ("teacherjson");

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
            List<ChooseTeacherEntity> models = JsonHelper.
                    parseArray (jsonArray.toString (), ChooseTeacherEntity.class);

            return models;

        } catch (Exception e) {
            LogUtil.e ("error = "+e.getMessage ());
            e.printStackTrace ();
        }
        return null;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public List<DeptListBean> getDeptList () {
        return deptList;
    }

    public void setDeptList (List<DeptListBean> deptList) {
        this.deptList = deptList;
    }

    public static class DeptListBean implements Serializable{
        private String deptId;
        private String deptName;
        private String teacherCode;
        private int type;

        public String getDeptId () {
            return deptId;
        }

        public void setDeptId (String deptId) {
            this.deptId = deptId;
        }

        public String getDeptName () {
            return deptName;
        }

        public void setDeptName (String deptName) {
            this.deptName = deptName;
        }

        public String getTeacherCode () {
            return teacherCode;
        }

        public void setTeacherCode (String teacherCode) {
            this.teacherCode = teacherCode;
        }

        public int getType () {
            return type;
        }

        public void setType (int type) {
            this.type = type;
        }
    }
}
