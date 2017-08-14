package com.gta.zssx.pub.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gta.zssx.R;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.fun.adjustCourse.view.base.AdjustCourse;
import com.gta.zssx.fun.adjustCourse.view.page.v2.CourseApplyActivityV2;
import com.gta.zssx.fun.assetmanagement.view.page.AssetWebViewShowActivity;
import com.gta.zssx.patrolclass.popup.AnimationTextView;

/**
 * Created by liang.lu on 2016/11/14 11:04.
 */

public class AddPageFragmentTwo extends android.support.v4.app.Fragment implements View.OnClickListener {


    public AddPageFragmentTwo() {
        super();
    }

    private FinishedListener listener;

    public void setListener(FinishedListener listener) {
        this.listener = listener;
    }

    public static AddPageFragmentTwo getInstance() {
        return new AddPageFragmentTwo();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_page_fragment_two, container, false);
        AnimationTextView lAdjustCourse = (AnimationTextView) view.findViewById(R.id.tv_adjust_course);

        AnimationTextView lAsset_dispatch = (AnimationTextView) view.findViewById (R.id.tv_asset_dispatch);
        AnimationTextView lAsset_transform = (AnimationTextView) view.findViewById (R.id.tv_asset_transform);
        AnimationTextView lAsset_repair = (AnimationTextView) view.findViewById (R.id.tv_asset_repair);
        AnimationTextView lAsset_scrap = (AnimationTextView) view.findViewById (R.id.tv_asset_scrap);

        lAdjustCourse.setOnClickListener(this);

        lAsset_dispatch.setOnClickListener (this);
        lAsset_transform.setOnClickListener (this);
        lAsset_repair.setOnClickListener (this);
        lAsset_scrap.setOnClickListener(this);

        if(!ZSSXApplication.instance.getUser().isSetAssetAdministrator()) {  // 没有分配权限
            lAsset_repair.setVisibility(View.GONE);
        }

        return view;

    }

    @Override
    public void onClick(View view) {
        if(listener != null) listener.Finished();
        switch (view.getId()) {
            default:
            case R.id.tv_adjust_course:
                AdjustCourse.getInstance().goToActivity(CourseApplyActivityV2.class.getSimpleName());
//                listener.Finished ();
                break;
            case R.id.tv_asset_dispatch: // 资产分配
//                listener.getTicket(getString(R.string.url_distribute_asset));
//                listener.Finished ();
                AssetWebViewShowActivity.start(getActivity(), getString(R.string.url_distribute_asset), 0, AssetWebViewShowActivity.LOADING_ASSET_NORMAL);
                break;
            case R.id.tv_asset_transform: // 资产转移
//                listener.getTicket(getString(R.string.url_shift_asset));
//                listener.Finished ();
                AssetWebViewShowActivity.start(getActivity(), getString(R.string.url_shift_asset), 0, AssetWebViewShowActivity.LOADING_ASSET_NORMAL);
                break;
            case R.id.tv_asset_repair:  // 资产维修
//                listener.getTicket(getString(R.string.url_repair_asset));
//                listener.Finished ();
                AssetWebViewShowActivity.start(getActivity(), getString(R.string.url_repair_asset), 0, AssetWebViewShowActivity.LOADING_ASSET_NORMAL);
                break;
            case R.id.tv_asset_scrap:  // 资产报废
//                listener.getTicket(getString(R.string.url_scrap_asset));
//                listener.Finished ();
                AssetWebViewShowActivity.start(getActivity(), getString(R.string.url_scrap_asset), 0, AssetWebViewShowActivity.LOADING_ASSET_NORMAL);
                
//                String lUrl3 = "http://" + ZSSXApplication.instance.getServerBean().getAssetUrl() + getString(R.string.text_distribute_asset) + "?ticket" + ZSSXApplication.instance.getUser().getTicket();
//                AssetWebViewShowActivity.start(getContext(), lUrl3, 0);
                break;
        }
    }

    public interface FinishedListener {
        void Finished();
    }
}
