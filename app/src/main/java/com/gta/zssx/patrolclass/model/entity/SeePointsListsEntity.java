package com.gta.zssx.patrolclass.model.entity;

import android.content.Context;
import android.support.annotation.Nullable;

import com.gta.zssx.pub.util.JsonHelper;
import com.gta.zssx.pub.util.LogUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.List;

/**
 * Created by liang.lu1 on 2016/7/14.
 */
public class SeePointsListsEntity implements Serializable {


    /**
     * deptName : 财经部
     * section : 第三节
     * status : 2
     * teacher : 张云龙
     * getScoreList : [{"name":"教师分","scoreOptions":[{"score":"-10","content":"学生伏台或睡觉"},{"content":"老师迟到10分钟内","score":"-10"},{"content":"老师迟到10分钟以上或者不再教师","score":"-20"},{"content":"正课改自习课","score":"-10"},{"content":"上课玩手机","score":"-10"},{"content":"提前下课","score":"-10"},{"content":"讥讽挖苦学生、体罚或变相体罚学生","score":"-10"},{"content":"坐着上课但未及时改正","score":"-10"},{"content":"缺课，整节课记为","score":"-100"}]},{"name":"学生分","scoreOptions":[{"content":"学生迟到","score":"-1"},{"content":"课堂吵闹，秩序混乱","score":"-5"}]},{"name":"卫生分","scoreOptions":[{"content":"教室、实习室无人，照明灯、空调、风扇打开","score":"-10"},{"content":"上课期间班级门锁上","score":"-5"}]}]
     */

    private String deptName;
    private String section;
    private int status;
    private String teacher;
    /**
     * name : 教师分
     * scoreOptions : [{"score":"-10","content":"学生伏台或睡觉"},{"content":"老师迟到10分钟内","score":"-10"},{"content":"老师迟到10分钟以上或者不再教师","score":"-20"},{"content":"正课改自习课","score":"-10"},{"content":"上课玩手机","score":"-10"},{"content":"提前下课","score":"-10"},{"content":"讥讽挖苦学生、体罚或变相体罚学生","score":"-10"},{"content":"坐着上课但未及时改正","score":"-10"},{"content":"缺课，整节课记为","score":"-100"}]
     */

    private List<GetScoreListBean> getScoreList;

    public String getDeptName () {
        return deptName;
    }

    public void setDeptName (String deptName) {
        this.deptName = deptName;
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

    public List<GetScoreListBean> getGetScoreList () {
        return getScoreList;
    }

    public void setGetScoreList (List<GetScoreListBean> getScoreList) {
        this.getScoreList = getScoreList;
    }

    public static class GetScoreListBean {
        private String name;
        private String score;

        public String getScore () {
            return score;
        }

        public void setScore (String score) {
            this.score = score;
        }

        /**
         * score : -10
         * content : 学生伏台或睡觉
         */

        private List<ScoreOptionsBean> scoreOptions;

        public String getName () {
            return name;
        }

        public void setName (String name) {
            this.name = name;
        }

        public List<ScoreOptionsBean> getScoreOptions () {
            return scoreOptions;
        }

        public void setScoreOptions (List<ScoreOptionsBean> scoreOptions) {
            this.scoreOptions = scoreOptions;
        }

        public static class ScoreOptionsBean {
            private String score;
            private String content;

            public String getScore () {
                return score;
            }

            public void setScore (String score) {
                this.score = score;
            }

            public String getContent () {
                return content;
            }

            public void setContent (String content) {
                this.content = content;
            }
        }
    }

    @Nullable
    public static SeePointsListsEntity getDatas(Context context){

        try {
            InputStream inputStream = context.getAssets ().open ("text");

            InputStreamReader reader = new InputStreamReader (inputStream);

            BufferedReader bufferedReader = new BufferedReader (reader);

            String readerData = "";
            StringBuffer stringBuffer = new StringBuffer ();
            while ((readerData=bufferedReader.readLine ())!=null){
                stringBuffer.append (readerData);
            }
            reader.close ();
            bufferedReader.close ();
            inputStream.close ();
            LogUtil.e ("解析成功："+stringBuffer.toString ());
            JSONObject jsonObject = new JSONObject (stringBuffer.toString ());
            SeePointsListsEntity model = JsonHelper.parseObject (jsonObject.toString (),SeePointsListsEntity.class);

            return model;

        } catch (Exception e) {
            e.printStackTrace ();
        }
        return null;
    }

}
