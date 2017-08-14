package com.gta.zssx.patrolclass.presenter;

import android.util.SparseIntArray;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.patrolclass.model.AddClassChooseModelImpl;
import com.gta.zssx.patrolclass.model.BaseDataBridge;
import com.gta.zssx.patrolclass.model.BaseModel;
import com.gta.zssx.patrolclass.model.entity.AddClassEntity;
import com.gta.zssx.patrolclass.model.entity.ClassChooseEntity;
import com.gta.zssx.patrolclass.view.AddClassChooseView;
import com.gta.zssx.pub.exception.PatrolClassException;
import com.gta.zssx.pub.widget.easysidebar.EasySection;

import java.util.ArrayList;
import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by tengfei.lv on 2016/6/15.
 * @since 1.0.0
 */
public class AddClassChoosePresenter extends BasePresenter<AddClassChooseView> implements
        BaseDataBridge.AddClassChooseBridge {

    /**
     * 记录右边栏的数据集合
     */
    private List<EasySection> mEasySections;

    /**
     * 记录右边栏的数据在recyclerview中对应的位置
     */
    private SparseIntArray mIndexIntArray;
    private List<ClassChooseEntity.ClassListBean> classListBeen;

    private BaseModel.AddClassChooseModel addClassChooseModel;
    String uId;

    public void loadClass () {
        if (!isViewAttached ())
            return;
        UserBean lUserBean = null;
        try {
            lUserBean = AppConfiguration.getInstance ().getUserBean ();
            uId = lUserBean.getUserId ();

        } catch (Exception e) {
            e.printStackTrace ();
        }
        addClassChooseModel = new AddClassChooseModelImpl ();

        getView ().showLoadingDialog (false);
        mCompositeSubscription.add (addClassChooseModel.loadData (this));
    }

    public List<EasySection> getEasySections () {
        return mEasySections;
    }

    public SparseIntArray getSparseIntArray () {
        return mIndexIntArray;
    }

    /**
     * 判断是否选择了班级
     *
     * @return
     */
    public boolean isCanFinish () {
        for (ClassChooseEntity.ClassListBean bean : classListBeen) {
            if (bean.getType () == 1 && bean.isCheck ()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onNext (List<ClassChooseEntity.ClassListBean> classListBeen) {
        this.classListBeen = classListBeen;
        if (classListBeen == null || classListBeen.size () == 0) {
            getView ().showEmpty ();
        } else {
            getView ().showSuccessDatas (classListBeen);
        }
    }

    @Override
    public void setEasySections (List<EasySection> mEasySections) {
        this.mEasySections = mEasySections;
    }

    @Override
    public void setIndexIntArray (SparseIntArray mIndexIntArray) {
        this.mIndexIntArray = mIndexIntArray;
    }

    @Override
    public void getOnAddClassNext (String s) {
        getView ().intoClassesPage ();
    }

    @Override
    public void onAddClassError (Throwable e) {
        getView ().hideDialog ();
        if (e instanceof PatrolClassException) {
            PatrolClassException exception = (PatrolClassException) e;
            int code = exception.getCode ();
            switch (code) {
                case PatrolClassException.HAS_CLASS_ADD:
                    getView ().showError (PatrolClassException.HAS_CLASS_ADD_STRING);
                    break;
                case PatrolClassException.HAS_CLASS_ADD_ALL:
                    getView ().showError (PatrolClassException.HAS_CLASS_ADD_ALL_STRING);
                    break;
                default:
                    break;
            }
        } else {
            getView ().onErrorHandle (e);
        }
    }

    @Override
    public void onError (Throwable e) {
        getView ().hideDialog ();
        getView ().onErrorHandle (e);

        //        getView ().showWarning (e.getMessage ());
    }

    @Override
    public void onCompleted () {
        getView ().hideDialog ();
    }

    /**
     * 上传班级已选班级列表数据
     */
    public void uploadChooseClass () {

        getView ().showLoadingDialog (false);

        mCompositeSubscription.add (addClassChooseModel.upLoadPlanClassList (this));
    }

    /**
     * 获取已选择的班级列表
     *
     * @return
     */
    @Override
    public List<AddClassEntity> getEntity () {
        List<AddClassEntity> classEntityLists = new ArrayList<> ();
        for (int i = classListBeen.size () - 1; i >= 0; i--) {
            ClassChooseEntity.ClassListBean bean = classListBeen.get (i);
            if (bean.getType () == 1 && bean.isCheck ()) {
                AddClassEntity addClassEntity = new AddClassEntity ();
                addClassEntity.setClassID (bean.getId ());
                addClassEntity.setTeacherID (uId);
                classEntityLists.add (addClassEntity);
            }
        }
        return classEntityLists;
    }

}
