package com.gta.zssx.fun.coursedaily.registercourse.view.page;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.PostSignBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionStudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListNewBean;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.SignPresenter;
import com.gta.zssx.fun.coursedaily.registercourse.view.SignView;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.SignAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseActivity;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseBaseFragment;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseFragmentBuilder;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ClassInfoDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.RegisteredRecordManager;
import com.gta.zssx.fun.coursedaily.registerrecord.view.page.AlreadyRegisteredRecordFromSignatureFragment;
import com.gta.zssx.fun.coursedaily.registerrecord.view.page.AlreadyRegisteredRecordActivity;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/21.
 * @since 1.0.0
 */
public class SignFragment extends RegisterCourseBaseFragment<SignView, SignPresenter> implements SignView {

    public RecyclerView mRecyclerView;
    public UserBean mUserBean;
    public ClassDataManager.DataCache mDataCache;
    public PostSignBean mPostSignBean;
    private boolean mIsModify;

    @NonNull
    @Override
    public SignPresenter createPresenter() {
        return new SignPresenter(getActivity());
    }

    public static final String PAGE_TAG = SignFragment.class.getSimpleName();

    @Override
    public void showResult(String s) {
        mActivity.finish();
        ClassInfoDto lClassInfoDto = new ClassInfoDto();
        lClassInfoDto.setClassName(ClassDataManager.getDataCache().getClassName());
        lClassInfoDto.setClassID(ClassDataManager.getDataCache().getsClassDisplayBean().getClassId() + "");
        lClassInfoDto.setSignDate(ClassDataManager.getDataCache().getSignDate());
        lClassInfoDto.setTeacherID(mUserBean.getUserId());
        lClassInfoDto.setIsFromClassLogMainpage(false);
        RegisteredRecordManager.setSaveCacheDto(null);  //确保缓存为空
        RegisteredRecordManager.setSaveCacheDtoFromSignature(null); //确保缓存为空
        AlreadyRegisteredRecordActivity.start(mActivity, AlreadyRegisteredRecordFromSignatureFragment.PAGE_TAG, lClassInfoDto);
        ClassDataManager.destroyDataCache();
    }

    @Override
    public void showHaveBeenSignMassage(String s) {

    }

    /*@Override
    public void showResult(List<StudentListNewBean> studentListBeen) {

    }*/

    @Override
    public void emptyUI() {

    }

    @Override
    public void getStudentListInfo(List<SectionStudentListBean> sectionStudentListBeanList) {

    }

    @Override
    public void isHaveSectionHaveBeenSign(int unSignNum) {

    }

    public static class Builder extends RegisterCourseFragmentBuilder<SignFragment> {

        boolean mIsModify;

        public Builder(Context context, boolean isModify) {
            super(context);
            mIsModify = isModify;
        }

        @Override
        public SignFragment create() {
            Bundle lBundle = new Bundle();

            lBundle.putBoolean(RegisterCourseActivity.sIsModify, mIsModify);
            SignFragment lSignFragment = new SignFragment();
            lSignFragment.setArguments(lBundle);
            return lSignFragment;
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
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mIsModify = getArguments().getBoolean(RegisterCourseActivity.sIsModify);
        mDataCache = ClassDataManager.getDataCache();
        mPostSignBean = new PostSignBean();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign, container, false);
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
        SignAdapter.Listener lListener = new SignAdapter.Listener() {
            @Override
            public void itemClick() {

            }

            @Override
            public void postSectionBean(List<PostSignBean.SectionBean> sectionBeanList) {

            }
        };

        Set<SectionBean> lSection = ClassDataManager.getDataCache().getSection();
        SignAdapter lAdapter = new SignAdapter(mActivity, lListener, new ArrayList<>(lSection), null, null);
        mRecyclerView.setAdapter(lAdapter);

    }

    //设置标题等
    private void initViews() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        mToolBarManager.setTitle("课堂得分")
                .showRightButton(true)
                .setRightText("签名确认")
                .clickRightButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPostSignBean.setClassID(mDataCache.getsClassDisplayBean().getClassId());
                        mPostSignBean.setSignDate(mDataCache.getSignDate());
                        mPostSignBean.setTeacherID(mUserBean.getUserId());
                        mPostSignBean.setSection(mDataCache.getPostSectionBeans());
                        List<StudentListBean> studentListBeanList = setBean();
                        mPostSignBean.setDetail(studentListBeanList);
                        if (mIsModify) {
                            mPostSignBean.setType(PostSignBean.MODIFY);
                            mPostSignBean.setOriginalSignDate(mDataCache.getOriginalClassBean().getOriginalSignDate());
                            mPostSignBean.setOriginalClassID(mDataCache.getOriginalClassBean().getOriginalClassID());
                            mPostSignBean.setOriginalSectionID(mDataCache.getOriginalClassBean().getOriginalSectionID());
                        } else {
                            mPostSignBean.setType(PostSignBean.ADD);
                        }
//                        presenter.postSignData(mPostSignBean);
                        presenter.postSignData(null,null);
                    }
                });
    }

    /**
     * 旧版登记所需数据
     * @return
     */
    private List<StudentListBean> setBean(){
        List<StudentListBean> listBeen = new ArrayList<>();
        List<StudentListNewBean> rxStudentListStudentListBeen = mDataCache.getStudentList();
        //转成旧的登记所数据
        for(int i = 0;i <rxStudentListStudentListBeen.size();i++){
            StudentListNewBean studentListNewBean = rxStudentListStudentListBeen.get(i);
            StudentListBean studentListBean = new StudentListBean();
            studentListBean.setStudentID(studentListNewBean.getStudentID());
            studentListBean.setStudentNO(studentListNewBean.getStudentNO());
            studentListBean.setStundentName(studentListNewBean.getStundentName());
//            studentListBean.setLESSON1(studentListNewBean.getLESSON1());
//            studentListBean.setLESSON2(studentListNewBean.getLESSON2());
//            studentListBean.setLESSON3(studentListNewBean.getLESSON3());
//            studentListBean.setLESSON4(studentListNewBean.getLESSON4());
//            studentListBean.setLESSON5(studentListNewBean.getLESSON5());
//            studentListBean.setLESSON6(studentListNewBean.getLESSON6());
//            studentListBean.setLESSON7(studentListNewBean.getLESSON7());
            listBeen.add(studentListBean);
        }
        return listBeen;
    }

    //绑定控件
    private void findView() {
        mRecyclerView = (RecyclerView) mActivity.findViewById(R.id.sign_rv);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView(false);
    }
}
