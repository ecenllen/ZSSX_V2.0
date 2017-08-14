package com.gta.zssx.fun.coursedaily.registercourse.view.pageV2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.gta.utils.resource.L;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.RxSectionBeanSignList;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StateBean;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.SectionChoosePresenter;
import com.gta.zssx.fun.coursedaily.registercourse.view.SectionChooseView;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.SectionChooseAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.SectionSingleChooseAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseActivity;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.RxBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/7/4.
 * @since 1.0.0
 */
public class SectionChooseActivity extends BaseActivity<SectionChooseView, SectionChoosePresenter> implements SectionChooseView {

    public static String sSignDate = "signDate";
    private Set<SectionBean> mSectionBeanSet;   //后面传回去的集合,点击完成的时候
    private Set<SectionBean> mSectionBeanSetBackPressed; //点击返回按钮的时候使用,特殊处理
    private boolean mIsModify;
    private int mSectionId;
    public String mSignDate;
    public int mClassId;
    public static String sSectionSet = "sectionBeanSet";
    public static String sIsSameDate = "isSameDate";
    public Set<SectionBean> mSectionHasCheck;   //一开始传入的节次集合
    public List<SectionBean> sectionBeanListHaveBeenSign;  //已登记的节次列表
    public boolean isSameWithFirstEnterModifyDate = false;//如果是修改和进入是同一天的，，需要把修改的节次变为UNsign状态

    //标题栏
    public Toolbar mToolbar;
    public ToolBarManager mToolBarManager;
    private static String sSectionId = "sectionId";
    private RecyclerView mRecyclerView;

    /**
     * @param context           上下文
     * @param isModify          是否是修改
     * @param originalSectionId 如果是修改则要传原始节次id用来把这节课改成未登记，否则随便传
     * @param classId           班级id
     * @param signDate          登记日期
     */
    /*public static void start(Context context, boolean isModify, int originalSectionId, int classId, String signDate) {
        final Intent lIntent = new Intent(context, SectionChooseActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putBoolean(CourseDetailActivity.sIsModify, isModify);
        lBundle.putInt(sSectionId, originalSectionId);
        lBundle.putInt(CourseDetailActivity.sClassId, classId);
        lBundle.putString(sSignDate, signDate);
        lIntent.putExtras(lBundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }
*/
    /**
     * @param context        上下文
     * @param isModify       是否是修改
     * @param sectionId      如果是修改则要传原始节次id用来把这节课改成未登记，否则随便传
     * @param requestCode    请求码
     * @param classId        班级id
     * @param signDate       登记日期
     * @param sectionBeanSet 用来恢复上一次选中的节次
     * @param isSameWithFirstEnterModifyDate 修改专用，其他请传入false
     */
    public static void startActivity4Result(Context context, boolean isModify,
                                            int sectionId, int requestCode,
                                            int classId, String signDate, Set<SectionBean> sectionBeanSet ,
                                            boolean isSameWithFirstEnterModifyDate) {
        final Intent lIntent = new Intent(context, SectionChooseActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putBoolean(CourseDetailActivity.sIsModify, isModify);
        lBundle.putInt(sSectionId, sectionId);
        lBundle.putInt(CourseDetailActivity.sClassId, classId);
        lBundle.putString(sSignDate, signDate);
        lBundle.putSerializable(sSectionSet, (Serializable) sectionBeanSet);
        lBundle.putBoolean(sIsSameDate,isSameWithFirstEnterModifyDate);
        lIntent.putExtras(lBundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        ((Activity) context).startActivityForResult(lIntent, requestCode);
    }

    @Override
    public void showResult(List<SectionBean> sectionBeanList) {
        showList(sectionBeanList);
    }

    private void showList(List<SectionBean> sectionBeanList) {
        if (mIsModify) {
            List<SectionBean> lSectionBeen =  new ArrayList<>();
            lSectionBeen.addAll(sectionBeanList);
            //TODO 要修改的天和修改刚刚进入的天是同一天的时候需要把要修改的节次变为未登记
            if(isSameWithFirstEnterModifyDate){
                lSectionBeen.clear();
                lSectionBeen = presenter.makeModifyUnSign(mSectionId);
            }
            SectionSingleChooseAdapter.Listener lListener = new SectionSingleChooseAdapter.Listener() {
                @Override
                public void itemClick(SectionBean sectionBean) {

                }

                @Override
                public void checkBoxClick(boolean isCheck, SectionBean sectionBean) {
                    mSectionBeanSet = new HashSet<>();
                    if (isCheck) {
                        mSectionBeanSet.add(sectionBean);
                    } else {
                        mSectionBeanSet.remove(sectionBean);
                    }
                    mToolBarManager.getRightButton().setEnabled(mSectionBeanSet.size() > 0);
                }
            };

            //TODO 根据信息显示节次
            List<SectionBean> lSectionBeanList = new ArrayList<>(mSectionHasCheck);
            int defaultCheckSection = 0;
            if(mSectionHasCheck.size() == 0){  //当进来的时候节次没有选择的时候，默认给第一个未登记的打钩
                for(int i=0;i<lSectionBeen.size();i++){
                    if(lSectionBeen.get(i).getSignStatus() == StateBean.UNSIGN){
                        defaultCheckSection = lSectionBeen.get(i).getSectionId();
                        break;
                    }
                }
            }else {
                defaultCheckSection = lSectionBeanList.get(0).getSectionId();
            }
            //TODO 刷新Adapter显示节次信息
            SectionSingleChooseAdapter  lSingleChooseAdapter = new SectionSingleChooseAdapter(lSectionBeen, this, lListener, defaultCheckSection);
            mRecyclerView.setAdapter(lSingleChooseAdapter);
        } else {
            SectionChooseAdapter.Listener lListener = new SectionChooseAdapter.Listener() {
                @Override
                public void itemClick(SectionBean sectionBean) {

                }

                @Override
                public void checkBoxClick(boolean isCheck, SectionBean sectionBean) {
                    if (isCheck) {
                        Log.e("lenita","add sectionBean.getSectionID() = "+sectionBean.getSectionOriginalId());
                        mSectionBeanSet.add(sectionBean);
                    } else {
                        Log.e("lenita","remove sectionBean.getSectionID() = "+sectionBean.getSectionOriginalId());
                        mSectionBeanSet.remove(sectionBean);
                    }
                    mToolBarManager.getRightButton().setEnabled(mSectionBeanSet.size() > 0);
                }
            };
            //拿到已登记的节次列表
            for(int i= 0;i<sectionBeanList.size();i++){
                if(sectionBeanList.get(i).getSignStatus() == SectionBean.SIGN){
                    sectionBeanListHaveBeenSign.add(sectionBeanList.get(i));
                }
            }
            SectionChooseAdapter lAdapter = new SectionChooseAdapter(sectionBeanList, this, lListener, mSectionHasCheck);
            mRecyclerView.setAdapter(lAdapter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_section_choose);
        initData();
        initView();
        loadData();
    }



    private void loadData() {
        try {
            UserBean lUserBean = AppConfiguration.getInstance().getUserBean();
            presenter.getSectionList(lUserBean.getUserId(), mClassId, mSignDate);  //获取该日期下的登记节次情况
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //初始化view
    private void initView() {
        findViews();
        initToolbar();
        setOnInteractListener();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initToolbar() {
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolBarManager.getRightButton().setEnabled(false);
        mToolBarManager.setTitle(this.getString(R.string.section_choose))
                .setRightText(this.getString(R.string.finish))
                .clickRightButton(v -> {
//                    Set<SectionBean> lSectionBeanSet = presenter.sortSet(mSectionBeanSet);
//                        ClassDataManager.getDataCache().setSection(lSectionBeanSet);
                    //TODO 点击完成的时候，先对mSectionBeanSet进行判断，添加的有没有已经被登记的
                    List<SectionBean> sectionBeanList = new ArrayList<>(mSectionBeanSet);
                    for(int i = 0;i< sectionBeanListHaveBeenSign.size();i++){
                        for(int j = 0;j<sectionBeanList.size();j++){
                            sectionBeanList.get(j).setHaveBeenSignFlag(false);
                            if(sectionBeanListHaveBeenSign.get(i).getSectionId() == sectionBeanList.get(j).getSectionId()){
                                sectionBeanList.remove(j);
                            }
                        }
                    }
                    Intent lIntent = new Intent();
                    Bundle lBundle = new Bundle();
                    mSectionBeanSet = new HashSet<>(sectionBeanList);  //剔除掉已登记的选择节次列表
                    lBundle.putSerializable(sSectionSet, (Serializable) mSectionBeanSet);
                    lIntent.putExtras(lBundle);
                    setResult(CourseDetailActivity.mResultCodeSection, lIntent);
                    finish();
                });

    }

    //事件处理
    private void setOnInteractListener() {

    }

    //绑定控件
    private void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mRecyclerView = (RecyclerView) findViewById(R.id.setion_choose_rv);
    }

    //用于页面间数据接收
    private void initData() {
//        presenter.attachView(this);
//        mSectionBeanSet.addAll(ClassDataManager.getDataCache().getSection());
        sectionBeanListHaveBeenSign = new ArrayList<>();
        mIsModify = getIntent().getExtras().getBoolean(RegisterCourseActivity.sIsModify);
        mSectionId = getIntent().getExtras().getInt(sSectionId);
        mClassId = getIntent().getExtras().getInt(CourseDetailActivity.sClassId);
        mSignDate = getIntent().getExtras().getString(sSignDate);
        mSectionHasCheck = (Set<SectionBean>) getIntent().getSerializableExtra(sSectionSet);
        mSectionBeanSetBackPressed = (Set<SectionBean>) getIntent().getSerializableExtra(sSectionSet); //用于特殊处理
        mSectionBeanSet = new HashSet<>();  //用于点击“完成”时，传回去选择的节次
        isSameWithFirstEnterModifyDate = getIntent().getBooleanExtra(sIsSameDate,false);
//        mSectionBeanSet.addAll(mSectionHasCheck);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        boolean isHaveAnySelectHaveBeenSign = IsHaveAnySelectHaveBeenSignWhenBackPress();
        Log.e("lenita","HaveSign? = "+isHaveAnySelectHaveBeenSign+", mSectionBeanSetBackPressed.size() ="+mSectionBeanSetBackPressed.size());
        if(isHaveAnySelectHaveBeenSign){
            /**
             *  如果发现有已经登记的节次，点击返回按钮也是要刷新界面的  CourseADetailActivity -516行
             */

            RxSectionBeanSignList rxSectionBeanSignList = new RxSectionBeanSignList();
            rxSectionBeanSignList.setSectionBeanList(mSectionBeanSetBackPressed);
            RxBus.getDefault().post(rxSectionBeanSignList);
        }
        finish();
    }

    public boolean IsHaveAnySelectHaveBeenSignWhenBackPress(){
        boolean isHaveAnySelectHaveBeenSign = false;
        List<SectionBean> sectionBeanList = new ArrayList<>(mSectionBeanSetBackPressed);
        for(int i = 0;i< sectionBeanListHaveBeenSign.size();i++){
            for(int j = 0;j<sectionBeanList.size();j++){
                sectionBeanList.get(j).setHaveBeenSignFlag(false); //注意，只要是选择节次回去，这里的标识颜色都是false，即正常颜色
                if(sectionBeanListHaveBeenSign.get(i).getSectionId() == sectionBeanList.get(j).getSectionId()){
                    isHaveAnySelectHaveBeenSign = true;
                    sectionBeanList.remove(j);
                }
            }
        }
        mSectionBeanSetBackPressed.clear();
        if(sectionBeanList.size() == 0){
            mSectionBeanSetBackPressed = new HashSet<>();
        }else {
            mSectionBeanSetBackPressed.addAll(sectionBeanList);
        }
        return isHaveAnySelectHaveBeenSign;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
//        presenter.detachView(false);
        mToolBarManager.destroy();
    }


    @NonNull
    @Override
    public SectionChoosePresenter createPresenter() {
        return new SectionChoosePresenter(this);
    }

}
