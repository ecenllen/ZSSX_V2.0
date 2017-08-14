package com.gta.zssx.fun.coursedaily.registercourse.presenter;

import android.content.Context;

import com.gta.utils.helper.Helper_String;
import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ApproveBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ClassDisplayBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DefaultRegistInfoBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.view.DetailView;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordFromSignatureDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ServerTimeDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.RegisteredRecordManager;
import com.gta.zssx.pub.exception.CustomException;
import com.gta.zssx.pub.util.LogUtil;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

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
public class RegisterDetailPresenter extends BasePresenter<DetailView> {

    public List<DateTime> mDateTimes;
    public ArrayList<String> mStrings;
    public List<DateTime> mDateTimesRange;
    private List<SectionBean> mSectionBeen;
    private  List<String> mNotSignSectionList = new ArrayList<>();  //服务器给出的来未登记的列表

    /**
     * 计算领导审核日到今天的时间集合
     *
     * @param beginDate 开始日期
     * @param endDate 结束日期
     * @return 集合
     */
    public List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
        List<Date> lDate = new ArrayList<>();
        lDate.add(beginDate);// 把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (!endDate.before(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
//        lDate.add(endDate);// 把结束时间加入集合,会发生重复，不添加
        return lDate;
    }

    /**
     * 把时间集合换成字符串集合
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 时间集合
     */
    public ArrayList<String> getDateRange(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date lParse = null;
        try {
            lParse = sdf.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date lEndDate = null;
        try {
            lEndDate = sdf.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Date> lDatesBetweenTwoDate = getDatesBetweenTwoDate(lParse, lEndDate);
        mDateTimes = new ArrayList<>();
        mStrings = new ArrayList<>();
        mDateTimesRange = new ArrayList<>();
        for (int i = 0; i < lDatesBetweenTwoDate.size(); i++) {
            DateTime lDateTime = new DateTime(lDatesBetweenTwoDate.get(i));
            mDateTimesRange.add(lDateTime);
            mDateTimes.add(lDateTime);
            String lDaysOfweek = Helper_String.getDayOfWeek(lDateTime);
            String lDateString = lDateTime.toString("MM月dd日");
            mStrings.add(lDateString + "    " + lDaysOfweek);
        }
        return mStrings;
    }

    public List<DateTime> getDateTimesRange() {
        return mDateTimesRange;
    }


    /**
     * 获取领导审核日
     */
    public void getApproveDate() {
        if (!isViewAttached()) {
            return;
        }

        getView().showLoadingDialog();
        Observable.zip(ClassDataManager.getApproveDate(), RegisteredRecordManager.getServerTime(), (approveBean, serverTimeDto) -> {
            Combined lCombined = new Combined();
            lCombined.setApproveBean(approveBean);
            lCombined.setServerTimeDto(serverTimeDto);
            return lCombined;
        }).subscribe(combined -> {

            getView().hideDialog();
            getView().showResult(combined);
        }, throwable -> {
            if (getView() == null)
                return;
            getView().onErrorHandle(throwable);
            getView().hideDialog();
        });
    }

    /**
     * 根据节次获得节次名称
     */
    public String getSectionName(int sectionId) {
        String string = null;
        switch (sectionId) {
            case 1:
                string = "第1节";
                break;
            case 2:
                string = "第2节";
                break;
            case 3:
                string = "第3节";
                break;
            case 4:
                string = "第4节";
                break;
            case 5:
                string = "第5节";
                break;
            case 6:
                string = "第6节";
                break;
            case 7:
                string = "第7节";
                break;
            default:
                break;
        }
        return string;
    }

    public static class Combined {
        private List<SectionBean> sectionBeans;
        private ServerTimeDto serverTimeDto;
        private ApproveBean approveBean;

        public ApproveBean getApproveBean() {
            return approveBean;
        }

        public void setApproveBean(ApproveBean approveBean) {
            this.approveBean = approveBean;
        }

        public List<SectionBean> getSectionBeans() {
            return sectionBeans;
        }

        public void setSectionBeans(List<SectionBean> sectionBeans) {
            this.sectionBeans = sectionBeans;
        }

        public ServerTimeDto getServerTimeDto() {
            return serverTimeDto;
        }

        public void setServerTimeDto(ServerTimeDto serverTimeDto) {
            this.serverTimeDto = serverTimeDto;
        }
    }

    /**
     * 同时请求时间和节次信息
     *
     * @param teacherId 老师id
     * @param classId   班级id
     * @param signData  登记日期
     * @param isNeedToGetLatestSection  是否根据日期去获取最近的节次，如果为false表示不需要获取最新
     */
    public void getTimeAndSection(String teacherId, int classId, String signData,boolean isNeedToGetLatestSection) {

        if (!isViewAttached())
            return;

        getView().showLoadingDialog();
        Subscription lSubscribe = Observable.zip(ClassDataManager.getClassStatus(teacherId, classId, signData), RegisteredRecordManager.getServerTime(),
                (sectionBeanList, serverTimeDto) -> {
                    Combined lCombined = new Combined();
                    lCombined.setSectionBeans(sectionBeanList);
                    lCombined.setServerTimeDto(serverTimeDto);
                    return lCombined;
                }).flatMap(combined -> {
            List<SectionBean> lSectionBeans = combined.getSectionBeans();
            List<SectionBean> lSectionBeen = new ArrayList<>();
            List<SectionBean> lSectionBeen01 = new ArrayList<>();
            for (int i = 0; i < lSectionBeans.size(); i++) {
                if (lSectionBeans.get(i).getSignStatus() == 0) {
                    lSectionBeen.add(lSectionBeans.get(i));
                } else {
                    lSectionBeen01.add(lSectionBeans.get(i));
                }
            }
            lSectionBeen.addAll(lSectionBeen01);
            combined.setSectionBeans(lSectionBeen);
            return Observable.just(combined);
        }).subscribe(combined -> {
            if (getView() == null)
                return;
            getView().hideDialog();
            String lDate = combined.getServerTimeDto().getDate();
            mSectionBeen = combined.getSectionBeans();
            if(isNeedToGetLatestSection){
                if(mSectionBeen == null || mSectionBeen.size() == 0){
                    getView().showLastestSection(null);
                    return;
                }
                getSection(lDate);
            }

        }, throwable -> {
            if (getView() == null)
                return;
            getView().hideDialog();
            getView().onErrorHandle(throwable);
        });

        mCompositeSubscription.add(lSubscribe);
    }

    /**
     * 获取节次列表
     *
     * @param context   上下文
     * @param teacherId 老师id
     * @param classId   班级id
     * @param signData  登记日期
     */
    @Deprecated
    public void getSectionList(final Context context, String teacherId, int classId, String signData) {

        if (!isViewAttached())
            return;

        /**
         * 防止下一页返回的时候被调用，把选择的节次覆盖掉
         */
        if (ClassDataManager.getDataCache().getSection() != null) {
            return;
        }
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
                        if (e instanceof CustomException) {
                            CustomException lErrorCodeException = (CustomException) e;
                            getView().showWarning(lErrorCodeException.getMessage());
                        }

                        getView().hideDialog();
                    }

                    @Override
                    public void onNext(List<SectionBean> sectionBeen) {
                        mSectionBeen = sectionBeen;
                        getServerTime();
                    }
                }));

    }


    /**
     * 获取服务器时间
     */
    @Deprecated
    public void getServerTime() {
        if (!isViewAttached()) {
            return;
        }
        mCompositeSubscription.add(RegisteredRecordManager.getServerTime()
                .subscribe(new Subscriber<ServerTimeDto>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().onErrorHandle(e);
                        }
                    }

                    @Override
                    public void onNext(ServerTimeDto serverTimeDto) {
                        String lDate = serverTimeDto.getDate();
//                        getSection(lDate);

                    }
                }));
    }

    private void getSection(String date) {
        String[] lSplit = date.split(" ");
        DateTime lCurrentTime = new DateTime(lSplit[0] + "T" + lSplit[1]);
        int lMinuteOfDay = lCurrentTime.getMinuteOfDay();
        List<SectionBean> lUnsignSection = getUnsignSection(lSplit[0]);

        /**
         * 获得未登记的离当前时间最近的节次
         * 节次间的间隔时间算上一节的时间
         * 节次间隔之前的算第一节
         * 节次间隔之后的算最后一节
         */
        if (lUnsignSection.size() == 0) {
            getView().showLastestSection(null);
            return;
        }
        for (int i = 0; i < lUnsignSection.size(); i++) {
            SectionBean lSectionBean = lUnsignSection.get(i);
            int lMinuteOfDayBegin = lSectionBean.getBeginDateTime().getMinuteOfDay();
            int lMinuteOfDayEnd = lSectionBean.getEndDateTime().getMinuteOfDay();

            if (0 == i) {
                if (lMinuteOfDay <= lMinuteOfDayBegin) {
                    getView().showLastestSection(lSectionBean);
                    return;
                }
            }

            if (lUnsignSection.size() - 1 == i) {
                if (lMinuteOfDay >= lMinuteOfDayBegin) {
                    getView().showLastestSection(lSectionBean);
                    return;
                }
            }

            if (i <= lUnsignSection.size() - 2) {
                SectionBean pre = lUnsignSection.get(i + 1);
                if (lMinuteOfDay > lMinuteOfDayBegin && lMinuteOfDay <= pre.getBeginDateTime().getMinuteOfDay()) {
                    getView().showLastestSection(lSectionBean);
                    return;
                }
            }

        }
    }

    /**
     * 获得带时间信息的未登记的节次列表
     *
     * @return 节次集合
     */
    private List<SectionBean> getUnsignSection(String date) {
        List<SectionBean> lSectionBeen = new ArrayList<>();
        for (int i = 0; i < mSectionBeen.size(); i++) {
            if (mSectionBeen.get(i).getSignStatus() == SectionBean.UNSIGN) {
                lSectionBeen.add(mSectionBeen.get(i));
            }
        }

//        String lNowDate = new DateTime().toString("yyyy-MM-dd");
        String lNowDate = date;
        for (int i = 0; i < lSectionBeen.size(); i++) {
            String lBeginTime = lSectionBeen.get(i).getBeginTime();
            DateTime lBeginDateTime = new DateTime(lNowDate + "T" + lBeginTime);
            lSectionBeen.get(i).setBeginDateTime(lBeginDateTime);
            String lEndTime = lSectionBeen.get(i).getEndTime();
            DateTime lEndDateTime = new DateTime(lNowDate + "T" + lEndTime);
            lSectionBeen.get(i).setEndDateTime(lEndDateTime);
        }
        return lSectionBeen;
    }

    /**
     * 获取默认课表
     * @param signDate
     * @param classId
     * @param sectionId
     * @param teacherId
     */
    public void getClassSectionDefaultTeachersAndCoures(String signDate,int classId,int sectionId,String teacherId){
        mCompositeSubscription.add(ClassDataManager.getClassSectionDefaultTeachersAndCoures(signDate,classId,sectionId,teacherId)
                .subscribe(new Subscriber<DefaultRegistInfoBean>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().onErrorHandle(e);
                        }
                    }

                    @Override
                    public void onNext(DefaultRegistInfoBean defaultRegistInfoBean) {
                        if (isViewAttached()){
                            DetailItemShowBean.TeacherInfoBean teacherInfoBean = new DetailItemShowBean.TeacherInfoBean();
                            teacherInfoBean.setTeacherNo(defaultRegistInfoBean.getTeacherNo());
                            teacherInfoBean.setTeacherGuid("");
                            getView().showSectionDefaultTeacherAndCourse(defaultRegistInfoBean,teacherInfoBean);
                        }
                    }
                }));
    }

    /**
     * 展示弹框的老师字符串
     * @param teacherInfoBeen
     * @return
     */
    public String getUpdateTeacherString(List<DetailItemShowBean.TeacherInfoBean> teacherInfoBeen){
        String teacherString = "";
        if(teacherInfoBeen.size() == 0){
            teacherString = "无";
        }else {
            if(teacherInfoBeen.size() < 3){
                for(int i = 0;i < teacherInfoBeen.size();i++){
                    if(i != 0){
                        teacherString += "，";
                    }
                    teacherString += teacherInfoBeen.get(i).getTeacherName();
                }
            }else {
                for(int i = 0;i < 2;i++){
                    if(i != 0){
                        teacherString += "，";
                    }
                    teacherString += teacherInfoBeen.get(i).getTeacherName();
                }
                teacherString += "...等"+teacherInfoBeen.size()+"名教师";
            }

        }
        return teacherString;
    }

    /**
     * 展示课程名称
     * @param courseInfoBeen
     * @return
     */
    public String getUpdateCourseString(List<DetailItemShowBean.CourseInfoBean> courseInfoBeen){
        String courseString = "";
        if(courseInfoBeen.size() == 0){
            courseString = "无";
        }else {
            if(courseInfoBeen.size() < 3){
                for(int i = 0;i < courseInfoBeen.size();i++){
                    if(i != 0){
                        courseString += "，";
                    }
                    courseString += courseInfoBeen.get(i).getCourseName();
                }
            }else {
                for(int i = 0;i < 2;i++){
                    if(i != 0){
                        courseString += "，";
                    }
                    courseString += courseInfoBeen.get(i).getCourseName();
                }
                courseString += "...等"+courseInfoBeen.size()+"门课程";
            }

        }
        return courseString;
    }

    /**
     * 从修改返回的信息中获得节次信息
     *
     * @param signatureDto 登记信息
     * @return 节次信息
     */
    public Set<SectionBean> getModifySection(RegisteredRecordFromSignatureDto signatureDto) {
        Set<SectionBean> lSectionBeanSet = new HashSet<>();
        //这里改节次改动大,需要id和SectionOriginalId
        SectionBean lSectionBean = new SectionBean();
        lSectionBean.setHaveBeenSignFlag(false);
        lSectionBean.setSectionId(signatureDto.getSection().getSectionId());
        lSectionBean.setSectionOriginalId(signatureDto.getSection().getSectionOriginalId());
        lSectionBean.setLesson(signatureDto.getSection().getLesson());
        lSectionBean.setSignStatus(SectionBean.UNSIGN);
        lSectionBeanSet.add(lSectionBean);
        return lSectionBeanSet;
    }

    /**
     * 从修改返回的信息中获得班级信息
     *
     * @param signatureDto 登记信息
     * @return 班级信息
     */
    public ClassDisplayBean getModifyClassDisplayBean(RegisteredRecordFromSignatureDto signatureDto) {
        ClassDisplayBean lClassDisplayBean = new ClassDisplayBean();
        lClassDisplayBean.setClassId(signatureDto.getClassID());
        lClassDisplayBean.setClassName(signatureDto.getClassName());
        String lSignInfo = signatureDto.getSignInfo();
        String[] lSplit = lSignInfo.split("/");
        lClassDisplayBean.setStudentCount(Integer.parseInt(lSplit[1]));
        return lClassDisplayBean;
    }

    /**
     * 获得登记日期的时间格式
     *
     * @param date 时间
     * @return 时间
     */
    public String getSignFormatDay(String date) {
        DateTime lDateTime = new DateTime(date);
        String lDayOfWeek = Helper_String.getDayOfWeek(lDateTime);
        return lDateTime.toString("MM月dd日") + "    " + lDayOfWeek;
    }

    /**
     * 排序选择好的节次
     *
     * @param sectionBeanSet 节次集合
     * @return 排序的节次集合
     */
    public Set<SectionBean> sortSetSection(Set<SectionBean> sectionBeanSet) {
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


    public void getIsHaveSectionHaveBeenSign(List<SectionBean> sectionBeanList,String signData, int classId){
        String sectionString = "0";  //0表示请求当天所有未被登记的节次
        boolean isCheckAllSection = true;
        if(sectionBeanList != null){
            sectionString = getSectionString(sectionBeanList);
            isCheckAllSection = false;
        }
        //判断是否有节次已经在别的地方抢先登记了
        if (!isViewAttached())
            return;

        getView().showLoadingDialog();
        final boolean finalIsCheckAllSection = isCheckAllSection;
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
                mNotSignSectionList = stringsNotSignList;
                getView().showSelectSectionNotSignCount(stringsNotSignList.size(), finalIsCheckAllSection);
                hideDialog();
            }
        });
        mCompositeSubscription.add(lSubscribe);
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
     * 获取未登记的节次列表,并标识状态
     * @param sectionBeanList
     * @return
     */
    public void getStatusSignList(List<SectionBean> sectionBeanList){
        if(mNotSignSectionList == null){
            LogUtil.Log("lenita","未登记的节次列表为空");
            return;
        }
        List<SectionBean> notSignList = new ArrayList<>();
        //TODO 拿到未登记列表
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
//                    Log.e("lenita","getNotSignList oid = "+Oid);
                    break; //直接打断这个循环
                }
            }
        }
    }

    /**
     * 看当前课程和老师是否和后台返回的完全匹配
     * @param teacherInfoBeanList
     * @param courseInfoBeanList
     * @param teacherInfoBeanOriginalList
     * @param courseInfoBeanOriginalList
     * @return
     */
    public boolean isNeedToShowPopupWindow(List<DetailItemShowBean.TeacherInfoBean> teacherInfoBeanList,List<DetailItemShowBean.CourseInfoBean> courseInfoBeanList,List<DetailItemShowBean.TeacherInfoBean> teacherInfoBeanOriginalList,List<DetailItemShowBean.CourseInfoBean> courseInfoBeanOriginalList){
        boolean isNeedToShowPopupWindow = true;
        int matchCourseNum = 0;
        int matchTeacherNum = 0;
        //TODO 拿到匹配数
        for(int i = 0;i< courseInfoBeanOriginalList.size();i++){
            for(int j = 0;j < courseInfoBeanList.size();j++){
                if(courseInfoBeanOriginalList.get(i).getCourseId() == courseInfoBeanList.get(j).getCourseId()){
                    matchCourseNum++;
                }
            }
        }
       //TODO 拿到匹配数
        for(int i = 0;i< teacherInfoBeanOriginalList.size();i++){
            for(int j = 0;j < courseInfoBeanList.size();j++){
                if(teacherInfoBeanOriginalList.get(i).getTeacherId() == teacherInfoBeanList.get(j).getTeacherId()){
                    matchTeacherNum++;
                }
            }
        }
        //TODO 当老师和课程全匹配的时候不需要弹框
        if(matchCourseNum == courseInfoBeanList.size() || matchTeacherNum == teacherInfoBeanList.size()){
            isNeedToShowPopupWindow = false;
        }
        return isNeedToShowPopupWindow;
    }

    //下面废弃
   /* String text = "";
    for (int i = 0; i < lSectionBeen.size(); i++) {
        int lSectionName = lSectionBeen.get(i).getSectionID();
        if (i == 0) {
            text = lSectionName + "";
        } else {
            text = text + "、" + lSectionName;
        }
    }
    mSectionTv.setText(String.valueOf("第 " + text + " 节"));*/

/*    private Set<DetailItemShowBean.TeacherInfoBean> testDataT(){
        Set<DetailItemShowBean.TeacherInfoBean> list = new HashSet<>();
        DetailItemShowBean.TeacherInfoBean teacherInfoBean1 = new DetailItemShowBean.TeacherInfoBean();
        DetailItemShowBean.TeacherInfoBean teacherInfoBean2 = new DetailItemShowBean.TeacherInfoBean();
        teacherInfoBean1.setTeacherId("1111");
        teacherInfoBean1.setTeacherName("张三");
        teacherInfoBean2.setTeacherId("2222");
        teacherInfoBean2.setTeacherName("李四");
        list.add(teacherInfoBean1);
        list.add(teacherInfoBean2);
        return list;
    }

    private Set<DetailItemShowBean.CourseInfoBean> testDataC(){
        Set<DetailItemShowBean.CourseInfoBean> list = new HashSet<>();
        DetailItemShowBean.CourseInfoBean teacherInfoBean1 = new DetailItemShowBean.CourseInfoBean();
        DetailItemShowBean.CourseInfoBean teacherInfoBean2 = new DetailItemShowBean.CourseInfoBean();
        teacherInfoBean1.setCourseId("1111");
        teacherInfoBean1.setCourseName("语文");
        teacherInfoBean2.setCourseId("2222");
        teacherInfoBean2.setCourseName("数学");
        list.add(teacherInfoBean1);
        list.add(teacherInfoBean2);
        return list;
    }*/
}
