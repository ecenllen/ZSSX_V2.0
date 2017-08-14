package com.gta.zssx.pub.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gta.utils.resource.SysRes;
import com.gta.zssx.R;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.fun.adjustCourse.view.base.AdjustCourse;
import com.gta.zssx.fun.adjustCourse.view.page.v2.CourseApplyActivityV2;
import com.gta.zssx.fun.assetmanagement.view.page.AssetWebViewShowActivity;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.CourseDaily;
import com.gta.zssx.patrolclass.popup.AnimationTextView;
import com.gta.zssx.patrolclass.view.page.AddClassChooseActivity;
import com.gta.zssx.patrolclass.view.page.PlanProtalResultActivity;


/**
 * Created by liang.lu on 2016/11/14 11:04.
 */

public class AddPageFragmentOne extends android.support.v4.app.Fragment implements
        View.OnClickListener {

    private FinishedListener listener;

    public void setListener(FinishedListener listener) {
        this.listener = listener;
    }

    public AddPageFragmentOne() {
        super();
    }

    public static AddPageFragmentOne getInstance() {
        return new AddPageFragmentOne();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_page_fragment, container, false);
        AnimationTextView lLog_register = (AnimationTextView) view.findViewById(R.id.tv_log_register);
        AnimationTextView lRandom_patrol = (AnimationTextView) view.findViewById(R.id.tv_random_class);
        AnimationTextView lPlan_patrol = (AnimationTextView) view.findViewById(R.id.tv_plan_class);
        AnimationTextView lAdjustCourse = (AnimationTextView) view.findViewById(R.id.tv_adjust_course);

        
        AnimationTextView lAsset_dispatch = (AnimationTextView) view.findViewById(R.id.tv_asset_dispatch);
        AnimationTextView lAsset_transform = (AnimationTextView) view.findViewById(R.id.tv_asset_transform);
        AnimationTextView lAsset_repair = (AnimationTextView) view.findViewById(R.id.tv_asset_repair);
        lLog_register.setOnClickListener(this);
        lRandom_patrol.setOnClickListener(this);
        lPlan_patrol.setOnClickListener(this);

        lAdjustCourse.setOnClickListener(this);
        lAsset_dispatch.setOnClickListener(this);
        lAsset_transform.setOnClickListener(this);
        lAsset_repair.setOnClickListener(this);
        
        if(!ZSSXApplication.instance.getUser().isSetAssetAdministrator()) {  // 没有分配权限,1.7.3 用这个控制IsAddAllocation
            lAsset_dispatch.setVisibility(View.GONE);
        }
        
        return view;

    }

    @Override
    public void onClick(View view) {
        if(listener != null) listener.Finished();
        switch (view.getId()) {
            case R.id.tv_log_register:
//                listener.Finished();
                if (!SysRes.isConnected(getContext())) {
                    com.gta.utils.resource.Toast.Short(getContext(), getString(R.string.net_work_error));
                } else {
                    CourseDaily.getInstance().displayWithCheck();
                }
                break;
            case R.id.tv_random_class:
//                listener.Finished();
                if (!SysRes.isConnected(getContext())) {
                    com.gta.utils.resource.Toast.Short(getContext(), getString(R.string.net_work_error));
                } else {
                    //跳转
                    AddClassChooseActivity.start(getContext(), "random", "1", "");
                }
                break;
            case R.id.tv_plan_class:
//                listener.Finished();
                if (!SysRes.isConnected(getContext())) {
                    com.gta.utils.resource.Toast.Short(getContext(), getString(R.string.net_work_error));
                } else {
                    //跳转
                    PlanProtalResultActivity.start(getContext(), "1", "");
                }
                break;
            case R.id.tv_adjust_course:
                if (!SysRes.isConnected(getContext())) {
                    com.gta.utils.resource.Toast.Short(getContext(), getString(R.string.net_work_error));
                    return;
                }
                AdjustCourse.getInstance().goToActivity(CourseApplyActivityV2.class.getSimpleName());
//                listener.Finished();
                break;
            case R.id.tv_asset_dispatch: // 资产分配
                if (!SysRes.isConnected(getContext())) {
                    com.gta.utils.resource.Toast.Short(getContext(), getString(R.string.net_work_error));
                    return;
                }
                AssetWebViewShowActivity.start(getActivity(), getString(R.string.url_distribute_asset), 0, AssetWebViewShowActivity.LOADING_ASSET_NORMAL);
                break;
            case R.id.tv_asset_transform: // 资产转移
                if (!SysRes.isConnected(getContext())) {
                    com.gta.utils.resource.Toast.Short(getContext(), getString(R.string.net_work_error));
                    return;
                }
                AssetWebViewShowActivity.start(getActivity(), getString(R.string.url_shift_asset), 0, AssetWebViewShowActivity.LOADING_ASSET_NORMAL);
                break;
            case R.id.tv_asset_repair:  // 资产维修
                AssetWebViewShowActivity.start(getActivity(), getString(R.string.url_repair_asset), 0, AssetWebViewShowActivity.LOADING_ASSET_NORMAL);
                break;
            default:
                break;
        }
    }

    public interface FinishedListener {
        void Finished();
    }

}
