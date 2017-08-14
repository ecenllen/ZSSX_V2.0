package com.gta.zssx.mobileOA.view.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.OfficeNoticeInfo;
import com.gta.zssx.mobileOA.presenter.OfficialRemindPresenter;
import com.gta.zssx.mobileOA.view.OfficailRemindView;
import com.gta.zssx.mobileOA.view.adapter.OfficeNoticeAdapter;
import com.gta.zssx.mobileOA.view.base.BaseOAFragment;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.hfrecyclerview.HFRecyclerView;
import com.gta.zssx.pub.util.RxBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by lan.zheng on 2016/11/3.  学校公告和学校公文共用
 */
public class OfficialRemindFragment extends BaseOAFragment<OfficailRemindView,OfficialRemindPresenter> implements  OfficailRemindView,HFRecyclerView.HFRecyclerViewListener{

    private TextView emptyTextView;
    private HFRecyclerView hfRecyclerView;
    private OfficeNoticeAdapter officeNoticeAdapter;
    private final static int SHOW_TITLE_PAGE_NOTICE = 0; //学校公告
    private final static int SHOW_TITLE_PAGE_OFFICIAL = 1; //学校公文
    private int type;

    public static final int REFRESH = 0;
    public static final int MORE = 1;
    private int pageIndexOfficial = 1;  //第一页，公告
    private int pageIndexOfficialDocument = 1; //第一页，公文
    private SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

    @NonNull
    @Override
    public OfficialRemindPresenter createPresenter() {
        return new OfficialRemindPresenter();
    }

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_official_or_notice_main_page, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = mActivity.getShowTitlePage();
        initView();
        initData();
    }

    private void initView(){
        hfRecyclerView = (HFRecyclerView) view.findViewById(R.id.recycler);
        emptyTextView = (TextView)view.findViewById(R.id.tv_emptyHint);
        setOnInteractListener();
    }


    private void setOnInteractListener () {
        hfRecyclerView.setCanLoadMore (true);
        hfRecyclerView.setCanRefresh (true);
        hfRecyclerView.setFooterViewText ("");
        hfRecyclerView.setRecyclerViewListener (this);
    }

    private void initData(){
        //初始化适配器
        OfficeNoticeInfo officeNoticeInfo1 = new OfficeNoticeInfo();
        Date d = new Date();
        officeNoticeInfo1.setServerTime(s.format(d));
        List<OfficeNoticeInfo.OfficeNoticeRemindEntity> list = new ArrayList<>();
        officeNoticeInfo1.setOfficeNoticeReminds(list);
        //界面默认显示
        emptyTextView.setVisibility(View.VISIBLE);
        officeNoticeAdapter = new OfficeNoticeAdapter(getActivity(),listener,officeNoticeInfo1);
        hfRecyclerView.setAdapter(officeNoticeAdapter);

        //服务器获取数据
//        if(type == SHOW_TITLE_PAGE_NOTICE){
//            presenter.getOfficialData(REFRESH,pageIndexOfficial);
            //测试数据
//            emptyTextView.setVisibility(View.GONE);
//            OfficeNoticeInfo officeNoticeInfo = presenter.testData();
//            officeNoticeAdapter = new OfficeNoticeAdapter(getActivity(), listener, officeNoticeInfo);
//            hfRecyclerView.setAdapter(officeNoticeAdapter);
//        }else {
            presenter.getOfficialDocumentData(REFRESH,pageIndexOfficialDocument);
            //测试数据
//            emptyTextView.setVisibility(View.GONE);
//            OfficeNoticeInfo officeNoticeInfo = presenter.testDataOfficial();
//            officeNoticeAdapter = new OfficeNoticeAdapter(getActivity(), listener, officeNoticeInfo);
//            hfRecyclerView.setAdapter(officeNoticeAdapter);
//        }

        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(Integer.class)
                .subscribe(position-> officeNoticeAdapter.changeNoticeStatus(position)));
    }

    OfficeNoticeAdapter.Listener listener = new OfficeNoticeAdapter.Listener() {
        @Override
        public void onClickListener(OfficeNoticeInfo.OfficeNoticeRemindEntity remind,int position) {
//            OfficialNoticeDetailActivity.start(getActivity(),type,remind.getId());
            Intent intent = new Intent(getActivity(), BacklogDetailActivity.class);
            intent.putExtra("id", remind.getId());
            intent.putExtra("runId",remind.getRunId());
            intent.putExtra("position",position);
            intent.putExtra("status",remind.getStatus());
//            if(type == SHOW_TITLE_PAGE_NOTICE){
//                intent.putExtra("type", Constant.DETAIL_TYPE_NOTICE);
//            }else {
                intent.putExtra("type", Constant.DETAIL_TYPE_DOC);
//            }
            startActivity(intent);
        }
    };

    @Override
    public void onRefresh() {
        type = mActivity.getShowTitlePage();
//        if(type == SHOW_TITLE_PAGE_NOTICE){
//            pageIndexOfficial = 1;
//            presenter.getOfficialData(REFRESH,pageIndexOfficial);
//
//        }else {
            pageIndexOfficialDocument = 1;
            presenter.getOfficialDocumentData(REFRESH,pageIndexOfficialDocument);
//        }
    }

    @Override
    public void onLoadMore() {
        type = mActivity.getShowTitlePage();
//        if(type == SHOW_TITLE_PAGE_NOTICE){
//            presenter.getOfficialData(MORE,pageIndexOfficial);
//        }else {
            presenter.getOfficialDocumentData(MORE,pageIndexOfficialDocument);
//        }
    }

    @Override
    public void showEmptyView() {
        hfRecyclerView.stopLoadMore(true);
        hfRecyclerView.stopRefresh(true);
        officeNoticeAdapter.clearData();
        emptyTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRemindList(OfficeNoticeInfo officeNoticeInfo,int action){
        emptyTextView.setVisibility(View.GONE);
//        type = mActivity.getShowTitlePage();
//        if(type == SHOW_TITLE_PAGE_NOTICE){  //公告
//            if(action == REFRESH){
//                hfRecyclerView.stopRefresh(true);
//                //重新设置数据集合
//                officeNoticeAdapter = new OfficeNoticeAdapter(getActivity(),listener,officeNoticeInfo);
//                hfRecyclerView.setAdapter(officeNoticeAdapter);
//            }else {
//                hfRecyclerView.stopLoadMore(true);
//                //加载更多就继续加数据
//                officeNoticeAdapter.setData(officeNoticeInfo);
//            }
//            pageIndexOfficial++;
//            officeNoticeAdapter.notifyDataSetChanged();
//        }else {     //公文
            if(action == REFRESH){
                hfRecyclerView.stopRefresh(true);
                //重新设置数据集合
                officeNoticeAdapter = new OfficeNoticeAdapter(getActivity(),listener,officeNoticeInfo);
                hfRecyclerView.setAdapter(officeNoticeAdapter);
            }else {
                hfRecyclerView.stopLoadMore(true);
                //加载更多就继续加数据
                officeNoticeAdapter.setData(officeNoticeInfo);
            }
            pageIndexOfficialDocument++;
            officeNoticeAdapter.notifyDataSetChanged();
//        }

    }

    @Override
    public void onLoadMoreError() {
        hfRecyclerView.stopLoadMore(false);
    }

    @Override
    public void onRefreshError() {
        hfRecyclerView.stopRefresh(false);
    }

    @Override
    public void onLoadMoreEmpty() {
        hfRecyclerView.stopLoadMore(false);
        hfRecyclerView.setFooterViewText("无更多信息");
    }

    @Override
    public void setReadSuccessful(int id, int position) {
        Intent intent = new Intent(getActivity(), BacklogDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("position", position);
//        if (type == SHOW_TITLE_PAGE_NOTICE) {
//            intent.putExtra("type", Constant.DETAIL_TYPE_NOTICE);
//        } else {
            intent.putExtra("type", Constant.DETAIL_TYPE_DOC);
//        }
        startActivity(intent);
    }
}
