package com.gta.zssx.fun.coursedaily.registercourse.presenter;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseIntArray;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.AddClassBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ClassChooseBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ClassChooseBean01;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SortClassBean;
import com.gta.zssx.fun.coursedaily.registercourse.view.ClassChooseView;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.exception.CustomException;
import com.gta.zssx.pub.widget.easysidebar.EasySection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/15.
 * @since 1.0.0
 */
public class ClassChoosePresenter extends BasePresenter<ClassChooseView> {

    public ClassChooseBean mClassChooseBean;
    public Subscription mSubscription;
    public SparseIntArray mSparseIntArray;
    public List<EasySection> mEasySections;
    public List<SortClassBean.ClassByYear> mClassByYears;
    public List<ClassChooseBean01.ClassListBean> mClassListBeen;
    public SparseIntArray mSparseHeaderPosition;

    public List<SortClassBean> getSortClassBeen() {
        return mSortClassBeen;
    }

    //用于记录index中选择的对应recycleview的位置
    public SparseIntArray getSparseIntArray() {
        return mSparseIntArray;
    }

    //用于adapter的数据
    public List<SortClassBean.ClassByYear> getClassByYears() {
        return mClassByYears;
    }

    //index显示的数据列表
    public List<EasySection> getEasySections() {
        return mEasySections;
    }

    public List<SortClassBean> mSortClassBeen;


    /**
     * 请求班级
     *
     * @param
     */
    @Deprecated
    public void loadClass() {
        if (!isViewAttached()) {
            return;
        }

        getView().showLoadingDialog();
        mSubscription = ClassDataManager.loadClass()
                .doOnNext(new Action1<ClassChooseBean>() {
                    @Override
                    public void call(ClassChooseBean classChooseBean) {
                        mClassChooseBean = classChooseBean;
                    }
                })
                .flatMap(new Func1<ClassChooseBean, Observable<List<SortClassBean>>>() {
                    @Override
                    public Observable<List<SortClassBean>> call(ClassChooseBean classChooseBean) {
                        List<SortClassBean> lSortClassBeans = getSortClassBeen(classChooseBean);
                        return Observable.just(lSortClassBeans);
                    }
                })
                .doOnNext(new Action1<List<SortClassBean>>() {
                    @Override
                    public void call(List<SortClassBean> sortClassBeen) {
                        sortData(sortClassBeen);
                    }
                })
                .subscribe(new Subscriber<List<SortClassBean>>() {
                    @Override
                    public void onCompleted() {
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (e instanceof CustomException) {
                            CustomException lErrorCodeException = (CustomException) e;
                            getView().showWarning(lErrorCodeException.getMessage());
                        }

                        getView().hideDialog();

                    }

                    @Override
                    public void onNext(List<SortClassBean> sortClassBeen) {

                        mSortClassBeen = sortClassBeen;
                    }
                });
        mCompositeSubscription.add(mSubscription);
    }

    /**
     * 记录右边栏
     *
     * @param sortClassBeen
     */
    private void sortData(List<SortClassBean> sortClassBeen) {
        mClassByYears = new ArrayList<>();
        mEasySections = new ArrayList<>();
        mSparseIntArray = new SparseIntArray();
        for (int i = 0; i < sortClassBeen.size(); i++) {
            int lSize = mClassByYears.size();
            mClassByYears.addAll(sortClassBeen.get(i).getClassByYears());
            String lYear = sortClassBeen.get(i).getYear();
            if (lYear.length() >= 4) {
                lYear = lYear.substring(2, 4);
            }
            mEasySections.add(new EasySection(lYear));
            mSparseIntArray.put(i, lSize);
        }
    }


    /**
     * 对所有班级按年级分类
     *
     * @param classChooseBean
     * @return
     */
    @NonNull
    private List<SortClassBean> getSortClassBeen(ClassChooseBean classChooseBean) {
        List<ClassChooseBean.GradeBean> lGradeBean = classChooseBean.getGradeBean();
        List<ClassChooseBean.ClassBean> lClassBean = classChooseBean.getClassBean();
        List<SortClassBean> lSortClassBeans = new ArrayList<>();
        SortClassBean lSortClassBean;
        for (int i = 0; i < lGradeBean.size(); i++) {
            lSortClassBean = new SortClassBean();
            ClassChooseBean.GradeBean lGradeBean1 = lGradeBean.get(i);
            lSortClassBean.setGradeCode(lGradeBean1.getGradeCode());
            lSortClassBean.setGradeId(lGradeBean1.getGradeId());
            lSortClassBean.setGradeName(lGradeBean1.getGradeName());
            lSortClassBean.setYear(lGradeBean1.getYear());
            List<SortClassBean.ClassByYear> lClassByYears = new ArrayList<>();
            SortClassBean.ClassByYear lClassByYear;
            for (int j = 0; j < lClassBean.size(); j++) {
                ClassChooseBean.ClassBean lClassBean1 = lClassBean.get(j);
                if (lGradeBean1.getGradeId().equals(lClassBean1.getGrade())) {
                    lClassByYear = new SortClassBean.ClassByYear();
                    lClassByYear.setClassName(lClassBean1.getClassName());
                    lClassByYear.setGrade(lClassBean1.getGrade());
                    lClassByYear.setId(lClassBean1.getId());
                    lClassByYear.setYear(lGradeBean1.getYear());
                    lClassByYears.add(lClassByYear);
                }
            }
            lSortClassBean.setClassByYears(lClassByYears);
            lSortClassBeans.add(lSortClassBean);
        }
        return lSortClassBeans;
    }


    /**
     * 上传班级
     *
     * @param addClassBeen
     * @param
     */
    public void addClass(List<AddClassBean> addClassBeen) {

        if (!isViewAttached()) {
            return;
        }

        if (getView() != null) {
            getView().showLoadingDialog();
        }

        mCompositeSubscription.add(ClassDataManager.addClass(addClassBeen)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().hideDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() == null)
                            return;
                        getView().onErrorHandle(e);
                        getView().hideDialog();
                    }

                    @Override
                    public void onNext(String s) {
                        if (isViewAttached()) {
                            getView().addSuccess();
                        }
                    }
                }));
    }


    public ClassChooseBean getClassChooseBean() {
        return mClassChooseBean;
    }


    /**
     * 排序选择好的班级对象
     *
     * @param classByYearRecords
     * @return
     */
    public List<ClassChooseBean01.ClassListBean> sortSet(Set<ClassChooseBean01.ClassListBean> classByYearRecords) {

        return new ArrayList<>(classByYearRecords);
    }


    /**
     * 生成上传班级bean对象
     *
     * @param classByYears
     * @return
     */
    public List<AddClassBean> getAddClassBean(List<ClassChooseBean01.ClassListBean> classByYears) {
        UserBean lUserBean = null;
        try {
            lUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.sort(classByYears, (classListBean, t1) -> {
            if (classListBean.getSortId() < t1.getSortId()) {
                return 1;
            } else {
                return -1;
            }
        });

        List<AddClassBean> lAddClassBeen = new ArrayList<>();
        for (ClassChooseBean01.ClassListBean s :
                classByYears) {
            AddClassBean lAddClassBean = new AddClassBean();
            lAddClassBean.setClassID(s.getId());

            lAddClassBean.setTeacherID(lUserBean.getUserId());
            lAddClassBeen.add(lAddClassBean);
        }
        return lAddClassBeen;
    }

    /**
     * 请求班级
     *
     * @param
     */
    public boolean isAllClassHaveSelect;
    public void loadClass01(Map<Integer,String> classMap) {
        if (!isViewAttached()) {
            return;
        }
        //TODO 因为presenter中进行了班级过滤，有可能全部班级都被添加过了，因此加入此标记
        isAllClassHaveSelect = true;
        getView().showLoadingDialog();
        mCompositeSubscription.add(ClassDataManager.loadClass01()
                .doOnNext(classChooseBean01s -> {
                    if (classChooseBean01s != null) {
                        mEasySections = new ArrayList<>();
                        mSparseIntArray = new SparseIntArray();
                        mSparseHeaderPosition = new SparseIntArray();
                        List<ClassChooseBean01.ClassListBean> lClassListBeen = new ArrayList<>();
                        for (int i = 0; i < classChooseBean01s.size(); i++) {
                            String lDeptCode = classChooseBean01s.get(i).getDeptCode();
                            int lSize = lClassListBeen.size();
                            List<ClassChooseBean01.ClassListBean> lClassList = classChooseBean01s.get(i).getClassList();
                            int lClassListSize = lClassList.size();
                            Log.e("lenita","classMap.size = "+classMap.size()+",lDeptCode = "+lDeptCode+"，lClassListSize = "+lClassListSize);
                            //TODO 当主页上有班级显示时，需要进行过滤已经添加过的班级
                            List<ClassChooseBean01.ClassListBean> HaveFilterClass = new ArrayList<>();
                            if(lClassList != null && lClassList.size() >0 ){
                                for(int j =0; j<lClassList.size();j++){
                                    int key = lClassList.get(j).getId();
                                    if(!classMap.containsKey(key)){
                                        //还是要显示的列表
                                        Log.e("lenita","lClassList.get(j).getId() = "+key);
                                        HaveFilterClass.add(lClassList.get(j));
                                    }
                                }
                                lClassList.clear();
                                lClassList.addAll(HaveFilterClass);
                            }
                            //过滤班级数量为空的部门
                            if (lClassList.size() > 0) {
                                lClassListBeen.addAll(lClassList);
                                mEasySections.add(new EasySection(lDeptCode));
                                mSparseIntArray.put(i, lSize);
                            } else {
                                mSparseIntArray.put(i, lSize);
                            }
                        }
                        //TODO 因为presenter中进行了班级过滤，有可能全部班级都被添加过了，这里做判断
                        for(int i= 0;i <classChooseBean01s.size();i++){
                            if(classChooseBean01s.get(i).getClassList().size() > 0 ){
                                isAllClassHaveSelect = false;
                            }
                        }
                    }else {
                        isAllClassHaveSelect = false;
                    }

                })
                .flatMap(classChooseBean01s -> {
                    List<ClassChooseBean01.ClassListBean> lClassListBeen = new ArrayList<>();
                    if (classChooseBean01s != null) {
                        for (int i = 0; i < classChooseBean01s.size(); i++) {
                            ClassChooseBean01 lClassChooseBean01 = classChooseBean01s.get(i);
                            List<ClassChooseBean01.ClassListBean> lClassList = lClassChooseBean01.getClassList();
                            int lSize = lClassListBeen.size();
                            lClassListBeen.addAll(lClassList);
                            for (int j = 0; j < lClassList.size(); j++) {
                                lClassList.get(j).setDeptId(lClassChooseBean01.getDeptId());
                                lClassList.get(j).setDeptName(lClassChooseBean01.getDeptName());
                                lClassList.get(j).setDeptCode(lClassChooseBean01.getDeptCode());
                                lClassList.get(j).setHeaderPosition(lSize);
                                lClassList.get(j).setSize(lClassList.size());
                            }
                        }
                        return Observable.just(classChooseBean01s);
                    }
                    return Observable.just(null);
                })
                .flatMap(classChooseBean01s -> {
                    mClassListBeen = new ArrayList<>();
                    if (classChooseBean01s != null) {
                        for (int i = 0; i < classChooseBean01s.size(); i++) {
                            mClassListBeen.addAll(classChooseBean01s.get(i).getClassList());
                        }

                        return Observable.just(mClassListBeen);
                    }

                    return Observable.just(null);
                })
                .flatMap(classListBeen -> {
                    if (classListBeen != null) {
                        for (int i = 0; i < classListBeen.size(); i++) {
                            ClassChooseBean01.ClassListBean lClassListBean = classListBeen.get(i);
                            lClassListBean.setSortId(i);
                        }
                        return Observable.just(classListBeen);
                    }
                    return null;
                })
                .subscribe(new Subscriber<List<ClassChooseBean01.ClassListBean>>() {
                    @Override
                    public void onCompleted() {
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (getView() != null) {
                            getView().onErrorHandle(e);
                            getView().hideDialog();
                            isAllClassHaveSelect = false;
                        }

                    }

                    @Override
                    public void onNext(List<ClassChooseBean01.ClassListBean> classListBeen) {
                        if (classListBeen == null) {
                            getView().showEmpty();
                        } else {
                            //TODO 显示班级
                            getView().showClass01(classListBeen);
                        }

                    }
                }));

    }


    public boolean getIsAllClassHaveSelect(){
        return isAllClassHaveSelect;
    }
}
