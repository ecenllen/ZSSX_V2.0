package com.gta.zssx.patrolclass.view.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.R;
import com.gta.zssx.patrolclass.model.entity.PatrolRegisterBeanNew;
import com.gta.zssx.patrolclass.view.adapter.ChooseSectionsAdapter2;
import com.gta.zssx.pub.base.BaseCommonActivity;

import java.io.Serializable;
import java.util.List;

import static com.gta.zssx.patrolclass.view.page.PatrolRegisterActivityNew.PATROL_CLASS_SECTION;

/**
 * Created by liang.lu on 2017/4/19 15:53.
 */

public class PatrolChooseSectionActivity extends BaseCommonActivity implements ChooseSectionsAdapter2.Listener {

    public static final String SECTION_DATA = "section_data";
    public static final String SECTION_NAME = "section_name";
    public static final String SECTION_ID = "section_id";
    public static final int RESULT_CODE_SECTION = 100;
    private RecyclerView mRecyclerView;
    private TextView mEmptyTv;
    private List<PatrolRegisterBeanNew.SectionsListBean> mSectionData;
    private String sectionName;
    private int sectionId;

    @Override
    public int getLayoutId () {
        return R.layout.activity_choose_sections;
    }

    @Override
    protected void initView () {
        findView ();
        initToolbar ();
    }

    private void findView () {
        mRecyclerView = (RecyclerView) findViewById (R.id.rv_setion_choose);
        mEmptyTv = (TextView) findViewById (R.id.tv_non);
        mRecyclerView.setHasFixedSize (true);
        mRecyclerView.setLayoutManager (new LinearLayoutManager (this));
    }

    private void initToolbar () {
        mToolBarManager
                .setTitle (this.getString (R.string.section_choose))
                .showRightButton (true)
                .setRightText (this.getString (R.string.finish))
                .clickRightButton (v -> {
                    boolean selected = isSectionSelected ();
                    if (!selected) {
                        ToastUtils.showShortToast("请选择节次");
                        return;
                    }
                    Intent mIntent = new Intent ();
                    Bundle mBundle = new Bundle ();
                    mBundle.putSerializable (SECTION_DATA, (Serializable) mSectionData);
                    mBundle.putString (SECTION_NAME, sectionName);
                    mBundle.putInt (SECTION_ID, sectionId);
                    mIntent.putExtras (mBundle);
                    setResult (RESULT_CODE_SECTION, mIntent);
                    finish ();
                });
    }

    @Override
    protected void requestData () {
        super.requestData ();
        if (mSectionData == null) {
            showEmpty ();
            return;
        }
        ChooseSectionsAdapter2 mAdapter = new ChooseSectionsAdapter2 ();
        mAdapter.setBeanList (mSectionData);
        mAdapter.setListener (this);
        mRecyclerView.setAdapter (mAdapter);
    }

    protected void initData () {
        mSectionData = (List<PatrolRegisterBeanNew.SectionsListBean>) getIntent ().getExtras ().getSerializable (PATROL_CLASS_SECTION);
    }

    @Override
    public void setEntities (List<PatrolRegisterBeanNew.SectionsListBean> sectionsListBeen) {
        this.mSectionData = sectionsListBeen;
    }

    private void showEmpty () {
        mRecyclerView.setVisibility (View.GONE);
        mEmptyTv.setVisibility (View.VISIBLE);
    }

    /**
     * 遍历section集合是否有选中的
     *
     * @return true or false
     */
    private boolean isSectionSelected () {
        for (int i = 0; i < mSectionData.size (); i++) {
            if (mSectionData.get (i).getType () == 0) {
                if (mSectionData.get (i).isCheck ()) {
                    sectionName = mSectionData.get (i).getSectionName ();
                    sectionId = mSectionData.get (i).getSectionId ();
                    return true;
                }
            }
        }
        return false;
    }
}
