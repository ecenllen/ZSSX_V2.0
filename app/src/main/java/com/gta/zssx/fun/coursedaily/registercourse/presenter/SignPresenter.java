package com.gta.zssx.fun.coursedaily.registercourse.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DataBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.PostSignBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionAtendentStatusListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionStudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StateBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListNewBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SubmitSignInfoBean;
import com.gta.zssx.fun.coursedaily.registercourse.view.SignView;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RxApproveStatus;
import com.gta.zssx.pub.exception.CustomException;
import com.gta.zssx.pub.util.LogUtil;
import com.gta.zssx.pub.util.RxBus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/21.
 * @since 1.0.0
 */
public class SignPresenter extends BasePresenter<SignView> {
    private Context mContext;
    public List<StudentListNewBean> mStudentListBeen;
    public Subscription mSubscription;
    public List<SectionStudentListBean> mSectionStudentListBeanList;
    private  List<String> mNotSignSectionList = new ArrayList<>();  //服务器给出的来未登记的列表

    public SignPresenter(Context context){
        mContext = context;
    }

    /**
     * 提交登记  - 新
     * @param submitSignInfoBean
     */
    public void postSignData(SubmitSignInfoBean submitSignInfoBean,List<SectionBean> sectionBeanList) {
        if (!isViewAttached())
            return;
        getView().showDialog("正在提交...", false);

        if(sectionBeanList != null){
            //当传入了节次信息，代表需要对提交的信息做处理，要先把已登记节次剔除再提交
            for(int i = 0;i < sectionBeanList.size();i++){
                if(sectionBeanList.get(i).getHaveBeenSignFlag()){
                    int Oid = sectionBeanList.get(i).getSectionOriginalId();
                    //TODO 发现登记节次，进行匹配
                    for(int j = 0;j <submitSignInfoBean.getSectionBean().size();j++){
                        if(submitSignInfoBean.getSectionBean().get(j).getSectionID() == Oid){
                            submitSignInfoBean.getSectionBean().remove(j);
                            Log.e("lenita","postSignData 删除已登记的节次Oid"+ Oid);
                        }
                    }
                }
            }
        }

        mCompositeSubscription.add(ClassDataManager.postSignNew(submitSignInfoBean)
                .flatMap(s -> Observable.just(s))
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            if (e instanceof CustomException) {
                                CustomException customException = (CustomException) e;
                                int lCode = customException.getCode();
                                if (lCode == CustomException.HAS_SIGN) {
                                    //发现有已经被登记的节次，弹出提示
                                    getView().showHaveBeenSignMassage(mContext.getResources().getString(R.string.text_contain_have_been_sign_section_went_submit));
                                }else {
                                    String msg = e.getMessage();
                                    Log.e("lenita", "postSignData 登记失败 e = " + msg);
                                    if (msg.contains(mContext.getResources().getString(R.string.text_can_not_deal_with_approve_data))) {
                                        String showMsg = submitSignInfoBean.getSignDate() + "的数据已经被审核，无法进行新增或修改操作";
                                        getView().showHaveBeenSignMassage(showMsg);
                                        /**
                                         * 回调到显示已登记列表的Fragment去更新列表状态
                                         * AlreadyRegisteredRecordFromSignatureFragment - 行
                                         * MyClassRecordFragment - 行
                                         * MyClassRecordSubTabFragment - 行
                                         */
                                        RxApproveStatus approveStatus = new RxApproveStatus();
                                        approveStatus.setApprove(true);
                                        RxBus.getDefault().post(approveStatus);
                                    }else {
                                        getView().onErrorHandle(e);
                                    }

                                }
                            }else {
                                getView().onErrorHandle(e);
                            }
                            getView().hideDialog();
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        getView().showResult(s);
                    }
                }));
    }

    /**
     *   判断是否有节次已经在别的地方抢先登记了
     */
    public void getIsHaveSectionHaveBeenSign(String sectionString,String signData, int classId){

        if (!isViewAttached())
            return;

        getView().showLoadingDialog();
        Subscription lSubscribe = ClassDataManager.checkSctionIsHaveBeenRegister(signData,classId, sectionString).subscribe(new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                if (getView() == null)
                    return;
                getView().onErrorHandle(throwable);
                hideDialog();
            }

            @Override
            public void onNext(List<String> stringsNotSignList) {
                if (getView() == null)
                    return;
                mNotSignSectionList.clear();
                mNotSignSectionList.addAll(stringsNotSignList);
                getView().isHaveSectionHaveBeenSign(stringsNotSignList.size());
                hideDialog();
            }
        });
        mCompositeSubscription.add(lSubscribe);
    }

    /**
     * 获取未登记的节次列表
     * @param sectionBeanList
     * @return
     */
    public void getStatusSignList(List<SectionBean> sectionBeanList){
        if(mNotSignSectionList == null){
            LogUtil.Log("lenita","未登记的节次列表为空");
            return;
        }
        List<SectionBean> notSignList = new ArrayList<>();
        //拿到未登记列表
        for(int i = 0;i<sectionBeanList.size();i++){
            sectionBeanList.get(i).setHaveBeenSignFlag(true);  //默认都是被登记
            SectionBean sectionBean = sectionBeanList.get(i);
            String Oid = String.valueOf(sectionBean.getSectionOriginalId());
            for(int j =0;j<mNotSignSectionList.size();j++){
                if (mNotSignSectionList.get(j).equals(Oid)) {
                    //TODO 没有被登记的变成false
                    sectionBeanList.get(i).setHaveBeenSignFlag(false);
                    //证明是未登记的一个节次，加入新的List中
                    notSignList.add(sectionBean);
                    Log.e("lenita","signPresenter getNotSignList oid = "+Oid);
                    break; //直接打断这个循环
                }
            }
        }
    }

    /**
     * 展示弹框的老师字符串
     * @param teacherInfoBeen
     * @return
     */
    public String getTeacherString(List<DetailItemShowBean.TeacherInfoBean> teacherInfoBeen){
        String teacherString = "";
        if(teacherInfoBeen.size() == 0){
            teacherString = "无";
        }else {
            if(teacherInfoBeen.size() == 1){
                teacherString = teacherInfoBeen.get(0).getTeacherName();
            }else if(teacherInfoBeen.size() == 2){
                for(int i = 0;i < teacherInfoBeen.size();i++){
                    String teacherName = teacherInfoBeen.get(i).getTeacherName();
                    if(i != 0){
                        teacherString += "，";
                    }
                    if(teacherName.length() > 5){
                        teacherString += teacherName.substring(0,5)+"...";
                    }else {
                        teacherString += teacherName;
                    }

                }
            }else {
                for(int i = 0;i < 2;i++){
                    String teacherName = teacherInfoBeen.get(i).getTeacherName();
                    if(i != 0){
                        teacherString += "，";
                    }
                    if(teacherName.length() > 5){
                        teacherString += teacherName.substring(0,5)+"...";
                    }else {
                        teacherString += teacherName;
                    }
                }
                teacherString += "等"+teacherInfoBeen.size()+"名教师";
            }

        }
        return teacherString;
    }

    /**
     * 展示课程名称
     * @param courseInfoBeen
     * @return
     */
    public String getCourseString(List<DetailItemShowBean.CourseInfoBean> courseInfoBeen){
        String courseString = "";
        if(courseInfoBeen.size() == 0){
            courseString = "无";
        }else {
            if(courseInfoBeen.size() == 1){
                courseString = courseInfoBeen.get(0).getCourseName();
            } else if(courseInfoBeen.size() == 2){
                for(int i = 0;i < courseInfoBeen.size();i++){
                    String courseName = courseInfoBeen.get(i).getCourseName();
                    if(i != 0){
                        courseString += "，";
                    }
                    if(courseName.length() > 5){
                        courseString += courseName.substring(0,5)+"...";
                    }else {
                        courseString += courseName;
                    }
                }
            }else {
                for(int i = 0;i < 2;i++){
                    String courseName = courseInfoBeen.get(i).getCourseName();
                    if(i != 0){
                        courseString += "，";
                    }
                    if(courseName.length() > 5){
                        courseString += courseName.substring(0,5)+"...";
                    }else {
                        courseString += courseName;
                    }
                }
                courseString += "等"+courseInfoBeen.size()+"门课程";
            }

        }
        return courseString;
    }

    /**
     * 保存节次String
     */
    public String getSectionString(List<SectionBean> sectionBeanList){
        String sectionString = "";
        for(int i =0;i<sectionBeanList.size(); i++){
            if(i != 0){
                sectionString += ",";
            }
            sectionString += sectionBeanList.get(i).getSectionOriginalId();
        }
        return sectionString;
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

    public List<SectionStudentListBean> getmSectionStudentListBeanList(){
        return mSectionStudentListBeanList;
    }

    public void getStudentListNew(String signData, int classId, String sectionString) {

        if (!isViewAttached())
            return;
        getView().showLoadingDialog();
        // TODO: 2016/6/22 死数据
        //70, "2016-05-05"
        mSubscription = ClassDataManager.getClassDiarySectionStudentMarkedDetail(signData,classId, sectionString)
                .subscribe(new Subscriber<List<SectionStudentListBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if(isViewAttached()){
                            getView().onErrorHandle(throwable);
                            getView().hideDialog();
                            getView().emptyUI();
                        }

                    }

                    @Override
                    public void onNext(List<SectionStudentListBean> sectionStudentListBeanList) {
                        if(isViewAttached()){
                            getView().hideDialog();
                            //只要返回数据为空的都显示空白
                            if(sectionStudentListBeanList.get(0).getStudentListNewBeen().size() == 0){
                                getView().emptyUI();
                                return;
                            }
                            //有数据放数据
                            mSectionStudentListBeanList = sectionStudentListBeanList;
                            getView().getStudentListInfo(sectionStudentListBeanList);
                        }

                    }
                });

        mCompositeSubscription.add(mSubscription);

    }

    //以下为学生列表相关的功能
    public List<PostSignBean.SectionBean> mPostSectionBeen;

    private Set<SectionBean> mSectionBeen;

    @Deprecated
    public DataBean getSignSection(DataBean dataBean, List<StudentListBean> studentListBeen) {
//        Set<SectionBean> lSectionBeanSet = sortSet(dataBean.getSectionBeen());
//        List<SectionBean> lSectionBeanList = new ArrayList<>(lSectionBeanSet);
        List<SectionBean> lSectionBeanList = dataBean.getSectionBeen();
        mPostSectionBeen = new ArrayList<>();
        mSectionBeen = new HashSet<>();

        Observable.from(lSectionBeanList)
                .flatMap(new Func1<SectionBean, Observable<SectionBean>>() {
                    @Override
                    public Observable<SectionBean> call(SectionBean sectionBean) {
                        int lSectionID = sectionBean.getSectionId();
                        /**
                         * 计算每节课迟到，请假，旷课，公假的人数
                         */
                        int delayCount = 0;
                        int leaveCount = 0;
                        int absentCount = 0;
                        int vocationCount = 0;
                        switch (lSectionID) {
                            case 1:
                                for (int i = 0; i < studentListBeen.size(); i++) {
                                    StudentListBean lStudentListBean = studentListBeen.get(i);
                                    int lLESSON1 = lStudentListBean.getLESSON1();
                                    if (lLESSON1 == StateBean.DELAY) {
                                        delayCount = delayCount + 1;
                                    } else if (lLESSON1 == StateBean.LEAVE) {
                                        leaveCount = leaveCount + 1;
                                    } else if (lLESSON1 == StateBean.ABSENT) {
                                        absentCount = absentCount + 1;
                                    } else if (lLESSON1 == StateBean.VOCATION) {
                                        vocationCount = vocationCount + 1;
                                    }
                                }
                                break;
                            case 2:
                                for (int i = 0; i < studentListBeen.size(); i++) {
                                    StudentListBean lStudentListBean = studentListBeen.get(i);
                                    int lLESSON1 = lStudentListBean.getLESSON2();
                                    if (lLESSON1 == StateBean.DELAY) {
                                        delayCount = delayCount + 1;
                                    } else if (lLESSON1 == StateBean.LEAVE) {
                                        leaveCount = leaveCount + 1;
                                    } else if (lLESSON1 == StateBean.ABSENT) {
                                        absentCount = absentCount + 1;
                                    } else if (lLESSON1 == StateBean.VOCATION) {
                                        vocationCount = vocationCount + 1;
                                    }
                                }
                                break;
                            case 3:
                                for (int i = 0; i < studentListBeen.size(); i++) {
                                    StudentListBean lStudentListBean = studentListBeen.get(i);
                                    int lLESSON1 = lStudentListBean.getLESSON3();
                                    if (lLESSON1 == StateBean.DELAY) {
                                        delayCount = delayCount + 1;
                                    } else if (lLESSON1 == StateBean.LEAVE) {
                                        leaveCount = leaveCount + 1;
                                    } else if (lLESSON1 == StateBean.ABSENT) {
                                        absentCount = absentCount + 1;
                                    } else if (lLESSON1 == StateBean.VOCATION) {
                                        vocationCount = vocationCount + 1;
                                    }
                                }
                                break;
                            case 4:
                                for (int i = 0; i < studentListBeen.size(); i++) {
                                    StudentListBean lStudentListBean = studentListBeen.get(i);
                                    int lLESSON1 = lStudentListBean.getLESSON4();
                                    if (lLESSON1 == StateBean.DELAY) {
                                        delayCount = delayCount + 1;
                                    } else if (lLESSON1 == StateBean.LEAVE) {
                                        leaveCount = leaveCount + 1;
                                    } else if (lLESSON1 == StateBean.ABSENT) {
                                        absentCount = absentCount + 1;
                                    } else if (lLESSON1 == StateBean.VOCATION) {
                                        vocationCount = vocationCount + 1;
                                    }
                                }
                                break;
                            case 5:
                                for (int i = 0; i < studentListBeen.size(); i++) {
                                    StudentListBean lStudentListBean = studentListBeen.get(i);
                                    int lLESSON1 = lStudentListBean.getLESSON5();
                                    if (lLESSON1 == StateBean.DELAY) {
                                        delayCount = delayCount + 1;
                                    } else if (lLESSON1 == StateBean.LEAVE) {
                                        leaveCount = leaveCount + 1;
                                    } else if (lLESSON1 == StateBean.ABSENT) {
                                        absentCount = absentCount + 1;
                                    } else if (lLESSON1 == StateBean.VOCATION) {
                                        vocationCount = vocationCount + 1;
                                    }
                                }
                                break;
                            case 6:
                                for (int i = 0; i < studentListBeen.size(); i++) {
                                    StudentListBean lStudentListBean = studentListBeen.get(i);
                                    int lLESSON1 = lStudentListBean.getLESSON6();
                                    if (lLESSON1 == StateBean.DELAY) {
                                        delayCount = delayCount + 1;
                                    } else if (lLESSON1 == StateBean.LEAVE) {
                                        leaveCount = leaveCount + 1;
                                    } else if (lLESSON1 == StateBean.ABSENT) {
                                        absentCount = absentCount + 1;
                                    } else if (lLESSON1 == StateBean.VOCATION) {
                                        vocationCount = vocationCount + 1;
                                    }
                                }
                                break;
                            case 7:
                                for (int i = 0; i < studentListBeen.size(); i++) {
                                    StudentListBean lStudentListBean = studentListBeen.get(i);
                                    int lLESSON1 = lStudentListBean.getLESSON7();
                                    if (lLESSON1 == StateBean.DELAY) {
                                        delayCount = delayCount + 1;
                                    } else if (lLESSON1 == StateBean.LEAVE) {
                                        leaveCount = leaveCount + 1;
                                    } else if (lLESSON1 == StateBean.ABSENT) {
                                        absentCount = absentCount + 1;
                                    } else if (lLESSON1 == StateBean.VOCATION) {
                                        vocationCount = vocationCount + 1;
                                    }
                                }
                                break;
                            default:
                                break;
                        }

                        //迟到算出勤
                        final int unusualCount = delayCount + leaveCount + absentCount + vocationCount;
                        //TODO 分数（旷课扣3，迟到扣1，其他不扣）
                        int points = delayCount+absentCount*3;
                        int lScore = 100 - points;
                        if (lScore < 0) {
                            lScore = 0;
                        }

                        PostSignBean.SectionBean lSectionBean1 = new PostSignBean.SectionBean();
                        lSectionBean1.setScore(lScore);
                        lSectionBean1.setSectionID(lSectionID);
//                        lSectionBean1.setCourseName(dataBean.getCourseName());
                        lSectionBean1.setRemark(dataBean.getMemo());

                        mPostSectionBeen.add(lSectionBean1);

                        sectionBean.setLeaveCount(leaveCount);
                        sectionBean.setAbsentCount(absentCount);
                        sectionBean.setDelayCount(delayCount);
                        sectionBean.setVocationCount(vocationCount);
                        sectionBean.setScore(lScore);
//                        sectionBean.setFlag(dataBean.getMemo());

                        return Observable.just(sectionBean);
                    }
                })
                .subscribe(new Subscriber<SectionBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SectionBean sectionBean) {
                        mSectionBeen.add(sectionBean);
                    }
                });
//        dataBean.setPostSectionBean(mPostSectionBeen);
        List<SectionBean> sectionBeanList = new ArrayList<>(mSectionBeen);
        dataBean.setSectionBeen(sectionBeanList);
        return dataBean;
    }

    /*public void getStudentList(int classId, String signData) {

        if (!isViewAttached())
            return;

        getView().sh oadingDialog();
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

    }*/

    /**
     * 返回学生列表
     * @return 学生列表
     */
    public List<StudentListNewBean> getStudentListBeen() {
        return mStudentListBeen;
    }

   /* public List<StudentListNewBean> setStudentDefaultState(List<StudentListNewBean> studentListBeen, Set<SectionBean> sectionBeen) {
        if (sectionBeen != null) {
            List<SectionBean> lSectionBeen = new ArrayList<>(sectionBeen);
            for (int i = 0; i < studentListBeen.size(); i++) {
                StudentListNewBean lStudentListBean = studentListBeen.get(i);
                for (int j = 0; j < lSectionBeen.size(); j++) {
                    int lSectionID = lSectionBeen.get(j).getSectionId();
                    switch (lSectionID) {
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
                    }
                }
            }
        }
        return studentListBeen;
    }*/

    int delayCount = 0;
    int leaveCount = 0;
    int absentCount = 0;
    int vocationCount = 0;
    int score = 0;

    /**
     * 拿到提交所需的节次考勤全信息 -- 重要
     * @param sectionStudentListBeanList
     * @return
     */
    public SectionAtendentStatusListBean setOriginalSectionStatus(List<SectionStudentListBean> sectionStudentListBeanList){
        SectionAtendentStatusListBean sectionAtendentStatusListBean = new SectionAtendentStatusListBean();
        List<SectionBean> sectionBeanList = new ArrayList<>();
        for (int i = 0;i<sectionStudentListBeanList.size();i++){
            //设置sectionBean的考勤、学生列表、节次Id和OriginalId
            SectionBean sectionBean = countAttendanceNum(sectionStudentListBeanList.get(i).getStudentListNewBeen(),false);
            sectionBean.setSectionId(sectionStudentListBeanList.get(i).getSectionId());
            sectionBean.setSectionOriginalId(sectionStudentListBeanList.get(i).getSectionOriginalId());
            sectionBean.setStudentListBeen(sectionStudentListBeanList.get(i).getStudentListNewBeen());
            sectionBeanList.add(sectionBean);
        }
        sectionAtendentStatusListBean.setSectionBeanList(sectionBeanList);  //设置考勤和学生列表
        List<Boolean> booleanList = new ArrayList<>();
        for(int j =0;j<sectionAtendentStatusListBean.getSectionBeanList().size();j++){
            booleanList.add(false);
        }
        sectionAtendentStatusListBean.setSameWithPreviousStatusList(booleanList);  //设置同步开关
        return sectionAtendentStatusListBean;
    }

    /**
     * 用于新登记
     * @param studentListNewBeanList
     * @return
     */
    public SectionBean countAttendanceNum(List<StudentListNewBean> studentListNewBeanList,boolean isModify) {
        SectionBean sectionBean = new SectionBean();
        delayCount = 0;
        leaveCount = 0;
        absentCount = 0;
        vocationCount = 0;
        score = 0;
        for(int i = 0;i< studentListNewBeanList.size();i++){
            int studentStatus = studentListNewBeanList.get(i).getMarkType();
            switch (studentStatus){
                case 2:
                    delayCount++;
                    break;
                case 3:
                    leaveCount++;
                    break;
                case 4:
                    absentCount++;
                    break;
                case 5:
                    vocationCount++;
                    break;
                default:
                    break;
            }
        }
        //TODO 分数（旷课扣3，迟到扣1，其他不扣）
        int points = delayCount+absentCount*3;
        score = 100 - points;
        if (score < 0) {
            score = 0;
        }
        sectionBean.setDelayCount(delayCount);
        sectionBean.setLeaveCount(leaveCount);
        sectionBean.setAbsentCount(absentCount);
        sectionBean.setVocationCount(vocationCount);
        if(!isModify){
            //TODO 不是修改的情况才初始化分数和备注
            Log.e("lenita","SignPresenter score = "+score);
            sectionBean.setScore(score);
            sectionBean.setScoreString(""+score);
            sectionBean.setRemark("");
        }
        return sectionBean;
    }

    /**
     * 用于修改进入 -- 目前只支持单条修改
     * @return
     */
    public SectionAtendentStatusListBean countAttendanceNumModify(String score,String remark,List<SectionStudentListBean> sectionStudentListBeanList){
        SectionAtendentStatusListBean sectionAtendentStatusListBean = new SectionAtendentStatusListBean();
        List<SectionBean> sectionBeanList = new ArrayList<>();
        for (int i = 0;i<sectionStudentListBeanList.size();i++){
            //设置sectionBean的考勤、学生列表、节次Id和OriginalId
            SectionBean sectionBean = countAttendanceNum(sectionStudentListBeanList.get(i).getStudentListNewBeen(),true);
            //TODO 注意修改进入的使用修改前的分数和备注
            sectionBean.setScoreString(score);
            sectionBean.setScore(Integer.valueOf(score));
            sectionBean.setRemark(remark);
            sectionBean.setSectionId(sectionStudentListBeanList.get(i).getSectionId());
            sectionBean.setSectionOriginalId(sectionStudentListBeanList.get(i).getSectionOriginalId());
            sectionBean.setStudentListBeen(sectionStudentListBeanList.get(i).getStudentListNewBeen());
            sectionBeanList.add(sectionBean);
        }
        sectionAtendentStatusListBean.setSectionBeanList(sectionBeanList);  //设置考勤和学生列表
        List<Boolean> booleanList = new ArrayList<>();
        for(int j =0;j<sectionAtendentStatusListBean.getSectionBeanList().size();j++){
            booleanList.add(false);
        }
        sectionAtendentStatusListBean.setSameWithPreviousStatusList(booleanList);  //设置同步开关

        return sectionAtendentStatusListBean;
    }

    /**
     * 转换成提交所需的老师数据
     * @param teacherInfoBeanList
     * @return
     */
    public List<DetailItemShowBean.TeacherInfoBean> formatTeacherList(List<DetailItemShowBean.TeacherInfoBean> teacherInfoBeanList){
        for(int i = 0 ;i < teacherInfoBeanList.size();i++){
            String teacherGuid = teacherInfoBeanList.get(i).getTeacherId();
            teacherInfoBeanList.get(i).setTeacherId("");
            teacherInfoBeanList.get(i).setTeacherGuid(teacherGuid);
        }
        return teacherInfoBeanList;
    }

    /**
     * 是否能提交登记
     * @param sectionAtendentStatusListBean
     * @return
     */
    public boolean isCanSign(SectionAtendentStatusListBean sectionAtendentStatusListBean){
        boolean isCanSign = true;
        for(int i = 0;i<sectionAtendentStatusListBean.getSectionBeanList().size();i++){
            String scoreString = sectionAtendentStatusListBean.getSectionBeanList().get(i).getScoreString();
            if(TextUtils.isEmpty(scoreString)){
                isCanSign = false;
            }
        }
        return isCanSign;
    }


    private List<SectionStudentListBean>  testData(List<SectionBean> mSectionBeanList){
        List<SectionStudentListBean> mSectionStudentListBeanList = new ArrayList<>();
        for(int i =0;i< mSectionBeanList.size();i++){
            //一份学生列表--这里要注意，要每份new一次，不然指针将指向同一个学生列表，后面都会出现问题
            List<StudentListNewBean> studentListNewBeanList  = new ArrayList<>();
            for(int j = 1;j<16;j++){
                StudentListNewBean studentListNewBean = new StudentListNewBean();
                studentListNewBean.setStundentName("测试"+j);
                studentListNewBean.setStudentID(""+j);
                studentListNewBean.setStudentNO(""+j);
                studentListNewBean.setNameOfPinYin("CS"+j);
                if(j == 10-i){  //随便改变一个值
                    studentListNewBean.setMarkType(3);
                }else {
                    studentListNewBean.setMarkType(1);
                }
                studentListNewBean.setStudentSex(1);
                studentListNewBeanList.add(studentListNewBean);
            }
            //一份含节次等信息的学生列表
            SectionStudentListBean sectionStudentListBean = new SectionStudentListBean();
            sectionStudentListBean.setSectionId(mSectionBeanList.get(i).getSectionId());  //节次Id
            sectionStudentListBean.setSectionOriginalId(mSectionBeanList.get(i).getSectionOriginalId());  //真实节次Id
            sectionStudentListBean.setStudentListNewBeenList(studentListNewBeanList);
            mSectionStudentListBeanList.add(sectionStudentListBean);
            Log.e("lenita","test sectionStudentListBeanList id = "+mSectionStudentListBeanList.get(i).getSectionOriginalId());
        }
        return mSectionStudentListBeanList;
    }
}
