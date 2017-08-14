package com.gta.zssx.patrolclass.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.patrolclass.model.entity.SeePointsListsEntity;
import com.gta.zssx.patrolclass.presenter.SeePointsListsPresenter;
import com.gta.zssx.patrolclass.view.SeePointsListsView;
import com.gta.zssx.patrolclass.view.adapter.SeePointsListsAdapter;
import com.gta.zssx.pub.base.BaseMvpActivity;

import java.util.List;

/**
 * Created by liang.lu1 on 2016/7/14.
 * 查询每条巡课记录详情页面
 */
public class SeePointsListsActivity extends BaseMvpActivity<SeePointsListsView, SeePointsListsPresenter>
        implements SeePointsListsView {
    private TextView DeptName;
    private TextView Section, noDataTv;
    private TextView Teacher;
    private TextView Status;
    private LinearLayout scoreTitleLayout;
    private ScrollView scrollView;
    private String pId;
    private String title;

    @Override
    public int getLayoutId () {
        return R.layout.activity_points_list;
    }

    @Override
    protected void initData () {
        super.initData ();
        pId = getIntent ().getStringExtra ("pId");
        title = getIntent ().getStringExtra ("title");
    }

    @Override
    protected void initView () {
        findViews ();
        initToolBar ();
        setOnInteractListener ();
    }

    private void findViews () {
        DeptName = (TextView) findViewById (R.id.tv_course_name_show);
        Section = (TextView) findViewById (R.id.tv_dpat_show);
        Teacher = (TextView) findViewById (R.id.tv_teacher_name_show);
        Status = (TextView) findViewById (R.id.tv_text_class_teacher);
        scrollView = (ScrollView) findViewById (R.id.scrollView);
        scoreTitleLayout = (LinearLayout) findViewById (R.id.ll_see_points_details);
        noDataTv = (TextView) findViewById (R.id.tv_no_data);
    }

    private void initToolBar () {
        mToolBarManager.showBack (true)
                .setTitle (title);
    }

    private void setOnInteractListener () {
    }

    @Override
    protected void requestData () {
        super.requestData ();
        presenter.getSeePointsListsData ();
    }

    @NonNull
    @Override
    public SeePointsListsPresenter createPresenter () {
        return new SeePointsListsPresenter ();
    }

    @Override
    public void showResult (SeePointsListsEntity model) {
        String deptName = model.getDeptName ();
        String section = model.getSection ();
        String teacher = model.getTeacher ();
        int status = model.getStatus ();
        DeptName.setText (deptName);
        Teacher.setText (teacher);
        switch (status) {
            case 0:
                Status.setText ("未送审");
                break;
            case 1:
                Status.setText ("待审核");
                break;
            case 2:
                Status.setText ("未通过");
                break;
            case 3:
                Status.setText ("已通过");
                break;
            default:
                break;
        }

        switch (section) {
            case "1":
                Section.setText ("第一节");
                break;
            case "2":
                Section.setText ("第二节");
                break;
            case "3":
                Section.setText ("第三节");
                break;
            case "4":
                Section.setText ("第四节");
                break;
            case "5":
                Section.setText ("第五节");
                break;
            case "6":
                Section.setText ("第六节");
                break;
            case "7":
                Section.setText ("第七节");
                break;
            default:
                break;

        }
    }


    //动态添加标题及recyclerview的显示
    @Override
    public void createScoreTitle (List<String> scoreTitle, SeePointsListsEntity model, List<String> allScore) {
        for (int i = 0; i < scoreTitle.size (); i++) {
            View view = LayoutInflater.from (this).inflate (R.layout.item_see_points_details, null);
            LinearLayout linearLayout = (LinearLayout) view.findViewById (R.id.ll_see_points);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams (layoutParams);
            scoreTitleLayout.addView (view);

            TextView titleTv = (TextView) view.findViewById (R.id.tv_score_title_status);
            TextView scoreTv = (TextView) view.findViewById (R.id.tv_teacher_score_show);
            RecyclerView mRecyclerView = (RecyclerView) view.findViewById (R.id.rl_teacher);
            mRecyclerView.setLayoutManager (new LinearLayoutManager (this));
            List<SeePointsListsEntity.GetScoreListBean.ScoreOptionsBean> bean = model.getGetScoreList ().get (i).getScoreOptions ();
            if (bean.size () > 0) {
                SeePointsListsAdapter adapter = new SeePointsListsAdapter (bean);
                mRecyclerView.setAdapter (adapter);
            } else {
                linearLayout.setVisibility (View.GONE);
            }
            scoreTv.setText (allScore.get (i));
            titleTv.setText (scoreTitle.get (i));
        }
    }

    @Override
    public String getPid () {
        return pId;
    }

    @Override
    public void showEmpty () {
        scrollView.setVisibility (View.GONE);
        noDataTv.setVisibility (View.VISIBLE);
    }

    public static void start (Context context, String pId, String title) {
        final Intent lIntent = new Intent (context, SeePointsListsActivity.class);
        lIntent.putExtra ("pId", pId);
        lIntent.putExtra ("title", title);
        if (!(context instanceof Activity)) {
            lIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity (lIntent);
    }

}
