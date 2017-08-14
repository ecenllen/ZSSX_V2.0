package com.gta.zssx.dormitory.presenter;


import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.dormitory.model.DormitoryModel;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassLevelList;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassListBean;
import com.gta.zssx.dormitory.model.bean.LevelList;
import com.gta.zssx.dormitory.view.DormitoryListInnerView;
import com.gta.zssx.pub.exception.CustomException;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import rx.Subscriber;

/**
 * [Description]
 * <p> 宿舍列表/班级列表，内部Presenter，外部是楼层或年级，内部是宿舍/班级
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Weiye.Chen on 2017/7/20.
 * @since 2.0.0
 */
public class DormitoryListInnerPresenter extends BasePresenter<DormitoryListInnerView> {

    /**
     * 获取宿舍列表/班级列表
     * @param inputData 日期
     * @param itemId 指标项ID
     * @param ListString 宿舍楼ID / 专业部ID， 每次只获取一栋楼/一个专业部下数据
     */
    public void getDormitoryOrClassList(String inputData, int itemId, ArrayList<String> ListString) {
        if(getView() == null) return;
        getView().showLoadingDialog();
        mCompositeSubscription.add(DormitoryModel.GetBuildingOrDeptInfo(inputData, itemId, ListString).subscribe(new Subscriber<DormitoryOrClassListBean>() {
            @Override
            public void onCompleted() {
                if(isViewAttached()) {
                    getView().hideDialog();
                }

            }

            @Override
            public void onError(Throwable throwable) {
                if(isViewAttached()) {
                    getView().hideDialog();
                    getView().onErrorHandle(throwable); //抛出异常,异常处理在Base的onErrorHandle处理
                    if(throwable instanceof CustomException || throwable instanceof SocketTimeoutException)
                        getView().showOnFailReloading(throwable.getMessage());
                    else
                        getView().showOnFailReloading("");
                }

            }

            @Override
            public void onNext(DormitoryOrClassListBean dormitoryOrClassListBean) {
                if(isViewAttached()) {
                    getView().showResult(dormitoryOrClassListBean);
                }
            }
        }));
    }

}
