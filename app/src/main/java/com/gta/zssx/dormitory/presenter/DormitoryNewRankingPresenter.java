package com.gta.zssx.dormitory.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.dormitory.model.DormitoryModel;
import com.gta.zssx.dormitory.model.bean.CheckDormitoryIfCanEnterBean;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassSingleInfoBean;
import com.gta.zssx.dormitory.view.DormitoryNewRankingView;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ServerTimeDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.RegisteredRecordManager;
import com.gta.zssx.pub.exception.CustomException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * [Description]
 * 新增录入页presenter
 * [How to use]
 * 获取服务器时间方法：getServerTime，获取宿舍楼或专业部列表：getDormitoryOrClassList，检查是否能进行下一步方法：checkIfCanInput
 * [Tips]
 *
 * Created by lan.zheng on 2017/7/24 12:13.
 */

public class DormitoryNewRankingPresenter extends BasePresenter<DormitoryNewRankingView> {
    private Subscription mSubscribeList;
    private Subscription mDormitoryOrClassSubscribeList;
    private Subscription mCheckIfCanInputSubscribeList;

    /**
     * 获取服务器时间
     */
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
                            getView().getServerTimeSuccess(serverTimeDto.getDate());
                        }
                    }
                });
        mCompositeSubscription.add(mSubscribeList);
    }

    /**
     * 获取宿舍楼或专业部列表
     * @param DormitoryOrClass 宿舍楼还是专业部
     */
    public void getDormitoryOrClassList(int DormitoryOrClass){
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mDormitoryOrClassSubscribeList = DormitoryModel.dormitoryOrClassList(DormitoryOrClass)
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
        mCompositeSubscription.add(mDormitoryOrClassSubscribeList);
    }

    /**
     * 检查是否能录入
     * @param ItemId  指标项名称
     * @param Listing  字符串
     * @param InputDate 日期
     */
    public void checkIfCanInput(int ItemId,String Listing,String InputDate){
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mCheckIfCanInputSubscribeList = DormitoryModel.checkIfCanInput(ItemId, Listing,InputDate)
                .subscribe(new Subscriber<CheckDormitoryIfCanEnterBean>() {

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
                            getView().resultIsCanInput(1,null);  //1.正常，0.不正常
                        }

                    }

                    @Override
                    public void onNext(CheckDormitoryIfCanEnterBean checkDormitoryIfCanEnterBean) {
                        if(isViewAttached()) {
                            //TODO 证明是可以进行正常登记
                            getView().resultIsCanInput(checkDormitoryIfCanEnterBean.getCode(),checkDormitoryIfCanEnterBean.getData());
                        }
                    }
                });
        mCompositeSubscription.add(mCheckIfCanInputSubscribeList);
    }

    /**
     * 获得要显示的宿舍楼或专业部的名字集合
     * @param mRecordList 勾选集合
     * @return String
     */
    public String formatDormitoryOrClassListName(List<DormitoryOrClassSingleInfoBean> mRecordList){
        String str = "";
        if(mRecordList.size() > 0){
            for(int i = 0; i < mRecordList.size();i++){
                if(i == 0){
                    str = mRecordList.get(i).getDormitoryOrClassName();
                }else {
                    str += "，" + mRecordList.get(i).getDormitoryOrClassName();
                }
            }
        }
        return str;
    }

    /**
     * 获得要显示的宿舍楼或专业部的Id集合
     * @param mRecordList 勾选集合
     * @return String
     */
    public String formatDormitoryOrClassListId(List<DormitoryOrClassSingleInfoBean> mRecordList){
        String str = "";
        if(mRecordList.size() > 0){
            for(int i = 0; i < mRecordList.size();i++){
                if(i == 0){
                    str = ""+mRecordList.get(i).getDormitoryOrClassId();
                }else {
                    str += "," + mRecordList.get(i).getDormitoryOrClassId();
                }
            }
        }
        return str;
    }


    /**
     * 获得检查后的那些宿舍楼或专业部已经被录入过
     * @param idList  被录入过的集合
     * @param mRecordList 勾选集合
     * @return String
     */
    public String getWitchCanNotInputString(List<Integer> idList,List<DormitoryOrClassSingleInfoBean> mRecordList){
        String str = "";
        for(int i = 0;i<idList.size();i++){
            for(int j = 0;j<mRecordList.size();j++){
                if(mRecordList.get(j).getDormitoryOrClassId() == idList.get(i)){
                    if(i == 0){
                        str = mRecordList.get(j).getDormitoryOrClassName();
                    }else {
                        str += "，" + mRecordList.get(j).getDormitoryOrClassName();
                    }
                }
            }
        }
        return str;
    }

    public List<DormitoryOrClassSingleInfoBean> getListDataTest(){
        List<DormitoryOrClassSingleInfoBean> list = new ArrayList<>();
        for(int i = 1;i<9;i++){
            DormitoryOrClassSingleInfoBean domitoryOrClassInfoBean = new DormitoryOrClassSingleInfoBean();
            domitoryOrClassInfoBean.setDormitoryOrClassName("test宿舍#"+i);
            domitoryOrClassInfoBean.setDormitoryOrClassId(i);
            list.add(domitoryOrClassInfoBean);
        }
        return list;
    }
}
