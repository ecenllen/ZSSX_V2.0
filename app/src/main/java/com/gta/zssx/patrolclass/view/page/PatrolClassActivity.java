package com.gta.zssx.patrolclass.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.personalcenter.view.page.PersonCenterActivity;
import com.gta.zssx.patrolclass.popup.AddPageActivity;
import com.gta.zssx.patrolclass.popup.AddProtalPopupWindow;
import com.gta.zssx.patrolclass.view.adapter.MyFragmentPagerAdapter;
import com.gta.zssx.pub.base.BaseCommonActivity;
import com.gta.zssx.pub.util.TimeUtils;

import java.util.ArrayList;

/**
 * Created by liang.lu1 on 2016/7/8.
 * 课堂巡视，首页
 */
public class PatrolClassActivity extends BaseCommonActivity implements View.OnClickListener,
        AddProtalPopupWindow.PopupWindowItemClickListener {
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentList;
    private TextView tv_finished;
    private TextView tv_unfinished;
    private TextView tab_home, tab_add, tab_my;
    private View toolBarAddIv;

    @Override
    public int getLayoutId () {
        return R.layout.fragment_patrol_class_main;
    }

    @Override
    protected void initView () {
        findViews ();
        initToolBar ();
        setOnInteractListener ();
    }

    private void setOnInteractListener () {
        tab_home.setOnClickListener (this);
        tab_add.setOnClickListener (this);
        tab_my.setOnClickListener (this);
        toolBarAddIv.setOnClickListener (this);
        tv_finished.setOnClickListener (new txListener (1));
        tv_unfinished.setOnClickListener (new txListener (0));
    }

    private void initToolBar () {
        mToolBarManager.showBack (true)
                .setTitle (getResources ().getString (R.string.class_patrol))
                .showRightIcTime (true)
                .showRightIcAddClass (true)
                .clickTime (v -> HistoryTimeActivity.start (PatrolClassActivity.this));
    }

    private void findViews () {
        tab_home = (TextView) findViewById (R.id.tab_home);
        tab_add = (TextView) findViewById (R.id.tab_add);
        tab_my = (TextView) findViewById (R.id.tab_my);
        tv_finished = (TextView) findViewById (R.id.tv_finished);
        tv_unfinished = (TextView) findViewById (R.id.tv_un_finished);
        toolBarAddIv = findViewById (R.id.iv_ic_add);
        mPager = (ViewPager) findViewById (R.id.viewPage_patrol);
    }

    @Override
    protected void requestData () {
        super.requestData ();
        fragmentList = new ArrayList<> ();
        Fragment finishedFragment = new FinishedFragment ();
        Fragment notFinishedFragment = new NotFinishedFragment ();
        fragmentList.add (notFinishedFragment);
        fragmentList.add (finishedFragment);

        //给ViewPage设置Adapter
        mPager.setAdapter (new MyFragmentPagerAdapter (getSupportFragmentManager (), fragmentList));
        //设置当前显示标签页为第一页
        mPager.setCurrentItem (0);
        tv_unfinished.setTextColor (getResources ().getColor (R.color.main_color));

        mPager.setOnPageChangeListener (new ViewPager.OnPageChangeListener () {
            @Override
            public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected (int position) {
                if (position == 0) {
                    tv_unfinished.setTextColor (getResources ().getColor (R.color.main_color));
                    tv_finished.setTextColor (getResources ().getColor (R.color.gray_666666));
                } else {
                    //已结束的变颜色
                    tv_finished.setTextColor (getResources ().getColor (R.color.main_color));
                    tv_unfinished.setTextColor (getResources ().getColor (R.color.gray_666666));
                }
            }

            @Override
            public void onPageScrollStateChanged (int state) {

            }
        });
    }

    @Override
    public void onClick (View v) {
        if (TimeUtils.isFastDoubleClick ()) {
            return;
        }
        switch (v.getId ()) {
            case R.id.tab_home:
                finish ();
                break;
            case R.id.tab_add:
                //                AddPageActivity.start4Result(this);
                //                new NewAddPopupWindow (PatrolClassActivity.this, this, 2).onCreate ();
                startActivity (new Intent (this, AddPageActivity.class));
                //                overridePendingTransition (R.anim.add_page_enter_anim, R.anim.add_page_out_anim);
                break;
            case R.id.tab_my:
                PersonCenterActivity.start (this);
                break;
            /*响应点击添加按钮，弹出popupwindow*/
            case R.id.iv_ic_add:
                int location[] = new int[2];
                toolBarAddIv.getLocationOnScreen (location);
                AddProtalPopupWindow window = new AddProtalPopupWindow (this, mToolBarManager.getToolbar (), location);
                window.setListener (this);
                break;
            default:
                break;

        }
    }

    @Override
    public void clickPlanItem () {
        //点击按计划巡课
        PlanProtalResultActivity.start (this, "1", "");
    }

    @Override
    public void clickRandomItem () {
        //随机巡课
        AddClassChooseActivity.start (this, "random", "1", "");
    }

    private class txListener implements View.OnClickListener {
        private int index = 0;

        txListener (int i) {
            index = i;
        }

        @Override
        public void onClick (View v) {
            mPager.setCurrentItem (index);
        }
    }

    public static void start (Context context) {
        final Intent lIntent = new Intent (context, PatrolClassActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity (lIntent);
    }
}
