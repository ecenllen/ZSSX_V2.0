package com.gta.zssx.fun.classroomFeedback.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.R;
import com.gta.zssx.fun.classroomFeedback.model.bean.ClassroomFeedbackBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.RegisterPageBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.RegisterPageDateBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.TransferDataBean;
import com.gta.zssx.fun.classroomFeedback.presenter.RegisterPresenter;
import com.gta.zssx.fun.classroomFeedback.view.RegisterView;
import com.gta.zssx.fun.classroomFeedback.view.adapter.RegisterPageAdapter;
import com.gta.zssx.fun.classroomFeedback.view.base.CustomLinearLayoutManager;
import com.gta.zssx.patrolclass.view.dialog.DeleteDialog;
import com.gta.zssx.pub.base.BaseMvpActivity;
import com.gta.zssx.pub.util.RxBus;
import com.gta.zssx.pub.util.TimeUtils;
import com.gta.zssx.pub.widget.CustomToast;

import java.util.List;

import rx.Subscriber;

import static com.gta.zssx.fun.classroomFeedback.view.page.RegisterDetailsActivity.CLASSROOM_REFRESH_STATE;

/**
 * [Description]
 * <p> 课堂教学反馈登记页面
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/06/20.
 * @since 2.0.0
 */
public class RegisterActivity extends BaseMvpActivity<RegisterView, RegisterPresenter> implements
        RegisterView, RegisterPageAdapter.RegisterPageItemListener, View.OnClickListener {
    public static final String HOME_PAGE_BEAN = "home_page_bean";
    public static final String CLASSROOM_BACK = "back";
    public static final String CLASSROOM_SUBMIT_SUCCESS = "classroom_submit_success";
    private LinearLayout mWeekDateLL;
    private LinearLayout mL;
    private LinearLayout mExamineLl;
    private LinearLayout mExamineIdeaLl;
    private RecyclerView mRecyclerView;
    private EditText mEditText;//编辑框
    private TextView mEmptyTv;
    private TextView mTextLengthTv;//显示填写意见或建议的字数变化
    private TextView mExaminePersonTv;//审核人
    private TextView mExamineDateTv;//审核日期
    private TextView mExamineOpinionTv;//审核意见
    private View mViewLineOne;
    private View mViewLineTwo;
    private Button mSaveEvaluate;//保存评价按钮
    private Button mSubmitBt;//送审
    private RegisterPageBean registerPageBean;
    private RegisterPageAdapter mAdapter;
    private ClassroomFeedbackBean.RegisterDataListBean bean;
    private List<RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean> allListBeen;
    private boolean isChanged;
    private boolean isRefresh;//refresh the current page data
    private boolean isShowMessage = true;
    private int currentPosition;//当前选中的TextView的坐标
    private String mOpinion;

    @Override
    public int getLayoutId () {
        return R.layout.activity_classroom_feedback_register;
    }

    protected void initView () {
        myHandler = new Handler ();
        initToolBar ();
        findViews ();
        setOnInteractListener ();
        setGone ();
    }

    private void findViews () {
        mL = (LinearLayout) findViewById (R.id.ll);
        mEmptyTv = (TextView) findViewById (R.id.tv_non);
        mWeekDateLL = (LinearLayout) findViewById (R.id.ll_classroom_feedback_register);
        mEditText = (EditText) findViewById (R.id.tv_teacher_proposal);
        mExamineDateTv = (TextView) findViewById (R.id.tv_examine_date);
        mExamineOpinionTv = (TextView) findViewById (R.id.tv_examine_content);
        mExaminePersonTv = (TextView) findViewById (R.id.tv_person);
        mRecyclerView = (RecyclerView) findViewById (R.id.recycler_register_page);
        mSaveEvaluate = (Button) findViewById (R.id.tv_save_evaluate);
        mSubmitBt = (Button) findViewById (R.id.button_submit);
        mTextLengthTv = (TextView) findViewById (R.id.tv_text_length);
        mExamineLl = (LinearLayout) findViewById (R.id.ll_examine);
        mExamineIdeaLl = (LinearLayout) findViewById (R.id.ll_examine_idea);
        mViewLineOne = findViewById (R.id.view1);
        mViewLineTwo = findViewById (R.id.view2);
    }

    private void initToolBar () {
        mToolBarManager.setTitle (bean.getWeekNumber ());
        mToolBarManager.getToolbar ().setNavigationOnClickListener (v -> onBack ());
    }

    protected void initData () {
        bean = (ClassroomFeedbackBean.RegisterDataListBean) getIntent ()
                .getExtras ().getSerializable (HOME_PAGE_BEAN);
    }

    @Override
    protected void requestData () {
        super.requestData ();
        presenter.getRegisterData (bean.getWeekDate (), bean.getWeekNumber (), "");
        RxBus ();
    }

    private void RxBus () {
        // RegisterDetailsActivity  SaveCacheData()
        addSubscription (RxBus.getDefault ().toObserverable (String.class)
                .subscribe (new Subscriber<String> () {
                    @Override
                    public void onCompleted () {

                    }

                    @Override
                    public void onError (Throwable e) {

                    }

                    @Override
                    public void onNext (String s) {
                        if (s.equals (RegisterDetailsActivity.SAVE_SUCCESS)) {
                            isRefresh = true;
                        }
                    }
                }));
    }

    /**
     * 当未通过的进来时，显示审核信息提示用户
     *
     * @param state 状态标示
     */
    private void showMessage (String state) {
        if (state.equals ("2")) {
            if (registerPageBean.getAuditorOpinion () != null && !registerPageBean.getAuditorOpinion ().equals ("")) {
                new DeleteDialog (this).showDialog (false, this.getResources ().getString (R.string.text_sure), registerPageBean.getAuditorOpinion (), null, false);
            }
        }
    }

    @Override
    protected void onResume () {
        super.onResume ();
        if (isRefresh) {
            presenter.getRegisterData (bean.getWeekDate (), bean.getWeekNumber (), RegisterDetailsActivity.SAVE_SUCCESS);
            isRefresh = false;
        }
    }

    public static void start (Context context, ClassroomFeedbackBean.RegisterDataListBean bean) {
        final Intent lIntent = new Intent (context, RegisterActivity.class);
        Bundle lBundle = new Bundle ();
        lBundle.putSerializable (HOME_PAGE_BEAN, bean);
        lIntent.putExtras (lBundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity (lIntent);
    }

    /**
     * 设置隐藏和显示  state "-1": 未登记；"0":未送审；"1":待审核；"3":已通过；"2":未通过；
     */
    private void setGone () {
        String state = bean.getState ();
        if (state.equals ("-1") || state.equals ("0")) {
            mExamineLl.setVisibility (View.GONE);
            mExamineIdeaLl.setVisibility (View.GONE);
            mViewLineOne.setVisibility (View.GONE);
            mViewLineTwo.setVisibility (View.GONE);
        } else if (state.equals ("1") || state.equals ("3")) {
            if (state.equals ("1")) {
                mExamineLl.setVisibility (View.GONE);
                mExamineIdeaLl.setVisibility (View.GONE);
                mViewLineOne.setVisibility (View.GONE);
                mViewLineTwo.setVisibility (View.GONE);
            }
            mSubmitBt.setVisibility (View.GONE);
            mEditText.setEnabled (false);
            mSaveEvaluate.setEnabled (false);
        }
    }

    private void setOnInteractListener () {
        /*禁用空格跟换行*/
        InputFilter filter = (source, start, end, dest, dart, dead) -> {
            if (source.equals (" ") || source.toString ().contentEquals ("\n"))
                return "";
            else
                return null;
        };
        /*最大字数限制*/
        InputFilter inputFilter = new InputFilter.LengthFilter (200);
        mEditText.addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged (Editable s) {
                mTextLengthTv.setText (s.length () + "/200");
                if (s.length () == 0) {
                    setEnabled (false);
                    return;
                }
                isChanged = !mEditText.getText ().toString ().equals (mOpinion);
                setEnabled (isChanged);
            }
        });
        CustomLinearLayoutManager mCustomLinearLayoutManager = new CustomLinearLayoutManager (this);
        mCustomLinearLayoutManager.setScrollEnabled (false);
        mRecyclerView.setLayoutManager (mCustomLinearLayoutManager);
        mRecyclerView.setHasFixedSize (true);
        mAdapter = new RegisterPageAdapter (this);
        mAdapter.setListener (this);
        mSaveEvaluate.setOnClickListener (this);
        mSubmitBt.setOnClickListener (this);
        mEditText.setFilters (new InputFilter[]{filter, inputFilter});
        mSaveEvaluate.setEnabled (false);
        mSaveEvaluate.setVisibility (mSaveEvaluate.isEnabled () ? View.VISIBLE : View.GONE);
    }

    @NonNull
    @Override
    public RegisterPresenter createPresenter () {
        return new RegisterPresenter ();
    }

    @Override
    public void showResult (RegisterPageBean registerPageBean) {
        this.registerPageBean = registerPageBean;
        if (bean.getState ().equals ("3")
                || bean.getState ().equals ("2")) {
            mExaminePersonTv.setText (registerPageBean.getAuditor ());
            mExamineDateTv.setText (registerPageBean.getAuditorDate ());
            mExamineOpinionTv.setText (registerPageBean.getAuditorOpinion ());
        }
        mOpinion = registerPageBean.getOpinion ();
        mEditText.setText (mOpinion);
        mEditText.setSelection (mEditText.getText ().length ());

        mAdapter.setSectionDataListBeen (presenter.getOneDaySectionData (registerPageBean, currentPosition));
        mRecyclerView.setAdapter (mAdapter);

        if (isShowMessage) {
            showMessage (bean.getState ());
            isShowMessage = false;
        }

        if (registerPageBean.getClassroomRegisterList ().get (currentPosition).isComplete ()) {
            View viewById = mWeekDateLL.getChildAt (currentPosition);
            viewById.setBackgroundResource (R.drawable.classroom_selector_item_press_bg_two);
        }
    }

    /**
     * 创建头部日期列表
     *
     * @param dateBeanList 日期实体
     */
    @Override
    public void createDateTextView (List<RegisterPageDateBean> dateBeanList) {
        mWeekDateLL.removeAllViews ();
        for (int i = 0; i < dateBeanList.size (); i++) {
            View view = LayoutInflater.from (this).inflate (R.layout.item_classroom_feedback_register, null);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams (0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            view.setLayoutParams (layoutParams);
            TextView mDateTv = (TextView) view.findViewById (R.id.tv_register_date);
            View viewLine = view.findViewById (R.id.view);
            int finalI = i;
            if (finalI == 0) {
                //默认进来第一个日期的textView设为选中加粗状态
                currentPosition = finalI;
                mDateTv.setTypeface (Typeface.defaultFromStyle (Typeface.BOLD));
                mDateTv.setSelected (true);
                viewLine.setVisibility (View.VISIBLE);
            }
            view.setOnClickListener (v -> {
                //设置点击选中的textView文本变粗
                if (currentPosition != finalI) {
                    mDateTv.setTypeface (Typeface.defaultFromStyle (Typeface.BOLD));
                    mDateTv.setSelected (true);
                    viewLine.setVisibility (View.VISIBLE);
                    View view1 = mWeekDateLL.getChildAt (currentPosition);
                    TextView tv = (TextView) view1.findViewById (R.id.tv_register_date);
                    View viewLine2 = view1.findViewById (R.id.view);
                    tv.setTypeface (Typeface.defaultFromStyle (Typeface.NORMAL));
                    tv.setSelected (false);
                    viewLine2.setVisibility (View.GONE);
                    //根据不同Position更新adapter
                    mAdapter.setSectionDataListBeen (presenter.getOneDaySectionData (registerPageBean, finalI));
                    mAdapter.notifyDataSetChanged ();
                    currentPosition = finalI;
                }
            });
            mWeekDateLL.addView (view);
            String s = dateBeanList.get (i).getDate ();
            StringBuilder mAddString = new StringBuilder ();
            for (int j = 0; j < s.length (); j++) {
                mAddString.append (s.charAt (j));
                if (dateBeanList.size () > 5) {
                    if (j == 1 || j == 4) {
                        mAddString.append ("\n");
                    }
                } else {
                    if (j == 1) {
                        mAddString.append ("\n");
                    }
                }
            }
            mDateTv.setText (mAddString.toString ());
            if (dateBeanList.get (i).isComplete ()) {
                view.setBackgroundResource (R.drawable.classroom_selector_item_press_bg_two);
            } else {
                view.setBackgroundResource (R.drawable.classroom_selector_item_press_bg_one);
            }
        }
    }

    /**
     * 所有节次数据
     *
     * @param sectionDataListBeanList 所有节次数据
     */
    @Override
    public void showAllSectionData (List<RegisterPageBean.ClassroomRegisterListBean.
            SectionDataListBean> sectionDataListBeanList) {
        this.allListBeen = sectionDataListBeanList;
    }

    @Override
    public void saveAuditContentSuccess (String s) {
        mOpinion = mEditText.getText ().toString ();
        ToastUtils.showShortToast (getString (R.string.gallery_save_file_success));
        RxBus.getDefault ().post (CLASSROOM_REFRESH_STATE);
        if (s.equals (CLASSROOM_BACK)) {
            onBackPressed ();
            return;
        }
        isChanged = false;
        setEnabled (false);
    }

    private Handler myHandler;

    @Override
    public void submitOneWeekDataSuccess () {
        RxBus.getDefault ().post (CLASSROOM_SUBMIT_SUCCESS);
        CustomToast.ToastWithImageShort (this, R.drawable.ic_submit_check, this.getResources ().getString (R.string.string_submit_success));
        myHandler.postDelayed (this :: finish, 1500); // 延期1.5秒后关闭当前Activity
    }

    @Override
    public void showEmpty () {
        mL.setVisibility (View.GONE);
        mEmptyTv.setVisibility (View.VISIBLE);
    }

    @Override
    public void RegisterPageItemClick (int position) {
        TransferDataBean dataBean = new TransferDataBean ();
        dataBean.setAllSectionBeanList (allListBeen);
        int index = 0;
        for (int i = 0; i < currentPosition; i++) {
            index += registerPageBean.getClassroomRegisterList ().get (i).getSectionDataList ().size ();
        }
        int mPosition = index + position;
        dataBean.setSectionBean (allListBeen.get (mPosition));
        dataBean.setState (bean.getState ());
        RegisterDetailsActivity.start (this, dataBean);
    }

    @Override
    public void onClick (View v) {
        if (TimeUtils.isFastDoubleClick ()) {
            return;
        }
        switch (v.getId ()) {
            case R.id.tv_save_evaluate:
                //保存评价
                presenter.saveAuditContent (bean.getWeekDate (), mEditText.getText ().toString (),
                        registerPageBean.getClassTeachId (), "");
                break;
            case R.id.button_submit:
                //送审
                submitJudge ();
                break;
            default:
                break;
        }

    }

    private void submitJudge () {
        if (mSaveEvaluate.isEnabled ()) {
            DeleteDialog mDeleteDialog = new DeleteDialog (RegisterActivity.this);
            mDeleteDialog.showDialog (false, getString (R.string.text_sure), getString (R.string.classroom_dialog_msg_five), null, false);
            return;
        }
        if (!isAllFinished ()) {
            DeleteDialog mDialog = new DeleteDialog (this);
            mDialog.showDialog ("", true, getString (R.string.text_sure), getString (R.string.classroom_dialog_msg_four),
                    () -> presenter.submitOneWeekData (bean.getWeekDate ()), null, true);
            mDialog.setExitText (getString (R.string.util_dialog_version_update_cancel));
            return;
        }
        presenter.submitOneWeekData (bean.getWeekDate ());
    }

    /**
     * 遍历所有是否已登记完成
     *
     * @return true or false;
     */
    private boolean isAllFinished () {
        for (int i = 0; i < registerPageBean.getClassroomRegisterList ().size (); i++) {
            if (!registerPageBean.getClassroomRegisterList ().get (i).isComplete ()) {
                return false;
            }
        }
        return true;
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
        if (mSaveEvaluate.isEnabled ()) {
            DeleteDialog dialog = new DeleteDialog (RegisterActivity.this);
            dialog.showDialog ("", true, getString (R.string.classroom_msg_yes), getString (R.string.classroom_dialog_msg_six),
                    () -> presenter.saveAuditContent (bean.getWeekDate (), mEditText.getText ().toString (),
                            registerPageBean.getClassTeachId (), CLASSROOM_BACK),
                    this :: onBackPressed, true);
            dialog.setExitText (getString (R.string.classroom_msg_no));
            return;
        }
        onBackPressed ();
    }

    private void setEnabled (boolean isEnabled) {
        if (isEnabled) {
            mSaveEvaluate.setEnabled (true);
            mSaveEvaluate.setBackgroundResource (R.drawable.classroom_save_evaluate_click);
            mSaveEvaluate.setVisibility (View.VISIBLE);
        } else {
            mSaveEvaluate.setEnabled (false);
            mSaveEvaluate.setBackgroundResource (R.drawable.classroom_save_evaluate);
            mSaveEvaluate.setVisibility (View.GONE);
        }
    }

}
