package com.gta.zssx.patrolclass.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherSearchEntity;
import com.gta.zssx.patrolclass.model.entity.DockScoreEntity;
import com.gta.zssx.patrolclass.model.entity.PatrolRegisterBean;
import com.gta.zssx.patrolclass.model.entity.SubmitEntity;
import com.gta.zssx.patrolclass.model.entity.SubmitRegisterPatrol;
import com.gta.zssx.patrolclass.presenter.DockScorePresenter;
import com.gta.zssx.patrolclass.presenter.PatrolRegisterPresenter;
import com.gta.zssx.patrolclass.view.PatrolRegisterView;
import com.gta.zssx.patrolclass.view.dialog.DatePickerView;
import com.gta.zssx.patrolclass.view.dialog.DeleteDialog;
import com.gta.zssx.pub.base.BaseMvpActivity;
import com.gta.zssx.pub.util.RxBus;
import com.gta.zssx.pub.util.TimeUtils;
import com.kyleduo.switchbutton.SwitchButton;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import rx.Subscriber;

import static com.gta.zssx.patrolclass.view.page.ChooseTeacherActivity.WHICH_PAGE;

/**
 * [Description] <p/>   巡课登记页面 [How to use]
 * <p>
 * [Tips] Created by tengfei.lv on 2016/7/19 13:59.
 */

public class PatrolRegisterActivity extends BaseMvpActivity<PatrolRegisterView, PatrolRegisterPresenter>
        implements View.OnClickListener, PatrolRegisterView {

    public static final String PATROL_REGISTER_PAGE = "patrol_register_page";
    public static boolean isClose;
    private TextView dateTv, classNameTv, sectionsTv, teacherNameTv, saveButton;
    private ImageView iv_right;
    private SwitchButton autoScoreSwitch;
    private RelativeLayout rl_date, rl_class, rl_section, rl_teacher;
    private View dockScoreView, dockScoreView2;
    private LinearLayout scoreTitleLayout;
    private DockScoreEntity entity;
    //mType:记录巡课登记的时，是随机巡课还是按计划巡课 type:区分是从修改进入还是普通进入 isTYpe:获取到的type，“0” 随机巡课， “1” 按计划巡课
    private String teacherId, autoScore, mType, type, isType, className, date, deptId;
    private int classId, sectionId, xId, pId;
    private List<Map<String, String>> mEntityMap;
    private List<SubmitRegisterPatrol.JsonBean.GetScoreListBean> mGetScoreListBeens;
    private List<DockScoreEntity.DockScoreBean.OptionsBean> mOptionsBeen = null;
    private List<DockScoreEntity.SectionsListBean> sectionsBeanList;
    private String servicedate;
    private SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd", Locale.getDefault ());
    private SimpleDateFormat sdf2 = new SimpleDateFormat ("yyyy年MM月dd日", Locale.getDefault ());

    @Override
    public int getLayoutId () {
        return R.layout.activity_patrol_register;
    }

    @Override
    protected void initData () {
        super.initData ();
        deptId = getIntent ().getStringExtra ("deptId");
        classId = getIntent ().getIntExtra ("classId", 0);
        date = getIntent ().getStringExtra ("date");
        sectionId = getIntent ().getIntExtra ("sectionId", 0);
        xId = getIntent ().getIntExtra ("xId", 0);
        pId = getIntent ().getIntExtra ("pId", 0);
        type = getIntent ().getStringExtra ("type");
        mType = getIntent ().getStringExtra ("mType");
        className = getIntent ().getStringExtra ("ClassName");
    }

    @Override
    protected void initView () {
        findView ();
        initToolbar ();
        setOnInteractListener ();
        rxBus ();
    }

    private void rxBus () {
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
                        teacherId = deptListBean.getTeacherId ();
                        teacherNameTv.setText (deptListBean.getName ());
                    }
                }));
    }

    @Override
    protected void onResume () {
        super.onResume ();
        if (isClose) {
            finish ();
            isClose = false;
        }
    }

    private void findView () {
        saveButton = (TextView) findViewById (R.id.right_button);
        rl_date = (RelativeLayout) findViewById (R.id.rl_date);
        rl_class = (RelativeLayout) findViewById (R.id.rl_class);
        rl_section = (RelativeLayout) findViewById (R.id.rl_section);
        rl_teacher = (RelativeLayout) findViewById (R.id.rl_teacher);
        iv_right = (ImageView) findViewById (R.id.iv_right_arrow_section1);
        dateTv = (TextView) findViewById (R.id.tv_patrolRegister_date);
        classNameTv = (TextView) findViewById (R.id.tv_patrolRegister_className);
        sectionsTv = (TextView) findViewById (R.id.tv_patrolRegister_sections);
        teacherNameTv = (TextView) findViewById (R.id.tv_patrolRegister_sections1);
        scoreTitleLayout = (LinearLayout) findViewById (R.id.ll_score_title);
        autoScoreSwitch = (SwitchButton) findViewById (R.id.switch_auto);
        dockScoreView = findViewById (R.id.layout_dockScore);
        dockScoreView2 = findViewById (R.id.layout_dockScore2);
        classNameTv.setText (className);
    }

    @Override
    public void requestData () {
        super.requestData ();
        boolean notNull = false;
        String date = "";
        //        if (mOptionsBeen != null) {
        //            notNull = true;
        //        }
        Date mDate = null;
        if (this.date.equals ("")) {
            try {
                mDate = sdf.parse (sdf.format (new Date (System.currentTimeMillis ())));
                date = sdf2.format (mDate);
            } catch (ParseException e) {
                e.printStackTrace ();
            }
        } else {
            try {
                mDate = sdf.parse (this.date);
                date = sdf2.format (mDate);
            } catch (ParseException e) {
                e.printStackTrace ();
            }
        }
        presenter.loadData (notNull, date);
    }

    //点击事件的处理,点击时触发
    private void setOnInteractListener () {
        autoScoreSwitch.setOnCheckedChangeListener ((buttonView, isChecked) -> {
            if (isChecked) {
                //自动计算得分
                autoScore = "1";
                dockScoreView.setVisibility (View.VISIBLE);
                dockScoreView2.setVisibility (View.GONE);
                getUnAutoScores ();
                setScoreNums ();
            } else {     //非自动计算分数
                autoScore = "2";
                setUnAutoScores ();
                setEditTextListener ();
                dockScoreView.setVisibility (View.GONE);
                dockScoreView2.setVisibility (View.VISIBLE);
            }
            setIsClick (isChecked);
        });

        rl_date.setOnClickListener (this);
        rl_class.setOnClickListener (this);
        rl_section.setOnClickListener (this);
        rl_teacher.setOnClickListener (this);
        dockScoreView.setOnClickListener (this);
        dockScoreView2.setOnClickListener (this);
    }

    /**
     * 设置用户在非自动计算得分时的扣分得分
     */
    private void setUnAutoScores () {
        for (int i = 0; i < scoreTitleLayout.getChildCount (); i++) {
            EditText view = (EditText) scoreTitleLayout.getChildAt (i).findViewById (R.id.tv_score_title_teacherScore);
            if (unAutoScores != null) {
                view.setText (unAutoScores[i]);
            } else {
                view.setText (getNoChangeScore (i));
            }
            view.setBackgroundResource (R.drawable.shape_text_view_enable_bg);
        }
    }

    private void setUnAutoScores (int position) {
        EditText view = (EditText) scoreTitleLayout.getChildAt (position).findViewById (R.id.tv_score_title_teacherScore);
        if (unAutoScores != null) {
            if (unAutoScores[position] != null)
                view.setText (unAutoScores[position]);
        } else {
            view.setText (getNoChangeScore (position));
        }
    }

    private String[] unAutoScores;

    /**
     * 获取手动计算分数时，用户在扣分项上填写的分数
     */
    private void getUnAutoScores () {
        if (unAutoScores == null) {
            unAutoScores = new String[titleSize];
        }
        for (int i = 0; i < scoreTitleLayout.getChildCount (); i++) {
            EditText view = (EditText) scoreTitleLayout.getChildAt (i).findViewById (R.id.tv_score_title_teacherScore);
            if (view.getText ().toString ().equals ("")) {
                if (unAutoScores[i] != null) {
                    view.setText (unAutoScores[i]);
                } else {
                    view.setText (entity.getDockScore ().get (i).getGetAllScore ());
                }
            }
            unAutoScores[i] = view.getText ().toString ();
            view.setBackgroundResource (R.drawable.shape_edittext_none_bg);
        }
    }

    /**
     * 设置扣分项是否可以点击
     *
     * @param isClick
     */
    private void setIsClick (boolean isClick) {
        int childCount = scoreTitleLayout.getChildCount ();
        for (int i = 0; i < childCount; i++) {
            scoreTitleLayout.getChildAt (i).findViewById (R.id.tv_score_title_teacherScore).setEnabled (!isClick);
        }
    }

    //设置扣分项上的得分
    private void setScoreText (int index, String text) {
        EditText view = (EditText) scoreTitleLayout.getChildAt (index).findViewById (R.id.tv_score_title_teacherScore);
        view.setText (text);
    }

    //获取扣分项上的分数
    private String getScoreText (int index) {
        EditText view = (EditText) scoreTitleLayout.getChildAt (index).findViewById (R.id.tv_score_title_teacherScore);
        return view.getText ().toString ();
    }

    private void initToolbar () {
        mToolBarManager.getRightButton ().setEnabled (true);
        mToolBarManager.showBack (true).setTitle ("巡课登记").showRightButton (true).setRightText ("完成").clickRightButton (v -> {
            if (type.equals ("2")) {
                clickSave ();
            } else {
                presenter.checkLoadClassPatrolState (2);
            }
        });
    }

    @NonNull
    @Override
    public PatrolRegisterPresenter createPresenter () {
        return PatrolRegisterPresenter.getInstance ();
    }

    /**
     * @param context
     * @param deptId
     * @param classId
     * @param date
     * @param sectionId
     * @param xId
     * @param type      区分是从修改进入还是普通进入。普通进入选择班级的时候直接finished当前页面
     */
    public static void start (Context context, String deptId, int classId, String date, int sectionId, int xId, String type, int pId, String mType, String ClassName) {
        final Intent lIntent = new Intent (context, PatrolRegisterActivity.class);
        lIntent.putExtra ("deptId", deptId);
        lIntent.putExtra ("classId", classId);
        lIntent.putExtra ("date", date);
        lIntent.putExtra ("sectionId", sectionId);
        lIntent.putExtra ("xId", xId);
        lIntent.putExtra ("type", type);
        lIntent.putExtra ("pId", pId);
        lIntent.putExtra ("mType", mType);
        lIntent.putExtra ("ClassName", ClassName);
        if (!(context instanceof Activity)) {
            lIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity (lIntent);
    }

    @Override
    public void onClick (View v) {
        if (TimeUtils.isFastDoubleClick ()) {
            return;
        }
        Intent intent = new Intent ();
        switch (v.getId ()) {
            case R.id.rl_date:
                showTimeSelect ();
                break;
            case R.id.layout_dockScore:
                Bundle lBundle01 = new Bundle ();
                lBundle01.putSerializable ("dockEntity", presenter.getDockScoreEntity ());
                lBundle01.putSerializable ("dockScoreList", (Serializable) mOptionsBeen);
                intent.putExtras (lBundle01);
                intent.setClass (this, DockScoreActivity.class);
                intent.putExtra ("isLook", false);
                startActivityForResult (intent, 1);
                break;
            case R.id.layout_dockScore2:
                Bundle lBundle02 = new Bundle ();
                lBundle02.putSerializable ("dockEntity", presenter.getDockScoreEntity ());
                lBundle02.putSerializable ("dockScoreList", (Serializable) mOptionsBeen);
                intent.putExtras (lBundle02);
                intent.setClass (this, DockScoreActivity.class);
                intent.putExtra ("isLook", true);   //true 表示不可以更改扣分详情里面的数据
                startActivity (intent);
                break;
            case R.id.rl_section:
                intent.setClass (this, ChooseSectionsActivity.class);
                Bundle lBundle = new Bundle ();
                lBundle.putSerializable ("beanList", (Serializable) sectionsBeanList);
                String type;
                if (PatrolRegisterActivity.this.type.equals ("1")) {
                    //新增进入
                    type = mType;
                } else {
                    type = isType;
                }
                lBundle.putString ("mType", type);
                intent.putExtras (lBundle);
                startActivityForResult (intent, 8);
                break;
            case R.id.rl_class:
                String mmType = null;
                if (entity != null) {
                    mmType = entity.getType ();
                }
                if (mmType != null) {
                    if (mmType.equals ("0")) {
                        Intent intent1 = new Intent (this, AddClassChooseActivity.class);
                        intent1.putExtra ("classType", "random");
                        intent1.putExtra ("type", "2");
                        intent1.putExtra ("date", date);
                        startActivityForResult (intent1, 100);
                    } else {
                        Intent intent1 = new Intent (this, PlanProtalResultActivity.class);
                        intent1.putExtra ("type", "2");
                        startActivityForResult (intent1, 101);
                    }
                } else {
                    if (this.mType != null) {
                        if (this.mType.equals ("0")) {
                            Intent intent1 = new Intent (this, AddClassChooseActivity.class);
                            intent1.putExtra ("classType", "random");
                            intent1.putExtra ("type", "2");
                            intent1.putExtra ("date", date);
                            startActivityForResult (intent1, 100);
                        } else {
                            Intent intent1 = new Intent (this, PlanProtalResultActivity.class);
                            intent1.putExtra ("type", "2");
                            startActivityForResult (intent1, 101);
                        }
                    }
                }
                break;
            case R.id.rl_teacher:
                intent.setClass (this, ChooseTeacherActivity.class);
                intent.putExtra (ChooseTeacherActivity.STATUS, ChooseTeacherActivity.STATUS_ONE);
                intent.putExtra (WHICH_PAGE, PATROL_REGISTER_PAGE);
                startActivityForResult (intent, 1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        switch (resultCode) {
            case 2:
                mEntityMap = (List<Map<String, String>>) data.getExtras ().getSerializable ("entityMap");
                mGetScoreListBeens = (List<SubmitRegisterPatrol.JsonBean.GetScoreListBean>) data.getExtras ().getSerializable ("scoreListBeen");
                mOptionsBeen = (List<DockScoreEntity.DockScoreBean.OptionsBean>) data.getExtras ().getSerializable ("checkList");
                setScoreNums ();
                break;
            case 5:
                //                mOptionsBeen = (List<DockScoreEntity.DockScoreBean.OptionsBean>) data.getExtras().getSerializable("checkList");
                break;
            case 6:
                sectionId = data.getExtras ().getInt ("sectionId");
                sectionsTv.setText (data.getExtras ().getString ("sectionName"));
                mType = data.getExtras ().getString ("mType");
                //                showEmty ();
                sectionsBeanList = (List<DockScoreEntity.SectionsListBean>) data.getExtras ().getSerializable ("beanList");
                requestData ();
                break;
            case 10:
                deptId = data.getExtras ().getString ("deptId");
                classId = data.getExtras ().getInt ("classId");
                className = data.getExtras ().getString ("className");
                mType = data.getExtras ().getString ("isType");
                //                showEmty ();
                classNameTv.setText (className);
                requestData ();
                break;
            case 11:
                deptId = data.getExtras ().getString ("deptId");
                classId = data.getExtras ().getInt ("classId");
                className = data.getExtras ().getString ("className");
                mType = data.getExtras ().getString ("isType");
                //                showEmty ();
                classNameTv.setText (className);
                requestData ();
                break;
            default:
                break;
        }

    }

    /**
     * 自动计算得分时，选择完成之后设置分数
     */
    private void setScoreNums () {
        List<Map<String, String>> resultMap = mEntityMap;
        if (resultMap != null) {   //如果已经有扣分详情
            for (int i = 0; i < resultMap.size (); i++) {
                setScoreText (i, resultMap.get (i).get ("score"));
            }
        } else {    //没有扣分详情，则用户没有进去扣分详情页面，所以如果切换到自动计算得分时，把扣分项的得分设置为""
            for (int i = 0; i < titleSize; i++) {
                setScoreText (i, entity.getDockScore ().get (i).getGetAllScore ());
            }
        }
    }

    @Override
    public void showSuccessData (DockScoreEntity dockScoreEntity) {
        this.entity = dockScoreEntity;
        if (isType == null) {
            isType = entity.getType ();
        }
        if (sectionsBeanList == null || sectionsBeanList.size () == 0) {
            if (presenter.getSectionsBeanList () != null) {
                sectionsBeanList = presenter.getSectionsBeanList ();
            }
        }
        teacherId = entity.getTeacherId ();
        if (entity.getAutoScore () != null) {
            autoScore = entity.getAutoScore ();
        }
        sectionId = dockScoreEntity.getSectionId ();
        dateTv.setText (dockScoreEntity.getCurrentdate ());
        classNameTv.setText (dockScoreEntity.getClassName ());
        sectionsTv.setText (dockScoreEntity.getSectionName ());
        if (dockScoreEntity.getTeacherName () == null) {
            teacherNameTv.setText ("暂无排课");
        } else {
            teacherNameTv.setText (dockScoreEntity.getTeacherName ());
        }
        for (int i = 0; i < sectionsBeanList.size (); i++) {
            DockScoreEntity.SectionsListBean bean = sectionsBeanList.get (i);
            if (bean.getSectionId () == sectionId) {
                bean.setCheck (true);
            }
        }

    }

    int titleSize;

    //动态创建扣分标题
    @Override
    public void createScoreTitle (List<String> scoreTitle) {
        if (scoreTitle == null) {
            return;
        }
        scoreTitleLayout.setVisibility (View.VISIBLE);
        titleSize = scoreTitle.size ();
        scoreTitleLayout.removeAllViews ();
        for (int i = 0; i < scoreTitle.size (); i++) {
            View view = LayoutInflater.from (this).inflate (R.layout.layout_score_title, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams (layoutParams);
            scoreTitleLayout.addView (view);
            TextView titleTv = (TextView) view.findViewById (R.id.tv_score_title_t);
            titleTv.setText (scoreTitle.get (i));
            EditText editText = (EditText) view.findViewById (R.id.tv_score_title_teacherScore);
            editText.setText (entity.getDockScore ().get (i).getGetAllScore ());
            if (autoScore.equals ("2")) {
                editText.setBackgroundResource (R.drawable.shape_text_view_enable_bg);
            } else {
                editText.setBackgroundResource (R.drawable.shape_edittext_none_bg);
            }
        }
        if (type.equals ("2")) {//修改
            switch (autoScore) {
                case "1":
                    autoScoreSwitch.setChecked (true);
                    setIsClick (true);
                    break;
                case "2":
                    autoScoreSwitch.setChecked (false);
                    setIsClick (false);
                    break;
                default:
                    autoScoreSwitch.setChecked (true);
                    setIsClick (true);
                    break;
            }
        } else {
            if (autoScore != null) {
                if (autoScore.equals ("1")) {
                    autoScoreSwitch.setChecked (true);
                    setIsClick (true);
                } else {
                    autoScoreSwitch.setChecked (false);
                    setIsClick (false);
                }
            }
        }


    }

    @Override
    public void showSubmitNext (List<SubmitEntity> integer) {
        //跳转页面
        int xId;
        SubmitEntity entity = integer.get (0);
        xId = entity.getxId ();
        PatrolClassActivity.start (this);
        PatrolClassSelActivity.start (PatrolRegisterActivity.this, -1, 2, xId, dateTv.getText ().toString ());
        finish ();
    }

    @Override
    public String getDeptId () {
        return deptId;
    }

    @Override
    public int getClassId () {
        return classId;
    }

    @Override
    public String getDate () {
        return date;
    }

    @Override
    public int getSectionId () {
        return sectionId;
    }

    @Override
    public int getXid () {
        return xId;
    }

    @Override
    public void showResult (List<DockScoreEntity.DockScoreBean.OptionsBean> optionsBeen) {
        for (int i = 0; i < optionsBeen.size (); i++) {
            if (!optionsBeen.get (i).isTitle ()) {
                if (optionsBeen.get (i).getGetScore ().length () > 0) {
                    optionsBeen.get (i).setCheck (true);
                    optionsBeen.get (i).setChangeScoer (optionsBeen.get (i).getGetScore ());
                }
            }
        }
        DockScorePresenter presenter = new DockScorePresenter ();
        presenter.calculateScore (entity, optionsBeen);
        mEntityMap = presenter.getResultMap ();
        mGetScoreListBeens = presenter.getGetScoreListBeens ();
        mOptionsBeen = optionsBeen;
        // 清空上次手动填写的分数
        unAutoScores = null;

    }

    public String getNoChangeScore (int position) {
        String getAllScore = entity.getDockScore ().get (position).getAllScore ();
        String getGetAllScore = entity.getDockScore ().get (position).getGetAllScore ();
        if (autoScore.equals ("2") && !getAllScore.equals (getGetAllScore)) {
            return getGetAllScore;
        }
        return getAllScore;
    }

    public void setEditTextListener () {

        for (int i = 0; i < scoreTitleLayout.getChildCount (); i++) {
            EditText view1 = (EditText) scoreTitleLayout.getChildAt (i).findViewById (R.id.tv_score_title_teacherScore);
            final int finalI = i;
            if (view1.getTag () != null) {
                return;
            }
            View.OnFocusChangeListener onFocusChangeListener = (view, b) -> {
                if (!b) {
                    if (view1.getText ().toString ().length () == 0) {
                        setUnAutoScores (finalI);
                    }
                }
            };
            TextWatcher textWatcher = new TextWatcher () {
                @Override
                public void beforeTextChanged (CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged (CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged (Editable editable) {
                    String s = editable.toString ();
                    if (s.length () > 1) {
                        char fir = s.charAt (0);
                        if (fir == '0') {
                            onError (1);
                            view1.setText ("");
                            return;
                        }
                    }
                    if (s.length () > 0) {
                        if (Integer.parseInt (s) > Integer.parseInt (entity.getDockScore ().get (finalI).getAllScore ())) {
                            onError (1);
                            view1.setText ("");
                        }
                    }

                }

            };
            view1.setOnFocusChangeListener (onFocusChangeListener);
            view1.addTextChangedListener (textWatcher);
            view1.setTag (onFocusChangeListener);

        }
    }

    public void onError (int res) {
        String msg = "";
        if (res == 1) {
            msg = "输入的分数有误,请重新输入";
        } else if (res == 2) {
            msg = "选项不能为空,请保证提交数据完整";
        } else if (res == 3) {
            msg = "当前节次暂无排课,请选择上课教师";
        }
        new DeleteDialog (this).showDialog (false, "确定", msg, null, false);
    }

    public boolean isNull () {
        if (dateTv.getText ().length () == 0 || classNameTv.getText ().length () == 0
                || sectionsTv.getText ().length () == 0 || teacherNameTv.getText ().length () == 0) {
            return false;
        } else if (scoreTitleLayout.getChildCount () > 0) {
            for (int i = 0; i < scoreTitleLayout.getChildCount (); i++) {
                EditText view = (EditText) scoreTitleLayout.getChildAt (i).findViewById (R.id.tv_score_title_teacherScore);
                if (view.getText ().length () == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void clickSave () {
        String[] scores = new String[titleSize];
        if (!autoScoreSwitch.isChecked ()) {
            for (int i = 0; i < scores.length; i++) {
                scores[i] = getScoreText (i);
            }
        }
        if (!isNull ()) {
            onError (2);
        } else if (teacherNameTv.getText ().equals ("暂无排课")) {
            onError (3);
        } else {
            PatrolRegisterBean lPatrolRegisterBean = new PatrolRegisterBean ();
            lPatrolRegisterBean.setpId (pId);
            lPatrolRegisterBean.setxId (xId);
            lPatrolRegisterBean.setClassId (classId);
            lPatrolRegisterBean.setGetScoreListBeens (mGetScoreListBeens);
            lPatrolRegisterBean.setDeptId (deptId);
            lPatrolRegisterBean.setSectionId (sectionId);
            lPatrolRegisterBean.setEntityMap (mEntityMap);
            lPatrolRegisterBean.setAutoScore (autoScore);
            lPatrolRegisterBean.setScores (scores);
            lPatrolRegisterBean.setDate (date);
            lPatrolRegisterBean.setTeacherId (teacherId);
            if (type.equals ("1")) {//新增进入
                lPatrolRegisterBean.setIsType (mType);
                presenter.finishRegisterPatrol (lPatrolRegisterBean);
            } else {
                if (isType != null) {
                    lPatrolRegisterBean.setIsType (isType);
                    presenter.finishRegisterPatrol (lPatrolRegisterBean);
                } else {
                    if (mType != null) {
                        lPatrolRegisterBean.setIsType (mType);
                        presenter.finishRegisterPatrol (lPatrolRegisterBean);
                    }
                }

            }
        }

    }

    @Override
    public void showDilog (boolean isShowSure, String content) {
        if (isShowSure) {
            new DeleteDialog (this).showDialog (true, "确定", content, this :: clickSave, false);
        } else {
            new DeleteDialog (this).showDialog (false, "确定", content, null, false);
        }
    }

    @Override
    public void showTimeDilog (String content) {
        saveButton.setEnabled (false);
        new DeleteDialog (this).showDialog (false, "确定", content, this :: showTimeSelect, false);
    }

    private void showTimeSelect () {
        DatePickerView.showView (this, dateTv.getText ().toString (), date12 -> {
            if (entity == null) {
                servicedate = sdf.format (new Date (System.currentTimeMillis ()));
            } else {
                servicedate = entity.getServicedate ();
            }
            Date date1 = null;
            try {
                date1 = sdf.parse (servicedate);
            } catch (ParseException e) {
                e.printStackTrace ();
            }
            boolean isFlag = date12.after (date1);
            PatrolRegisterActivity.this.date = sdf.format (date12);
            //                showEmty ();
            dateTv.setText (sdf2.format (date12));
            if (isFlag) {
                showTimeDilog (PatrolRegisterActivity.this.getResources ().getString (R.string.patrol_class_date_error));
                saveButton.setEnabled (false);
                return;
            } else {
                saveButton.setEnabled (true);
            }
            presenter.checkLoadClassPatrolState (1);

            //                loadData ();
        });
    }
}
