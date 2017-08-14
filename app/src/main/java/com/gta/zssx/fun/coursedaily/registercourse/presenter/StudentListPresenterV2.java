package com.gta.zssx.fun.coursedaily.registercourse.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListNewBean;
import com.gta.zssx.fun.coursedaily.registercourse.view.StudentListViewV2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import rx.Subscription;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/24.
 * @since 1.0.0
 */
@Deprecated
public class StudentListPresenterV2 extends BasePresenter<StudentListViewV2> {
    public List<StudentListNewBean> mStudentListBeen;
    public Subscription mSubscription;

    public void getStudentList(int classId, String signData) {

        if (!isViewAttached())
            return;

        getView().showLoadingDialog();
        // TODO: 2016/6/22 死数据
        //70, "2016-05-05"
        mSubscription = ClassDataManager.getStudentList(classId, signData)
                .subscribe(studentListBeen -> {
                    getView().hideDialog();
                    mStudentListBeen = studentListBeen;
                    getView().showResult(studentListBeen);
                }, throwable -> {
                    if (getView() == null)
                        return;
                    getView().onErrorHandle(throwable);
                    getView().hideDialog();
                    getView().emptyUI();
                });

        mCompositeSubscription.add(mSubscription);

    }

    public List<StudentListNewBean> getStudentListBeen() {
        return mStudentListBeen;
    }

    public List<StudentListNewBean> setStudentDefaultState(List<StudentListNewBean> studentListBeen, Set<SectionBean> sectionBeen) {
        if (sectionBeen != null) {
            List<SectionBean> lSectionBeen = new ArrayList<>(sectionBeen);
            for (int i = 0; i < studentListBeen.size(); i++) {
                StudentListNewBean lStudentListBean = studentListBeen.get(i);
                for (int j = 0; j < lSectionBeen.size(); j++) {
                    int lSectionID = lSectionBeen.get(j).getSectionId();
                    /*switch (lSectionID) {
                        case 1:
                            if (lStudentListBean.getLESSON1() == 0) {
                                lStudentListBean.setLESSON1(1);
                            }
                            break;
                        case 2:
                            if (lStudentListBean.getLESSON2() == 0) {
                                lStudentListBean.setLESSON2(1);
                            }
                            break;
                        case 3:
                            if (lStudentListBean.getLESSON3() == 0) {
                                lStudentListBean.setLESSON3(1);
                            }
                            break;
                        case 4:
                            if (lStudentListBean.getLESSON4() == 0) {
                                lStudentListBean.setLESSON4(1);
                            }
                            break;
                        case 5:
                            if (lStudentListBean.getLESSON5() == 0) {
                                lStudentListBean.setLESSON5(1);
                            }
                            break;
                        case 6:
                            if (lStudentListBean.getLESSON6() == 0) {
                                lStudentListBean.setLESSON6(1);
                            }
                            break;
                        case 7:
                            if (lStudentListBean.getLESSON7() == 0) {
                                lStudentListBean.setLESSON7(1);
                            }
                            break;
                        default:
                            break;
                    }*/
                }
            }
        }
        return studentListBeen;
    }

    /**
     * 排序选择好的节次
     *
     * @param sectionBeanSet
     * @return
     */
    public Set<SectionBean> sortSet(Set<SectionBean> sectionBeanSet) {
        Set<SectionBean> lSectionBeanSet = new TreeSet<>((lhs, rhs) -> {
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
