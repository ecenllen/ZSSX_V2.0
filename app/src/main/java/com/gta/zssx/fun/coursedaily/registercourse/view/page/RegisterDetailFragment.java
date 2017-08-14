package com.gta.zssx.fun.coursedaily.registercourse.view.page;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.gta.utils.resource.SysRes;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ApproveBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DefaultRegistInfoBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.OriginalClassBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.RegisterDetailPresenter;
import com.gta.zssx.fun.coursedaily.registercourse.view.DetailView;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseActivity;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseBaseFragment;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseFragmentBuilder;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordFromSignatureDto;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/18.
 * @since 1.0.0
 */
public class RegisterDetailFragment extends RegisterCourseBaseFragment<DetailView, RegisterDetailPresenter> implements DetailView {

    public static String sCLASSNAME = "CLASSNAME";
    private TextView mCourseDateTv;
    private TextView mSectionTv;
    private EditText mCourseNameEt;
    private OptionsPickerView mOptionsPickerView;
    private String mStartDate;
    private ArrayList<String> mDateRange;
    public static final String PAGE_TAG = RegisterDetailFragment.class.getSimpleName();
    private String mSelectedDate;
    private String mClassName;
    private RegisteredRecordFromSignatureDto mSignatureDto;
    private boolean mIsModify;
    private UserBean mUserBean;

    @NonNull
    @Override
    public RegisterDetailPresenter createPresenter() {
        return new RegisterDetailPresenter();
    }


    @Override
    public void showResult(RegisterDetailPresenter.Combined combined) {
        initTimePicker(combined.getApproveBean());
    }

    @Override
    public void showLastestSection(SectionBean sectionBean) {
        if (sectionBean != null) {
            mSectionTv.setText(sectionBean.getLesson());
            Set<SectionBean> lSectionBeen = new HashSet<>();
            lSectionBeen.add(sectionBean);
            ClassDataManager.getDataCache().setSection(lSectionBeen);
        } else {
            mSectionTv.setText(mActivity.getString(R.string.no_unsign_section));
        }

    }

    @Override
    public void showSectionDefaultTeacherAndCourse(DefaultRegistInfoBean defaultRegistInfoBean, DetailItemShowBean.TeacherInfoBean teacherInfoBean) {

    }

    @Override
    public void showSelectSectionNotSignCount(int notSignNum,boolean isCheckAllSection) {

    }

    public static class Builder extends RegisterCourseFragmentBuilder<RegisterDetailFragment> {

        String mClassName;
        RegisteredRecordFromSignatureDto mSignatureDto;
        boolean mBoolean;

        public Builder(Context context, String className4Title, boolean isModify) {
            super(context);
            mClassName = className4Title;
            mBoolean = isModify;
        }

        public Builder(Context context, RegisteredRecordFromSignatureDto registeredRecordFromSignatureDto, boolean isModify) {
            super(context);
            mClassName = registeredRecordFromSignatureDto.getClassName();
            mBoolean = isModify;
            mSignatureDto = registeredRecordFromSignatureDto;
        }

        @Override
        public RegisterDetailFragment create() {
            Bundle lBundle = new Bundle();
            lBundle.putString(sCLASSNAME, mClassName);
            lBundle.putBoolean(RegisterCourseActivity.sIsModify, mBoolean);
            lBundle.putSerializable(RegisterCourseActivity.sModifyBean, mSignatureDto);
            RegisterDetailFragment lRegisterDetailFragment = new RegisterDetailFragment();
            lRegisterDetailFragment.setArguments(lBundle);
            return lRegisterDetailFragment;
        }

        @Override
        public String getPageTag() {
            return PAGE_TAG;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dataAction();
    }

    //fragment间参数传递
    private void dataAction() {
        mClassName = getArguments().getString(sCLASSNAME);
        mSignatureDto = (RegisteredRecordFromSignatureDto) getArguments().getSerializable(RegisterCourseActivity.sModifyBean);
        mIsModify = getArguments().getBoolean(RegisterCourseActivity.sIsModify);

        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mIsModify) {
            OriginalClassBean lOriginalClassBean = new OriginalClassBean();
            lOriginalClassBean.setOriginalClassID(mSignatureDto.getClassID());
            lOriginalClassBean.setOriginalSectionID(mSignatureDto.getSectionID());
            lOriginalClassBean.setOriginalSignDate(mSignatureDto.getSignDate());
            ClassDataManager.getDataCache().setOriginalClassBean(lOriginalClassBean);

            RegisterDetailPresenter lDetailPresenter = new RegisterDetailPresenter();
            String lSignDate1 = mSignatureDto.getSignDate();
            ClassDataManager.getDataCache().setSignDate(lSignDate1);
            ClassDataManager.getDataCache().setSection(lDetailPresenter.getModifySection(mSignatureDto));
            ClassDataManager.getDataCache().setsClassDisplayBean(lDetailPresenter.getModifyClassDisplayBean(mSignatureDto));
            ClassDataManager.getDataCache().setClassName(mSignatureDto.getCourseName());
            ClassDataManager.getDataCache().setSignatureDto(mSignatureDto);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register_detail, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        uiAction();
    }

    private void uiAction() {
        findView();
        initViews();
        setOnInteractListener();
    }

    //事件处理
    private void setOnInteractListener() {

        /**
         * 获取领导审核日
         */
        presenter.getApproveDate();

        TextWatcher lWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean lEmpty = s.toString().isEmpty();
                boolean lEquals = mCourseNameEt.getText().toString().trim().isEmpty();
                boolean lEquals1 = s.toString().equals(mActivity.getString(R.string.no_unsign_section));
                mToolBarManager.getRightButton().setEnabled(!lEmpty && !lEquals&& !lEquals1);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        mCourseNameEt.addTextChangedListener(lWatcher);
        mSectionTv.addTextChangedListener(lWatcher);

        mCourseDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionsPickerView.show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Set<SectionBean> lSection = ClassDataManager.getDataCache().getSection();
        if (lSection != null) {
            List<SectionBean> lSectionBeen = new ArrayList<>(lSection);
            String text = "";
            for (int i = 0; i < lSectionBeen.size(); i++) {
                int lSectionName = lSectionBeen.get(i).getSectionId();
                if (i == 0) {
                    text = lSectionName + "";
                } else {
                    text = text + "," + lSectionName;
                }
            }
            mSectionTv.setText("第 " + text + " 节");
        }
    }

    //设置标题等
    private void initViews() {

        mToolBarManager.getRightButton().setEnabled(false);
        mToolBarManager.setTitle(mClassName)
                .showLeftImage(false)
                .showBack(true)
                .showRightButton(true)
                .setRightText("完成")
                .clickRightButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SysRes.showSoftInputOrNot(mCourseNameEt, false);
                        ClassDataManager.getDataCache().setClassName(mCourseNameEt.getText().toString().trim());
                        new StudentListFragmentV2.Builder(mActivity, mClassName, mIsModify).display();
                    }
                });


        mOptionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                mSelectedDate = mDateRange.get(options1);
                mCourseDateTv.setText(mSelectedDate);
                DateTime lDateTime = presenter.getDateTimesRange().get(options1);
                String lSignDate = lDateTime.toString("yyyy-MM-dd");
                ClassDataManager.getDataCache().setSignDate(lSignDate);

                /**
                 * 选择日期后，更新节次信息
                 */
                ClassDataManager.getDataCache().setSection(null);
                presenter.getSectionList(mActivity, mUserBean.getUserId(), ClassDataManager.getDataCache().getsClassDisplayBean().getClassId(), lSignDate);
            }
        });

        mSectionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsModify) {
                    ArrayList<SectionBean> lSectionBeen = new ArrayList<>(ClassDataManager.getDataCache().getSection());
                    new SectionChooseFragment.Builder(mActivity, mIsModify, lSectionBeen.get(0).getSectionId()).display();
                } else {
                    new SectionChooseFragment.Builder(mActivity, mIsModify).display();
                }
            }
        });

        String lToString = new DateTime().toString("yyyy-MM-dd");

        /**
         * 从修改进入
         */
        if (mIsModify) {
            mCourseDateTv.setText(presenter.getSignFormatDay(ClassDataManager.getDataCache().getSignDate()));
            Set<SectionBean> lSection = ClassDataManager.getDataCache().getSection();
            ArrayList<SectionBean> lSectionBeen = new ArrayList<>(lSection);
            mSectionTv.setText(presenter.getSectionName(lSectionBeen.get(0).getSectionId()));
            mCourseNameEt.setText(ClassDataManager.getDataCache().getClassName());
        } else {

            /**
             * 如果是新增默认显示最近的未登记的课程
             */
            if (mSelectedDate == null) {
                ClassDataManager.getDataCache().setSignDate(lToString);
                mCourseDateTv.setText(mActivity.getString(R.string.today));
            } else {
                mCourseDateTv.setText(mSelectedDate);
            }


            int lClassId = ClassDataManager.getDataCache().getsClassDisplayBean().getClassId();
            presenter.getSectionList(mActivity, mUserBean.getUserId(), lClassId, ClassDataManager.getDataCache().getSignDate());
        }
    }

    private void initTimePicker(ApproveBean approveBean) {
        mStartDate = approveBean.getDate();
        mDateRange = presenter.getDateRange(mStartDate, new DateTime().toString("yyyy-MM-dd"));
        mOptionsPickerView.setPicker(mDateRange);
        mOptionsPickerView.setSelectOptions(mDateRange.size() - 1);
        mOptionsPickerView.setCyclic(false);
    }

    //绑定控件
    private void findView() {
        mCourseDateTv = (TextView) mActivity.findViewById(R.id.course_date_tv);
//        mSectionTv = (TextView) mActivity.findViewById(R.id.section_tv);
//        mCourseNameEt = (EditText) mActivity.findViewById(R.id.course_name_et);
        mOptionsPickerView = new OptionsPickerView(mActivity);
    }


    @Override
    public boolean onBackPress() {
        ClassDataManager.getDataCache().setSection(null);
        ClassDataManager.getDataCache().setClassName(null);
        ClassDataManager.getDataCache().setSignDate(null);
        ClassDataManager.getDataCache().setApproveBean(null);
        SysRes.showSoftInputOrNot(mCourseNameEt, false);
        return super.onBackPress();
    }
}
