package com.gta.zssx.patrolclass.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.patrolclass.fragment.PatrolPointsDetailsFragment;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherSearchEntity;
import com.gta.zssx.patrolclass.model.entity.DockScorePageBean;
import com.gta.zssx.patrolclass.model.entity.PatrolRegisterBeanNew;
import com.gta.zssx.patrolclass.presenter.DockScoreNewPresenter;
import com.gta.zssx.patrolclass.view.DockScoreNewView;
import com.gta.zssx.patrolclass.view.adapter.DockScoreNewAdapter2;
import com.gta.zssx.patrolclass.view.adapter.DockScoreNewAdapter2.DockScoreListChangedListener;
import com.gta.zssx.patrolclass.view.dialog.DeleteDialog;
import com.gta.zssx.patrolclass.view.dialog.ReuseScoreDialog;
import com.gta.zssx.pub.base.BaseMvpActivity;
import com.gta.zssx.pub.util.RxBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

import static com.gta.zssx.R.id.text_view_change_teacher_click;
import static com.gta.zssx.patrolclass.view.page.ChooseTeacherActivity.WHICH_PAGE;

/**
 * 扣分详情页
 * Created by liang.lu on 2017/3/9 16:59.
 */

public class DockScoreNewActivity extends BaseMvpActivity<DockScoreNewView, DockScoreNewPresenter>
        implements DockScoreNewAdapter2.InputScoreErrorListener, RadioGroup.OnCheckedChangeListener
        , View.OnClickListener, DockScoreListChangedListener, ReuseScoreDialog.ReuseScoreDialogListener {
    public static final String OPTION_BEANS = "optionBeans";
    public static final String AUTO_SCORE = "1"; // 自动计算
    public static final String UN_AUTO_SCORE = "2"; // 非自动计算
    public static final String DOCK_SCORE_NEW_PAGE = "dock_score_new_page";
    private List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> mOptionBeans;
    private String mTeacherName;
    private String mTeacherId;
    private String mAutoScore; // 区分自动计算 1  非自动计算 2
    private TextView mTeacherNameTv;
    private TextView mChangeTeacherTv;
    private TextView mReuseScoreTv;
    private RadioGroup mRadioGroup;
    private RecyclerView mRecyclerView;
    private TextView mSaveTv;
    private LinearLayout mLinearLayout;
    private ScrollView mScrollView;
    private DockScoreNewAdapter2 mAdapter;
    private Map<Integer, String> unAutoScoreMap; // 用来保存非自动计算各项的得分
    private int mPosition;
    private ArrayList<PatrolRegisterBeanNew.TeacherListBean> mTeacherBeanList;

    @NonNull
    @Override
    public DockScoreNewPresenter createPresenter () {
        return new DockScoreNewPresenter ();
    }

    public static void start (Context context, DockScorePageBean mDockScorePageBean) {
        final Intent lIntent = new Intent (context, DockScoreNewActivity.class);
        Bundle mBundle = new Bundle ();
        mBundle.putSerializable (OPTION_BEANS, mDockScorePageBean);
        lIntent.putExtras (mBundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity (lIntent);
    }

    @Override
    public int getLayoutId () {
        return R.layout.activity_dock_score_new;
    }

    @Override
    protected void initView () {
        findViews ();
        initToolbar ();
        setInteractListener ();
    }

    private void initToolbar () {
        mToolBarManager
                .showBack (true)
                .setTitle (R.string.dock_score_edit);
    }

    private void setInteractListener () {
        mRadioGroup.setOnCheckedChangeListener (this);
        mReuseScoreTv.setOnClickListener (this);
        mSaveTv.setOnClickListener (this);
        mChangeTeacherTv.setOnClickListener (this);
        mTeacherNameTv.setOnClickListener (this);
        setPointsItemListener ();
    }

    private void findViews () {
        mTeacherNameTv = (TextView) findViewById (R.id.text_view_dock_score_teacher_name);
        mChangeTeacherTv = (TextView) findViewById (text_view_change_teacher_click);
        mReuseScoreTv = (TextView) findViewById (R.id.text_view_reuse_fraction_click);
        mRadioGroup = (RadioGroup) findViewById (R.id.radio_group_dock_score);
        mRecyclerView = (RecyclerView) findViewById (R.id.recycler_view_dock_score);
        mSaveTv = (TextView) findViewById (R.id.text_view_save);
        mLinearLayout = (LinearLayout) findViewById (R.id.linear_layout_dock_score);
        mScrollView = (ScrollView) findViewById (R.id.scrollView);
    }

    protected void initData () {
        mTeacherBeanList =
                (ArrayList<PatrolRegisterBeanNew.TeacherListBean>) getIntent ()
                        .getSerializableExtra (OPTION_BEANS);
        mPosition = getIntent ().getIntExtra (PatrolPointsDetailsFragment.POSITION, 0);

        readyOptions (mTeacherBeanList);
        mTeacherName = mTeacherBeanList.get (mPosition).getTeacherName ();
        mTeacherId = mTeacherBeanList.get (mPosition).getTeacherId ();
        mAutoScore = mTeacherBeanList.get (mPosition).getAutoScore ();
        //  chooseTeacher finish
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
                        mTeacherId = deptListBean.getTeacherId ();
                        mTeacherName = deptListBean.getName ();
                        mTeacherNameTv.setText (mTeacherName);

                    }
                }));
    }

    @Override
    protected void requestData () {
        super.requestData ();
        mRadioGroup.check (mAutoScore.equals (AUTO_SCORE) ? R.id.radio_button_automatic
                : R.id.radio_button_manual_input);
        mRecyclerView.setVisibility (mAutoScore.equals (AUTO_SCORE) ? View.VISIBLE : View.GONE);
        mScrollView.setVisibility (mAutoScore.equals (UN_AUTO_SCORE) ? View.VISIBLE : View.GONE);
        mRecyclerView.setLayoutManager (new LinearLayoutManager (this));
        mRecyclerView.setHasFixedSize (true);
        mAdapter = new DockScoreNewAdapter2 (mOptionBeans);
        mAdapter.setInputScoreErrorListener (this);
        mAdapter.setListChangedListener (this);
        mAdapter.setRecyclerView (mRecyclerView);
        //mAdapter.setmOptionBeans (mOptionBeans);
        //mAdapter.setListener (this);
        //mAdapter.setTitleTextChangeListener (this);
        mRecyclerView.setAdapter (mAdapter);
        mTeacherNameTv.setText (mTeacherName);

        createdScoreTitle (mTeacherBeanList.get (mPosition));
    }

    /**
     * 把TeacherListBean变成option的集合，adapter中要用此数据
     *
     * @param mTeacherBeanList mTeacherBeanList
     */
    private void readyOptions (ArrayList<PatrolRegisterBeanNew.TeacherListBean> mTeacherBeanList) {
        if (mOptionBeans != null) {
            mOptionBeans = null;
        }
        mOptionBeans = new ArrayList<> ();

        for (int i = 0, size = mTeacherBeanList.get (mPosition).getDockScore ().size (); i < size; i++) {
            PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean bean =
                    new PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean ();
            bean.setIsTitle (true);
            bean.setTitle (mTeacherBeanList.get (mPosition).getDockScore ().get (i).getName ());
            bean.setItemType (1);
            bean.setGetScore (mTeacherBeanList.get (mPosition).getDockScore ().get (i).getGetAllScore ());
            bean.setAllScore (mTeacherBeanList.get (mPosition).getDockScore ().get (i).getAllScore ());
            bean.setId (mTeacherBeanList.get (mPosition).getDockScore ().get (i).getId ());

            mOptionBeans.add (bean);
            List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> options =
                    mTeacherBeanList.get (mPosition).getDockScore ().get (i).getOptions ();
            for (int j = 0, ss = options.size (); j < ss; j++) {
                if (options.get (j).getGetScore () != null && options.get (j).getGetScore ()
                        .length () > 0) {
                    options.get (j).setIsCheck (true);
                }
                mOptionBeans.add (options.get (j));
                //                }
            }
        }
    }

    /**
     * adapter 的回调方法，当输入错误时调用，给出提示信息
     *
     * @param position position
     */
    @Override
    public void onInputScoreError (int position) {
        if (mOptionBeans.get (position).isIsTitle ()) {
            showErrorDialog (this.getResources ().getString (R.string.error_title_score));
        } else {
            showErrorDialog (this.getResources ().getString (R.string.input_score_error));
        }
    }

    @Override
    public void onCheckedChanged (RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_button_automatic:
                mRecyclerView.setVisibility (View.VISIBLE);
                mScrollView.setVisibility (View.GONE);
                mAutoScore = AUTO_SCORE;
                break;
            case R.id.radio_button_manual_input:
                mRecyclerView.setVisibility (View.GONE);
                mScrollView.setVisibility (View.VISIBLE);
                mAutoScore = UN_AUTO_SCORE;
                break;
        }
    }

    int titleSize = 0;

    /**
     * 动态创建手动扣分项
     *
     * @param mTeacherBean mTeacherBean
     */
    private void createdScoreTitle (PatrolRegisterBeanNew.TeacherListBean mTeacherBean) {
        if (mTeacherBean == null) {
            return;
        }

        titleSize = mTeacherBean.getDockScore ().size ();
        unAutoScoreMap = new HashMap<> ();
        for (int i = 0; i < titleSize; i++) {
            View view = LayoutInflater.from (this).inflate (R.layout.layout_score_title, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams (ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams (layoutParams);
            mLinearLayout.addView (view);
            TextView titleTv = (TextView) view.findViewById (R.id.tv_score_title_t);
            titleTv.setText (mTeacherBean.getDockScore ().get (i).getName ());
            EditText editText = (EditText) view.findViewById (R.id.tv_score_title_teacherScore);
            editText.setText (mTeacherBean.getDockScore ().get (i).getUnAutoScore ());
            editText.setBackgroundResource (R.drawable.shape_text_view_enable_bg);
            if (!mTeacherBean.getDockScore ().get (i).getUnAutoScore ()
                    .equals (mTeacherBean.getDockScore ().get (i).getAllScore ())) {
                editText.setTextColor (this.getResources ().getColor (R.color.red));
            }
            unAutoScoreMap.put (i, editText.getText ().toString ());
        }
    }

    Map<Integer, String> map;

    /**
     * 非自动计算时，给每个动态输入项添加输入的监听
     */
    private void setPointsItemListener () {
        map = new HashMap<> ();
        for (int i = 0; i < titleSize; i++) {
            View itemV = mLinearLayout.getChildAt (i);
            EditText itemEt = (EditText) itemV.findViewById (R.id.tv_score_title_teacherScore);
            map.put (i, itemEt.getText ().toString ());
            int finalI = i;
            itemEt.setOnFocusChangeListener ((v, hasFocus) -> {
                EditText editText = (EditText) v;
                if (editText.getText ().toString ().equals ("") && !hasFocus) {
                    editText.setText (map.get (finalI));
                    editText.setSelection (map.get (finalI).length ());
                }
            });
            final String[] beforeText = {""};
            itemEt.addTextChangedListener (new TextWatcher () {
                @Override
                public void beforeTextChanged (CharSequence s, int start, int count, int after) {
                    beforeText[0] = s.toString ();
                }

                @Override
                public void onTextChanged (CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged (Editable s) {
                    String s1 = setTextChangedListener (s, beforeText, mTeacherBeanList.get (mPosition)
                            .getDockScore ().get (finalI), itemEt);
                    unAutoScoreMap.put (finalI, s1);
                }
            });
        }
    }

    /**
     * 设置非自动计算时，各得分项所得分数的字体颜色，若每项得分小于该项的总分，则分数字体颜色为红色
     * 设置非自动计算时，修改各得分项分数时的监听
     *
     * @param s             修改后的分数
     * @param dockScoreBean 标题实体
     * @param itemEt        editText 输入控件
     * @param beforeText    改变之前的分数
     */
    private String setTextChangedListener (Editable s, String[] beforeText, PatrolRegisterBeanNew.
            TeacherListBean.DockScoreBean dockScoreBean, EditText itemEt) {
        if (beforeText[0].equals (s.toString ())) {
            return s.toString ();
        }
        CharSequence ss = s.toString ();
        if (!ss.equals (dockScoreBean.getAllScore ())) {
            itemEt.setTextColor (this.getResources ().getColor (R.color.red));
        } else {
            itemEt.setTextColor (this.getResources ().getColor (R.color.black));
        }

        if (ss.length () > 1) {
            char c = ss.charAt (0);
            if (c == '0') {
                if (beforeText[0].equals ("0") && ss.charAt (1) == '0') {
                    ss = "0";
                } else {
                    ss = ss.subSequence (1, ss.length ());
                }
                itemEt.setText (ss);
                itemEt.setSelection (ss.length ());
            }
        }
        if (ss.length () > 0) {
            if (Integer.parseInt (ss.toString ()) > Integer.parseInt (dockScoreBean.getAllScore ())) {
                showErrorDialog (this.getResources ().getString (R.string.error_dialog_msg));
                itemEt.setText ("");
            }
        }

        return ss.toString ();
    }

    /*标示着数据是从该扣分详情页面保存过去的数据  */
    public static final int STATUS = 200;

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.text_view_reuse_fraction_click:
                //点击复用其它教师分数
                List<String> mNames = new ArrayList<> ();
                for (int i = 0; i < mTeacherBeanList.size (); i++) {
                    if (!mTeacherBeanList.get (i).getTeacherName ().equals (mTeacherName)) {
                        mNames.add (mTeacherBeanList.get (i).getTeacherName ());
                    }
                }
                if (mNames.size () < 1) {
                    showErrorDialog (this.getResources ().getString (R.string.no_teachers));
                    return;
                }

                ReuseScoreDialog mDialog = new ReuseScoreDialog (this);
                mDialog.setmListener (this);
                mDialog.setmNames (mNames);
                mDialog.setCanceledOnTouchOutside (false);
                mDialog.show ();
                break;
            case R.id.text_view_change_teacher_click:
                //点击更换教师
                Intent mIntent = new Intent ();
                mIntent.setClass (this, ChooseTeacherActivity.class);
                Bundle mBundle = new Bundle ();
                mBundle.putString (WHICH_PAGE, DOCK_SCORE_NEW_PAGE);
                mBundle.putString (ChooseTeacherActivity.STATUS, ChooseTeacherActivity.STATUS_ONE);
                mBundle.putSerializable (OPTION_BEANS, mTeacherBeanList);
                mIntent.putExtras (mBundle);
                startActivity (mIntent);
                break;
            case R.id.text_view_save:
                //点击保存
                clickSaveTv ();
                break;
            default:
                break;
        }
    }

    /**
     * 扣分详情页面点击保存后的数据处理，关闭该页面，数据回传给巡课登记页面
     */
    private void clickSaveTv () {
        if (mAutoScore.equals (UN_AUTO_SCORE)) { //非自动计算的情况
            for (int i = 0; i < titleSize; i++) {
                String score = unAutoScoreMap.get (i);
                if (score.equals ("")) {
                    showErrorDialog (this.getResources ().getString (R.string.error_dialog_msg));
                    return;
                }
            }

        } else {
            boolean allValues = presenter.isAllValues (mOptionBeans);
            if (!allValues) {
                showErrorDialog (this.getResources ().getString (R.string.error_dialog_msg));
                return;
            }
        }
        presenter.resumeData (mOptionBeans, mTeacherId, mTeacherName, mAutoScore, unAutoScoreMap);
        mTeacherBeanList.set (mPosition, presenter.getTeacherListBean ());
        Intent intent = new Intent ();
        Bundle mBundle = new Bundle ();
        mBundle.putSerializable (OPTION_BEANS, mTeacherBeanList);
        mBundle.putInt (PatrolPointsDetailsFragment.STATUS, STATUS);
        intent.putExtras (mBundle);
        setResult (RESULT_OK, intent);
        finish ();
    }

    /**
     * DockScoreNewAdapter 中的回调方法，adapter中的数据发生变化时，回调该方法，使activity随时可以接收到
     *
     * @param data data
     */
    @Override
    public void setList (List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> data) {
        this.mOptionBeans = data;
    }

    private void showErrorDialog (String msg) {
        new DeleteDialog (this).showDialog (false, "确定", msg, null, false);
    }

    /**
     * 点击复用其它教师分数时的弹框回调，点击确定时，回调该方法
     *
     * @param name name
     */
    @Override
    public void onConfirm (String name) {
        for (int i = 0; i < mTeacherBeanList.size (); i++) {
            if (mTeacherBeanList.get (i).getTeacherName ().equals (name)) {
                mTeacherBeanList.get (mPosition).setDockScore (mTeacherBeanList.get (i).getDockScore ());
                if (mAutoScore.equals (mTeacherBeanList.get (i).getAutoScore ())) {
                    listRefresh ();
                } else {
                    mAutoScore = mTeacherBeanList.get (i).getAutoScore ();
                    mRadioGroup.check (mAutoScore.equals (AUTO_SCORE) ? R.id.radio_button_automatic
                            : R.id.radio_button_manual_input);
                    listRefresh ();
                }
                break;
            }
        }
    }

    /**
     * 点击复用其它老师分数时，进行数据的更新替换
     */
    private void listRefresh () {
        if (mAutoScore.equals (AUTO_SCORE)) {
            readyOptions (mTeacherBeanList);
            if (mAdapter != null) {
                mAdapter = null;
            }
            mAdapter = new DockScoreNewAdapter2 (mOptionBeans);
            mAdapter.setInputScoreErrorListener (this);
            mAdapter.setListChangedListener (this);
            mAdapter.setRecyclerView (mRecyclerView);
            mRecyclerView.setAdapter (mAdapter);
        } else {
            for (int j = 0; j < mLinearLayout.getChildCount (); j++) {
                View itemV = mLinearLayout.getChildAt (j);
                EditText itemEt = (EditText) itemV.findViewById (R.id.tv_score_title_teacherScore);
                itemEt.setText (mTeacherBeanList.get (mPosition).getDockScore ().get (j).getUnAutoScore ());
            }
        }
    }
}
