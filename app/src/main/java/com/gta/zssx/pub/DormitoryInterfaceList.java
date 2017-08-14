package com.gta.zssx.pub;

import com.gta.zssx.dormitory.model.bean.CheckDormitoryIfCanEnterBean;
import com.gta.zssx.dormitory.model.bean.DormitoryEnterNameSingleHttpInfoBean;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassSingleInfoBean;
import com.gta.zssx.dormitory.model.bean.DormitoryEnterNameMultipleItemInfoBean;
import com.gta.zssx.dormitory.model.bean.DormitoryEnterNameMultipleItemInfoBeanV2;
import com.gta.zssx.dormitory.model.bean.DormitoryEnterNameSingleItemInfoBean;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassListBean;
import com.gta.zssx.dormitory.model.bean.DormitoryRankingBean;
import com.gta.zssx.dormitory.model.bean.ItemInfoBean;
import com.gta.zssx.dormitory.model.bean.ItemLevelBean;
import com.gta.zssx.dormitory.model.bean.OptionBean;
import com.gta.zssx.dormitory.model.bean.OptionHttpBean;
import com.gta.zssx.dormitory.model.bean.RecordIdBean;
import com.gta.zssx.dormitory.model.bean.SaveDormitoryEnterNameMultipleBean;
import com.gta.zssx.pub.http.HttpResult;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * [Description]
 * <p> 宿舍模块接口列表定义
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by lan.zheng on 2017/6/16.
 * @since 2.0.0
 */
public interface DormitoryInterfaceList {

    /**
     *  直接写 - 测试接口
     *
     * @param loginName 登录名
     * @return 测试
     */
    @GET("")
    Observable<HttpResult<String>> userLoginTest(@Query("loginName") String loginName);


    /**
     * 宿舍评分录入列表
     * @param UserId  用户Id
     * @param SendToAuditState  状态
     * @param PageSize 一页几条
     * @param PageIndex 页码
     * @return 列表
     */
    @GET("GetDataList")
    Observable<HttpResult<List<DormitoryRankingBean>>> dormitoryRankingList(@Query("UserId") String UserId, @Query("SendToAuditState") int SendToAuditState, @Query("PageIndex") int PageIndex, @Query("PageSize") int PageSize);

    /**
     * 选择指标项
     * @param ItemId 指标项Id
     * @return 列表
     */
    @GET("GetItemList")
    Observable<HttpResult<ItemInfoBean>> getItemList(@Query("ItemId") int ItemId);

    /**
     * 指标项搜索
     * @param KeyWord 关键字
     * @return 列表
     */
    @GET("GetItemInfoByKey")
    Observable<HttpResult<List<ItemLevelBean>>> getItemSearchList(@Query("KeyWord") String KeyWord);

    /**
     * 选择宿舍楼或专业部
     * @param DormitoryOrClass  0:宿舍楼，1：专业部
     * @return 列表
     */
    @GET("GetDormitoryOrDeptList")
    Observable<HttpResult<List<DormitoryOrClassSingleInfoBean>>> dormitoryOrClassList(@Query("DormitoryOrClass") int DormitoryOrClass);

    /**
     * 删除或送审一条记录
     * @param RecordId 记录Id
     * @param ActionType 操作类型
     * @return 成功与否
     */
    @GET("SendAuditOrDel")
    Observable<HttpResult<String>> deleteOrSubmitRecord(@Query("RecordId") int RecordId,@Query("ActionType") int ActionType,@Query("UserId") String UserId);

    /**
     * 检查是否能进行下一步
     * @param ItemId  指标项Id
     * @param Listing 宿舍楼或班级String
     * @param InputDate 日期
     * @return 列表
     */
    @GET("CheckIfCanInput")
    Observable<HttpResult<CheckDormitoryIfCanEnterBean>> checkIfCanInput(@Query("ItemId") int ItemId, @Query("Listing") String Listing, @Query("InputDate") String InputDate);

    /**
     * 检查是否能进行下一步
     * @param recordId  记录ID
     * @param userId 用户ID
     * @param InputDate 日期
     * @return 列表
     */
    @GET("ChangeInputDate")
    Observable<HttpResult<String>> ChangeInputDate(@Query("RecordId") int recordId, @Query("UserId") String userId, @Query("InputDate") String InputDate);


    /**
     * 录入姓名单项 - 班级维度
     * @param DateTime
     * @param ItemId
     * @param DetailId
     * @return
     */
    @GET("GetClassDataDetailNameSingle")
    Observable<HttpResult<DormitoryEnterNameSingleItemInfoBean>> getEnterNameSingleItemClassInfo(@Query("DateTime") String DateTime, @Query("ItemId") int ItemId, @Query("DetailId") String DetailId,@Query("ActionType") int ActionType);


    /**
     * 录入姓名单项 - 宿舍维度
     * @param DateTime
     * @param ItemId
     * @param DetailId
     * @return
     */
    @GET("GetDormitoryInfoNameSingle")
    Observable<HttpResult<DormitoryEnterNameSingleItemInfoBean>> getEnterNameSingleItemDormitoryInfo(@Query("DateTime") String DateTime, @Query("ItemId") int ItemId, @Query("DetailId") String DetailId,@Query("ActionType") int ActionType);

    /**
     * 录入姓名多项 - 班级维度
     * @param DateTime
     * @param ItemId
     * @param DetailId
     * @return
     */
    @GET("GetClassDataDetailNameMutil")
    Observable<HttpResult<DormitoryEnterNameMultipleItemInfoBeanV2>> getEnterNameMultipleItemClassInfo(@Query("DateTime") String DateTime, @Query("ItemId") int ItemId, @Query("DetailId") String DetailId,@Query("ActionType") int ActionType);

    /**
     * 录入姓名多项 - 宿舍维度
     * @param DateTime
     * @param ItemId
     * @param DetailId
     * @return
     */
    @GET("GetDormitoryInfoNameMutil")
    Observable<HttpResult<DormitoryEnterNameMultipleItemInfoBean>> getEnterNameMultipleItemDormitoryInfo(@Query("DateTime") String DateTime, @Query("ItemId") int ItemId, @Query("DetailId") String DetailId,@Query("ActionType") int ActionType);

    /**宿舍列表/班级列表
     * @param InputDate 日期
     * @param ItemId 指标项ID
     * @param ListString 宿舍楼ID/专业部ID 列表
     * @return data
     */
    @GET("GetBuildingOrDeptInfo")
    Observable<HttpResult<DormitoryOrClassListBean>> GetBuildingOrDeptInfo(@Query("InputDate") String InputDate, @Query("ItemId") int ItemId, @Query("ListString") List<String> ListString);

    /**班级维度-不录入姓名-有选项设置
     * @param DateTime 日期
     * @param ItemId 指标项ID
     * @param DetailId 某一 宿舍ID/专业ID
     * @return data
     */
    @GET("GetClassNoNameOption")
    Observable<HttpResult<OptionBean>> GetClassNoNameHasOption(@Query("ItemId") int ItemId, @Query("DateTime") String DateTime, @Query("DetailId") String DetailId, @Query("ActionType") int actionType);

    /**班级维度-不录入姓名-无选项设置
     * @param DateTime 日期
     * @param ItemId 指标项ID
     * @param DetailId 某一 宿舍ID/专业ID
     * @return data
     */
    @GET("GetClassNoNameNunOption")
    Observable<HttpResult<OptionBean>> GetClassNoNameNoOption(@Query("ItemId") int ItemId, @Query("DateTime") String DateTime, @Query("DetailId") String DetailId, @Query("ActionType") int actionType);

    /**宿舍维度-不录入姓名-有选项设置
     * @param DateTime 日期
     * @param ItemId 指标项ID
     * @param DetailId 某一 宿舍ID/专业ID
     * @return data
     */
    @GET("GetDormitoryInfoNoNameOption")
    Observable<HttpResult<OptionBean>> GetDormitoryNoNameHasOption(@Query("ItemId") int ItemId, @Query("DateTime") String DateTime, @Query("DetailId") String DetailId, @Query("ActionType") int actionType);

    /**宿舍维度-不录入姓名-无选项设置
     * @param DateTime 日期
     * @param ItemId 指标项ID
     * @param DetailId 某一 宿舍ID/专业ID
     * @return data
     */
    @GET("GetDormitoryInfoNoNameNunOption")
    Observable<HttpResult<OptionBean>> GetDormitoryNoNameNoOption(@Query("ItemId") int ItemId, @Query("DateTime") String DateTime, @Query("DetailId") String DetailId, @Query("ActionType") int actionType);

    /**
     * 录入姓名多项 - 宿舍维度 - 保存
     * @param saveDormitoryEnterNameMultipleBean 内容
     * @return RecordId
     */
    @POST("SaveDormScoreNameMutil")
    Observable<HttpResult<RecordIdBean>> saveEnterNameMultipleItemDormitoryInfo(@Body SaveDormitoryEnterNameMultipleBean saveDormitoryEnterNameMultipleBean);

    /**
     * 录入姓名多项 - 班级维度 - 保存
     * @param saveDormitoryEnterNameMultipleBean 内容
     * @return RecordId
     */
    @POST("SaveClassScoreNameMutil")
    Observable<HttpResult<RecordIdBean>> saveEnterNameMultipleItemClassInfo(@Body SaveDormitoryEnterNameMultipleBean saveDormitoryEnterNameMultipleBean);

    /**
     *  班级维度-录入姓名-保存单项设置
     *  InputDate 日期
     *  UserId 用户ID
     *  ItemId 指标项ID
     *  recordId 记录ID-新增为-1，其他为列表的KEY
     *  DetailContent 提交内容-学生列表，
     *  BuildingOrDeptIds 宿舍
     */
    @POST("SaveClassScoreNameSingle")
        Observable<HttpResult<RecordIdBean>> SaveClassScoreNameSingle(@Body DormitoryEnterNameSingleHttpInfoBean dormitoryEnterNameSingleHttpInfoBean);

    /**
     * 班级维度-录入姓名-保存单项设置
     *  InputDate 日期
     *  UserId 用户ID
     *  ItemId 指标项ID
     *  recordId 记录ID-新增为-1，其他为列表的KEY
     *  DetailContent 提交内容-学生列表，
     *  BuildingOrDeptIds 宿舍楼ID列表或专业部ID列表
     *
     */
    @POST("SaveDormScoreNameSingle")
    Observable<HttpResult<RecordIdBean>> SaveDormScoreNameSingle(@Body DormitoryEnterNameSingleHttpInfoBean dormitoryEnterNameSingleHttpInfoBean);

    /**
     * 班级维度-录入姓名-保存单项设置
     *  InputDate 日期
     *  UserId 用户ID
     *  ItemId 指标项ID
     *  recordId 记录ID-新增为-1，其他为列表的KEY
     *  DetailContent 提交内容-学生列表，
     *  BuildingOrDeptIds 宿舍楼ID列表或专业部ID列表
     *
     */
    @POST("SaveDormScoreNoNameOption")
    Observable<HttpResult<RecordIdBean>> SaveDormScoreNoNameHasOption(@Body OptionHttpBean optionHttpBean);

    /**
     * 班级维度-录入姓名-保存单项设置
     *  InputDate 日期
     *  UserId 用户ID
     *  ItemId 指标项ID
     *  recordId 记录ID-新增为-1，其他为列表的KEY
     *  DetailContent 提交内容-学生列表，
     *  BuildingOrDeptIds 宿舍楼ID列表或专业部ID列表
     *
     */
    @POST("SaveDormScoreNoNameNunOption")
    Observable<HttpResult<RecordIdBean>> SaveDormScoreNoNameNoOption(@Body OptionHttpBean optionHttpBean);

    /**
     * 班级维度-录入姓名-保存单项设置
     *  InputDate 日期
     *  UserId 用户ID
     *  ItemId 指标项ID
     *  recordId 记录ID-新增为-1，其他为列表的KEY
     *  DetailContent 提交内容-学生列表，
     *  BuildingOrDeptIds 宿舍楼ID列表或专业部ID列表
     *
     */
    @POST("SaveClassScoreNoNameOption")
    Observable<HttpResult<RecordIdBean>> SaveClassScoreNoNameHasOption(@Body OptionHttpBean optionHttpBean);

    /**
     * 班级维度-录入姓名-保存单项设置
     *  InputDate 日期
     *  UserId 用户ID
     *  ItemId 指标项ID
     *  recordId 记录ID-新增为-1，其他为列表的KEY
     *  DetailContent 提交内容-学生列表，
     *  BuildingOrDeptIds 宿舍楼ID列表或专业部ID列表
     *
     */
    @POST("SaveClassScoreNoNameNunOption")
    Observable<HttpResult<RecordIdBean>> SaveClassScoreNoNameNoOption(@Body OptionHttpBean optionHttpBean);

}
