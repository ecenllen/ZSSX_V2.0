package com.gta.zssx.fun.coursedaily.registercourse.view.page;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.gta.utils.resource.Toast;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ClassDisplayBean;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.CourseDailyPresenter;
import com.gta.zssx.fun.coursedaily.registercourse.view.CourseDailyView;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ClassDisplayAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ui.IsDeleteSelectItemDialog;
import com.gta.zssx.fun.coursedaily.registercourse.view.pageV2.ClassChooseActivity;
import com.gta.zssx.fun.coursedaily.registercourse.view.pageV2.CourseDailyActivity;
import com.gta.zssx.fun.coursedaily.registercourse.view.pageV2.CourseDetailActivity;
import com.gta.zssx.fun.coursedaily.registerrecord.view.page.AlreadyRegisteredRecordActivity;
import com.gta.zssx.fun.coursedaily.registerrecord.view.page.MyClassRecordFragment;
import com.gta.zssx.fun.coursedaily.registerrecord.view.page.MyClassRecordSelectTabFragment;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 课堂日志
 * Created by xiao.peng on 2016/11/9.
 */
public class CourseDailyFragment extends BaseFragment<CourseDailyView, CourseDailyPresenter> implements CourseDailyView {

    @Bind(R.id.class_display_rv)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.no_data_tv)
    TextView noDataTv;

    public ClassDisplayAdapter mClassDisplayAdapter;
    public UserBean mUserBean;
    public int mDeletePosition;

    private List<UserBean.ClassList> mClassLists;
    private String mUserId;
    private Map<Integer,String> classMap;
    List<ClassDisplayBean> classDisplayList;

    public static CourseDailyFragment newInstance() {

        Bundle args = new Bundle();
        CourseDailyFragment fragment = new CourseDailyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public CourseDailyPresenter createPresenter() {
        return new CourseDailyPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_dispalys, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
        loadData();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void loadData() {
        mSwipeRefreshLayout.post(() -> presenter.getClassList());
    }

    //事件处理
    private void setOnInteractListener() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main_color, R.color.blue, R.color.purple);
        mSwipeRefreshLayout.setOnRefreshListener(() -> presenter.getClassList());
    }

    //初始化view
    private void initView() {
        setOnInteractListener();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    //用于页面间数据接收
    private void initData() {

//        presenter.attachView(this);
        classMap = new HashMap<>();
        classDisplayList = new ArrayList<>();
        mClassLists = new ArrayList<>();
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mUserBean != null) {
            mClassLists = mUserBean.getClassName();
            mUserId = mUserBean.getUserId();
            //先更新是否是班主任
            presenter.getTeacherClassUpdate(mUserBean, CourseDailyActivity.FIRST_GET_IS_TEACHER);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void showResult(List<ClassDisplayBean> classDisplayBeen) {
        noDataTv.setVisibility(View.GONE);
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(false));
        //TODO 拿到班级的Map集合，用于搜索不显示已经添加过的班级
        classMap = new HashMap<>();
        if(classDisplayBeen.size() > 0){
            classDisplayList = classDisplayBeen;
            for(int i = 0;i<classDisplayBeen.size();i++){
                ClassDisplayBean classDisplayBean = classDisplayBeen.get(i);
                int classId = classDisplayBean.getClassId();
                String className = classDisplayBean.getClassName();
                classMap.put(classId,className);
            }
        }
        /*Set<Integer> set = classMap.keySet();
        Iterator<Integer> iterator = set.iterator();
        while (iterator.hasNext()){
            Log.e("lenita","map key = "+iterator.next());
        }*/
        showList(classDisplayBeen);
    }

    @Override
    public void showLoadingDialog() {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));
    }

    @Override
    public void emptyUI(boolean isEmpty) {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(false));
        if (mClassDisplayAdapter != null) {
            if (isEmpty) {
                noDataTv.setVisibility(View.VISIBLE);
//                mClassDisplayAdapter.notifyEmpty();
            }
        }
    }

    @Override
    public void isTeacher(UserBean userBean) {
        boolean lTeacherName = userBean.isTeacherName();
        if (lTeacherName) {
            mClassLists = userBean.getClassName();
            if (mClassLists.size() > 0) {
                if (mClassLists.size() > 1) {
                    AlreadyRegisteredRecordActivity.start(getActivity(), MyClassRecordSelectTabFragment.PAGE_TAG, mClassLists, true);
                } else {
                    AlreadyRegisteredRecordActivity.start(getActivity(), MyClassRecordFragment.PAGE_TAG, mClassLists, true);
                }
            } else {
                Toast.Short(getActivity(), getResources().getString(R.string.text_class_log));
            }
        } else {
            mClassDisplayAdapter.notifyItemChanged(mClassDisplayAdapter.getItemCount() - 1);
            Toast.Short(getActivity(), "你不是班主任");
        }
    }


    @Override
    public void deleteClassSuccess() {
        Toast.Short(getActivity(), getResources().getString(R.string.text_delete_success));
//        if(mClassDisplayAdapter !=null){
//            mClassDisplayAdapter.updateList(ClassId);
//        }

        if(classMap.containsKey(classDisplayBeanList.get(mDeletePosition).getClassId())){
            classMap.remove(classDisplayBeanList.get(mDeletePosition).getClassId());
        }
        classDisplayList.remove(mDeletePosition);
        mClassDisplayAdapter.notifyDelete(mDeletePosition);
        if (mClassDisplayAdapter.getItemCount() <= 1) {
            AppConfiguration.runOnThread(new Runnable() {
                @Override
                public void run() {
                    AppConfiguration.getAppConfiguration().setFirstIn(null).saveAppConfiguration();
                }
            });
        }
    }

    private String ClassId;
    private String ClassName;
    private List<ClassDisplayBean> classDisplayBeanList = new ArrayList<>();

    private void showList(List<ClassDisplayBean> classDisplayBeen) {
        classDisplayBeanList = classDisplayBeen;
        ClassDisplayAdapter.Listener lListener = new ClassDisplayAdapter.Listener() {
            @Override
            public void itemClick(ClassDisplayBean item) {
                ClassDataManager.getDataCache().setsClassDisplayBean(null);
                ClassDataManager.getDataCache().setsClassDisplayBean(item);
                CourseDetailActivity.start(mActivity, item.getClassName(), false, null, item.getClassId());
            }

            @Override
            public void addClick() {
                ClassDataManager.getDataCache().setClassDisplay(null);
                ClassChooseActivity.start(mActivity,classMap);
            }

            @Override
            public void myClassClick() {
                try {
                    UserBean lUserBean = AppConfiguration.getInstance().getUserBean();
                    presenter.getTeacherClassUpdate(lUserBean, CourseDailyActivity.NORMAL_GET_IS_TEACHER);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void itemLongClick(ClassDisplayBean classDisplayBean, int position) {
                ClassId = classDisplayBean.getClassId() + "";
                ClassName = classDisplayBean.getClassName();
                mDeletePosition = position;
//                backgroundAlpha(0.8f);
                popupConfirmDialog("是否确认删除班级");
            }
        };

        mClassDisplayAdapter = new ClassDisplayAdapter(mActivity, lListener, classDisplayBeen);
        mRecyclerView.setAdapter(mClassDisplayAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (CourseDailyActivity.isRefresh) {
            presenter.getClassList();
            CourseDailyActivity.isRefresh = false;
        }

        if (mClassDisplayAdapter != null) {
            mClassDisplayAdapter.notifyItemChanged(mClassDisplayAdapter.getItemCount() - 1);
        }

    }

    private IsDeleteSelectItemDialog mDialog;

    //显示弹框确认是否删除班级
    private void popupConfirmDialog(String content) {
        if (mDialog != null)
            mDialog = null; //置空然后重新赋值
        String text = content + "“" + ClassName + "”？";
        mDialog = new IsDeleteSelectItemDialog(mActivity, text, new IsDeleteSelectItemDialog.Listener() {
            @Override
            public void onDialogDismissListener() {
//                backgroundAlpha(1f);
            }

            @Override
            public void onSureListerner() {
                presenter.deleteTeacherClass(ClassId, mUserId, getActivity());
            }
        });  //使用自定义的样式
        mDialog.show();
    }

  /*  private void backgroundAlpha(float b) {
        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
        layoutParams.alpha = b;
        getActivity().getWindow().setAttributes(layoutParams);
    }*/
}
