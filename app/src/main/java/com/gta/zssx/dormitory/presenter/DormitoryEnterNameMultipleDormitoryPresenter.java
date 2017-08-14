package com.gta.zssx.dormitory.presenter;

import android.text.TextUtils;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.DormitoryModel;
import com.gta.zssx.dormitory.model.bean.ClassInfoBean;
import com.gta.zssx.dormitory.model.bean.DetailItemBean;
import com.gta.zssx.dormitory.model.bean.DormitoryEnterNameMultipleItemInfoBean;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassSingleInfoBean;
import com.gta.zssx.dormitory.model.bean.EnterNameMultipleItemBean;
import com.gta.zssx.dormitory.model.bean.RecordIdBean;
import com.gta.zssx.dormitory.model.bean.SaveDormitoryEnterNameMultipleBean;
import com.gta.zssx.dormitory.view.DormitoryEnterNameMultipleDormitoryView;
import com.gta.zssx.pub.exception.CustomException;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * [Description]
 * <p>录入姓名多项-宿舍维度presenter
 * [How to use]
 * <p>获取数据方法：getEnterNameMultipleItemDormitoryInfo，保存数据方法：saveEnterNameMultipleItemDormitoryInfo
 * [Tips]
 * <p>
 * Created by lan.zheng on 2017/7/24 12:07.
 */
 
public class DormitoryEnterNameMultipleDormitoryPresenter extends BasePresenter<DormitoryEnterNameMultipleDormitoryView> {
    public Subscription mSubscribeList;
    public Subscription mSaveSubscribeList;

    /**
     * 获取录入姓名多项的列表信息
     */
    public void getEnterNameMultipleItemDormitoryInfo(String DateTime,int ItemId,String DetailId,int ActionType) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribeList = DormitoryModel.getEnterNameMultipleItemDormitoryInfo(DateTime,ItemId,DetailId,ActionType)
                .subscribe(new Subscriber<DormitoryEnterNameMultipleItemInfoBean>() {

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
                    public void onNext(DormitoryEnterNameMultipleItemInfoBean dormitoryEnterNameMultipleItemInfoBean) {
                        if(isViewAttached()) {
                            if(dormitoryEnterNameMultipleItemInfoBean !=null && dormitoryEnterNameMultipleItemInfoBean.getStudentList() != null){
                                getView().showDataInfo(dormitoryEnterNameMultipleItemInfoBean);
                            }else {
                                if(dormitoryEnterNameMultipleItemInfoBean == null){
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
    public void saveEnterNameMultipleItemDormitoryInfo(String InputDate,String UserId, int ItemId, int RecordId, String BuildingOrDeptIds, DormitoryEnterNameMultipleItemInfoBean DetailContent){

        if (!isViewAttached()) {
            return;
        }
        SaveDormitoryEnterNameMultipleBean saveDormitoryEnterNameMultipleBean = new SaveDormitoryEnterNameMultipleBean();
        saveDormitoryEnterNameMultipleBean.setInputDate(InputDate);
        saveDormitoryEnterNameMultipleBean.setBuildingOrDeptIds(BuildingOrDeptIds);
        saveDormitoryEnterNameMultipleBean.setRecordId(RecordId);
        saveDormitoryEnterNameMultipleBean.setItemId(ItemId);
        saveDormitoryEnterNameMultipleBean.setUserId(UserId);
        saveDormitoryEnterNameMultipleBean.setDetailContent(DetailContent);
        getView().showLoadingDialog();
        mSaveSubscribeList = DormitoryModel.saveEnterNameMultipleItemDormitoryInfo(saveDormitoryEnterNameMultipleBean)
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
//                                getView().showToast("recordIdBean == null,服务器并未返回RecordId");
                                getView().saveAndGetRecordId(RecordId); //没有返回的时候给-1
                            }
                        }
                    }
                });
        mCompositeSubscription.add(mSaveSubscribeList);
    }


    /**
     * 剔除没有数据的学生 - 为了防止勾选的指标项有0分的情况，不进行剔除操作
     * @param dormitoryEnterNameMultipleItemInfoBean  原列表
     * @return 剔除后的列表
     */
    /*public DormitoryEnterNameMultipleItemInfoBean formatInfoData(DormitoryEnterNameMultipleItemInfoBean dormitoryEnterNameMultipleItemInfoBean){
        List<EnterNameMultipleItemBean> list = new ArrayList<>();
        for(int i = 0;i <dormitoryEnterNameMultipleItemInfoBean.getStudentList().size();i++){
            String score = dormitoryEnterNameMultipleItemInfoBean.getStudentList().get(i).getStudentScore();
            //只有当不为空和分数大于0的数据才需要上传
            if(!TextUtils.isEmpty(score) && Float.parseFloat(score) > 0){
                list.add(dormitoryEnterNameMultipleItemInfoBean.getStudentList().get(i));
            }
        }
        dormitoryEnterNameMultipleItemInfoBean.setStudentList(list);
        return dormitoryEnterNameMultipleItemInfoBean;
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
     *  宿舍维度 - 班级得分赋值
     * @param classInfoBeanList 班级集合
     * @param enterNameMultipleItemBeanList 床位列表
     * @param scoreList 分数集合
     */
    public void setNewClassScore(List<ClassInfoBean> classInfoBeanList,List<EnterNameMultipleItemBean> enterNameMultipleItemBeanList,List<Float> scoreList){
        for(int i = 0;i < classInfoBeanList.size();i++) {
            int ClassId =  classInfoBeanList.get(i).getClassId();
            float score = 0;
            boolean isClassStudentHaveScore = isClassStudentHaveScore(ClassId,enterNameMultipleItemBeanList);
            if(isClassStudentHaveScore){  //有进行分数计算
                for(int j = 0;j <enterNameMultipleItemBeanList.size();j++ ) {
                    int studentClassId = enterNameMultipleItemBeanList.get(j).getClassId();
                    //TODO 每个学生轮询，找到属于这个班级的学生，拿到分数
                    if(studentClassId == ClassId){
                        //TODO 分数累加
                        score += scoreList.get(j);
                    }
                }
                float f =   (float)(Math.round(score*1000))/1000;
                classInfoBeanList.get(i).setClassSingleScore(String.valueOf(f));
            }else {  //没有直接给空
                classInfoBeanList.get(i).setClassSingleScore("");
            }

        }

    }


    private boolean isClassStudentHaveScore(int ClassId,List<EnterNameMultipleItemBean> enterNameMultipleItemBeanList){
        boolean isHave = false;
        //TODO 判断属于该班级的学生是否有分数
        for(int j = 0;j <enterNameMultipleItemBeanList.size();j++ ) {
            int studentClassId = enterNameMultipleItemBeanList.get(j).getClassId();
            //TODO 如果有属于该班级的学生，且有分数
            if(studentClassId == ClassId && !TextUtils.isEmpty(enterNameMultipleItemBeanList.get(j).getStudentScore())){
                isHave = true;
            }
        }
        return isHave;
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
     * 判断学生分数情况 - 针对宿舍总分
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


    public DormitoryEnterNameMultipleItemInfoBean testData(){
        DormitoryEnterNameMultipleItemInfoBean dormitoryEnterNameMultipleItemInfoBean = new DormitoryEnterNameMultipleItemInfoBean();
        dormitoryEnterNameMultipleItemInfoBean.setDormitoryScore("18");
        List<EnterNameMultipleItemBean> enterNameMultipleItemBeanList = new ArrayList<>();
        List<DetailItemBean> itemBeanList = new ArrayList<>();
        for(int i = 1;i < 8;i++){
            DetailItemBean itemBean = new DetailItemBean();
            itemBean.setDetailItemId(i);
            itemBean.setDetailItemName("指标项"+i);
            itemBean.setDetailItemScoreSet(""+i);
            if(i%3 == 0){
                itemBean.setDetailItemCheck(true);
            }else {
                itemBean.setDetailItemCheck(false);
            }
            itemBeanList.add(itemBean);
        }

        EnterNameMultipleItemBean bean1 = new EnterNameMultipleItemBean();
        bean1.setStudentName("学生一");
        bean1.setStudentNo("123456789");
        bean1.setStudentScore("9");
        bean1.setClassId(1);
        bean1.setBedName("一床");  //TODO 必须要有床位信息和班级信息
        bean1.setBedId("");
        bean1.setIndexItemBeanList(itemBeanList);
        EnterNameMultipleItemBean bean2 = new EnterNameMultipleItemBean();
        bean2.setStudentName("学生二");
        bean2.setStudentNo("123456789");
        bean2.setStudentScore("9");
        bean2.setClassId(1);
        bean2.setBedName("二床");  //TODO 必须要有床位信息和班级信息
        bean2.setBedId("");
        bean2.setIndexItemBeanList(itemBeanList);

        enterNameMultipleItemBeanList.add(bean1);
        enterNameMultipleItemBeanList.add(bean2);
//        EnterNameMultipleItemBean bean3 = new EnterNameMultipleItemBean();
//        bean3.setStudentName("学生三");
//        bean3.setStudentNo("123456789");
//        bean3.setStudentScore(0);
//        bean3.setClassId(1);
//        bean3.setBedName("三床");   //TODO 必须要有床位信息和班级信息
//        bean3.setBedId(3);
//        bean3.setIndexItemBeanList(itemBeanList);
//        enterNameMultipleItemBeanList.add(bean3);
        dormitoryEnterNameMultipleItemInfoBean.setStudentList(enterNameMultipleItemBeanList);

        List<ClassInfoBean> classInfoBeanList = new ArrayList<>();
        for(int i = 1; i < 6 ;i++){
            ClassInfoBean classInfoBean = new ClassInfoBean();
            classInfoBean.setClassId(i);
            classInfoBean.setClassName("测试班级"+i);
            classInfoBean.setClassSingleScore("0");
            classInfoBean.setClassSingleRemark("备注"+i);
            classInfoBeanList.add(classInfoBean);
        }
        dormitoryEnterNameMultipleItemInfoBean.setClassList(classInfoBeanList);

        return dormitoryEnterNameMultipleItemInfoBean;
    }
}
