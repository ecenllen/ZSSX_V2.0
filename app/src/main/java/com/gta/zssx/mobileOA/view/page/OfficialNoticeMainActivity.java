package com.gta.zssx.mobileOA.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.view.adapter.ui.titlePopupWindow;
import com.gta.zssx.mobileOA.view.base.BaseOAActivity;
import com.gta.zssx.pub.common.Constant;

/**
 * Created by lan.zheng on 2016/10/31.
 * 不需要presenter数据的时候直接用这个基础BaseOAActivity
 */
public class OfficialNoticeMainActivity extends BaseOAActivity implements View.OnClickListener{
    private TextView titleTextView;
    private ImageView searchImageView;
    private String[] titles;
    private Toolbar toolbar;
    private titlePopupWindow mTitlePopupWindow;
    private final static int SHOW_TITLE_PAGE_NOTICE = 0; //学校公告
    private final static int SHOW_TITLE_PAGE_OFFICIAL = 1; //学校公告
//    private ViewPager mPager;
//    private ArrayList<Fragment> fragmentList;
//    private TabLayout tlTabs;
//    private String[] tabs1 = new String[]{"未读", "已读"};
//    private String[] tabs2 = new String[]{"未值班", "已值班"};
//    private TFragmentPagerAdapter myFragmentPagerAdapter;

    public static void start(Context context) {
        final Intent lIntent = new Intent(context, OfficialNoticeMainActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_notice_main_page);
        initView();
        initData();
    }

    private void initView(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); //如果是公文公告，这里就是直接finish
            }
        });
        setSelectTitleToolbar(toolbar);
        setShowTitlePage(SHOW_TITLE_PAGE_NOTICE);
        titles = new String[]{getString(R.string.text_school_notice),getString(R.string.text_school_official)};
        titleTextView = (TextView) findViewById(R.id.tv_select_title);
        titleTextView.setCompoundDrawables(null,null,null,null);
        titleTextView.setText(titles[1]);
        searchImageView = (ImageView)findViewById(R.id.iv_search);
    }

    private void initData(){
//        titleTextView.setText(titles[SHOW_TITLE_PAGE_NOTICE]);
        searchImageView.setVisibility(View.VISIBLE);
//        titleTextView.setOnClickListener(this);
        searchImageView.setOnClickListener(this);

        /*tlTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        tlTabs.setTabGravity(TabLayout.GRAVITY_CENTER);
        fragmentList = new ArrayList<>();*/
        initFragmentManager();
        goToFragment(SHOW_TITLE_PAGE_OFFICIAL);  //默认“学校公文”
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select_title:
                showTitleItems();
                break;
            case R.id.iv_search:
                Intent intent = new Intent(OfficialNoticeMainActivity.this, SearchActivity.class);
                intent.putExtra("officialNoticeType",getShowTitlePage());
                intent.putExtra(Constant.KEY_SEARCH_TYPE,Constant.SEARCH_TYPE_OFFICIAL);
//                if(getShowTitlePage() == SHOW_TITLE_PAGE_NOTICE){  //判断要搜索的种类
//                    intent.putExtra(Constant.KEY_OFFICIAL_NOTICE_TYPE,Constant.OFFICIAL_NOTICE_TYPE_NOTICE);
//                }else {
                    intent.putExtra(Constant.KEY_OFFICIAL_NOTICE_TYPE,Constant.OFFICIAL_NOTICE_TYPE_OFFICIAL);
//                }
                startActivity(intent);
                break;
        }
    }

    private void showTitleItems(){
        String title = titleTextView.getText().toString();
        mTitlePopupWindow = new titlePopupWindow(this,titles,title,new titlePopupWindow.Listener() {
            @Override
            public void onPopupWindowDismissListener() {
                //无操作
            }

            @Override
            public void onItemClickListener(int position) {
                setShowTitlePage(position);  //点击时设置判断
                if(title.equals(titles[position]))
                    return;
                titleTextView.setText(titles[position]);
                goToFragment(position);
            }
        });
        mTitlePopupWindow.showAsDropDown(titleTextView);
    }

    private void initFragmentManager() {
        fragmentManager = getSupportFragmentManager();
    }

    private void highFragment(FragmentTransaction fragmentTransaction) {
        if (officialRemindFragment != null) {
            fragmentTransaction.hide(officialRemindFragment);
        }
        if (noticeRemindFragment != null) {
            fragmentTransaction.hide(noticeRemindFragment);
        }
    }

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private OfficialRemindFragment noticeRemindFragment;  //学校公告
    private OfficialRemindFragment officialRemindFragment;  //学校公文
    private void goToFragment(int p) {
        fragmentTransaction = fragmentManager.beginTransaction();
        highFragment(fragmentTransaction);
//        switch (p) {
//            case 0:
//                if (noticeRemindFragment == null) {
//                    noticeRemindFragment = new OfficialRemindFragment();//不能用setArgument方法，java.lang.IllegalStateException: Fragment already active
//                    fragmentTransaction.add(R.id.container, noticeRemindFragment);
//                } else {
//                    fragmentTransaction.show(noticeRemindFragment);
//                    noticeRemindFragment.onRefresh();
//                }
//                break;
//            case 1:
                if (officialRemindFragment == null) {
                    officialRemindFragment = new OfficialRemindFragment();//不能用setArgument方法，java.lang.IllegalStateException: Fragment already active
                    fragmentTransaction.add(R.id.container, officialRemindFragment);
                } else {
                    fragmentTransaction.show(officialRemindFragment);
                    officialRemindFragment.onRefresh();
                }
//                break;
//        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    /*private void showContentFragment(int p){
        LogUtil.Log("lenita","title = "+titles[p]);
        setShowTitlePage(p);  //要显示的是标题对应的那种选项
        showWhichTab = SHOW_TAB_DEFAULT;
        fragmentList.clear();
        mPager.clearOnPageChangeListeners();
        myFragmentPagerAdapter = null;
        if(p == 0){
            for (int i = 0; i < tabs1.length; i++) {
                Bundle bundle = new Bundle();
                bundle.putInt("tab",i);//先设tab要显示的是“未读、已读”，再生成fragment
                Fragment fragment = new OfficialRemindFragment();
                fragment.setArguments(bundle);
                fragmentList.add(fragment);
            }
        }else if (p == 1){
            for (int i = 0; i < tabs1.length; i++) {
                Bundle bundle = new Bundle();
                bundle.putInt("tab",i);//先设tab要显示的是“未读、已读”，再生成fragment
                Fragment fragment = new OfficeWebFragment();
                fragment.setArguments(bundle);
                fragmentList.add(fragment);
            }
        }
        myFragmentPagerAdapter = new TFragmentPagerAdapter(getSupportFragmentManager(), tabs1, fragmentList);
        //给ViewPage设置Adapter
        mPager.setAdapter(myFragmentPagerAdapter);
        //设置当前显示标签页为上次记录位置
        myFragmentPagerAdapter.notifyDataSetChanged();
        mPager.setCurrentItem(showWhichTab);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogUtil.Log("lenita","mPager position = "+position);
                showWhichTab = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tlTabs.setupWithViewPager(mPager);
    }

    public class TFragmentPagerAdapter extends FragmentStatePagerAdapter {
        private String[] titles;
        private List<Fragment> fragments;

        public TFragmentPagerAdapter(FragmentManager fm,String[] titles,List<Fragment> fragments) {
            super(fm);
            this.titles = titles;
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments == null? 0:fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }*/
}
