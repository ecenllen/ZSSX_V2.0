package com.gta.zssx.fun.coursedaily.registercourse.model;

import android.content.Context;

import com.gta.zssx.fun.coursedaily.registercourse.model.bean.AddClassBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.AddCourseBeen;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ApproveBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ClassChooseBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ClassChooseBean01;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ClassDisplayBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DefaultRegistInfoBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.LogChartBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.LogListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.OriginalClassBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.PostSignBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SearchClassBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionStudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListNewBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SubmitSignInfoBean;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.CourseDaily;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordFromSignatureDto;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.InterfaceList;
import com.gta.zssx.pub.InterfaceName;
import com.gta.zssx.pub.http.HttpMethod;
import com.gta.zssx.pub.http.HttpResult;
import com.gta.zssx.pub.http.ModelSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/16.
 * @since 1.0.0
 */
public class ClassDataManager {

/*    private static ClassInfoDto sClassInfoDto = new ClassInfoDto();
    */
    /**
     * 签名确认的缓存
     *//*
    public void  setClassInfoDto(ClassInfoDto classInfoDto){
        sClassInfoDto = classInfoDto;
    }

    public ClassInfoDto getClassInfoDto(){
        return sClassInfoDto;
    }

    public void distroyClassInfoDtoCache(){
        sClassInfoDto = new ClassInfoDto();
    }*/


    private static DataCache sDataCache = new DataCache();


    public static class DataCache {

        /**
         * 修改时提交的原原班级信息
         */
        private OriginalClassBean mOriginalClassBean;
        /**
         * 提交审核的课程对象
         */
        List<PostSignBean.SectionBean> mSectionBeans;
        /**
         * 课程名称
         */
        private String className;

        /**
         * 领导审核日
         */
        private ApproveBean mApproveBean;
        /**
         * 登记日期
         */
        private String signDate;
        /**
         * 缓存选择的节次
         */
        private Set<SectionBean> mSectionBeen;

        private List<SectionBean> mSectionBeanList;

        public List<SectionBean> getSectionBeanList() {
            return mSectionBeanList;
        }

        public void setSectionBeanList(List<SectionBean> sectionBeanList) {
            mSectionBeanList = sectionBeanList;
        }

        /**
         * 缓存学生名单
         */
        private List<StudentListNewBean> sStudentListBeen;

        /**
         * 缓存老师选择的班级
         */
        private List<ClassDisplayBean> sClassDisplayBeen;

        /**
         * 保存登记课程对象
         */
        private ClassDisplayBean sClassDisplayBean;

        private RegisteredRecordFromSignatureDto mSignatureDto;

        public RegisteredRecordFromSignatureDto getSignatureDto() {
            return mSignatureDto;
        }

        public void setSignatureDto(RegisteredRecordFromSignatureDto signatureDto) {
            mSignatureDto = signatureDto;
        }

        public void setSection(Set<SectionBean> sectionBeen) {
            mSectionBeen = sectionBeen;
        }

        public Set<SectionBean> getSection() {
            return mSectionBeen;
        }

        public void setStudentList(List<StudentListNewBean> studentListBeen) {

            sStudentListBeen = studentListBeen;
        }

        public List<StudentListNewBean> getStudentList() {
            return sStudentListBeen;
        }

        public void setClassDisplay(List<ClassDisplayBean> classDisplayBeen) {

            sClassDisplayBeen = classDisplayBeen;
        }

        public List<ClassDisplayBean> getClassDisplay() {
            return sClassDisplayBeen;
        }

        public ClassDisplayBean getsClassDisplayBean() {
            return sClassDisplayBean;
        }

        public void setsClassDisplayBean(ClassDisplayBean sClassDisplayBean) {
            this.sClassDisplayBean = sClassDisplayBean;
        }

        public String getSignDate() {
            return signDate;
        }

        public void setSignDate(String signDate) {
            this.signDate = signDate;
        }

        public ApproveBean getApproveBean() {
            return mApproveBean;
        }

        public void setApproveBean(ApproveBean approveBean) {
            mApproveBean = approveBean;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public List<PostSignBean.SectionBean> getPostSectionBeans() {
            return mSectionBeans;
        }

        public void setPostSectionBeans(List<PostSignBean.SectionBean> sectionBeans) {
            mSectionBeans = sectionBeans;
        }

        public OriginalClassBean getOriginalClassBean() {
            return mOriginalClassBean;
        }

        public void setOriginalClassBean(OriginalClassBean originalClassBean) {
            mOriginalClassBean = originalClassBean;
        }
    }

    public static DataCache getDataCache() {

        return sDataCache;
    }

    /**
     * 清除缓存
     */
    public static void destroyDataCache() {
        sDataCache = new DataCache();
    }

    /**
     * 请求班级选择列表
     *
     * @return
     */
    public static Observable<ClassChooseBean> loadClass() {
        InterfaceList lInterfaceList = getInterfaceList();
        return HttpMethod.getInstance().call(lInterfaceList.loadClass());
    }

    private static InterfaceList getInterfaceList() {
        return HttpMethod
                .getInstance()
                .retrofitClient(CourseDaily.getInstance().getmServerAddress())
                .create(InterfaceList.class);
    }

    /**
     * 请求班级选择列表
     *
     * @return
     */
    public static Observable<List<ClassChooseBean01>> loadClass01() {
        InterfaceList lInterfaceList = getInterfaceList();
        return HttpMethod.getInstance().call(lInterfaceList.loadClass01());
    }

    /**
     * 模糊搜索班级
     *
     * @param className
     * @return
     */
    public static Observable<List<SearchClassBean>> search(String className) {
        InterfaceList lInterfaceList = getInterfaceList();
        return HttpMethod.getInstance().call(lInterfaceList.search(className));
    }

    /**
     * 获取添加的班级列表
     *
     * @param teacherId
     * @return
     */
    public static Observable<List<ClassDisplayBean>> getClassList(String teacherId) {
        InterfaceList lInterfaceList = getInterfaceList();
        return HttpMethod.getInstance()
                .call(lInterfaceList.getClassList(teacherId))
                .doOnNext(classDisplayBeen -> getDataCache().setClassDisplay(classDisplayBeen));
    }


    public static Observable<List<ClassDisplayBean>> getClassListTest(String teacherId, Context context) {
        Observable<HttpResult<List<ClassDisplayBean>>> lClassList = HttpMethod
                .getInstance()
                .retrofitClient(CourseDaily.getInstance().getmServerAddress())
                .create(InterfaceList.class)
                .getClassList(teacherId);
        return new ModelSource<>(context, lClassList)
                .setInterfaceName(InterfaceName.GET_CLASS_LIST)
                .setIsDataValidFun(new Func1<List<ClassDisplayBean>, Boolean>() {
                    @Override
                    public Boolean call(List<ClassDisplayBean> classDisplayBeen) {
                        return classDisplayBeen != null;
                    }
                })
                .setMemoryFun(new Func1<Observable<HttpResult<List<ClassDisplayBean>>>, List<ClassDisplayBean>>() {
                    @Override
                    public List<ClassDisplayBean> call(Observable<HttpResult<List<ClassDisplayBean>>> httpResultObservable) {
                        return getDataCache().getClassDisplay();
                    }
                }, new Action1<List<ClassDisplayBean>>() {
                    @Override
                    public void call(List<ClassDisplayBean> classDisplayBeen) {
                        getDataCache().setClassDisplay(classDisplayBeen);
                    }
                })
                .setMode(ModelSource.MODE_MND)
                .Observable();
    }


    /**
     * 获取课堂登记情况
     *
     * @param teacherId
     * @param classId
     * @param signData
     * @return
     */
    public static Observable<List<SectionBean>> getClassStatus(String teacherId, int classId, String signData) {
        InterfaceList lInterfaceList = getInterfaceList();
        return HttpMethod.getInstance().call(lInterfaceList.getClassStatus(teacherId, String.valueOf(classId), signData));
    }

    public static Observable<List<SectionBean>> getClassStatusTest(String teacherId, int classId, String signData, Context context) {

        Observable<HttpResult<List<SectionBean>>> lClassStatus = HttpMethod
                .getInstance()
                .retrofitClient(CourseDaily.getInstance().getmServerAddress())
                .create(InterfaceList.class)
                .getClassStatus(teacherId, classId + "", signData);

        return new ModelSource<>(context, lClassStatus)
                //设置硬盘保存的key
                .setInterfaceName(InterfaceName.GET_SIGN_STATUS)
                //判断数据有效性
                .setIsDataValidFun(new Func1<List<SectionBean>, Boolean>() {
                    @Override
                    public Boolean call(List<SectionBean> sectionBeen) {
                        return sectionBeen != null;
                    }
                })
                //设置内存缓存
                .setMemoryFun(new Func1<Observable<HttpResult<List<SectionBean>>>, List<SectionBean>>() {
                    @Override
                    public List<SectionBean> call(Observable<HttpResult<List<SectionBean>>> httpResultObservable) {
                        return getDataCache().getSectionBeanList();
                    }
                }, new Action1<List<SectionBean>>() {
                    @Override
                    public void call(List<SectionBean> sectionBeen) {
                        getDataCache().setSectionBeanList(sectionBeen);
                    }
                })
                //设置缓存模式
                .setMode(ModelSource.MODE_DNM)
                .Observable();

    }


    /**
     * 获取学生名单
     *
     * @param classId
     * @param signData
     * @return
     */
    public static Observable<List<StudentListNewBean>> getStudentList(int classId, String signData) {
        InterfaceList lInterfaceList = getInterfaceList();
        return HttpMethod
                .getInstance()
                .call(lInterfaceList.getStudentList(classId, signData))
                .doOnNext(new Action1<List<StudentListNewBean>>() {
                    @Override
                    public void call(List<StudentListNewBean> studentListBeen) {
                        getDataCache().setStudentList(studentListBeen);
                    }
                });
    }


    /**
     * 添加班级
     *
     * @param addClassBeen
     * @return
     */
    public static Observable<String> addClass(List<AddClassBean> addClassBeen) {
        InterfaceList lInterfaceList = getInterfaceList();
        return HttpMethod.getInstance().call(lInterfaceList.addClass(addClassBeen));
    }


    /**
     * 获取领导审核日
     *
     * @return
     */
    public static Observable<ApproveBean> getApproveDate() {
        InterfaceList lInterfaceList = getInterfaceList();
        return HttpMethod.getInstance()
                .call(lInterfaceList.getApproveDate())
                .doOnNext(new Action1<ApproveBean>() {
                    @Override
                    public void call(ApproveBean approveBean) {
                        getDataCache().setApproveBean(approveBean);
                    }
                });
    }


    /**
     * 提交审核
     *
     * @param postSignBean
     * @return
     */
    public static Observable<String> postSign(PostSignBean postSignBean) {
        InterfaceList lInterfaceList = HttpMethod
                .getInstance()
                .retrofitClient(CourseDaily.getInstance().getmServerAddress(), 10)
                .create(InterfaceList.class);
        return HttpMethod.getInstance().call(lInterfaceList.postSignInfo(postSignBean));
    }


    /**
     * 提交审核--新
     *
     * @param submitSignInfoBean
     * @return
     */
    public static Observable<String> postSignNew(SubmitSignInfoBean submitSignInfoBean) {
        List<SubmitSignInfoBean> list = new ArrayList<>();
        list.add(submitSignInfoBean);
        InterfaceList lInterfaceList = HttpMethod
                .getInstance()
                .retrofitClient(CourseDaily.getInstance().getmServerAddress(), 10)
                .create(InterfaceList.class);
        return HttpMethod.getInstance().call(lInterfaceList.postSignInfoNew(list));
    }

    /**
     * 获取老师带的班级
     *
     * @param teacherId
     * @return
     */
    public static Observable<UserBean> getTeacherClassUpdate(String teacherId) {
        InterfaceList lInterfaceList = getInterfaceList();
        return HttpMethod.getInstance().call(lInterfaceList.getTeacherClassUpdate(teacherId));
    }


    /**
     * 删除添加班级
     *
     * @param
     * @return
     */
    public static Observable<String> deleteTeacherClass(String ClassId, String TeacherId) {
        InterfaceList lInterfaceList = getInterfaceList();
        return HttpMethod.getInstance().call(lInterfaceList.deleteTeacherClass(ClassId, TeacherId));
    }

    /**
     * 获取教师所教课程 19
     * @param teacherDbid 教师Id
     * @param workDate  当前时间
     * @return
     */
    public static Observable<List<DetailItemShowBean.CourseInfoBean>> getTeacherTaughtCourse(String teacherDbid, String workDate) {
        InterfaceList lInterfaceList = getInterfaceList();
        return HttpMethod.getInstance().call(lInterfaceList.getTeacherTaughtCourse(teacherDbid, workDate));
    }

    /**
     * 搜索课程 20
     * @param keyWords 关键字
     * @return
     */
    public static Observable<List<DetailItemShowBean.CourseInfoBean>> searchAllCourseByKeyWord(String keyWords) {
        InterfaceList lInterfaceList = getInterfaceList();
        return HttpMethod.getInstance().call(lInterfaceList.searchAllCourseByKeyWord(keyWords));
    }

    /**
     * 添加自定义课程 21
     * @param courseName 课程名
     * @param teacherId  创建者
     * @return
     */
    public static Observable<DetailItemShowBean.CourseInfoBean> submitUserDefinedCourse(String courseName,String teacherId) {
        InterfaceList lInterfaceList = getInterfaceList();
        AddCourseBeen addCourseBeen = new AddCourseBeen();
        addCourseBeen.setCourseName(courseName);
        addCourseBeen.setCreatorId(teacherId);
        return HttpMethod.getInstance().call(lInterfaceList.submitUserDefinedCourse(addCourseBeen));
    }

    /**
     * 节次课表的默认老师和课程
     * @param signDate
     * @param classId
     * @param sectionId
     * @return
     */
    public static Observable<DefaultRegistInfoBean> getClassSectionDefaultTeachersAndCoures(String signDate,int classId,int sectionId,String teacherId){
        InterfaceList lInterfaceList = getInterfaceList();
        return HttpMethod.getInstance().call(lInterfaceList.GetClassSectionDefaultTeachersAndCoures(signDate,classId,sectionId,teacherId));
    }

    /**
     * 测试修改后的接口8
     */
    public static Observable<List<SectionStudentListBean>> getClassDiarySectionStudentMarkedDetail(String signDate, int classId, String sectionId){
        InterfaceList lInterfaceList = getInterfaceList();
        return HttpMethod.getInstance().call(lInterfaceList.GetClassDiarySectionStudentMarkedDetail(signDate,classId,sectionId));
    }


    /**
     * 查询要登记的节次是否有被登记过
     */
    public static Observable<List<String>> checkSctionIsHaveBeenRegister(String signDate, int classId, String sectionId){
        InterfaceList lInterfaceList = getInterfaceList();
        return HttpMethod.getInstance().call(lInterfaceList.checkSctionIsHaveBeenRegister(signDate,classId,sectionId));
    }

    /**
     * 日志统计-图表
     */
    public static Observable<LogChartBean> getLogChart(String date) {
        InterfaceList lInterfaceList = getInterfaceList();
        return HttpMethod.getInstance().call(lInterfaceList.getLogChart(date));
    }

    /**
     * 日志统计-部门列表
     */
    public static Observable<List<LogListBean>> getLogList(String date) {
        InterfaceList lInterfaceList = getInterfaceList();
        return HttpMethod.getInstance().call(lInterfaceList.getLogList(date));
    }

    /**
     * 日志统计-班级列表
     */
    public static Observable<List<LogListBean>> getLogClassList(String date, String mId) {
        InterfaceList lInterfaceList = getInterfaceList();
        return HttpMethod.getInstance().call(lInterfaceList.getLogClassList(date, mId));
    }

}