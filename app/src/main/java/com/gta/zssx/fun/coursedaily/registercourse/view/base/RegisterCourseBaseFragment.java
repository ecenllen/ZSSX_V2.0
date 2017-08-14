package com.gta.zssx.fun.coursedaily.registercourse.view.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.gta.utils.mvp.BaseMvpFragment;
import com.gta.utils.mvp.BasePresenter;
import com.gta.utils.mvp.BaseView;
import com.gta.utils.resource.Toast;
import com.gta.utils.resource.TranProgressDialog;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.view.pageV2.CourseSignWithAttendanceStatusActivity;
import com.gta.zssx.fun.coursedaily.registercourse.view.pageV2.StudentListActivity;
import com.gta.zssx.pub.exception.CustomException;
import com.gta.zssx.pub.manager.ToolBarManager;

import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/15.
 * @since 1.0.0
 */
public abstract class RegisterCourseBaseFragment<V extends BaseView, P extends BasePresenter<V>> extends BaseMvpFragment<V, P> {

    public static final String PAGE_TAG = RegisterCourseBaseFragment.class.getSimpleName();

    protected StudentListActivity mStudentListActivity;
    protected CourseSignWithAttendanceStatusActivity mActivity;  //Fragment使用getActivity()直接拿到,必要时使用
    protected ToolBarManager mToolBarManager;
    public TranProgressDialog mProgressDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof StudentListActivity) {
            mStudentListActivity = (StudentListActivity) activity;
        } else if (activity instanceof CourseSignWithAttendanceStatusActivity){
            mActivity = (CourseSignWithAttendanceStatusActivity) activity;
        } else{
            throw new RuntimeException("null activity");
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mActivity.setMyBaseFragmentWeakReference(this);

    }

    public boolean onBackPress() {
        return false;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mToolBarManager.renew();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private ProgressDialog dialog;

    @Override
    public void showLoadingDialog() {
        showDialog("", getResourceString(R.string.loading), true);
    }

    @Override
    public void showLoadingDialog(boolean isCancelable) {
        showDialog("", getResourceString(R.string.loading), isCancelable);
    }

    @Override
    public void showDialog(String message) {
        showDialog("", message, true);
    }

    @Override
    public void showDialog(String message, boolean isCancelable) {
        showDialog("", message, isCancelable);
    }

    @Override
    public void showDialog(String title, String message) {
        showDialog(title, message, false);
    }

    @Override
    public void showDialog(String title, String message, boolean isCancelable) {
        hideDialog();
        dialog = ProgressDialog.show(getActivity(), title, message, true);
        dialog.setCancelable(isCancelable);

//        mProgressDialog = TranProgressDialog.show(mActivity);
//        mProgressDialog.setCancelable(isCancelable);
    }

    @Override
    public void hideDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
//        if (mProgressDialog != null) {
//            mProgressDialog.dismiss();
//        }
    }

    @Override
    public void showInfo(String message) {
        Toast.Short(mActivity, message);
    }

    @Override
    public void showWarning(String warningMessage) {
        Toast.Short(mActivity, warningMessage);
//        Toasteroid.show(getActivity(), warningMessage, Toasteroid.STYLES.WARNING);
    }

    @Override
    public void showError(String error) {
        Toast.Short(mActivity, error);
//        Toasteroid.show(getActivity(), error, Toasteroid.STYLES.ERROR);
    }

    @Override
    public String getResourceString(int stringId) {
        return getString(stringId);
    }

    @Override
    public String getResourceString(int stringId, Object... formatArgs) {
        return getString(stringId, formatArgs);
    }

    @Override
    public void onErrorHandle(Throwable e) {
        if (e instanceof CustomException) {
            CustomException lErrorCodeException = (CustomException) e;
            showWarning(lErrorCodeException.getMessage());
        }

        if (e instanceof SocketTimeoutException) {
            showWarning("请求超时，请稍候重试");
        }

        if (e instanceof HttpException) {
            showError("服务器错误，请稍候重试");
        }
    }

    @Override
    public void showWarningLong(String error) {

    }

    @Override
    public void showInfoLong(String message) {

    }

    @Override
    public void showErrorLong(String error) {

    }

    @Override
    public void showOnFailReloading(String error) {

    }
}
