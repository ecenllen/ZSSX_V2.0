package com.gta.zssx.fun.coursedaily.registercourse.presenter;

import android.content.Context;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.view.SectionChooseView;
import com.gta.zssx.pub.exception.CustomException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import rx.Observable;
import rx.Subscriber;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/18.
 * @since 1.0.0
 */
public class SectionChoosePresenter extends BasePresenter<SectionChooseView> {
    public Context mContext;
    public List<SectionBean> mSectionBeen;

    public SectionChoosePresenter(Context context){
        mContext = context;
    }

    public void getSectionList(String teacherId, int classId, String signData) {

        if (!isViewAttached())
            return;

        getView().showLoadingDialog();
        mCompositeSubscription.add(ClassDataManager.getClassStatus(teacherId, classId, signData)

                //排序
                .flatMap(sectionBeen -> {
                    List<SectionBean> lSectionBeen = new ArrayList<>();
                    List<SectionBean> lSectionBeen01 = new ArrayList<>();
                    for (int i = 0; i < sectionBeen.size(); i++) {
                        if (sectionBeen.get(i).getSignStatus() == 0) {
                            lSectionBeen.add(sectionBeen.get(i));
//                                sectionBeen.remove(sectionBeen.get(i));
                        } else {
                            lSectionBeen01.add(sectionBeen.get(i));
                        }
                    }
                    lSectionBeen.addAll(lSectionBeen01);
                    return Observable.just(lSectionBeen);
                })
                .subscribe(new Subscriber<List<SectionBean>>() {
                    @Override
                    public void onCompleted() {
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() == null)
                            return;
                        if (e instanceof CustomException) {
                            CustomException lErrorCodeException = (CustomException) e;
                            getView().showWarning(lErrorCodeException.getMessage());
                        }

                        getView().hideDialog();
                    }

                    @Override
                    public void onNext(List<SectionBean> sectionBeen) {
                        mSectionBeen = sectionBeen;
                        getView().showResult(sectionBeen);
                    }
                }));

    }

    /**
     * 使修改的节次变成未登记
     * @param sectionId 节次id
     * @return
     */
    public List<SectionBean> makeModifyUnSign(int sectionId) {

        //把当前节次变为未登记
        List<SectionBean> lSectionBeen = getSectionBeen();
        for (int i = 0; i < lSectionBeen.size(); i++) {
            if (lSectionBeen.get(i).getSectionId() == sectionId) {
                lSectionBeen.get(i).setSignStatus(SectionBean.UNSIGN);
            }
        }

        //排序
        Collections.sort(lSectionBeen, (sectionBean, t1) -> {
            if (sectionBean.getSectionId() > t1.getSectionId()) {
                return 1;
            } else {
                return -1;
            }
        });

        //把未登记和已登记分开
        List<SectionBean> lSectionBeen02 = new ArrayList<>();
        List<SectionBean> lSectionBeen01 = new ArrayList<>();
        for (int i = 0; i < lSectionBeen.size(); i++) {
            if (lSectionBeen.get(i).getSignStatus() == 0) {
                lSectionBeen02.add(lSectionBeen.get(i));
            } else {
                lSectionBeen01.add(lSectionBeen.get(i));
            }
        }
        lSectionBeen02.addAll(lSectionBeen01);


        return lSectionBeen02;
    }


    public List<SectionBean> getSectionBeen() {
        return mSectionBeen;
    }

    /**
     * 转回所需要的节次数据Set
     * @param sectionBeanSet
     * @return
     */
    /*public Set<SectionBean> sortSetData(Set<SectionBean> sectionBeanSet){
        //排序算法
        List<SectionBean> sectionBeanList = new ArrayList<>(sectionBeanSet);
        Set<SectionBean> sectionBeanHashSet = new HashSet<>();
        sectionBeanHashSet.addAll(sectionBeanList);
        return sectionBeanHashSet;
    }*/

    /**
     * 排序选择好的节次-Fragment
     * @param sectionBeanSet
     * @return
     */
    public Set<SectionBean> sortSet(Set<SectionBean> sectionBeanSet) {
        Set<SectionBean> lSectionBeanSet = new TreeSet<SectionBean>((lhs, rhs) -> {
            if (lhs.getSectionId() > rhs.getSectionId()) {
                return 1;
            } else {
                return -1;
            }
        });
        for (SectionBean section :
                sectionBeanSet) {
            lSectionBeanSet.add(section);
        }

        return lSectionBeanSet;

    }

}
