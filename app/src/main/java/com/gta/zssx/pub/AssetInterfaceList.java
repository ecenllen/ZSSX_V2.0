package com.gta.zssx.pub;

import com.gta.zssx.fun.assetmanagement.model.bean.ApprovalDetailBean;
import com.gta.zssx.fun.assetmanagement.model.bean.AssetBean;
import com.gta.zssx.fun.assetmanagement.model.bean.BatchAddNotesBean;
import com.gta.zssx.fun.assetmanagement.model.bean.UpLoadRemarksAssetBean;
import com.gta.zssx.pub.http.HttpResult;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by liang.lu on 2016/11/10 16:42.
 */

public interface AssetInterfaceList {

    /**
     * 获取资产数据
     *
     * @param Id
     * @return
     */
    @GET (AssetInterfaceName.GET_ASSET_BY_ID)
    Observable<HttpResult<List<AssetBean>>> GetAssetByAssetId (@Query ("assetId") int Id, @Query ("checkId") int checkId);

    /**
     * 上传头像
     *
     * @param map 文件对象
     * @return
     */
    @Multipart
    @POST (AssetInterfaceName.MOBILE_UP_LOAD_ASSET_FILE)
    Observable<HttpResult<String>> mobileUploadAssetFile (@PartMap Map<String, RequestBody> map);

    /**
     * 资产盘点上传的数据
     *
     * @param upLoadRemarksAssetBean
     * @return
     */
    @POST (AssetInterfaceName.UP_LOAD_REMARKS_ASSET)
    Observable<HttpResult<String>> upLoadRemarksAsset (@Body UpLoadRemarksAssetBean upLoadRemarksAssetBean);

    /**
     * 批量添加备注
     *
     * @param batchAddNotesBean
     * @return String
     */
    @POST (AssetInterfaceName.BATCH_ADD_NOTES)
    Observable<HttpResult<String>> batchAddNotes (@Body BatchAddNotesBean batchAddNotesBean);

    /**
     * 获取审批详情数据
     *
     * @param approvalId
     * @return 
     */
    @GET (AssetInterfaceName.APPROVAL_DETAIL)
    Observable<HttpResult<ApprovalDetailBean>> approvalDetail (@Query ("approvalId") int approvalId);
}
