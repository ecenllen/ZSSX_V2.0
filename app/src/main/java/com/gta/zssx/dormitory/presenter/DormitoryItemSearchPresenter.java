package com.gta.zssx.dormitory.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.dormitory.model.DormitoryModel;
import com.gta.zssx.dormitory.model.bean.ItemLevelBean;
import com.gta.zssx.dormitory.view.DormitoryItemSearchView;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ServerTimeDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.RegisteredRecordManager;
import com.gta.zssx.pub.exception.CustomException;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * [Description]
 * 指标项搜索presenter
 * [How to use]
 * 获取搜索关键字对应的指标项：getSearchItemData
 * [Tips]
 *
 * Created by lan.zheng on 2017/7/24 12:11.
 */

public class DormitoryItemSearchPresenter extends BasePresenter<DormitoryItemSearchView> {

    public Subscription mSubscribeList;
    /**
     * 获取搜索关键字对应的指标项
     */
    public void getSearchItemData(String keyWord) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribeList = DormitoryModel.getItemSearchList(keyWord)
                .subscribe(new Subscriber<List<ItemLevelBean>>() {

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
                            getView().showToast("无数据");
                        }

                    }

                    @Override
                    public void onNext(List<ItemLevelBean> itemLevelBeanList) {
                        if(isViewAttached()) {
                            if(itemLevelBeanList != null && itemLevelBeanList.size() >0){
                                getView().showResult(itemLevelBeanList);
                            }else {
                                getView().showToast("无数据");
                            }
                        }
                    }
                });
        mCompositeSubscription.add(mSubscribeList);
    }


 /*   public List<ItemLevelBean>  getListTestData(){
        List<ItemLevelBean> itemLevelBeanList = new ArrayList<>();
        for(int i = 1;i < 8;i++){
            ItemLevelBean itemLevelBean = new ItemLevelBean();
            itemLevelBean.setItemName("指标项"+i);
            itemLevelBean.setItemId(i);
            itemLevelBean.setItemDimension(0);
            itemLevelBeanList.add(itemLevelBean);
        }
        return itemLevelBeanList;
    }*/
}
