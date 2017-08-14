package com.gta.zssx.dormitory.presenter;


import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.DormitoryModel;
import com.gta.zssx.dormitory.model.bean.ClassInfoBean;
import com.gta.zssx.dormitory.model.bean.DormitoryEnterNameSingleHttpInfoBean;
import com.gta.zssx.dormitory.model.bean.DormitoryEnterNameSingleItemInfoBean;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassSingleInfoBean;
import com.gta.zssx.dormitory.model.bean.EnterNameSingleItemBean;
import com.gta.zssx.dormitory.model.bean.RecordIdBean;
import com.gta.zssx.dormitory.view.DormitoryEnterNameSingleView;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.exception.CustomException;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * [Description]
 * <p> 宿舍管理-评分录入-宿舍维度或班级维度-获取单项评分数据或保存单项评分数据
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Weiye.Chen on 2017/07/24.
 * @since 2.0.0
 */
public class DormitoryEnterNameSinglePresenter extends BasePresenter<DormitoryEnterNameSingleView> {

    /**
     *  dimensionType  班级维度/宿舍维度
     *  InputDate 日期
     *  UserId 用户ID
     *  ItemId 指标项ID
     *  recordId 记录ID
     *  detailContent 提交内容
     *  BuildingOrDeptIds 宿舍楼ID 或 专业部ID （id,id,id 这样拼接）
     */
    private int mItemId;
    private String mInputData;
    private String mDetailId;
    private int mActionType;
    private DormitoryEnterNameSingleHttpInfoBean dormitoryEnterNameSingleHttpInfoBean;

    /**
     * 宿舍维度-录入姓名-获取单项
     * @param DateTime      日期
     * @param ItemId        指标项ID
     * @param DetailId      宿舍ID/班级ID
     * @param ActionType    1 新增，2 修改，3 查看
     * @param DimensionType 宿舍维度/班级维度
     */
    public void getDormitoryOptionDetail(String DateTime, int ItemId, String DetailId, int ActionType, int DimensionType) {
        this.mItemId = ItemId;
        this.mInputData = DateTime;
        this.mDetailId = DetailId;
        this.mActionType = ActionType;
        if (DimensionType == Constant.DimensionType_Dormitory) {
            getEnterNameSingleItemDormitoryInfo(); // 宿舍维度
        } else {
            getEnterNameSingleItemClassInfo();    // 班级维度
        }
    }

    /**
     * 宿舍维度、班级维度，获取单项设置评分，网络请求之后统一回调
     */
    private class GetDataSubscriber extends Subscriber<DormitoryEnterNameSingleItemInfoBean> {
        @Override
        public void onCompleted() {
            if (getView() != null) {
                getView().hideDialog();
            }
        }

        @Override
        public void onError(Throwable throwable) {
            if (getView() != null) {
                getView().hideDialog();
                getView().onErrorHandle(throwable); //抛出异常,异常处理在Base的onErrorHandle处理
                if(throwable instanceof CustomException || throwable instanceof SocketTimeoutException)
                    getView().showOnFailReloading(throwable.getMessage());
                else
                    getView().showOnFailReloading("");
            }
        }

        @Override
        public void onNext(DormitoryEnterNameSingleItemInfoBean dormitoryEnterNameSingleItemInfoBean) {
            if (getView() != null) {
                getView().showResult(dormitoryEnterNameSingleItemInfoBean);
            }
        }
    }

    /**
     * 获取宿舍维度-录入姓名-单项评分数据
     */
    private void getEnterNameSingleItemDormitoryInfo() {
        if (getView() == null) return;
        getView().showLoadingDialog();
        mCompositeSubscription.add(DormitoryModel.getEnterNameSingleItemDormitoryInfo(mInputData, mItemId, mDetailId, mActionType).subscribe(new GetDataSubscriber()));
    }

    /**
     * 获取班级维度-录入姓名-单项评分数据
     */
    private void getEnterNameSingleItemClassInfo() {
        if (getView() == null) return;
        getView().showLoadingDialog();
        mCompositeSubscription.add(DormitoryModel.getEnterNameSingleItemClassInfo(mInputData, mItemId, mDetailId, mActionType).subscribe(new GetDataSubscriber()));
    }

    /**
     * 保存宿舍维度/班级维度 - 单项设置评分录入
     * @param dimensionType 宿舍维度/班级维度
     * @param dormitoryEnterNameSingleHttpInfoBean 保存参数
     */
    public void SaveDormitoryOrClassSingleData(int dimensionType, DormitoryEnterNameSingleHttpInfoBean dormitoryEnterNameSingleHttpInfoBean) {
        this.dormitoryEnterNameSingleHttpInfoBean = dormitoryEnterNameSingleHttpInfoBean;
        saveNameSingleDimensionType(dimensionType);
    }


    /**
     * 根据不同维度，调用不同方法
     * @param dimensionType 1宿舍维度，2班级维度
     */
    private void saveNameSingleDimensionType(int dimensionType) {
        if(dimensionType == Constant.DimensionType_Dormitory) {
            SaveDormScoreNameSingle();
        } else {
            SaveClassScoreNameSingle();
        }
    }

    /**
     * 宿舍维度、班级维度，保存单项设置评分，网络请求之后统一回调
     */
    private class SaveDataSubscriber extends Subscriber<RecordIdBean> {
        @Override
        public void onCompleted() {
            if (getView() != null) {
                getView().hideDialog();
            }
        }

        @Override
        public void onError(Throwable throwable) {
            if (getView() != null) {
                getView().hideDialog();
                if (throwable instanceof CustomException) {   //抛出自定义的异常
                    CustomException lE = (CustomException) throwable;
                    if (lE.getCode() == CustomException.NETWORK_UNAVAILABLE) {
                        getView().showError(throwable.getMessage());
                    } else {
                        getView().onErrorHandle(throwable);  //抛出默认异常
                    }
                } else {
                    getView().onErrorHandle(throwable);  //抛出默认异常
                }
                getView().saveSuccess(false, 0);
            }
        }

        @Override
        public void onNext(RecordIdBean recordId) {
            if (getView() != null) {
                getView().saveSuccess(true, recordId.getRecordId());
            }
        }
    }

    /**
     * 保存班级维度-单项评分
     */
    private void SaveClassScoreNameSingle() {
        if (getView() == null) return;
        getView().showDialog(getView().getResourceString(R.string.string_loading_saving));
        mCompositeSubscription.add(DormitoryModel.SaveClassScoreNameSingle(dormitoryEnterNameSingleHttpInfoBean).subscribe(new SaveDataSubscriber()));
    }

    /**
     * 保存宿舍维度-单项评分
     */
    private void SaveDormScoreNameSingle() {
        if (getView() == null) return;
        getView().showDialog(getView().getResourceString(R.string.string_loading_saving));
        mCompositeSubscription.add(DormitoryModel.SaveDormScoreNameSingle(dormitoryEnterNameSingleHttpInfoBean).subscribe(new SaveDataSubscriber()));
    }

    /**
     * 宿舍楼ID列表/专业部ID列表，把ID格式化成1，2，3格式
     * @param isAll 是否全部，全部则传0
     * @param dormitoryIdList 宿舍楼ID列表/专业部ID列表
     * @return 格式化后字符串,这里可考虑异步执行
     */
    public String formatBuildingOrDeptIDs(boolean isAll, List<DormitoryOrClassSingleInfoBean> dormitoryIdList) {
        StringBuilder ids = new StringBuilder();
        if(isAll) {
            ids.append("0");    // 全部宿舍楼/专业部，则传0
        } else {
            if(dormitoryIdList != null) {
                for(int i = 0; i < dormitoryIdList.size(); i++) {   // 拼接宿舍楼或专业部ID（id,id,id,id）
                    if(i == 0)
                        ids.append(dormitoryIdList.get(i).getDormitoryOrClassId());
                    else
                        ids.append("," + dormitoryIdList.get(i).getDormitoryOrClassId());
                }
            }
        }
        return ids.toString();
    }
}
