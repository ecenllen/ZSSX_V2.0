package com.gta.zssx.fun.assetmanagement.model;

import com.gta.zssx.fun.assetmanagement.model.bean.ApprovalDetailBean;
import com.gta.zssx.fun.assetmanagement.model.bean.AssetBean;
import com.gta.zssx.fun.assetmanagement.model.bean.BatchAddNotesBean;
import com.gta.zssx.fun.assetmanagement.model.bean.ListBean;
import com.gta.zssx.fun.assetmanagement.model.bean.UpLoadRemarksAssetBean;
import com.gta.zssx.fun.assetmanagement.view.base.AssetManagement;
import com.gta.zssx.pub.AssetInterfaceList;
import com.gta.zssx.pub.http.HttpMethod;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by lan.zheng on 2016/8/5.
 */
public class AssetManagerInterface {
    private static DataCache sDataCache = new DataCache ();
    public static List<ListBean.dataEntry> mWeatherBeen;

    public static DataCache getDataCache () {
        return sDataCache;
    }

    public static void destroyDataCache () {
        sDataCache = null;
    }

    public static class DataCache {

        private void setWeatherBeans (List<ListBean.dataEntry> weatherBeanList) {

            mWeatherBeen = weatherBeanList;
        }
    }

    private static AssetInterfaceList getAssetInterfaceList () {
        String lUrl = AssetManagement.getInstance ().getmServerAddress ();
        return HttpMethod
                .getInstance ()
                .retrofitClient (lUrl)
                .create (AssetInterfaceList.class);
    }

    /**
     * 获取资产信息
     *
     * @param Id
     * @return
     */
    public static Observable<List<AssetBean>> getAssetData (int Id, int checkId) {
        AssetInterfaceList interfaceList = getAssetInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.GetAssetByAssetId (Id, checkId));
    }

    /**
     * 上传资产图片
     *
     * @param map
     * @return
     */
    public static Observable<String> mobileUploadAssetFile (Map<String, RequestBody> map) {
        AssetInterfaceList interfaceList = getAssetInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.mobileUploadAssetFile (map));
    }

    /**
     * 上传盘点的资产信息
     *
     * @param upLoadRemarksAssetBean
     * @return
     */
    public static Observable<String> upLoadRemarksAsset (UpLoadRemarksAssetBean upLoadRemarksAssetBean) {
        AssetInterfaceList interfaceList = getAssetInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.upLoadRemarksAsset (upLoadRemarksAssetBean));
    }

    /**
     * 批量添加备注
     *
     * @param batchAddNotesBean
     * @return
     */
    public static Observable<String> batchAddNotes (BatchAddNotesBean batchAddNotesBean) {
        AssetInterfaceList interfaceList = getAssetInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.batchAddNotes (batchAddNotesBean));
    }

    /**
     * 获取审批详情数据
     *
     * @param approvalId 审批申请单ID
     * @return ApprovalDetailBean
     */
    public static Observable<ApprovalDetailBean> getApprovalDetailInfo(int approvalId) {
        AssetInterfaceList interfaceList = getAssetInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.approvalDetail (approvalId));
    }
}
