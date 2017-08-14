package com.gta.zssx.dormitory.presenter;


import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.DormitoryModel;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassSingleInfoBean;
import com.gta.zssx.dormitory.model.bean.OptionBean;
import com.gta.zssx.dormitory.model.bean.OptionHttpBean;
import com.gta.zssx.dormitory.model.bean.RecordIdBean;
import com.gta.zssx.dormitory.view.OptionView;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.exception.CustomException;

import java.net.SocketTimeoutException;
import java.util.List;

import rx.Subscriber;

/**
 * [Description]
 * <p> 宿舍维度/班级维度--有选项、无选项设置presenter
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Weiye.Chen on 2017/7/22.
 * @since 2.0.0
 */
public class DormitoryOrClassOptionPresenter extends BasePresenter<OptionView> {

    private int mItemId;
    private String mInputData;
    private String mDetailId;
    private int mActionType;
    private String userId;
    private int recordId;
    private String buildingOrDeptIds;
    private OptionBean detailContent;
    private OptionHttpBean optionHttpBean;

    /**
     *  获取评分录入-选项设置详情
     * @param itemId 指标项ID
     * @param inputData 日期
     * @param DetailId 宿舍ID/班级ID
     * @param actionType 1 新增， 2 修改 ，3 查看
     * @param dimensionType 宿舍维度或班级维度
     * @param optionType 有选项设置或无选项设置
     */
    public void getDormitoryOrClassOptionDetail(int itemId, String inputData, String DetailId, int actionType, int dimensionType, int optionType) {
        this.mItemId = itemId;
        this.mInputData = inputData;
        this.mDetailId = DetailId;
        this.mActionType = actionType;
        if(getView() == null) return;
        getView().showLoadingDialog();
        if(dimensionType == Constant.DimensionType_Dormitory) {
            if(optionType == Constant.ScoringTemplateType_HasOption) {
                getDormitoryNoNameHasOption(); // 宿舍维度-不录入姓名-有选项设置
            } else {
                getDormitoryNoNameNoOption();    // 宿舍维度-不录入姓名-无选项设置
            }
        } else {
            if(optionType == Constant.ScoringTemplateType_HasOption) {
                getClassNoNameHasOption(); // 班级维度-不录入姓名-有选项设置
            } else {
                getClassNoNameNoOption();    // 班级维度-不录入姓名-无选项设置
            }
        }

    }

    private class GetDataSubscriber extends Subscriber<OptionBean> {
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
        public void onNext(OptionBean dormitoryEnterNameSingleItemInfoBean) {
            if (getView() != null) {
                getView().showResult(dormitoryEnterNameSingleItemInfoBean);
            }
        }
    }

    /**
     * 获取班级维度，不录入姓名，有选择设置详情
     */
    private void getClassNoNameHasOption() {
        mCompositeSubscription.add(DormitoryModel.GetClassNoNameHasOption(mItemId, mInputData, mDetailId, mActionType).subscribe(new GetDataSubscriber()));
    }

    /**
     * 获取班级维度，不录入姓名，无选择设置详情
     */
    private void getClassNoNameNoOption() {
        mCompositeSubscription.add(DormitoryModel.GetClassNoNameNoOption(mItemId, mInputData, mDetailId, mActionType).subscribe(new GetDataSubscriber()));
    }

    /**
     * 获取宿舍维度，不录入姓名，有选择设置详情
     */
    private void getDormitoryNoNameHasOption() {
        mCompositeSubscription.add(DormitoryModel.GetDormitoryNoNameHasOption(mItemId, mInputData, mDetailId, mActionType).subscribe(new GetDataSubscriber()));
    }

    /**
     * 获取宿舍维度，不录入姓名，无选择设置详情
     */
    private void getDormitoryNoNameNoOption() {
        mCompositeSubscription.add(DormitoryModel.GetDormitoryNoNameNoOption(mItemId, mInputData, mDetailId, mActionType).subscribe(new GetDataSubscriber()));
    }


    /**
     * 保存评分录入-有选项设置、无选项设置
     * @param dimensionType 宿舍维度/班级维度
     * @param optionType 有选项或无选项
     * @param optionHttpBean 保存参数实体
     */
    public void SaveDormitoryOrClassOptionData(int dimensionType, int optionType, OptionHttpBean optionHttpBean) {
        this.optionHttpBean = optionHttpBean;
        if(getView() == null) return;
        getView().showLoadingDialog();
        saveDormitoryDimensionType(dimensionType, optionType);
    }

    /**
     * 区分不同维度进行保存
     * @param dimensionType 宿舍维度/班级维度
     * @param optionType 有选项或无选项
     */
    private void saveDormitoryDimensionType(int dimensionType, int optionType) {
        if(Constant.DimensionType_Dormitory == dimensionType) {
            if(optionType == Constant.ScoringTemplateType_HasOption) {
                saveDormitoryNoNameHasOption();
            } else {
                saveDormitoryNoNameNoOption();
            }
        } else {
            if(optionType == Constant.ScoringTemplateType_HasOption) {
                saveClassNoNameHasOption();
            } else {
                saveClassNoNameNoOption();
            }
        }
    }

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
            if(getView() != null) {
                if(recordId != null)
                    getView().saveSuccess(true, recordId.getRecordId());
                else
                    getView().saveSuccess(false, 0);
            }
        }
    }

    /**
     * 保存-宿舍维度-不录入姓名-有选项设置
     */
    private void saveDormitoryNoNameHasOption() {
        mCompositeSubscription.add(DormitoryModel.SaveDormScoreNoNameHasOption(optionHttpBean).subscribe(new SaveDataSubscriber()));
    }

    /**
     * 保存-宿舍维度-不录入姓名-无选项设置
     */
    private void saveDormitoryNoNameNoOption() {
        mCompositeSubscription.add(DormitoryModel.SaveDormScoreNoNameNoOption(optionHttpBean).subscribe(new SaveDataSubscriber()));
    }

    /**
     * 保存-班级维度-不录入姓名-有选项设置
     */
    private void saveClassNoNameHasOption() {
        mCompositeSubscription.add(DormitoryModel.SaveClassScoreNoNameHasOption(optionHttpBean).subscribe(new SaveDataSubscriber()));
    }

    /**
     * 保存-班级维度-不录入姓名-无选项设置
     */
    private void saveClassNoNameNoOption() {
        mCompositeSubscription.add(DormitoryModel.SaveClassScoreNoNameNoOption(optionHttpBean).subscribe(new SaveDataSubscriber()));
    }

    /**
     * 遍历宿舍楼ID/专业部ID，转换格式为：1，2，3列表格式
     * @param isAll 是否全部，全部为0
     * @param dormitoryIdList 宿舍楼列表/专业部列表
     * @return 1,2,3,4
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
