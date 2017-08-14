package com.gta.zssx.fun.classroomFeedback.model.bean;

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
 * [Description]
 * <p> 课堂教学反馈首页列表数据
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/6/28 10:27.
 * @since 2.0.0
 */
public class ClassroomFeedbackBean implements Serializable {

    /**
     * ClassName : 14会计2
     * RegisterDataList : [{"WeekDate":1,"WeekNumber":"一","TimeSlot":"02月01日-02月05日","State":"-1"},{"WeekDate":2,"WeekNumber":"二","TimeSlot":"02月06日-02月12日","State":"-1"},{"WeekDate":3,"WeekNumber":"三","TimeSlot":"02月13日-02月19日","State":"-1"},{"WeekDate":4,"WeekNumber":"四","TimeSlot":"02月20日-02月26日","State":"-1"},{"WeekDate":5,"WeekNumber":"五","TimeSlot":"02月27日-03月05日","State":"-1"},{"WeekDate":6,"WeekNumber":"六","TimeSlot":"03月06日-03月12日","State":"-1"},{"WeekDate":7,"WeekNumber":"七","TimeSlot":"03月13日-03月19日","State":"-1"},{"WeekDate":8,"WeekNumber":"八","TimeSlot":"03月20日-03月26日","State":"-1"},{"WeekDate":9,"WeekNumber":"九","TimeSlot":"03月27日-04月02日","State":"-1"},{"WeekDate":10,"WeekNumber":"一十零","TimeSlot":"04月03日-04月09日","State":"-1"},{"WeekDate":11,"WeekNumber":"一十一","TimeSlot":"04月10日-04月16日","State":"-1"},{"WeekDate":12,"WeekNumber":"一十二","TimeSlot":"04月17日-04月23日","State":"-1"},{"WeekDate":13,"WeekNumber":"一十三","TimeSlot":"04月24日-04月30日","State":"-1"},{"WeekDate":14,"WeekNumber":"一十四","TimeSlot":"05月01日-05月07日","State":"-1"},{"WeekDate":15,"WeekNumber":"一十五","TimeSlot":"05月08日-05月14日","State":"-1"},{"WeekDate":16,"WeekNumber":"一十六","TimeSlot":"05月15日-05月21日","State":"-1"},{"WeekDate":17,"WeekNumber":"一十七","TimeSlot":"05月22日-05月28日","State":"-1"},{"WeekDate":18,"WeekNumber":"一十八","TimeSlot":"05月29日-06月04日","State":"-1"},{"WeekDate":19,"WeekNumber":"一十九","TimeSlot":"06月05日-06月11日","State":"-1"},{"WeekDate":20,"WeekNumber":"二十零","TimeSlot":"06月12日-06月18日","State":"-1"},{"WeekDate":21,"WeekNumber":"二十一","TimeSlot":"06月19日-06月25日","State":"-1"},{"WeekDate":22,"WeekNumber":"二十二","TimeSlot":"06月26日-07月02日","State":"-1"},{"WeekDate":23,"WeekNumber":"二十三","TimeSlot":"07月03日-07月07日","State":"-1"}]
     */

    private String ClassName;
    /**
     * WeekDate : 1
     * WeekNumber : 一
     * TimeSlot : 02月01日-02月05日
     * State : -1
     */

    private List<RegisterDataListBean> RegisterDataList;

    public String getClassName () {
        return ClassName;
    }

    public void setClassName (String ClassName) {
        this.ClassName = ClassName;
    }

    public List<RegisterDataListBean> getRegisterDataList () {
        return RegisterDataList;
    }

    public void setRegisterDataList (List<RegisterDataListBean> RegisterDataList) {
        this.RegisterDataList = RegisterDataList;
    }

    public static class RegisterDataListBean implements Serializable {
        private int WeekDate;
        private String WeekNumber;
        private String TimeSlot;
        private String State;
        private String ClassName;
        private boolean IsTitle;

        public String getClassName () {
            return ClassName;
        }

        public void setClassName (String className) {
            ClassName = className;
        }

        public boolean isTitle () {
            return IsTitle;
        }

        public void setTitle (boolean title) {
            IsTitle = title;
        }

        public int getWeekDate () {
            return WeekDate;
        }

        public void setWeekDate (int WeekDate) {
            this.WeekDate = WeekDate;
        }

        public String getWeekNumber () {
            return WeekNumber;
        }

        public void setWeekNumber (String WeekNumber) {
            this.WeekNumber = WeekNumber;
        }

        public String getTimeSlot () {
            return TimeSlot;
        }

        public void setTimeSlot (String TimeSlot) {
            this.TimeSlot = TimeSlot;
        }

        public String getState () {
            return State;
        }

        public void setState (String State) {
            this.State = State;
        }
    }

    public static List<ClassroomFeedbackBean> getClassroomFeedbackData (Context context) {
        InputStream inputStream;
        try {
            inputStream = context.getAssets ().open ("classroom_feedback.json");
            InputStreamReader reader = new InputStreamReader (inputStream);

            BufferedReader bufferedReader = new BufferedReader (reader);

            String readerData;
            StringBuilder stringBuffer = new StringBuilder ();
            while ((readerData = bufferedReader.readLine ()) != null) {
                stringBuffer.append (readerData);
            }

            reader.close ();
            bufferedReader.close ();
            inputStream.close ();
            LogUtil.e ("解析成功：" + stringBuffer.toString ());
            JSONObject jsonObject = new JSONObject (stringBuffer.toString ());
            JSONArray jsonArray = jsonObject.getJSONArray ("Data");

            return JsonHelper.
                    parseArray (jsonArray.toString (), ClassroomFeedbackBean.class);
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return null;
    }
}
