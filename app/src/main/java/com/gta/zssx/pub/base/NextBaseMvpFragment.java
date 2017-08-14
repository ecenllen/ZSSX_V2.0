package com.gta.zssx.pub.base;

import android.support.annotation.NonNull;
import android.view.View;

import com.gta.utils.mvp.*;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ui.IsDeleteSelectItemDialog;

/**
 * 封装上一宿舍/下一宿舍里面嵌套的Fragment
 */

public abstract class NextBaseMvpFragment<V extends BaseView, P extends BasePresenter<V>> extends BaseMvpFragment<V, P> {
    public static final String KEY_ADDITION_OR_SUBTRACTION = "additionOrSubtraction"; // 增分或扣分
    public static final String KEY_DORMITORY_OR_CLASS = "dormitoryOrClass"; // 宿舍维度或班级维度
    public static final String KEY_IS_CAN_MODIFY = "actionType"; // 是否能修改
    public static final String KEY_IS_HAS_OPTION = "optionType"; // 是否有选项设置

    /** 1 增分， 2 扣分*/
    public int isAdditionOrSubtraction;
    /** 是否宿舍维度，否则班级维度*/
    public int dimensionType;
    /** 1 有选项， 2 无选项*/
    public int optionType;
    /**  1 新增， 2 修改 ，3 查看*/
    public int actionType;

    /**
     * 是否修改过内容，此值改变的时候，同时需要设置setSaveButtonClickable(isChanged);
     */
    public boolean isChanged;

    /**
     * 是点击了上一宿舍/下一宿舍，还是保存
     */
    public int witchBottomActionClick;  //必要参数,点击了底部的哪个按钮

    /**
     * 点击上下宿舍切换时，数据是否有修改？
     * @return true 为数据有修改，弹窗提示是否保存
     */
    public abstract boolean isDataChanged();

    /**
     * 向服务器保存当前页面数据,isClickPreviousOrNext用于判断是点击保存还是上一宿舍和下一宿舍，用于currentPosition改变
     */
    public abstract void saveCurrentPageData();

    @Override
    public void requestData() {
        super.requestData();
    }
}
