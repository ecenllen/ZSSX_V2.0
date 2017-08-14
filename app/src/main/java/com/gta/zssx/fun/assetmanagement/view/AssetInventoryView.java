package com.gta.zssx.fun.assetmanagement.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.assetmanagement.model.bean.AssetBean;

/**
 * Created by liang.lu on 2016/11/10 17:45.
 */

public interface AssetInventoryView extends BaseView {
    void showResult (AssetBean assetBean);

    void continuePreview ();

    void hideDismissDialog ();

    void showProgressDialog ();

    void hideProgressDialog ();

    void changeMessage (String message);

    void onSuccessTicket(String ticket);
    
}
