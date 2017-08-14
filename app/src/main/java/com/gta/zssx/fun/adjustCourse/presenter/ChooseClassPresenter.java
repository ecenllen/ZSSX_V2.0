package com.gta.zssx.fun.adjustCourse.presenter;

import android.content.Context;
import android.util.SparseIntArray;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.adjustCourse.model.AdjustDataManager;
import com.gta.zssx.fun.adjustCourse.model.bean.StuClassBean;
import com.gta.zssx.fun.adjustCourse.view.ChooseClassView;
import com.gta.zssx.pub.widget.easysidebar.EasySection;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/11/3.
 * @since 1.0.0
 */
public class ChooseClassPresenter extends BasePresenter<ChooseClassView> {
    private Context mContext;

    public SparseIntArray mSparseIntArray;
    public List<EasySection> mEasySections;
    public List<StuClassBean.ClassListBean> mClassListBeen;

    public ChooseClassPresenter(Context context) {
        mContext = context;
    }

    public void getAllClass() {
        if (getView() == null)
            return;
        getView().showLoadingDialog();
        mCompositeSubscription.add(AdjustDataManager.getAllClass()
                .flatMap(new Func1<List<StuClassBean>, Observable<List<StuClassBean>>>() {
                    @Override
                    public Observable<List<StuClassBean>> call(List<StuClassBean> stuClassBeen) {
                        List<StuClassBean> lStuClassBeen = new ArrayList<>();
                        for (int i = 0; i < stuClassBeen.size(); i++) {
                            StuClassBean lStuClassBean = stuClassBeen.get(i);
                            if (lStuClassBean.getClassList() != null && lStuClassBean.getClassList().size() > 0) {
                                lStuClassBeen.add(lStuClassBean);
                            }
                        }
                        return Observable.just(lStuClassBeen);
                    }
                })
                .flatMap(new Func1<List<StuClassBean>, Observable<List<StuClassBean>>>() {
                    @Override
                    public Observable<List<StuClassBean>> call(List<StuClassBean> stuClassBeen) {

                        for (int i = 0; i < stuClassBeen.size(); i++) {
                            List<StuClassBean.ClassListBean> lClassListBeen = new ArrayList<>();
                            List<StuClassBean.ClassListBean> lClassList = stuClassBeen.get(i).getClassList();
                            for (int j = 0; j < lClassList.size(); j++) {
                                StuClassBean.ClassListBean lClassListBean = lClassList.get(j);
                                if (lClassListBean.getClassName() != null && !lClassListBean.getClassName().isEmpty()) {
                                    lClassListBeen.add(lClassListBean);
                                }
                            }
                            stuClassBeen.get(i).setClassList(lClassListBeen);
                        }
                        return Observable.just(stuClassBeen);
                    }
                })
                .doOnNext(new Action1<List<StuClassBean>>() {
                    @Override
                    public void call(List<StuClassBean> stuClassBeen) {
                        mEasySections = new ArrayList<>();
                        mSparseIntArray = new SparseIntArray();

                        List<StuClassBean.ClassListBean> lClassListBeen = new ArrayList<>();
                        for (int i = 0; i < stuClassBeen.size(); i++) {
                            char lC = stuClassBeen.get(i).getValue().charAt(0);
                            String lDeptCode = String.valueOf(lC);
                            int lSize = lClassListBeen.size();
                            List<StuClassBean.ClassListBean> lClassList = stuClassBeen.get(i).getClassList();
                            //过滤班级数量为空的部门
//                            if (lClassList != null && lClassList.size() > 0) {
//
//                            }
                            lClassListBeen.addAll(lClassList);
                            mEasySections.add(new EasySection(lDeptCode));
                            mSparseIntArray.put(i, lSize);
                        }
                    }
                })
                .flatMap(new Func1<List<StuClassBean>, Observable<List<StuClassBean.ClassListBean>>>() {
                    @Override
                    public Observable<List<StuClassBean.ClassListBean>> call(List<StuClassBean> stuClassBeen) {
                        mClassListBeen = new ArrayList<>();
                        if (stuClassBeen != null) {
                            for (int i = 0; i < stuClassBeen.size(); i++) {
                                mClassListBeen.addAll(stuClassBeen.get(i).getClassList());
                            }

                            return Observable.just(mClassListBeen);
                        }

                        return Observable.just(null);
                    }
                })
                .subscribe(new Subscriber<List<StuClassBean.ClassListBean>>() {
                    @Override
                    public void onCompleted() {
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().hideDialog();
                            getView().onErrorHandle(e);
                        } else {

                        }
                    }

                    @Override
                    public void onNext(List<StuClassBean.ClassListBean> stuClassBean) {
                        getView().showResult(stuClassBean);
                    }
                }));
    }

    public List<EasySection> getEasySections() {
        return mEasySections;
    }

    //用于记录index中选择的对应recycleview的位置
    public SparseIntArray getSparseIntArray() {
        return mSparseIntArray;
    }
}
