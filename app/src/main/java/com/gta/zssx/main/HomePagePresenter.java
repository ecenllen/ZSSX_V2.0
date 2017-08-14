package com.gta.zssx.main;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.fun.personalcenter.model.bean.UpdateBean;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.update.DownloadDialog;
import com.gta.zssx.pub.update.UpdataInfo;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/8/25.
 * @since 1.0.0
 */
public class HomePagePresenter extends BasePresenter<HomePageView> {

    private UserBean mUserBean;

    HomePagePresenter () {
        mUserBean = ZSSXApplication.instance.getUser ();
    }

    void checkUpdate (final Context context) {

        if (!isViewAttached ())
            return;
        mCompositeSubscription.add (HomeDataManager.checkUpdate ()
                .subscribe (new Subscriber<UpdateBean> () {
                    @Override
                    public void onCompleted () {

                    }

                    @Override
                    public void onError (Throwable e) {

                    }

                    @Override
                    public void onNext (UpdateBean updateBean) {
                        UpdataInfo mUpdataInfo = new UpdataInfo ();
                        mUpdataInfo.setUrl (updateBean.getFilePath ());
                        mUpdataInfo.setServerVersion (updateBean.getVersionCode ());
                        try {
                            mUpdataInfo.setUpdateLevel (updateBean.getUpdateLevel ());
                            PackageInfo pinfo = context.getPackageManager ().getPackageInfo (context.getPackageName ()
                                    , PackageManager.GET_CONFIGURATIONS);
                            String versionCode = String.valueOf (pinfo.versionCode);
                            mUpdataInfo.setCurrentVersion (versionCode);

                            if (Integer.parseInt (mUpdataInfo.getServerVersion ()) > Integer.parseInt (versionCode)) {
                                mUpdataInfo.setDescription (updateBean.getUpdateMessage ());
                                mUpdataInfo.setVersionName (updateBean.getVersionName ());
                                DownloadDialog.getInstance (context, mUpdataInfo).isUpdate ();
                            } else {
                                //                                无版本更新
                                //                                com.gta.utils.resource.Toast.Short(context, "暂无版本更新");
                            }

                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace ();
                        }
                    }
                }));
    }

    /**
     * "B00",课堂日志
     * "E00200",课堂巡视
     * "200",OA协同
     * "0106",调代课
     * "ZC",资产
     * "0107",课表查询
     * "406402",宿舍
     * "E00300"课堂教学反馈
     */
    private String[] itemList = new String[]{"B00", "E00200", "200", "0106", "ZC", "0107", "406402", "E00300"};

    /**
     * 根据登录接口获取有权限的模块集合
     *
     * @return List<UserBean.MenuItemListBean>
     */
    List<UserBean.MenuItemListBean> getPageList () {
        List<UserBean.MenuItemListBean> itemListBeanList = mUserBean.getMenuItemList ();
        List<UserBean.MenuItemListBean> beanList = new ArrayList<> ();
        if(itemListBeanList != null) {
            for (String s : itemList) {
                for (int j = 0; j < itemListBeanList.size (); j++) {
                    UserBean.MenuItemListBean bean = itemListBeanList.get (j);
                    if (bean.getPowerCode ().equals (s)) {
                        beanList.add (bean);
                        break;
                    }
                }
            }
        }
        return beanList;
    }

    /**
     * 根据首页有权限的模块获取 AddPageActivity中的小图标集合
     *
     * @param listBeen 首页有权限模块集合
     * @return 小图标集合
     */
    List<UserBean.MenuItemListBean> getSmallIconsList (List<UserBean.MenuItemListBean> listBeen) {
        List<UserBean.MenuItemListBean> newList = new ArrayList<> ();
        for (int i = 0; i < listBeen.size (); i++) {
            UserBean.MenuItemListBean menuItemListBean = listBeen.get (i);
            String powerCode = menuItemListBean.getPowerCode ();
            switch (powerCode) {
                case "B00":
                    UserBean.MenuItemListBean journalBean = new UserBean.MenuItemListBean ();
                    journalBean.setPowerName ("日志登记");
                    newList.add (journalBean);
                    break;
                case "E00200":
                    UserBean.MenuItemListBean patrolBeanOne = new UserBean.MenuItemListBean ();
                    UserBean.MenuItemListBean patrolBeanTwo = new UserBean.MenuItemListBean ();
                    patrolBeanOne.setPowerName ("随机巡课");
                    patrolBeanTwo.setPowerName ("按计划巡课");
                    newList.add (patrolBeanOne);
                    newList.add (patrolBeanTwo);
                    break;
                case "0106":
                    UserBean.MenuItemListBean classBean = new UserBean.MenuItemListBean ();
                    classBean.setPowerName ("调代课申请");
                    newList.add (classBean);
                    break;
                case "ZC":
                    if (mUserBean.isSetAssetAdministrator ()) {
                        UserBean.MenuItemListBean AssetsBeanOne = new UserBean.MenuItemListBean ();
                        AssetsBeanOne.setPowerName ("资产分配");
                        newList.add (AssetsBeanOne);
                    }
                    UserBean.MenuItemListBean AssetsBeanTwo = new UserBean.MenuItemListBean ();
                    UserBean.MenuItemListBean AssetsBeanThree = new UserBean.MenuItemListBean ();
                    UserBean.MenuItemListBean AssetsBeanFour = new UserBean.MenuItemListBean ();
                    AssetsBeanTwo.setPowerName ("资产转移");
                    AssetsBeanThree.setPowerName ("资产维修");
                    AssetsBeanFour.setPowerName ("资产报废");
                    newList.add (AssetsBeanTwo);
                    newList.add (AssetsBeanThree);
                    newList.add (AssetsBeanFour);
                    break;
            }

        }
        return newList;
    }

}
