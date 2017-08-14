package com.gta.zssx.dormitory.presenter;


import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.dormitory.model.DormitoryModel;
import com.gta.zssx.dormitory.model.bean.CheckDormitoryIfCanEnterBean;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassSingleInfoBean;
import com.gta.zssx.dormitory.model.bean.OptionHttpBean;
import com.gta.zssx.dormitory.model.bean.RecordIdBean;
import com.gta.zssx.dormitory.view.DormitoryOrClassListSubmitView;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.exception.CustomException;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;


/**
 * [Description]
 * <p> 宿舍列表/班级列表页 送审，修改日期
 * [How to use]
 * <p>送审方法submitDormitoryOrClassList， 修改日期方法checkIfCanInput
 * [Tips]
 *
 * @author Create by Weiye.Chen on 2017/7/21.
 * @since 2.0.0
 */
public class DormitoryOrClassListSubmitPresenter extends BasePresenter<DormitoryOrClassListSubmitView> {
    private OptionHttpBean mOptionHttpBean;

    /**
     * 送审操作
     *  RecordId   记录ID
     *  ActionType 2 送审 1 删除--这里只有送审操作
     *  UserId     用户ID
     *  @param optionHttpBean 送审数据实体
     *  @param ActionType 2 送审 1 删除--这里只有送审操作
     *  @param dimensionType 1 宿舍维度， 2班级维度
     */
    public void submitDormitoryOrClassList(OptionHttpBean optionHttpBean, int ActionType, int dimensionType) {
        if (getView() == null) {
            return;
        }
        getView().showLoadingDialog();
        this.mOptionHttpBean = optionHttpBean;
        if (mOptionHttpBean.getRecordId() == -1) { // 当-1的时候，表示新增进来，没有保存过评分就送审，直接调用保存操作，并且recordId设为0
            mOptionHttpBean.setRecordId(0);
            if(dimensionType == Constant.DimensionType_Dormitory)
                saveDormitoryNoNameHasOption(); // 宿舍维度的，随意调用一个宿舍维度的任何选项的保存接口即可
            else
                saveClassNoNameHasOption(); // 班级维度的，随意调用一个班级维度的任何选项的保存接口即可
        } else {
            mCompositeSubscription.add(DormitoryModel.deleteOrSubmitRecord(optionHttpBean.getRecordId(), ActionType, mOptionHttpBean.getUserId())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {
                            if (isViewAttached()) {
                                getView().hideDialog();
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            if (getView() != null) {
                                getView().hideDialog();
                                getView().submitSuccessOrFail(false);
                            }
                        }

                        @Override
                        public void onNext(String s) {
                            if (getView() != null) {
                                getView().submitSuccessOrFail(true);
                            }
                        }
                    }));
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
                getView().submitSuccessOrFail(false);
            }
        }

        @Override
        public void onNext(RecordIdBean recordId) {
            if(getView() != null) {
                if(recordId != null)
                    getView().submitSuccessOrFail(true);
                else
                    getView().submitSuccessOrFail(false);
            }
        }
    }

    /**
     * 保存-宿舍维度-不录入姓名-有选项设置
     * 注意：这里是相当于送审操作
     */
    private void saveDormitoryNoNameHasOption() {
        mCompositeSubscription.add(DormitoryModel.SaveDormScoreNoNameHasOption(mOptionHttpBean).subscribe(new SaveDataSubscriber()));
    }

    /**
     * 保存-班级维度-不录入姓名-有选项设置
     * 注意：这里是相当于送审操作
     */
    private void saveClassNoNameHasOption() {
        mCompositeSubscription.add(DormitoryModel.SaveClassScoreNoNameHasOption(mOptionHttpBean).subscribe(new SaveDataSubscriber()));
    }

    /**
     * 检查是否能录入
     * 提交的指标项、日期、宿舍楼是否有存在
     *
     * @param ItemId    指标项名称
     * @param Listing   宿舍楼列表/专业部列表
     * @param InputDate 日期
     */
    public void checkIfCanInput(int ItemId, String Listing, String InputDate, int recordId) {
        if (getView() == null) {
            return;
        }
        getView().showLoadingDialog();
        // 先检查修改日期是否存在相同
        Observable<CheckDormitoryIfCanEnterBean> observable1 = DormitoryModel.checkIfCanInput(ItemId, Listing, InputDate);
        /*上面检查通过之后，再进行修改日期*/
        Observable<String> observable2 = DormitoryModel.ChangeInputDate(recordId, ZSSXApplication.instance.getUser().getUserId(), InputDate);

        mCompositeSubscription.add(observable1.flatMap(new Func1<CheckDormitoryIfCanEnterBean, Observable<String>>() {
            @Override
            public Observable<String> call(CheckDormitoryIfCanEnterBean checkDormitoryIfCanEnterBean) {
                if (checkDormitoryIfCanEnterBean.getCode() == 0) {  // 不存在相同，可以修改日期
                    if(recordId == -1) { // 新增进入，不需要调用修改日期接口
                        return Observable.create(subscriber -> {
                            subscriber.onNext("");
                            subscriber.onCompleted();
                        });
                    } else
                        return observable2;
                }
                return Observable.create(subscriber -> subscriber.onError(null)); // 存在相同，不可以修改日期
            }
        }).subscribe(new Subscriber<String>() {
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
                    getView().changedDateSuccess(false);
                }

            }

            @Override
            public void onNext(String s) {
                if (getView() != null) {
                    getView().changedDateSuccess(true);
                }
            }
        }));
    }

    /**
     * 遍历宿舍楼ID/专业部ID，转换格式为：1，2，3列表格式
     *
     * @param isAll           是否全部，全部为0
     * @param dormitoryIdList 宿舍楼列表/专业部列表
     * @return 1, 2, 3, 4
     */
    public String formatBuildingOrDeptIDs(boolean isAll, List<DormitoryOrClassSingleInfoBean> dormitoryIdList) {
        String ids = "";
        if (isAll) {
            ids = "0";    // 全部宿舍楼/专业部，则传0
        } else {
            if (dormitoryIdList != null) {
                for (int i = 0; i < dormitoryIdList.size(); i++) {   // 拼接宿舍楼或专业部ID（id,id,id,id）
                    if (i == 0)
                        ids += dormitoryIdList.get(i).getDormitoryOrClassId();
                    else
                        ids += ("," + dormitoryIdList.get(i).getDormitoryOrClassId());
                }
            }
        }
        return ids;
    }
}
