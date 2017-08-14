package com.gta.zssx.dormitory.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.dormitory.model.DormitoryModel;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassSingleInfoBean;
import com.gta.zssx.dormitory.view.DormitorySelectLevelView;
import com.gta.zssx.fun.adjustCourse.view.base.BaseViewPagerFragment;
import com.gta.zssx.pub.exception.CustomException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import rx.Subscriber;
import rx.Subscription;


/**
 * [Description]
 * 获取专业部或宿舍楼presenter
 * [How to use]
 * 获取专业部或宿舍楼方法:getDormitoryOrClassList
 * [Tips]
 *
 * Created by lan.zheng on 2017/7/24 13:07.
 */

public class DormitorySelectLevelPresenter extends BasePresenter<DormitorySelectLevelView>{
    public Subscription mDormitoryOrClassList;

    /**
     * 获取专业部或宿舍楼方法
     * @param DormitoryOrClass 专业部或宿舍楼flag
     */
    public void getDormitoryOrClassList(int DormitoryOrClass){
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mDormitoryOrClassList = DormitoryModel.dormitoryOrClassList(DormitoryOrClass)
                .subscribe(new Subscriber<List<DormitoryOrClassSingleInfoBean>>() {

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
                            if (throwable instanceof CustomException) {   //抛出自定义的异常
                                CustomException lE = (CustomException) throwable;
                                if (lE.getCode() == CustomException.NETWORK_UNAVAILABLE) {
                                    getView().showError(throwable.getMessage());
                                }else {
                                    getView().onErrorHandle(throwable);  //抛出默认异常
                                }
                            }else {
                                getView().onErrorHandle(throwable);  //抛出默认异常
                            }
                            getView().getDormitoryOrClassFailed();
                        }

                    }

                    @Override
                    public void onNext(List<DormitoryOrClassSingleInfoBean> domitoryOrClassInfoBeanList) {
                        if(isViewAttached()) {
                            if(domitoryOrClassInfoBeanList != null && domitoryOrClassInfoBeanList.size() > 0){
                                getView().getDormitoryOrClassSuccess(domitoryOrClassInfoBeanList);
                            }else {
                                getView().getDormitoryOrClassFailed();
                            }
                        }
                    }
                });
        mCompositeSubscription.add(mDormitoryOrClassList);
    }

    /**
     * 初始化一份选中记录集
     * @param mDormitoryOrClassInfoBeanList 所有数据
     * @param checkList  勾选集合
     */
    public List<DormitoryOrClassSingleInfoBean> formatRecordList(List<DormitoryOrClassSingleInfoBean> mDormitoryOrClassInfoBeanList,Set<Integer> checkList){
        List<DormitoryOrClassSingleInfoBean> recordList = new ArrayList<>();
        List<Integer> mCheckList = new ArrayList<>(checkList);
        for(int i = 0;i < mDormitoryOrClassInfoBeanList.size();i++){
            for(int j = 0;j<checkList.size();j++ ){
                if(mCheckList.get(j) == i) {
                    recordList.add(mDormitoryOrClassInfoBeanList.get(i));
                }
            }
        }
        return recordList;
    }
}
