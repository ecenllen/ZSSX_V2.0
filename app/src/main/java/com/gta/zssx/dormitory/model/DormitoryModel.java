package com.gta.zssx.dormitory.model;

import com.gta.zssx.dormitory.model.bean.CheckDormitoryIfCanEnterBean;
import com.gta.zssx.dormitory.model.bean.DormitoryEnterNameSingleHttpInfoBean;
import com.gta.zssx.dormitory.model.bean.DormitoryEnterNameSingleItemInfoBean;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassSingleInfoBean;
import com.gta.zssx.dormitory.model.bean.DormitoryEnterNameMultipleItemInfoBean;
import com.gta.zssx.dormitory.model.bean.DormitoryEnterNameMultipleItemInfoBeanV2;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassListBean;
import com.gta.zssx.dormitory.model.bean.DormitoryRankingBean;
import com.gta.zssx.dormitory.model.bean.ItemInfoBean;
import com.gta.zssx.dormitory.model.bean.ItemLevelBean;
import com.gta.zssx.dormitory.model.bean.OptionBean;
import com.gta.zssx.dormitory.model.bean.OptionHttpBean;
import com.gta.zssx.dormitory.model.bean.RecordIdBean;
import com.gta.zssx.dormitory.model.bean.SaveDormitoryEnterNameMultipleBean;
import com.gta.zssx.pub.DormitoryInterfaceList;
import com.gta.zssx.pub.http.HttpMethod;
import com.gta.zssx.pub.http.HttpResult;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * [Description]
 * <p> 宿舍模块接口模型
 * [How to use]
 * <p>
 * [Tips]
 * @author Created by lan.zheng on 2016/7/15.
 * @since 2.0.0
 */
public class DormitoryModel extends BaseDormitoryManager {
    private static final Class mInterfaceListClass = com.gta.zssx.pub.DormitoryInterfaceList.class;

    public static Observable<String> getTest() {
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.userLoginTest(""));
    }


    /**
     * 宿舍评分录入列表
     *
     * @param UserId  用户Id
     * @param SendToAuditState  未送审还是已送审
     * @param PageSize  一页几条
     * @param PageIndex 页数
     * @return List<DormitoryRankingBean>
     */
    public static Observable<List<DormitoryRankingBean>> getDormitoryRankingList(String UserId, int SendToAuditState, int PageSize, int PageIndex){
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.dormitoryRankingList(UserId,SendToAuditState,PageSize,PageIndex));
    }

    /**
     * 获取指标项列表
     *
     * @param ItemId 指标项Id
     * @return  ItemInfoBean
     */
    public static Observable<ItemInfoBean> getItemList(int ItemId ){
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.getItemList(ItemId));
    }


    /**
     * 获取宿舍楼或专业部列表
     *
     * @param DormitoryOrClass  专业部还是宿舍楼
     * @return List<DormitoryOrClassSingleInfoBean>
     */
    public static Observable<List<DormitoryOrClassSingleInfoBean>> dormitoryOrClassList(int DormitoryOrClass ){
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.dormitoryOrClassList(DormitoryOrClass));
    }

    /**
     * 删除或送审一条记录
     *
     * @param RecordId  记录Id
     * @param ActionType  操作类型
     * @return String
     */
    public static Observable<String> deleteOrSubmitRecord(int RecordId,int ActionType,String UserId){
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.deleteOrSubmitRecord(RecordId,ActionType,UserId));
    }

    /**
     * 获取指标项搜索列表
     *
     * @param KeyWord  关键字
     * @return  List<ItemLevelBean>
     */
    public static Observable<List<ItemLevelBean>> getItemSearchList(String KeyWord ){
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.getItemSearchList(KeyWord));
    }

    /**
     *  检查是否能进行评分录入
     * @param ItemId  指标项Id
     * @param Listing  选择的宿舍楼或专业部
     * @param InputDate 日期
     * @return  List<Integer>  以被录入过的宿舍楼或专业部
     */
    public static Observable<CheckDormitoryIfCanEnterBean> checkIfCanInput(int ItemId, String Listing, String InputDate){
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.checkIfCanInput(ItemId,Listing,InputDate));
    }

    /**
     *  检查是否能进行评分录入
     * @param recordId  记录ID
     * @param userId 用户ID
     * @return  List<Integer>  以被录入过的宿舍楼或专业部
     */
    public static Observable<String> ChangeInputDate(int recordId, String userId, String inputDate){
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.ChangeInputDate(recordId, userId, inputDate));
    }


    /**
     * 录入姓名多项 - 宿舍维度
     *
     * @param DateTime 日期
     * @param ItemId   指标项ID
     * @param DetailId 宿舍或班级ID
     * @param ActionType 操作
     * @return DormitoryEnterNameMultipleItemInfoBean
     */
    public static Observable<DormitoryEnterNameMultipleItemInfoBean> getEnterNameMultipleItemDormitoryInfo( String DateTime, int ItemId,String DetailId,int ActionType){
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.getEnterNameMultipleItemDormitoryInfo(DateTime,ItemId,DetailId,ActionType));
    }

    /**
     * 录入姓名多项 - 班级维度
     *
     * @param DateTime 日期
     * @param ItemId 指标项ID
     * @param DetailId 宿舍或班级ID
     * @param ActionType 操作
     * @return DormitoryEnterNameMultipleItemInfoBeanV2
     */
    public static Observable<DormitoryEnterNameMultipleItemInfoBeanV2> getEnterNameMultipleItemClassInfo(String DateTime, int ItemId, String DetailId,int ActionType){
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.getEnterNameMultipleItemClassInfo(DateTime,ItemId,DetailId,ActionType));
    }

    /**
     * 录入姓名单项 - 宿舍维度
     *
     * @param DateTime 日期
     * @param ItemId   指标项ID
     * @param DetailId 宿舍或班级ID
     * @param ActionType 操作
     * @return DormitoryEnterNameSingleItemInfoBean
     */
    public static Observable<DormitoryEnterNameSingleItemInfoBean> getEnterNameSingleItemDormitoryInfo(String DateTime, int ItemId, String DetailId, int ActionType){
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.getEnterNameSingleItemDormitoryInfo(DateTime,ItemId,DetailId,ActionType));
    }

    /**
     * 录入姓名单项 - 班级维度
     *
     * @param DateTime 日期
     * @param ItemId 指标项ID
     * @param DetailId 宿舍或班级ID
     * @param ActionType 操作
     * @return DormitoryEnterNameSingleItemInfoBeanV2
     */
    public static Observable<DormitoryEnterNameSingleItemInfoBean> getEnterNameSingleItemClassInfo(String DateTime, int ItemId, String DetailId, int ActionType){
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.getEnterNameSingleItemClassInfo(DateTime,ItemId,DetailId,ActionType));
    }



    /**
     * 宿舍列表/班级列表
     *
     * @param InputData 日期
     * @param ItemId   指标项ID
     * @param ListString 宿舍楼ID或专业部ID
     * @return data
     */
    public static Observable<DormitoryOrClassListBean> GetBuildingOrDeptInfo(String InputData, int ItemId, ArrayList<String> ListString) {
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.GetBuildingOrDeptInfo(InputData, ItemId, ListString));
    }

    /**
     * 班级维度-不录入姓名-无选项设置
     *
     * @param date 日期
     * @param ItemId   指标项ID
     * @param detailId 宿舍ID或班级ID
     * @return data
     */
    public static Observable<OptionBean> GetClassNoNameNoOption(int ItemId, String date, String detailId, int actionType) {
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.GetClassNoNameNoOption(ItemId, date, detailId, actionType));
    }

    /**
     * 班级维度-不录入姓名-获取有选项设置
     *
     * @param date 日期
     * @param ItemId   指标项ID
     * @param detailId 宿舍ID或班级ID
     * @return data
     */
    public static Observable<OptionBean> GetClassNoNameHasOption(int ItemId, String date, String detailId, int actionType) {
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.GetClassNoNameHasOption(ItemId, date, detailId, actionType));
    }

    /**
     * 宿舍维度-不录入姓名-获取有选项设置数据
     *
     * @param date 日期
     * @param ItemId   指标项ID
     * @param detailId 宿舍ID或班级ID
     * @return data
     */
    public static Observable<OptionBean> GetDormitoryNoNameHasOption(int ItemId, String date, String detailId, int actionType) {
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.GetDormitoryNoNameHasOption(ItemId, date, detailId, actionType));
    }

    /**
     * 宿舍维度-不录入姓名-获取无选项设置
     *
     * @param date 日期
     * @param ItemId   指标项ID
     * @param detailId 宿舍ID或班级ID
     * @return data
     */
    public static Observable<OptionBean> GetDormitoryNoNameNoOption(int ItemId, String date, String detailId, int actionType) {
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.GetDormitoryNoNameNoOption(ItemId, date, detailId, actionType));
    }

    /**
     * 录入姓名多项 - 宿舍维度 - 保存
     * @param saveDormitoryEnterNameMultipleBean 内容
     * @return RecordId
     */
    public static Observable<RecordIdBean> saveEnterNameMultipleItemDormitoryInfo(SaveDormitoryEnterNameMultipleBean saveDormitoryEnterNameMultipleBean) {
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.saveEnterNameMultipleItemDormitoryInfo(saveDormitoryEnterNameMultipleBean));
    }

    /**
     * 录入姓名多项 - 班级维度 - 保存
     * @param saveDormitoryEnterNameMultipleBean 内容
     * @return RecordId
     */
    public static Observable<RecordIdBean> saveEnterNameMultipleItemClassInfo(SaveDormitoryEnterNameMultipleBean saveDormitoryEnterNameMultipleBean) {
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.saveEnterNameMultipleItemClassInfo(saveDormitoryEnterNameMultipleBean));
    }


    /**
     * 班级维度-录入姓名-保存单项设置
     */
    public static Observable<RecordIdBean> SaveClassScoreNameSingle(DormitoryEnterNameSingleHttpInfoBean dormitoryEnterNameSingleHttpInfoBean) {
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.SaveClassScoreNameSingle(dormitoryEnterNameSingleHttpInfoBean));
    }

    /**
     * 班级维度-录入姓名-保存单项设置
     *  InputDate 日期
     *  UserId 用户ID
     *  ItemId 指标项ID
     *  recordId 记录ID-新增为-1，其他为列表的KEY
     *  detailContent 提交内容-学生列表，
     *  BuildingOrDeptIds 全部宿舍楼ID/专业部ID
     *
     */
    public static Observable<RecordIdBean> SaveDormScoreNameSingle(DormitoryEnterNameSingleHttpInfoBean dormitoryEnterNameSingleHttpInfoBean) {
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.SaveDormScoreNameSingle(dormitoryEnterNameSingleHttpInfoBean));
    }

    /**
     * 班级维度-录入姓名-保存单项设置
     *  InputDate 日期
     *  UserId 用户ID
     *  ItemId 指标项ID
     *  recordId 记录ID-新增为-1，其他为列表的KEY
     *  detailContent 提交内容-学生列表，
     *  BuildingOrDeptIds 全部宿舍楼ID/专业部ID
     *
     */
    public static Observable<RecordIdBean> SaveDormScoreNoNameHasOption(OptionHttpBean optionHttpBean) {
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.SaveDormScoreNoNameHasOption(optionHttpBean));
    }

    /**
     * 班级维度-录入姓名-保存单项设置
     *  InputDate 日期
     *  UserId 用户ID
     *  ItemId 指标项ID
     *  recordId 记录ID-新增为-1，其他为列表的KEY
     *  detailContent 提交内容-学生列表，
     *  BuildingOrDeptIds 全部宿舍楼ID/专业部ID
     *
     */
    public static Observable<RecordIdBean> SaveDormScoreNoNameNoOption(OptionHttpBean optionHttpBean) {
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.SaveDormScoreNoNameNoOption(optionHttpBean));
    }

    /**
     * 班级维度-录入姓名-保存单项设置
     *  InputDate 日期
     *  UserId 用户ID
     *  ItemId 指标项ID
     *  recordId 记录ID-新增为-1，其他为列表的KEY
     *  detailContent 提交内容-学生列表，
     *  BuildingOrDeptIds 全部宿舍楼ID/专业部ID
     *
     */
    public static Observable<RecordIdBean> SaveClassScoreNoNameHasOption(OptionHttpBean optionHttpBean) {
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.SaveClassScoreNoNameHasOption(optionHttpBean));
    }

    /**
     * 班级维度-录入姓名-保存单项设置
     *  InputDate 日期
     *  UserId 用户ID
     *  ItemId 指标项ID
     *  recordId 记录ID-新增为-1，其他为列表的KEY
     *  detailContent 提交内容-学生列表，
     *  BuildingOrDeptIds 全部宿舍楼ID/专业部ID
     *
     */
    public static Observable<RecordIdBean> SaveClassScoreNoNameNoOption(OptionHttpBean optionHttpBean) {
        DormitoryInterfaceList lInterfaceList = getInterfaceList(mInterfaceListClass);
        return HttpMethod.getInstance().call(lInterfaceList.SaveClassScoreNoNameNoOption(optionHttpBean));
    }

}
