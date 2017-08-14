package com.gta.zssx.fun.classroomFeedback.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.R;
import com.gta.zssx.fun.classroomFeedback.model.bean.RegisterDetailsBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.RegisterPageBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.TransferDataBean;
import com.gta.zssx.fun.classroomFeedback.presenter.RegisterDetailsPresenter;
import com.gta.zssx.fun.classroomFeedback.view.RegisterDetailsView;
import com.gta.zssx.fun.classroomFeedback.view.adapter.RegisterDetailsAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherSearchEntity;
import com.gta.zssx.patrolclass.view.dialog.DeleteDialog;
import com.gta.zssx.pub.base.BaseMvpActivity;
import com.gta.zssx.pub.util.RxBus;
import com.gta.zssx.pub.util.TimeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

import static com.gta.zssx.fun.classroomFeedback.view.page.ChangedTeacherActivity.STATE;
import static com.gta.zssx.fun.classroomFeedback.view.page.ChangedTeacherActivity.TEACHER_ID;
import static com.gta.zssx.fun.classroomFeedback.view.page.CourseSearchActivity.REGISTER_DATE;
import static com.gta.zssx.fun.classroomFeedback.view.page.CourseSearchActivity.RESULT_CODE_COURSE_SEARCH;
import static com.gta.zssx.patrolclass.view.page.ChooseTeacherActivity.STATUS_ONE;

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
public class RegisterDetailsActivity extends BaseMvpActivity<RegisterDetailsView, RegisterDetailsPresenter>
        implements View.OnClickListener, RegisterDetailsAdapter.ChangedUIScoreListener, RegisterDetailsView {
    public static final String SECTION_DATA = "section_data";
    public static final String WHICH_PAGE = "which_page";
    public static final String PAGE_CLASSROOM = "page_classroom";
    public static final String SAVE_SUCCESS = "save_success";
    public static final String CLASSROOM_REFRESH_STATE = "classroom_refresh_state";
    public static final int RESULT_CODE_CLASSROOM_COURSE = 9999;
    public static final int REQUEST_CODE = 111;
    private LinearLayout mL;
    private RecyclerView mOptionRecyclerView;
    private TextView mEmptyTv;
    private TextView mSectionTv;
    private TextView mTeacherNameTv;
    private TextView mCourseTv;
    private EditText mScoreEt;
    private TextView mUpSectionTv;
    private TextView mNextSectionTv;
    private TextView mSaveTv;
    private String mTeacherId;
    private String mTeacherName;
    private String mDate;
    private String mCourseId;
    private String mCourseName;
    private String mState;//状态 "-1": 未登记；"0":未送审；"1":待审核；"2":已通过；"3":未通过；
    private boolean isChangedScore;//判断是否有修改
    private boolean clickBack;//是否按返回键
    private TransferDataBean mTransferBean;
    private RegisterDetailsBean detailsBean;
    private RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean sectionBean;
    private List<RegisterDetailsBean.ScoreOptionsListBean.OptionsBean> mOptionsBeanList;//扣分项所有数据
    private Map<Integer, RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean> map = new HashMap<> ();
    private RegisterDetailsAdapter mOptionAdapter;
    private List<RegisterDetailsBean.ScoreOptionsListBean.OptionsBean> mOptionsList;

    @Override
    public int getLayoutId () {
        return R.layout.activity_classroom_register_details;
    }

    @NonNull
    @Override
    public RegisterDetailsPresenter createPresenter () {
        return new RegisterDetailsPresenter ();
    }

    @Override
    protected void initView () {
        intiToolBar (sectionBean);
        findViews ();
        setInteractListener ();
        setEnable ();
    }

    private void findViews () {
        mL = (LinearLayout) findViewById (R.id.ll);
        mEmptyTv = (TextView) findViewById (R.id.tv_non);
        mSectionTv = (TextView) findViewById (R.id.tv_section);
        mTeacherNameTv = (TextView) findViewById (R.id.tv_teacher_name);
        mScoreEt = (EditText) findViewById (R.id.edit_register_details);
        mUpSectionTv = (TextView) findViewById (R.id.tv_previous_section);
        mNextSectionTv = (TextView) findViewById (R.id.tv_next_section);
        mSaveTv = (TextView) findViewById (R.id.tv_save);
        mOptionRecyclerView = (RecyclerView) findViewById (R.id.recycler_classroom_register_details);
        mCourseTv = (TextView) findViewById (R.id.tv_course_name);
    }

    protected void initData () {
        mTransferBean = (TransferDataBean) getIntent ().getExtras ().getSerializable (SECTION_DATA);
        assert mTransferBean != null;
        sectionBean = mTransferBean.getSectionBean ();
        mState = mTransferBean.getState ();
    }

    private void intiToolBar (RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean sectionBean) {
        mToolBarManager.setTitle (sectionBean.getWeekDateText ())
                .setSamllTitle (true, sectionBean.getDateText ());
        mToolBarManager.getToolbar ().setNavigationOnClickListener (v -> onBack ());
    }

    @Override
    protected void requestData () {
        super.requestData ();
        presenter.loadData (sectionBean);
        RxBus ();
    }

    private void RxBus () {
        // chooseTeacher FINISH
        addSubscription (RxBus.getDefault ().toObserverable (ChooseTeacherSearchEntity.class)
                .subscribe (new Subscriber<ChooseTeacherSearchEntity> () {
                    @Override
                    public void onCompleted () {
                    }

                    @Override
                    public void onError (Throwable e) {
                    }

                    @Override
                    public void onNext (ChooseTeacherSearchEntity deptListBean) {
                        isChangedScore = true;
                        mTeacherId = deptListBean.getTeacherId ();
                        mTeacherName = deptListBean.getName ();
                        mTeacherNameTv.setText (mTeacherName);
                        setClickable ();
                    }
                }));
    }

    @Override
    public void showResult (RegisterDetailsBean Bean,
                            RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean sectionBean) {
        this.sectionBean = sectionBean;
        detailsBean = Bean;
        isChangedScore = false;
        mOptionsBeanList = presenter.getAdapterData (Bean);
        mOptionsList = presenter.getAlso (mOptionsBeanList);
        sectionBean.setOptionsBeanList (mOptionsBeanList);
        sectionBean.setOptionsList (mOptionsList);
        mTeacherId = sectionBean.getTeacherID ();
        mTeacherName = sectionBean.getTeacherName ();
        mDate = sectionBean.getDate ();
        mCourseId = sectionBean.getCourseId ();
        mCourseName = sectionBean.getCourseName ();
        upData ();
        if (mOptionAdapter == null) {
            mOptionAdapter = new RegisterDetailsAdapter (this, mState);
            mOptionAdapter.setListener (this);
            mOptionAdapter.setOptionsBeanList (mOptionsBeanList);
            mOptionAdapter.setTotalScore (detailsBean.getTotalScore ());
            mOptionRecyclerView.setAdapter (mOptionAdapter);
        } else {
            mOptionAdapter.setOptionsBeanList (mOptionsBeanList);
            mOptionAdapter.notifyDataSetChanged ();
        }
    }

    private void setEnable () {
        if (mState.equals ("1") || mState.equals ("3")) {
            mCourseTv.setEnabled (false);
            mTeacherNameTv.setEnabled (false);
            mScoreEt.setEnabled (false);
        }
    }

    public static void start (Context context, TransferDataBean bean) {
        final Intent lIntent = new Intent (context, RegisterDetailsActivity.class);
        Bundle mBundle = new Bundle ();
        mBundle.putSerializable (SECTION_DATA, bean);
        lIntent.putExtras (mBundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity (lIntent);
    }

    private void setInteractListener () {
        mOptionRecyclerView.setLayoutManager (new LinearLayoutManager (this));
        mOptionRecyclerView.setHasFixedSize (true);
        mTeacherNameTv.setOnClickListener (this);
        mUpSectionTv.setOnClickListener (this);
        mNextSectionTv.setOnClickListener (this);
        mSaveTv.setOnClickListener (this);
        mCourseTv.setOnClickListener (this);
        mSaveTv.setEnabled (false);
        mScoreEt.addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (s.length () > 1 && s.charAt (0) == '0') {
                    mScoreEt.setText ("0");
                    mScoreEt.setSelection (mScoreEt.getText ().length ());
                }
            }

            @Override
            public void afterTextChanged (Editable s) {
                if (s.length () == 0) {
                    setClickable (mSaveTv, false);
                    return;
                }
                if (!isChangedScore) {
                    if (!mScoreEt.getText ().toString ().equals (sectionBean.getScore () + "")) {
                        isChangedScore = true;
                    }
                }
                if (!mCourseTv.getText ().toString ().equals ("") && !mTeacherNameTv.getText ().equals ("")) {
                    setClickable (mSaveTv, isChangedScore);
                }
                if (s.length () > 0 && !s.toString ().equals ("-")
                        && Integer.valueOf (s.toString ()) > detailsBean.getTotalScore ()) {
                    DeleteDialog mDeleteDialog = new DeleteDialog (RegisterDetailsActivity.this);
                    mDeleteDialog.showDialog (false, RegisterDetailsActivity.this.getResources ().getString (R.string.text_sure), RegisterDetailsActivity.this.getResources ().getString (R.string.classroom_dialog_msg), null, false);
                    mScoreEt.setText ("");
                }
            }
        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_CODE_COURSE_SEARCH:
                    isChangedScore = true;
                    DetailItemShowBean.CourseInfoBean courseInfoBean =
                            (DetailItemShowBean.CourseInfoBean) data.getExtras ()
                                    .getSerializable (CourseSearchActivity.COURSE_INFO_BEAN);
                    assert courseInfoBean != null;
                    mCourseId = courseInfoBean.getCourseId ();
                    mCourseName = courseInfoBean.getCourseName ();
                    mCourseTv.setText (mCourseName);
                    setClickable ();
                    break;
            }
        }
    }

    @Override
    public void onClick (View v) {
        if (TimeUtils.isFastDoubleClick ()) {
            return;
        }
        switch (v.getId ()) {
            case R.id.tv_course_name:
                Intent mIntent = new Intent (this, CourseSearchActivity.class);
                Bundle mBundle = new Bundle ();
                mBundle.putString (TEACHER_ID, mTeacherId);
                mBundle.putString (REGISTER_DATE, mDate);
                mIntent.putExtras (mBundle);
                startActivityForResult (mIntent, REQUEST_CODE);
                break;
            case R.id.tv_teacher_name:
                Intent lIntent = new Intent (this, ChangedTeacherActivity.class);
                Bundle lBundle = new Bundle ();
                lBundle.putString (STATE, STATUS_ONE);
                if (mTeacherId != null) {
                    lBundle.putString (TEACHER_ID, mTeacherId);
                }
                lIntent.putExtras (lBundle);
                startActivity (lIntent);
                break;
            case R.id.tv_previous_section:
                clickPreviousOrNextSection (1);
                break;
            case R.id.tv_next_section:
                clickPreviousOrNextSection (2);
                break;
            case R.id.tv_save:
                clickPreviousOrNextSection (3);
                break;
        }
    }

    private void clickPreviousOrNextSection (int state) {
        if (state == 3) {
            presenter.saveScoreData (presenter.readySubmitData (getSectionBean ()), state, true);
        } else {
            if (isChangedScore) {
                //如果分数又变化
                DeleteDialog mDeleteDialog = new DeleteDialog (this);
                mDeleteDialog.showDialog ("", true, this.getResources ().getString (R.string.classroom_msg_yes), this.getResources ().getString (R.string.classroom_dialog_msg_two)
                        , () -> {
                            if (mCourseTv.getText ().equals ("") || mTeacherNameTv.getText ().equals ("")
                                    || mScoreEt.getText ().toString ().equals ("")) {
                                new DeleteDialog (this).showDialog (false, this.getResources ().getString (R.string.text_sure), this.getResources ().getString (R.string.classroom_dialog_msg_three), null, false);
                                return;
                            }
                            presenter.saveScoreData (presenter.readySubmitData (getSectionBean ()), state, true);
                        }
                        , () -> {
                            presenter.upDataOptions (mOptionsBeanList, mOptionsList);
                            SaveCacheData (state, false);
                        }, true);
                mDeleteDialog.setExitText (this.getResources ().getString (R.string.classroom_msg_no));
            } else {
                map.put (sectionBean.getCurrentPosition (), sectionBean);
                getData (state);
            }
        }

    }

    private void upData () {
        mCourseTv.setText (mCourseName);
        mTeacherNameTv.setText (mTeacherName);
        mSectionTv.setText (sectionBean.getSectionText ());
        if (!sectionBean.isSectionComplete ()) {
            mScoreEt.setText ("");
        } else {
            mScoreEt.setText (sectionBean.getScore () + "");
            mScoreEt.setSelection (mScoreEt.getText ().length ());
        }
        mScoreEt.setHint ("满分" + detailsBean.getTotalScore () + "分");
        setClickable (mUpSectionTv, !presenter.isTheLastOne (1, sectionBean, mTransferBean));
        setClickable (mNextSectionTv, !presenter.isTheLastOne (2, sectionBean, mTransferBean));
    }

    @Override
    public void changeScore (String score, int position, boolean isChanged) {
        isChangedScore = isChanged;
        mScoreEt.setText (score);
        mScoreEt.setSelection (mScoreEt.getText ().length ());
        setClickable ();
        if (Integer.valueOf (score) < 0) {
            new DeleteDialog (this).showDialog (false, getString (R.string.util_dialog_version_update_comfirm), getString (R.string.classroom_dialog_msg_seven)
                    , () -> mOptionAdapter.upData (position, false), false);
        }
    }

    /*第一次保存完，刷新首页，状态变成未送审*/
    private boolean isSaved = true;

    /**
     * 保存评分后的回调
     *
     * @param state 区分上一节，下一节，或者单独点保存
     */
    @Override
    public void SaveCacheData (int state, boolean isSave) {
        if (state != 3) {
            if (isSave) {
                upDataSection ();
                RxBus.getDefault ().post (SAVE_SUCCESS);
            }
            map.put (sectionBean.getCurrentPosition (), sectionBean);
            getData (state);
        } else {
            if (clickBack) {
                onBackPressed ();
                clickBack = false;
            }
            isChangedScore = false;
            upDataSection ();
            setClickable (mSaveTv, false);
            RxBus.getDefault ().post (SAVE_SUCCESS);
            ToastUtils.showShortToast (getString (R.string.gallery_save_file_success));
        }
        if (isSaved) {
            RxBus.getDefault ().post (CLASSROOM_REFRESH_STATE);
        }
        isSaved = false;
    }

    private void upDataSection () {
        presenter.upDataOptions (mOptionsList, mOptionsBeanList);
        sectionBean.setTeacherID (mTeacherId);
        sectionBean.setTeacherName (mTeacherNameTv.getText ().toString ());
        sectionBean.setScore (Integer.valueOf (mScoreEt.getText ().toString ()));
        sectionBean.setSectionComplete (true);
        sectionBean.setCourseId (mCourseId);
        sectionBean.setCourseName (mCourseName);
    }

    private RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean getSectionBean () {
        RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean sectionBean
                = new RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean ();
        sectionBean.setTeacherID (mTeacherId);
        sectionBean.setTeacherName (mTeacherNameTv.getText ().toString ());
        sectionBean.setScore (Integer.valueOf (mScoreEt.getText ().toString ()));
        sectionBean.setCourseId (mCourseId);
        sectionBean.setCourseName (mCourseName);
        sectionBean.setOptionsBeanList (mOptionsBeanList);
        sectionBean.setSection (this.sectionBean.getSection ());
        sectionBean.setDate (this.sectionBean.getDate ());
        sectionBean.setWeekDate (this.sectionBean.getWeekDate ());
        sectionBean.setClassTeachId (this.sectionBean.getClassTeachId ());
        return sectionBean;
    }

    /**
     * 点击上一节或下一节，获取本地数据   true 本地数据， false  网络请求
     *
     * @param state 区分上一节下一节  1  上一节    2 下一节
     * @return 是否有本地数据
     */
    public boolean getLocalData (int state) {
        int currentOption = sectionBean.getCurrentPosition ();
        if (state == 1) {
            for (int i = currentOption - 1; i >= 0; i--) {
                if (!mTransferBean.getAllSectionBeanList ().get (i).isEmpty ()) {
                    if (map.get (i) != null) {
                        upDataLocal (i);
                        return true;
                    }
                    break;
                }
            }
            return false;
        } else {
            for (int i = currentOption + 1; i < mTransferBean.getAllSectionBeanList ().size (); i++) {
                if (!mTransferBean.getAllSectionBeanList ().get (i).isEmpty ()) {
                    if (map.get (i) != null) {
                        upDataLocal (i);
                        return true;
                    }
                    break;
                }
            }
            return false;
        }
    }

    private void upDataLocal (int position) {
        sectionBean = map.get (position);
        mTeacherId = sectionBean.getTeacherID ();
        mTeacherName = sectionBean.getTeacherName ();
        mCourseId = sectionBean.getCourseId ();
        mCourseName = sectionBean.getCourseName ();
        mOptionsBeanList = sectionBean.getOptionsBeanList ();
        mOptionsList = sectionBean.getOptionsList ();
    }

    /**
     * 当点击上一节或下一节时，获取本地或服务器数据
     *
     * @param state 区分上一节下一节
     */
    public void getData (int state) {
        boolean b = getLocalData (state);
        if (b) {
            //获取到了本地数据，更新数据
            intiToolBar (sectionBean);
            isChangedScore = false;
            upData ();
            if (mOptionAdapter != null) {
                mOptionAdapter.setOptionsBeanList (sectionBean.getOptionsBeanList ());
                mOptionAdapter.notifyDataSetChanged ();
            }
        } else {
            if (state == 1) {
                //获取上一节的信息进行网络请求获取数据
                int currentOption = sectionBean.getCurrentPosition ();
                RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean sectionBeanPrevious = null;
                for (int i = currentOption - 1; i >= 0; i--) {
                    if (!mTransferBean.getAllSectionBeanList ().get (i).isEmpty ()) {
                        sectionBeanPrevious = mTransferBean.getAllSectionBeanList ().get (i);
                        break;
                    }
                }
                presenter.loadData (sectionBeanPrevious);
                intiToolBar (sectionBeanPrevious);
            } else {
                //获取下一节的信息进行网络请求获取数据
                int currentOption = sectionBean.getCurrentPosition ();
                RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean sectionBeanNext = null;
                for (int i = currentOption + 1; i < mTransferBean.getAllSectionBeanList ().size (); i++) {
                    if (!mTransferBean.getAllSectionBeanList ().get (i).isEmpty ()) {
                        sectionBeanNext = mTransferBean.getAllSectionBeanList ().get (i);
                        break;
                    }
                }
                presenter.loadData (sectionBeanNext);
                intiToolBar (sectionBeanNext);
            }
        }
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack ();
            return true;
        }
        return super.onKeyDown (keyCode, event);
    }

    private void onBack () {
        if (isChangedScore) {
            DeleteDialog mDeleteDialog = new DeleteDialog (this);
            mDeleteDialog.showDialog ("", true, this.getResources ().getString (R.string.classroom_msg_yes), this.getResources ().getString (R.string.classroom_dialog_msg_two), () -> {
                if (mCourseTv.getText ().equals ("") || mTeacherNameTv.getText ().equals ("")
                        || mScoreEt.getText ().toString ().equals ("")) {
                    new DeleteDialog (this).showDialog (false, this.getResources ().getString (R.string.text_sure), this.getResources ().getString (R.string.classroom_dialog_msg_three), null, false);
                    return;
                }
                clickBack = true;
                clickPreviousOrNextSection (3);
            }, this :: onBackPressed, true);
            mDeleteDialog.setExitText (this.getResources ().getString (R.string.classroom_msg_no));
            return;
        }
        onBackPressed ();
    }

    public void setClickable () {
        if (!mTeacherNameTv.getText ().equals ("") && !mCourseTv.getText ().toString ().equals ("")
                && !mScoreEt.getText ().toString ().equals ("") && Integer.valueOf (mScoreEt.getText ().toString ()) >= 0) {
            mSaveTv.setEnabled (true);
            mSaveTv.setBackgroundResource (R.drawable.circlecouner_button_yellow_selector);
        } else {
            mSaveTv.setEnabled (false);
            mSaveTv.setBackgroundResource (R.drawable.btn_background_yellow_selector);
        }
    }

    public void setClickable (TextView view, boolean clickAble) {
        if (clickAble) {
            view.setEnabled (true);
            if (view.equals (mSaveTv)) {
                view.setBackgroundResource (R.drawable.circlecouner_button_yellow_selector);
                return;
            }
            view.setBackgroundResource (R.drawable.circlecouner_button_green_selector);
        } else {
            view.setEnabled (false);
            if (view.equals (mSaveTv)) {
                view.setBackgroundResource (R.drawable.btn_background_yellow_selector);
                return;
            }
            view.setBackgroundResource (R.drawable.btn_background_blue_selector);
        }
    }

    @Override
    public void showEmpty () {
        mL.setVisibility (View.GONE);
        mEmptyTv.setVisibility (View.VISIBLE);
    }
}
