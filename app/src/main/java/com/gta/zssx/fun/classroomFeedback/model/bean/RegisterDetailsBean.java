package com.gta.zssx.fun.classroomFeedback.model.bean;

import android.content.Context;

import com.gta.zssx.pub.util.JsonHelper;
import com.gta.zssx.pub.util.LogUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.List;

/**
 * [Description]
 * <p> 登记详情数据（有扣分项的页面的）
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/6/28 10:27.
 * @since 2.0.0
 */
public class RegisterDetailsBean implements Serializable {

    /**
     * TotalScore : 90
     * ScoreOptionsList : [{"DeductItemName":"课堂效果","DeductItemID":123,"Options":[{"OptionTitle":"上课打学生（-5分）","OptionScore":"5","OptionId":111,"CheckState":"1"},{"OptionTitle":"上课打学生（-5分）","OptionScore":"5","OptionId":111,"CheckState":"1"},{"OptionTitle":"上课打学生（-5分）","OptionScore":"5","OptionId":111,"CheckState":"1"}]},{"DeductItemName":"课堂效果","Options":[{"OptionTitle":"上课打学生（-5分）","OptionScore":"5","OptionId":111,"CheckState":"1"},{"OptionTitle":"上课打学生（-5分）","OptionScore":"5","OptionId":111,"CheckState":"1"},{"OptionTitle":"上课打学生（-5分）","OptionScore":"5","OptionId":111,"CheckState":"1"}]},{"DeductItemName":"课堂效果","Options":[{"OptionTitle":"上课打学生（-5分）","OptionScore":"5","OptionId":111,"CheckState":"1"},{"OptionTitle":"上课打学生（-5分）","OptionScore":"5","OptionId":111,"CheckState":"1"},{"OptionTitle":"上课打学生（-5分）","OptionScore":"5","OptionId":111,"CheckState":"1"}]}]
     */

    private int TotalScore;
    /**
     * DeductItemName : 课堂效果
     * DeductItemID : 123
     * Options : [{"OptionTitle":"上课打学生（-5分）","OptionScore":"5","OptionId":111,"CheckState":"1"},{"OptionTitle":"上课打学生（-5分）","OptionScore":"5","OptionId":111,"CheckState":"1"},{"OptionTitle":"上课打学生（-5分）","OptionScore":"5","OptionId":111,"CheckState":"1"}]
     */

    private List<ScoreOptionsListBean> ScoreOptionsList;

    public int getTotalScore () {
        return TotalScore;
    }

    public void setTotalScore (int TotalScore) {
        this.TotalScore = TotalScore;
    }

    public List<ScoreOptionsListBean> getScoreOptionsList () {
        return ScoreOptionsList;
    }

    public void setScoreOptionsList (List<ScoreOptionsListBean> ScoreOptionsList) {
        this.ScoreOptionsList = ScoreOptionsList;
    }

    public static class ScoreOptionsListBean {
        private String DeductItemName;
        private int DeductItemID;
        /**
         * OptionTitle : 上课打学生（-5分）
         * OptionScore : 5
         * OptionId : 111
         * CheckState : 1
         */

        private List<OptionsBean> Options;

        public String getDeductItemName () {
            return DeductItemName;
        }

        public void setDeductItemName (String DeductItemName) {
            this.DeductItemName = DeductItemName;
        }

        public int getDeductItemID () {
            return DeductItemID;
        }

        public void setDeductItemID (int DeductItemID) {
            this.DeductItemID = DeductItemID;
        }

        public List<OptionsBean> getOptions () {
            return Options;
        }

        public void setOptions (List<OptionsBean> Options) {
            this.Options = Options;
        }

        public static class OptionsBean {
            private String OptionTitle;
            private int OptionScore;
            private int OptionId;
            private boolean CheckState = false;
            private boolean IsTitle;

            public boolean isTitle () {
                return IsTitle;
            }

            public void setTitle (boolean title) {
                IsTitle = title;
            }

            public String getOptionTitle () {
                return OptionTitle;
            }

            public void setOptionTitle (String OptionTitle) {
                this.OptionTitle = OptionTitle;
            }

            public int getOptionScore () {
                return OptionScore;
            }

            public void setOptionScore (int OptionScore) {
                this.OptionScore = OptionScore;
            }

            public int getOptionId () {
                return OptionId;
            }

            public void setOptionId (int OptionId) {
                this.OptionId = OptionId;
            }

            public boolean isCheckState () {
                return CheckState;
            }

            public void setCheckState (boolean checkState) {
                CheckState = checkState;
            }
        }
    }

    public static RegisterDetailsBean getRegisterDetailsBean (Context context) {
        try {
            InputStream inputStream = context.getAssets ().open ("register_details.json");

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

            return JsonHelper.parseObject (jsonObject.toString (), RegisterDetailsBean.class);

        } catch (Exception e) {
            e.printStackTrace ();
        }
        return null;
    }
}
