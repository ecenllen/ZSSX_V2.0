package com.gta.zssx.dormitory.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.utils.resource.L;
import com.gta.zssx.dormitory.model.DormitoryModel;
import com.gta.zssx.dormitory.model.bean.ItemInfoBean;
import com.gta.zssx.dormitory.model.bean.ItemLevelBean;
import com.gta.zssx.dormitory.view.DormitorySelectItemView;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ServerTimeDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.RegisteredRecordManager;
import com.gta.zssx.pub.exception.CustomException;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * [Description]
 * 指标项选择presenter
 * [How to use]
 * 获取指标项数据方法：getItemData
 * [Tips]
 *
 * Created by lan.zheng on 2017/7/24 12:14.
 */

public class DormitorySelectItemPresenter extends BasePresenter<DormitorySelectItemView> {
    public Subscription mItemSubscribeList;


    public void getItemData(int itemId,boolean isNeedToRequestNextLevel) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mItemSubscribeList = DormitoryModel.getItemList(itemId)
                .subscribe(new Subscriber<ItemInfoBean>() {

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
                        }
                        if(!isNeedToRequestNextLevel){
                            getView().showEmptyView();
                        }

                    }

                    @Override
                    public void onNext(ItemInfoBean itemInfoBean) {
                        if(isViewAttached()) {
                            if(itemInfoBean != null && itemInfoBean.getLevelList().size() >0){
                                getView().showResult(itemInfoBean,isNeedToRequestNextLevel);
                            }else {
                                //如果是请求下一层级的，无数据的时候弹出提示，如果是首页，直接显示空页面
                                if(!isNeedToRequestNextLevel){
                                    getView().showEmptyView();
                                }else {
                                    getView().showToast();
                                }

                            }

                        }
                    }
                });
        mCompositeSubscription.add(mItemSubscribeList);
    }

    public List<ItemInfoBean> getItemList(){
        List<ItemInfoBean> itemInfoBeanList = new ArrayList<>();
        ItemInfoBean itemInfoBean1 = new ItemInfoBean();
        itemInfoBean1.setId(1);
        itemInfoBean1.setName("层级0");
        ItemInfoBean itemInfoBean2 = new ItemInfoBean();
        itemInfoBean2.setId(1);
        itemInfoBean2.setName("层级00");
        ItemInfoBean itemInfoBean3 = new ItemInfoBean();
        itemInfoBean3.setId(1);
        itemInfoBean3.setName("层级000");
        ItemInfoBean itemInfoBean4 = new ItemInfoBean();
        itemInfoBean4.setId(1);
        itemInfoBean4.setName("层级0000");
        List<ItemLevelBean> itemLevelBeanList = new ArrayList<>();
        for (int i = 1;i < 4;i++){
            ItemLevelBean itemLevelBean = new ItemLevelBean();
            itemLevelBean.setItemId(i);
            itemLevelBean.setItemName("测试指标项"+i);
            itemLevelBean.setItemDimension(1);
            itemLevelBean.setType(0);
            itemLevelBeanList.add(itemLevelBean);
        }
        itemInfoBean1.setLevelList(itemLevelBeanList);
        itemInfoBean2.setLevelList(itemLevelBeanList);
        itemInfoBean3.setLevelList(itemLevelBeanList);
        itemInfoBean4.setLevelList(itemLevelBeanList);

        itemInfoBeanList.add(itemInfoBean1);
        itemInfoBeanList.add(itemInfoBean2);
        itemInfoBeanList.add(itemInfoBean3);
        itemInfoBeanList.add(itemInfoBean4);
        return itemInfoBeanList;
    }


    /**
     * 获取服务器时间
     */
/*
    public void getServerTime() {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribeList = RegisteredRecordManager.getServerTime()
                .subscribe(new Subscriber<ServerTimeDto>() {

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
                        }

                    }

                    @Override
                    public void onNext(ServerTimeDto serverTimeDto) {
                        if(isViewAttached()) {

                        }
                    }
                });
        mCompositeSubscription.add(mSubscribeList);
    }
*/


}
