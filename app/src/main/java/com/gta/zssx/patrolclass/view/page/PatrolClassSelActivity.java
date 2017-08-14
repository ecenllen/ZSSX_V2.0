package com.gta.zssx.patrolclass.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.gta.utils.resource.Toast;
import com.gta.zssx.R;
import com.gta.zssx.patrolclass.model.entity.PatrolClassSelEntity;
import com.gta.zssx.patrolclass.presenter.PatrolClassSelPresenter;
import com.gta.zssx.patrolclass.view.PatrolClassSelView;
import com.gta.zssx.patrolclass.view.adapter.PatrolClassSelAdapter;
import com.gta.zssx.patrolclass.view.dialog.BottomDialog;
import com.gta.zssx.patrolclass.view.dialog.DeleteDialog;
import com.gta.zssx.pub.base.BaseMvpActivity;
import com.gta.zssx.pub.hfrecyclerview.HFRecyclerView;
import com.gta.zssx.pub.hfrecyclerview.HFRecyclerViewItemClickListener;
import com.gta.zssx.pub.util.LogUtil;
import com.gta.zssx.pub.util.TimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by liang.lu1 on 2016/7/13.
 */
public class PatrolClassSelActivity extends BaseMvpActivity<PatrolClassSelView, PatrolClassSelPresenter> implements
        PatrolClassSelView, View.OnClickListener, HFRecyclerView.HFRecyclerViewListener, HFRecyclerViewItemClickListener {
    //时间
    private TextView dateTv, go_on_plan_class, go_on_random_class, noDataTv;
    //显示空页面提示
    //    private TextView noDataTv;
    private HFRecyclerView mRecyclerView;
    private int state;
    private List<PatrolClassSelEntity> models;
    private PatrolClassSelAdapter adapter;
    //长按选中的item位置
    private int position = -1;
    private int Ttype;
    int xId;
    String pId;
    String date;

    @Override
    public int getLayoutId () {
        return R.layout.activity_patrol_class_sel;
    }

    @Override
    protected void initData () {
        super.initData ();
        Ttype = getIntent ().getIntExtra ("type", -1);
        state = getIntent ().getIntExtra ("state", -1);
        xId = getIntent ().getIntExtra ("xId", -1);
        date = getIntent ().getStringExtra ("date");
    }

    @Override
    protected void initView () {
        findViews ();
        initToolBar ();
        setOnInteractListener ();
        loadData ();
    }

    private void loadData () {
        if (Ttype == 2) {
            go_on_plan_class.setText ("继续按计划巡课");
            go_on_random_class.setText ("继续随机巡课");
        } else {
            go_on_plan_class.setText ("新增按计划巡课");
            go_on_random_class.setText ("新增随机巡课");
        }
    }

    private void initToolBar () {
        mToolBarManager.showBack (true)
                .setTitle (getResources ().getString (R.string.class_patrol_history));
    }

    private void findViews () {
        dateTv = (TextView) findViewById (R.id.tv_date2);
        noDataTv = (TextView) findViewById (R.id.tv_non);
        go_on_plan_class = (TextView) findViewById (R.id.go_on_plan_class);
        go_on_random_class = (TextView) findViewById (R.id.go_on_random_class);
        mRecyclerView = (HFRecyclerView) findViewById (R.id.recycler);
    }

    @Override
    protected void requestData () {
        super.requestData ();
        presenter.getPatrolClassSelData ();
    }

    private void setOnInteractListener () {
        go_on_plan_class.setOnClickListener (this);
        go_on_random_class.setOnClickListener (this);
        mRecyclerView.setCanLoadMore (true);
        mRecyclerView.setCanRefresh (true);
        mRecyclerView.setFooterViewText ("");
        mRecyclerView.setRecyclerViewListener (this);
        mRecyclerView.setItemClickListener (this);
    }


    @NonNull
    @Override
    public PatrolClassSelPresenter createPresenter () {
        return new PatrolClassSelPresenter ();
    }

    @Override
    public void showResult (List<PatrolClassSelEntity> models) {
        this.models = models;
        isShowSubmit ();
        dateTv.setText (presenter.setDateTv (date, Ttype));

        mRecyclerView.setVisibility (View.VISIBLE);
        adapter = new PatrolClassSelAdapter (this, Ttype);
        adapter.setmPatrolClassSelModels (models);
        mRecyclerView.setAdapter (adapter);
    }

    @Override
    public void showEmpty () {
        mRecyclerView.setVisibility (View.GONE);
        noDataTv.setVisibility (View.VISIBLE);
        mToolBarManager.showRightButton (false);
        dateTv.setVisibility (View.GONE);

    }

    @Override
    public int getXid () {
        return xId;
    }

    @Override
    public String getPid () {
        return pId;
    }

    @Override
    public void DeleteItemSuccess () {
        models.remove (position);
        adapter.setmPatrolClassSelModels (models);
        adapter.notifyItemRemoved (position);
        Toast.Short (PatrolClassSelActivity.this, "删除成功");
        loadData ();
    }

    @Override
    public void submitSuccess () {
        finish ();
        //        PatrolClassActivity.start4Result (this);
    }

    @Override
    public void hideLoadMoreAndRefresh (List<PatrolClassSelEntity> mPatrolClassSelModels) {
        this.models = mPatrolClassSelModels;
        mRecyclerView.stopRefresh (true);
        mRecyclerView.stopLoadMore (true);
        adapter.setmPatrolClassSelModels (mPatrolClassSelModels);
        adapter.notifyDataSetChanged ();
    }

    @Override
    public void onLoadMoreError () {
        mRecyclerView.stopLoadMore (false);
    }

    @Override
    public void onRefreshError () {
        mRecyclerView.stopRefresh (false);
    }

    @Override
    public void onLoadMoreEmpty () {
        mRecyclerView.stopLoadMore (false);
        mRecyclerView.setFooterViewText ("无更多信息");
        //        isShowSubmit ();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        presenter.detachView (false);
        mToolBarManager.destroy ();
    }

    /**
     * @param context context
     * @param state   状态，根据state来决定是否显示 一键送审
     * @param type    1 表示是巡课列表记录进入的 2 表示是巡课登记页面点击完成之后进入的。
     * @param xId     巡课ID
     */
    public static void start (Context context, int state, int type, int xId, String date) {
        final Intent lIntent = new Intent (context, PatrolClassSelActivity.class);
        lIntent.putExtra ("xId", xId);
        lIntent.putExtra ("state", state);
        lIntent.putExtra ("date", date);
        lIntent.putExtra ("type", type);
        if (!(context instanceof Activity)) {
            lIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity (lIntent);
    }

    /**
     * 显示Dialog
     *
     * @param content 文字内容
     * @param res     1 删除 2 一键送审
     */
    private void popupConfirmDialog (String content, final int res) {
        String msg;
        boolean isShowMainColor = false;
        if (res == 1) {
            msg = "确定";
        } else {
            msg = "一键送审";
            isShowMainColor = true;
        }

        new DeleteDialog (this).showDialog (true, msg, content,
                () -> {
                    if (res == 1) {
                        presenter.DeletePatrolItem ();
                    } else {
                        //一键送审
                        presenter.submitApproval ();
                    }
                }, isShowMainColor);
    }

    @Override
    public void onClick (View v) {
        if (TimeUtils.isFastDoubleClick ()) {
            return;
        }
        switch (v.getId ()) {
            case R.id.go_on_plan_class:
                //点击继续按计划巡课
                PlanProtalResultActivity.start (this, "1", presenter.setDate (date, Ttype));
                break;
            case R.id.go_on_random_class:
                //点击继续按随机巡课
                AddClassChooseActivity.start (this, "random", "1", presenter.setDate (date, Ttype));
                break;
        }
    }

    @Override
    public void onLoadMore () {
        presenter.doLoadMore ();
    }

    @Override
    public void onRefresh () {
        presenter.doRefresh ();
    }


    /**
     * 是否显示一键送审
     */
    public void isShowSubmit () {
        if (models != null) {
            if (state == 0 || state == 2 || Ttype == 2) {
                mToolBarManager.showRightButton (true)
                        .setRightText ("一键送审")
                        .clickRightButton (v -> popupConfirmDialog ("每天只能送审一次,送审后当天不能再登记,是否确认当天登记已完成,依然送审?", 2));

            }
        } else {
            mToolBarManager.showSave (false);
        }
    }

    @Override
    public void onItemClick (View v, int position) {
        SeePointsListsActivity.start (this, models.get (position).getPId (), models.get (position).getClassName ());
    }

    @Override
    public void onLongItemClick (View v, int position) {
        pId = models.get (position).getPId ();
        int classId = models.get (position).getClassId ();
        String deptId = models.get (position).getDeptId ();
        int sectionId = models.get (position).getSectionId ();
        String className = models.get (position).getClassName ();
        LogUtil.e ("pId ==" + pId);
        int status = models.get (position).getStatus ();
        if (status == 0 || status == 2) {
            this.position = position;
            new BottomDialog (this).showBottomDialog (new BottomDialog.BottomDialogClickListener () {
                @Override
                public void modifyClick () {
                    //跳转到修改页面
                    String mDate;
                    if (Ttype == 1) {
                        mDate = date.substring (0, 10);
                    } else {
                        SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy年MM月dd日");
                        Date late = null;
                        try {
                            late = dateFormat.parse (date);
                        } catch (ParseException e) {
                            e.printStackTrace ();
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd");
                        mDate = sdf.format (late);
                    }
                    PatrolRegisterActivity.start (PatrolClassSelActivity.this, deptId, classId, mDate, sectionId, xId, "2", Integer.parseInt (pId), "", className);
                }

                @Override
                public void deleteClick () {
                    //弹出对话框，询问是否真的要删除
                    popupConfirmDialog ("确定删除该条巡课登记信息吗?", 1);
                }
            });
        }
    }
}
