package com.gta.zssx.fun.classroomFeedback.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.classroomFeedback.model.ClassroomFeedbackManager;
import com.gta.zssx.fun.classroomFeedback.model.bean.ClassroomSaveEvaluateBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.RegisterPageBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.RegisterPageDateBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.SubmitOneWeekBean;
import com.gta.zssx.fun.classroomFeedback.view.RegisterView;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Subscriber;

/**
 * [Description]
 * <p> 课堂教学反馈登记页面
 * [How to use]
 * <p> 保存评价方法 saveAuditContent 获取登记详情信息方法 getRegisterData
 * [Tips]
 *
 * @author Created by liang.lu on 2017/06/20.
 * @since 2.0.0
 */

public class RegisterPresenter extends BasePresenter<RegisterView> {
    private UserBean mUserBean;
    private List<RegisterPageDateBean> dateBeanList = new ArrayList<> ();
    private SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd", Locale.getDefault ());
    private SimpleDateFormat sdf2 = new SimpleDateFormat ("yyyy年MM月dd日", Locale.getDefault ());

    public RegisterPresenter () {
        super ();
        try {
            mUserBean = AppConfiguration.getInstance ().getUserBean ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    private int index;

    public void getRegisterData (int weekly, String weeklyText, String state) {
        if (!isViewAttached ())
            return;
        getView ().showLoadingDialog ();

        mCompositeSubscription.add (ClassroomFeedbackManager.getClassroomRegisterData (mUserBean.getClassId (), weekly)
                .doOnNext (registerPageBean -> {
                    //                    dateBeanList = new ArrayList<> ();
                    if (state.equals ("")) {
                        List<RegisterPageBean.ClassroomRegisterListBean> classroomRegisterListBeen = registerPageBean.getClassroomRegisterList ();
                        for (RegisterPageBean.ClassroomRegisterListBean bean :
                                classroomRegisterListBeen) {
                            RegisterPageDateBean dateBean = new RegisterPageDateBean ();
                            dateBean.setComplete (bean.isComplete ());
                            dateBean.setDate (getWeek (bean.getWeekDate ()) + getDate (bean.getDate ()));
                            dateBeanList.add (dateBean);
                        }
                        getView ().createDateTextView (dateBeanList);
                    }
                }).doOnNext (registerPageBean -> {
                    index = 0;
                    List<RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean> sectionDataListBeanList = new ArrayList<> ();
                    List<RegisterPageBean.ClassroomRegisterListBean> classroomRegisterListBeen = registerPageBean.getClassroomRegisterList ();
                    for (int i = 0; i < classroomRegisterListBeen.size (); i++) {
                        RegisterPageBean.ClassroomRegisterListBean registerBean = classroomRegisterListBeen.get (i);
                        String date = registerBean.getDate ();
                        int weekDate = registerBean.getWeekDate ();
                        List<RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean> sectionList = registerBean.getSectionDataList ();
                        for (int j = 0; j < sectionList.size (); j++) {
                            RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean sectionBean = sectionList.get (j);
                            sectionBean.setSectionText (setSectionName (sectionBean.getSection ()));
                            sectionBean.setDate (date);
                            sectionBean.setDateText (getWeek (weekDate) + "（" + getDate (date) + "）");
                            sectionBean.setWeekDate (weekDate);
                            sectionBean.setWeekDateText (weeklyText);
                            sectionBean.setEmpty (sectionBean.getCourseName ().equals (""));
                            sectionBean.setCurrentPosition (index);
                            sectionBean.setOptionsBeanList (new ArrayList<> ());
                            sectionBean.setClassTeachId (registerPageBean.getClassTeachId ());
                            sectionDataListBeanList.add (sectionBean);
                            index++;
                        }
                    }
                    getView ().showAllSectionData (sectionDataListBeanList);
                }).subscribe (new Subscriber<RegisterPageBean> () {
                    @Override
                    public void onCompleted () {
                        if (isViewAttached ()) {
                            getView ().hideDialog ();
                        }
                    }

                    @Override
                    public void onError (Throwable e) {
                        if (isViewAttached ()) {
                            getView ().hideDialog ();
                            getView ().onErrorHandle (e);
                            getView ().showEmpty ();
                        }
                    }

                    @Override
                    public void onNext (RegisterPageBean registerPageBean) {
                        if (isViewAttached ()) {
                            if (registerPageBean == null) {
                                getView ().showEmpty ();
                                return;
                            }
                            getView ().showResult (registerPageBean);
                        }
                    }
                }));
    }

    public void saveAuditContent (int weekly, String content, int classTeacherId, String state) {
        if (!isViewAttached ())
            return;
        getView ().showLoadingDialog ();

        ClassroomSaveEvaluateBean evaluateBean = new ClassroomSaveEvaluateBean ();
        evaluateBean.setClassId (mUserBean.getClassId ());
        evaluateBean.setWeekly (weekly);
        evaluateBean.setContent (content);
        evaluateBean.setClassTeachId (classTeacherId);
        mCompositeSubscription.add (ClassroomFeedbackManager.saveAuditContent (evaluateBean)
                .subscribe (new Subscriber<String> () {
                    @Override
                    public void onCompleted () {
                        if (isViewAttached ()) {
                            getView ().hideDialog ();
                        }
                    }

                    @Override
                    public void onError (Throwable e) {
                        if (isViewAttached ()) {
                            getView ().hideDialog ();
                            getView ().onErrorHandle (e);
                        }
                    }

                    @Override
                    public void onNext (String s) {
                        if (isViewAttached ()) {
                            getView ().saveAuditContentSuccess (state);
                        }
                    }
                }));
    }

    public void submitOneWeekData (int weekDate) {
        if (!isViewAttached ())
            return;
        getView ().showLoadingDialog ();

        SubmitOneWeekBean oneWeekBean = new SubmitOneWeekBean ();
        oneWeekBean.setClassId (mUserBean.getClassId ());
        oneWeekBean.setWeekDate (weekDate);
        mCompositeSubscription.add (ClassroomFeedbackManager.submitOneWeekData (oneWeekBean)
                .subscribe (new Subscriber<String> () {
                    @Override
                    public void onCompleted () {
                        if (isViewAttached ()) {
                            getView ().hideDialog ();
                        }
                    }

                    @Override
                    public void onError (Throwable e) {
                        if (isViewAttached ()) {
                            getView ().hideDialog ();
                            getView ().onErrorHandle (e);
                        }
                    }

                    @Override
                    public void onNext (String s) {
                        if (isViewAttached ()) {
                            getView ().submitOneWeekDataSuccess ();
                        }
                    }
                }));
    }

    /**
     * 获取某一天的节次数据
     *
     * @param registerPageBean 实体
     * @param position         角标
     * @return 节次 Data
     */
    public List<RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean> getOneDaySectionData (RegisterPageBean registerPageBean, int position) {
        List<RegisterPageBean.ClassroomRegisterListBean> listBean = registerPageBean.getClassroomRegisterList ();
        return listBean.get (position).getSectionDataList ();
    }

    /**
     * 得到日期，格式：5月22日
     *
     * @param date 日期  2017-05-06
     * @return 日期
     */
    public String getDate (String date) {
        try {
            Date mDate = sdf.parse (date);
            String s;
            s = sdf2.format (mDate);
            return s.substring (5, 11);
        } catch (ParseException e) {
            e.printStackTrace ();
        }
        return null;
    }

    /**
     * 返回周几  格式：周五
     *
     * @param weekDate 周次  5
     * @return 周次
     */
    private String getWeek (int weekDate) {
        String mWeekDate = "";
        switch (weekDate) {
            case 1:
                mWeekDate = "周一";
                break;
            case 2:
                mWeekDate = "周二";
                break;
            case 3:
                mWeekDate = "周三";
                break;
            case 4:
                mWeekDate = "周四";
                break;
            case 5:
                mWeekDate = "周五";
                break;
            case 6:
                mWeekDate = "周六";
                break;
            case 7:
                mWeekDate = "周日";
                break;
        }
        return mWeekDate;
    }

    private String setSectionName (int section) {
        String sectionName = "";
        switch (section) {
            case 1:
                sectionName = "第一节";
                break;
            case 2:
                sectionName = "第二节";
                break;
            case 3:
                sectionName = "第三节";
                break;
            case 4:
                sectionName = "第四节";
                break;
            case 5:
                sectionName = "第五节";
                break;
            case 6:
                sectionName = "第六节";
                break;
            case 7:
                sectionName = "第七节";
                break;
            case 8:
                sectionName = "第八节";
                break;
            case 9:
                sectionName = "第九节";
                break;
            case 10:
                sectionName = "第十节";
                break;
            case 11:
                sectionName = "第十一节";
                break;
        }
        return sectionName;
    }

}
