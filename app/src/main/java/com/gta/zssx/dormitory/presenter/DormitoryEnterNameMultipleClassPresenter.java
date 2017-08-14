package com.gta.zssx.dormitory.presenter;

import android.text.TextUtils;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.DormitoryModel;
import com.gta.zssx.dormitory.model.bean.DetailItemBean;
import com.gta.zssx.dormitory.model.bean.DormitoryEnterNameMultipleItemInfoBeanV2;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassSingleInfoBean;
import com.gta.zssx.dormitory.model.bean.EnterNameMultipleItemBean;
import com.gta.zssx.dormitory.model.bean.RecordIdBean;
import com.gta.zssx.dormitory.model.bean.SaveDormitoryEnterNameMultipleBean;
import com.gta.zssx.dormitory.view.DormitoryEnterNameMultipleClassView;
import com.gta.zssx.pub.exception.CustomException;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * [Description]
 * <p> 录入姓名多项-班级维度presenter
 * [How to use]
 * <p>获取数据方法：getEnterNameMultipleItemClassInfo，保存数据方法：saveEnterNameMultipleItemClassInfo
 * [Tips]
 * <p>
 * Created by lan.zheng on 2017/7/24 12:07.
 */
 
public class DormitoryEnterNameMultipleClassPresenter extends BasePresenter<DormitoryEnterNameMultipleClassView> {
    public Subscription mSubscribeList;
    public Subscription mSaveSubscribeList;
    /**
     * 获取录入姓名多项的列表信息
     */
    public void getEnterNameMultipleItemClassInfo(String DateTime,int ItemId,String DetailId,int ActionType) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribeList = DormitoryModel.getEnterNameMultipleItemClassInfo(DateTime,ItemId,DetailId,ActionType)
                .subscribe(new Subscriber<DormitoryEnterNameMultipleItemInfoBeanV2>() {

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
                            getView().getDataInfoFailed();
                            if(throwable instanceof CustomException || throwable instanceof SocketTimeoutException)
                                getView().showOnFailReloading(throwable.getMessage());
                            else
                                getView().showOnFailReloading("");
                        }

                    }

                    @Override
                    public void onNext(DormitoryEnterNameMultipleItemInfoBeanV2 dormitoryEnterNameMultipleItemInfoBeanV2) {
                        if(isViewAttached()) {
                            if(dormitoryEnterNameMultipleItemInfoBeanV2 !=null && dormitoryEnterNameMultipleItemInfoBeanV2.getStudentList() != null){
                                getView().showDataInfo(dormitoryEnterNameMultipleItemInfoBeanV2);
                            }else {
                                if(dormitoryEnterNameMultipleItemInfoBeanV2 == null){
                                    getView().showToast("暂无数据");
                                }else {
                                    getView().showToast("无学生数据");
                                }
                            }
                        }
                    }
                });
        mCompositeSubscription.add(mSubscribeList);
    }

    /**
     * 保存录入姓名多项的列表信息
     */
    public void saveEnterNameMultipleItemClassInfo(String InputDate,String UserId, int ItemId, int RecordId, String BuildingOrDeptIds, DormitoryEnterNameMultipleItemInfoBeanV2 DetailContent){
        if (!isViewAttached()) {
            return;
        }
        //TODO 真实要提交数据封装
        SaveDormitoryEnterNameMultipleBean saveDormitoryEnterNameMultipleBean = new SaveDormitoryEnterNameMultipleBean();
        saveDormitoryEnterNameMultipleBean.setInputDate(InputDate);
        saveDormitoryEnterNameMultipleBean.setBuildingOrDeptIds(BuildingOrDeptIds);
        saveDormitoryEnterNameMultipleBean.setRecordId(RecordId);
        saveDormitoryEnterNameMultipleBean.setItemId(ItemId);
        saveDormitoryEnterNameMultipleBean.setUserId(UserId);
        saveDormitoryEnterNameMultipleBean.setDetailContent(DetailContent);
        getView().showLoadingDialog();
        mSaveSubscribeList = DormitoryModel.saveEnterNameMultipleItemClassInfo(saveDormitoryEnterNameMultipleBean)
                .subscribe(new Subscriber<RecordIdBean>() {

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
                                    getView().saveDataFailHandle(throwable.getMessage());
                                }else {
                                    getView().onErrorHandle(throwable);  //抛出默认异常
                                    getView().saveDataFailHandle(getView().getResourceString(R.string.string_save_fail));
                                }
                            }else {
                                getView().onErrorHandle(throwable);  //抛出默认异常
                                getView().saveDataFailHandle(throwable.getMessage());
                            }
                        }

                    }

                    @Override
                    public void onNext(RecordIdBean recordIdBean) {
                        if(isViewAttached()) {
                            if(recordIdBean != null){
                                getView().saveAndGetRecordId(recordIdBean.getRecordId());//TODO 只要保存成功，都返回RecordId,并更新数据集合
                            }else {
                                getView().saveAndGetRecordId(RecordId); //没有返回的时候
                            }

                        }
                    }
                });
        mCompositeSubscription.add(mSaveSubscribeList);
    }

    /**
     * 去除多余的0
     * @param score
     * @return
     */
    public String formatScore(String score){
        if(score.indexOf(".") > 0){
            score = score.replaceAll("0+?$", "");//去掉多余的0
            score = score.replaceAll("[.]$", "");//如最后一位是.则去掉
            return score;
        }else {
            return score;
        }
    }

    /**
     * 剔除没有数据的学生 - 为了防止勾选的指标项有0分的情况，不进行剔除操作
     * @param dormitoryEnterNameMultipleItemInfoBeanV2 原列表
     * @return 剔除后的列表
     */
   /* public DormitoryEnterNameMultipleItemInfoBeanV2 formatInfoData(DormitoryEnterNameMultipleItemInfoBeanV2 dormitoryEnterNameMultipleItemInfoBeanV2){
        List<EnterNameMultipleItemBean> list = new ArrayList<>();
        for(int i = 0;i <dormitoryEnterNameMultipleItemInfoBeanV2.getStudentList().size();i++){
            String score = dormitoryEnterNameMultipleItemInfoBeanV2.getStudentList().get(i).getStudentScore();
            //只有当不为空和分数大于0的数据才需要上传
            if(!TextUtils.isEmpty(score) && Float.parseFloat(score) > 0){
                list.add(dormitoryEnterNameMultipleItemInfoBeanV2.getStudentList().get(i));
            }
        }
        dormitoryEnterNameMultipleItemInfoBeanV2.setStudentList(list);
        return dormitoryEnterNameMultipleItemInfoBeanV2;
    }*/

    /**
     * 获得宿舍楼或专业部ID字符串
     * @param dormitoryOrClassSingleInfoBeanList  集合
     * @return
     */
    public String getBuildingOrDeptId(List<DormitoryOrClassSingleInfoBean> dormitoryOrClassSingleInfoBeanList){
        if(dormitoryOrClassSingleInfoBeanList.size() == 0){
            return "0";  //全部
        }
        String s = "";
        for(int i = 0;i<dormitoryOrClassSingleInfoBeanList.size();i++){
            if(i != 0){
                s += ",";
            }
            s += String.valueOf(dormitoryOrClassSingleInfoBeanList.get(i).getDormitoryOrClassId());
        }
        return s;
    }

    /**
     * 判断学生分数情况
     * @param enterNameMultipleItemBeanList 学生列表集合
     * @return 有无分数
     */
    public boolean isHaveStudentScore(List<EnterNameMultipleItemBean> enterNameMultipleItemBeanList){
        boolean isHave = false;
        for(int i = 0;i<enterNameMultipleItemBeanList.size();i++ ){
            //TODO 有学生的分数不为空，证明要显示总分
            if(!TextUtils.isEmpty(enterNameMultipleItemBeanList.get(i).getStudentScore())){
                isHave = true;
            }
        }
        return isHave;
    }

    public DormitoryEnterNameMultipleItemInfoBeanV2 testData(){
        DormitoryEnterNameMultipleItemInfoBeanV2 dormitoryEnterNameMultipleItemInfoBeanV2 = new DormitoryEnterNameMultipleItemInfoBeanV2();
        dormitoryEnterNameMultipleItemInfoBeanV2.setClassScore("0");
        dormitoryEnterNameMultipleItemInfoBeanV2.setRemark("备注测试");
        List<EnterNameMultipleItemBean> enterNameMultipleItemBeanList = new ArrayList<>();
        List<DetailItemBean> itemBeanList = new ArrayList<>();
        for(int i = 1;i < 8;i++){
            DetailItemBean itemBean = new DetailItemBean();
            itemBean.setDetailItemId(i);
            itemBean.setDetailItemName("指标项"+i);
            itemBean.setDetailItemScoreSet(""+i);
            if(i%2 == 0){
                itemBean.setDetailItemCheck(false);
            }else {
                itemBean.setDetailItemCheck(true);
            }

            itemBeanList.add(itemBean);
        }

        EnterNameMultipleItemBean bean1 = new EnterNameMultipleItemBean();
        bean1.setStudentName("学生一");
        bean1.setStudentNo("123456789");
        bean1.setStudentScore("16");
        bean1.setIndexItemBeanList(itemBeanList);
        EnterNameMultipleItemBean bean2 = new EnterNameMultipleItemBean();
        bean2.setStudentName("学生二");
        bean2.setStudentNo("123456789");
        bean2.setStudentScore("16");
        bean2.setIndexItemBeanList(itemBeanList);
        enterNameMultipleItemBeanList.add(bean1);
        enterNameMultipleItemBeanList.add(bean2);
        dormitoryEnterNameMultipleItemInfoBeanV2.setStudentList(enterNameMultipleItemBeanList);
        return dormitoryEnterNameMultipleItemInfoBeanV2;
    }
}
