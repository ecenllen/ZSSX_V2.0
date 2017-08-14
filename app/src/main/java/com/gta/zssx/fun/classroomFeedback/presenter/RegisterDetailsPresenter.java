package com.gta.zssx.fun.classroomFeedback.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.classroomFeedback.model.ClassroomFeedbackManager;
import com.gta.zssx.fun.classroomFeedback.model.bean.RegisterDetailsBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.RegisterPageBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.SubmitSaveScoreBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.TransferDataBean;
import com.gta.zssx.fun.classroomFeedback.view.RegisterDetailsView;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * [Description]
 * <p> 课堂教学反馈登记详情页面
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/06/20.
 * @since 2.0.0
 */

public class RegisterDetailsPresenter extends BasePresenter<RegisterDetailsView> {
    private UserBean mUserBean;

    public RegisterDetailsPresenter () {
        super ();
        try {
            mUserBean = AppConfiguration.getInstance ().getUserBean ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public void loadData (RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean sectionBean) {
        if (!isViewAttached ())
            return;
        getView ().showLoadingDialog ();

        mCompositeSubscription.add (ClassroomFeedbackManager.getRegisterDetails (mUserBean.getClassId (), sectionBean.getSection (), sectionBean.getDate (), sectionBean.getWeekDate ())
                .subscribe (new Subscriber<RegisterDetailsBean> () {
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
                    public void onNext (RegisterDetailsBean detailsBean) {
                        if (isViewAttached ()) {
                            if (detailsBean == null) {
                                getView ().showEmpty ();
                                return;
                            }
                            getView ().showResult (detailsBean, sectionBean);
                        }
                    }
                }));
    }

    public List<RegisterDetailsBean.ScoreOptionsListBean.OptionsBean> getAdapterData (RegisterDetailsBean detailsBean) {
        List<RegisterDetailsBean.ScoreOptionsListBean.OptionsBean> optionsBeanList = new ArrayList<> ();
        assert detailsBean != null;
        for (RegisterDetailsBean.ScoreOptionsListBean bean :
                detailsBean.getScoreOptionsList ()) {
            RegisterDetailsBean.ScoreOptionsListBean.OptionsBean optionsBean = new RegisterDetailsBean.ScoreOptionsListBean.OptionsBean ();
            optionsBean.setTitle (true);
            optionsBean.setOptionId (bean.getDeductItemID ());
            optionsBean.setOptionTitle (bean.getDeductItemName ());
            optionsBeanList.add (optionsBean);
            optionsBeanList.addAll (bean.getOptions ());
        }
        return optionsBeanList;
    }

    /**
     * 打包保存评分的数据
     *
     * @param sectionBean 设有打包数据的实体
     * @return SubmitSaveScoreBean
     */
    public SubmitSaveScoreBean readySubmitData (RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean sectionBean) {
        SubmitSaveScoreBean bean = new SubmitSaveScoreBean ();
        bean.setClassID (mUserBean.getClassId ());
        bean.setClassTeachId (sectionBean.getClassTeachId ());
        SubmitSaveScoreBean.JsonBean jsonBean = new SubmitSaveScoreBean.JsonBean ();
        jsonBean.setDate (sectionBean.getDate ());
        jsonBean.setScore (sectionBean.getScore ());
        jsonBean.setSection (sectionBean.getSection ());
        jsonBean.setTeacherId (sectionBean.getTeacherID ());
        jsonBean.setCourseName (sectionBean.getCourseName ());
        jsonBean.setWeekDate (sectionBean.getWeekDate ());
        List<SubmitSaveScoreBean.JsonBean.SaveScoreOptionsListBean> saveScoreOptionsListBeen = new ArrayList<> ();
        for (RegisterDetailsBean.ScoreOptionsListBean.OptionsBean optionBean : sectionBean.getOptionsBeanList ()) {
            if (optionBean.isTitle ()) {
                SubmitSaveScoreBean.JsonBean.SaveScoreOptionsListBean scoreOptionBean = new SubmitSaveScoreBean.JsonBean.SaveScoreOptionsListBean ();
                scoreOptionBean.setDeductItemID (optionBean.getOptionId ());
                scoreOptionBean.setSaveOptionsList (new ArrayList<> ());
                saveScoreOptionsListBeen.add (scoreOptionBean);
            } else {
                if (optionBean.isCheckState ()) {
                    SubmitSaveScoreBean.JsonBean.SaveScoreOptionsListBean.SaveOptionsListBean saveOptionBean = new SubmitSaveScoreBean.JsonBean.SaveScoreOptionsListBean.SaveOptionsListBean ();
                    saveOptionBean.setOptionId (optionBean.getOptionId ());
                    List<SubmitSaveScoreBean.JsonBean.SaveScoreOptionsListBean.SaveOptionsListBean> optionList = saveScoreOptionsListBeen.get (saveScoreOptionsListBeen.size () - 1).getSaveOptionsList ();
                    optionList.add (saveOptionBean);
                }
            }
        }
        jsonBean.setSaveScoreOptionsList (saveScoreOptionsListBeen);
        bean.setJson (jsonBean);
        return bean;
    }

    /**
     * 保存评分
     *
     * @param submitSaveScoreBean 参数实体
     */
    public void saveScoreData (SubmitSaveScoreBean submitSaveScoreBean, int state, boolean isSave) {
        if (!isViewAttached ())
            return;
        getView ().showLoadingDialog ();

        mCompositeSubscription.add (ClassroomFeedbackManager.uploadScoreData (submitSaveScoreBean)
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
                            getView ().SaveCacheData (state, isSave);
                        }
                    }
                }));
    }

    public List<RegisterDetailsBean.ScoreOptionsListBean.OptionsBean> getAlso (List<RegisterDetailsBean.ScoreOptionsListBean.OptionsBean> list) {
        List<RegisterDetailsBean.ScoreOptionsListBean.OptionsBean> optionsBeanList = new ArrayList<> ();
        for (int i = 0; i < list.size (); i++) {
            RegisterDetailsBean.ScoreOptionsListBean.OptionsBean optionsBean = new RegisterDetailsBean.ScoreOptionsListBean.OptionsBean ();
            optionsBean.setOptionId (list.get (i).getOptionId ());
            optionsBean.setCheckState (list.get (i).isCheckState ());
            optionsBean.setOptionTitle (list.get (i).getOptionTitle ());
            optionsBean.setOptionScore (list.get (i).getOptionScore ());
            optionsBean.setTitle (list.get (i).isTitle ());
            optionsBeanList.add (optionsBean);
        }
        return optionsBeanList;
    }

    public void upDataOptions (List<RegisterDetailsBean.ScoreOptionsListBean.OptionsBean> mOptionsBeanList,
                               List<RegisterDetailsBean.ScoreOptionsListBean.OptionsBean> mOptionsList) {
        for (int i = 0; i < mOptionsBeanList.size (); i++) {
            if (!mOptionsBeanList.get (i).isTitle ()) {
                mOptionsBeanList.get (i).setCheckState (mOptionsList.get (i).isCheckState ());
            }
        }
    }

    /**
     * 判断当前的是不是第一节或者最后一节
     *
     * @param state 1 表示当前节次的前面节次  2 表示当前节次后面的节次
     * @return 没有了 返回true 还有返回 false
     */
    public boolean isTheLastOne (int state, RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean sectionBean, TransferDataBean transferBean) {
        int currentPosition = sectionBean.getCurrentPosition ();
        if (state == 1) {
            if (currentPosition == 0) {
                return true;
            }
            for (int i = currentPosition - 1; i >= 0; i--) {
                if (!transferBean.getAllSectionBeanList ().get (i).isEmpty ()) {
                    return false;
                }
            }
            return true;
        } else {
            if (currentPosition == transferBean.getAllSectionBeanList ().size () - 1) {
                return true;
            }
            for (int i = currentPosition + 1; i < transferBean.getAllSectionBeanList ().size (); i++) {
                if (!transferBean.getAllSectionBeanList ().get (i).isEmpty ()) {
                    return false;
                }
            }
            return true;
        }

    }
}
