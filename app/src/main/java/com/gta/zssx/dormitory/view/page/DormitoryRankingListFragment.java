package com.gta.zssx.dormitory.view.page;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.gta.utils.resource.Toast;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.DormitoryListArgumentBean;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassSingleInfoBean;
import com.gta.zssx.dormitory.model.bean.DormitoryRankingBean;
import com.gta.zssx.dormitory.model.bean.RxDormitoryRankingListUpdateBean;
import com.gta.zssx.dormitory.model.bean.RxSaveNeedToOpt;
import com.gta.zssx.dormitory.presenter.DormitoryRankingListPresenter;
import com.gta.zssx.dormitory.view.DormitoryRankingListView;
import com.gta.zssx.dormitory.view.adapter.DormitoryItemListAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ui.IsDeleteSelectItemDialog;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.base.BaseMvpFragment;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.hfrecyclerview.HFRecyclerView;
import com.gta.zssx.pub.util.Contanst;
import com.gta.zssx.pub.util.RxBus;
import com.gta.zssx.pub.widget.CustomToast;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * [Description]
 * 评分录入列表显示Fragment
 * [How to use]
 * flag为0表示未送审，flag为1表示已送审,共用一个Fragment
 * [Tips]
 * <p>
 * Created by lan.zheng on 2017/7/24 13:14.
 */

public class DormitoryRankingListFragment extends BaseMvpFragment<DormitoryRankingListView, DormitoryRankingListPresenter> implements DormitoryRankingListView, HFRecyclerView.HFRecyclerViewListener {

    private int mFlag;  //0:未送审,1:已送审
    public static final int ACTION_REFRESH = 0;  //下拉刷新
    public static final int ACTION_LOAD_MORE = 1;  //上拉加载更多
    private int mIndex;  //显示第几页
    private UserBean mUserBean;
    private String mUserId;  //用户Id
    private HFRecyclerView mRecyclerView;
    private TextView emptyTextView;  //空页面
    private DormitoryItemListAdapter dormitoryItemListAdapter;  //适配器
    private int mPosition;  //用于删除和送审成功位置记录
    public CompositeSubscription mCompositeSubscription;//监听

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dormitory_ranking_list;
    }

    @Override
    protected void initView(View view) {
        mRecyclerView = (HFRecyclerView) view.findViewById(R.id.rv_ranking_list);
        emptyTextView = (TextView) view.findViewById(R.id.tv_empty);
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
            mUserId = mUserBean.getUserId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setOnInteractListener();
    }

    private void setOnInteractListener() {
        mCompositeSubscription = new CompositeSubscription();
        mRecyclerView.setCanLoadMore(true);
        mRecyclerView.setCanRefresh(true);
        mRecyclerView.setFooterViewText("");
        mRecyclerView.setRecyclerViewListener(this);
        dormitoryItemListAdapter = new DormitoryItemListAdapter(getActivity(), mFlag, listener);
        mRecyclerView.setAdapter(dormitoryItemListAdapter);
        /**
         * 添加刷新页面监听:rx_dormitory_ranking_list_refresh
         */
        mCompositeSubscription.add(RxBus.getDefault().toObserverable(RxDormitoryRankingListUpdateBean.class)
                .subscribe(new Subscriber<RxDormitoryRankingListUpdateBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RxDormitoryRankingListUpdateBean rxDormitoryRankingListUpdateBean) {
                        //TODO 执行了送审操作的要更新已审核的列表,因为送审后的变成了送审中
                        if (rxDormitoryRankingListUpdateBean.getNeedToUpdatePage() == RxDormitoryRankingListUpdateBean.REFRESH_ALL_LIST) {
                            requestData();
                        } else {
                            if (mFlag == 0 && rxDormitoryRankingListUpdateBean.getNeedToUpdatePage() == RxDormitoryRankingListUpdateBean.REFRESH_NOT_SUBMIT_LIST) {
                                //更新未送审列表
                                requestData();
                            } else if (mFlag == 1 && rxDormitoryRankingListUpdateBean.getNeedToUpdatePage() == RxDormitoryRankingListUpdateBean.REFRESH_HAVE_BEEN_SUBMIT_LIST) {
                                //更新已送审列表
                                requestData();
                            }
                        }
                    }
                }));
    }

    @NonNull
    @Override
    public DormitoryRankingListPresenter createPresenter() {
        return new DormitoryRankingListPresenter();
    }

    @Override
    protected void initData() {
        super.initData();
        mFlag = getArguments().getInt(DormitoryMainActivity.ITEM_FLAG, 0);
    }

    @Override
    public void requestData() {
        super.requestData();
        mIndex = 1;
        presenter.getRankingList(mUserId, mFlag, mIndex, ACTION_REFRESH);
    }

    DormitoryItemListAdapter.Listener listener = new DormitoryItemListAdapter.Listener() {
        @Override
        public void itemActionClick(int action, int position, DormitoryRankingBean dormitoryRankingBean) {
            switch (action) {
                case Constant.DORMITORY_ACTION_DETAIL:
                    //TODO 进入类型，新增/修改/查看
                    int ActionType = 0;
                    if (mFlag == 0) {
                        ActionType = Constant.ACTION_TYPE_MODIFY;
                    } else if (mFlag == 1) {
                        ActionType = Constant.ACTION_TYPE_JUST_CHECK;
                    }
                    //TODO 查看一条记录，进入DormitoryListActivity
                    int recordId = dormitoryRankingBean.getRecordId();
                    int itemId = dormitoryRankingBean.getItemId();
                    String itemName = dormitoryRankingBean.getItemName();
                    String date = getDate(dormitoryRankingBean.getScoreDate());
                    boolean isAll = dormitoryRankingBean.getIsAll();
                    int dimensionType = dormitoryRankingBean.getDimensionType();
                    ArrayList<DormitoryOrClassSingleInfoBean> dormitoryOrClassInfoBeanArrayList = (ArrayList<DormitoryOrClassSingleInfoBean>) dormitoryRankingBean.getDormitoryOrClassInfoBean();

                    DormitoryListArgumentBean dormitoryListArgumentBean = new DormitoryListArgumentBean();
                    dormitoryListArgumentBean.setRecordId(recordId);
                    dormitoryListArgumentBean.setItemId(itemId);
                    dormitoryListArgumentBean.setItemName(itemName);
                    dormitoryListArgumentBean.setDate(date);
                    dormitoryListArgumentBean.setActionType(ActionType);
                    dormitoryListArgumentBean.setAll(isAll);
                    dormitoryListArgumentBean.setDimensionType(dimensionType);
                    dormitoryListArgumentBean.setDormitoryIdList(dormitoryOrClassInfoBeanArrayList);
                    DormitoryListActivity.start(getActivity(), dormitoryListArgumentBean);
                    break;
                case Constant.DORMITORY_ACTION_SUBMIT:
                    mPosition = position;
                    presenter.deleteOrSubmitRankingListItem(dormitoryRankingBean.getRecordId(), Constant.DORMITORY_ACTION_SUBMIT, mUserId);
                    break;
                case Constant.DORMITORY_ACTION_DELETE:
                    mPosition = position;
                    popupConfirmDialog(dormitoryRankingBean.getRecordId());
                    break;
                default:
                    break;
            }
        }
    };

    private String getDate(String date) {
        String str = "";
        String[] split = date.split("T");
        if (split.length >= 1) {
            str = split[0];
        }
        return str;
    }

    private IsDeleteSelectItemDialog mDialog;

    //显示弹框确认是否删除班级
    private void popupConfirmDialog(int recordId) {

        if (mDialog != null)
            mDialog = null; //置空然后重新赋值
        String text = "确认删除该条评分录入信息？";
        mDialog = new IsDeleteSelectItemDialog(mActivity, text, new IsDeleteSelectItemDialog.Listener() {
            @Override
            public void onDialogDismissListener() {
            }

            @Override
            public void onSureListerner() {
                //TODO 删除数据
                presenter.deleteOrSubmitRankingListItem(recordId, Constant.DORMITORY_ACTION_DELETE, mUserId);
            }
        });  //使用自定义的样式
        mDialog.show();
    }


    @Override
    public void onRefresh() {
        mRecyclerView.stopLoadMore(true);
        mRecyclerView.stopRefresh(true);
        mIndex = 1;
        presenter.getRankingList(mUserId, mFlag, mIndex, ACTION_REFRESH);
    }

    @Override
    public void onLoadMore() {
        mRecyclerView.stopLoadMore(true);
        mRecyclerView.stopRefresh(true);
        presenter.getRankingList(mUserId, mFlag, mIndex, ACTION_LOAD_MORE);
    }

    @Override
    public void showToast(String msg, int actionType) {
        Toast.Short(getActivity(), msg);
        if (actionType == ACTION_LOAD_MORE) {
            //TODO 无更多结果
            mRecyclerView.stopLoadMore(true);
            mRecyclerView.stopRefresh(true);
            mRecyclerView.setFooterViewText("无更多结果");
        }

    }

    @Override
    public void showEmpty(int action, boolean success) {
        if (!success) {  // 请求失败
            if (ACTION_REFRESH == action)
                mRecyclerView.stopRefresh(false);   // 刷新
            else
                mRecyclerView.stopLoadMore(false);  // 加载更多
        } else {
//            mRecyclerView.setVisibility(View.GONE);
            dormitoryItemListAdapter.refreshData(null);
            emptyTextView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void getListSuccess(List<DormitoryRankingBean> dormitoryRankingBeanList, int refreshOrLoadMore) {
        mIndex++;
        if (refreshOrLoadMore == ACTION_LOAD_MORE) {
            dormitoryItemListAdapter.setMoreData(dormitoryRankingBeanList);
            mRecyclerView.stopLoadMore(true);
        } else {  //刷新
            dormitoryItemListAdapter.refreshData(dormitoryRankingBeanList);
            mRecyclerView.stopRefresh(true);
        }
        mRecyclerView.setVisibility(View.VISIBLE);
        emptyTextView.setVisibility(View.GONE);

    }

    @Override
    public void deleteOrSubmitSuccess(int actionType) {
        dormitoryItemListAdapter.removeItemData(mPosition);
        //TODO 当数据为空的时候，显示暂无数据
        if (dormitoryItemListAdapter.getItemCount() == 0) {
            emptyTextView.setVisibility(View.VISIBLE);
        }
        if (actionType == Constant.DORMITORY_ACTION_SUBMIT) {
            //送审成功的,一定要进行回调刷新已审核列表并且显示特别的弹框(由于已审核未审核都是该Fragment,所以只能用回调，送审成功只需要更新已审核界面，未审核界面会有删除动画)
            CustomToast.ToastWithImageShort(getActivity(), R.drawable.ic_submit_check_large, getString(R.string.string_submit_success));
            //rx_dormitory_ranking_list_refresh
            RxDormitoryRankingListUpdateBean dormitoryRankingListUpdateBean = new RxDormitoryRankingListUpdateBean();
            dormitoryRankingListUpdateBean.setNeedToUpdatePage(RxDormitoryRankingListUpdateBean.REFRESH_HAVE_BEEN_SUBMIT_LIST);
            RxBus.getDefault().post(dormitoryRankingListUpdateBean);
        } else {
            Toast.Short(getActivity(), "删除成功");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.unsubscribe();
    }
}
