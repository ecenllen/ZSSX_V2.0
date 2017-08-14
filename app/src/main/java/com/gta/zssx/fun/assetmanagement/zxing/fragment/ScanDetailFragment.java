package com.gta.zssx.fun.assetmanagement.zxing.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.utils.fragment.BaseFragment;
import com.gta.utils.fragment.FragmentBuilder;
import com.gta.zssx.R;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/8/2.
 * @since 1.0.0
 */
public class ScanDetailFragment extends BaseFragment {

    public static String sCanInfo = "canInfo";
    public TextView mTextView;
    public String mScanInfo;

    public static class ScanDetailBuilder extends FragmentBuilder<ScanDetailFragment> {

        public String mScanInfo;

        public ScanDetailBuilder(Context context, String scanInfo) {
            super(context);
            mScanInfo = scanInfo;
        }


        @Override
        public ScanDetailFragment create() {
            Bundle lBundle = new Bundle();
            lBundle.putString(sCanInfo, mScanInfo);
            ScanDetailFragment lScanDetailFragment = new ScanDetailFragment();
            lScanDetailFragment.setArguments(lBundle);
            return lScanDetailFragment;
        }

        @Override
        protected int getContainerResID() {
            return 0;
        }

        @Override
        public String getPageTag() {
            return PAGE_TAG;
        }
    }

    public static final String PAGE_TAG = ScanDetailFragment.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_scan, container, false);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dataAction();
    }

    void dataAction() {
        initArgs();
    }

    private void initArgs() {
        mScanInfo = getArguments().getString(sCanInfo);
    }

          /*///////////////////////////////////////////////////////////////////////////
          // UI处理
          ///////////////////////////////////////////////////////////////////////////*/

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uiAction();
    }

    //每次显示页面时执行
    void uiAction() {
        findViews();
        initUI();
    }

    private void findViews() {
    }

    private void initUI() {
        initViews();
        setOnInteractListener();
    }

    private void initViews() {
        if (mScanInfo != null) {
            mTextView.setText(mScanInfo);
        }
    }

    private void setOnInteractListener() {
    }

}
