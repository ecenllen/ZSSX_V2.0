package com.gta.zssx.fun.coursedaily.registerrecord.model;

import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ClassInfoDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.DeleteRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.DetailRegisteredRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.MyClassRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.MyClassSubTabInfoDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordFromSignatureDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.SaveCacheDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ServerTimeDto;
import com.gta.zssx.pub.http.HttpMethod;
import com.gta.zssx.pub.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Observable;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 * @author Created by lan.zheng on 2016/6/12.
 * @since 1.0.0
 * * 类似于DAO的类，用于传数据入服务器和从服务器获取数据，在这里叫Manager，此类用于“已登记”相关的数据获取和传输
 */
public class RegisteredRecordManager extends BaseManager{
    private static final Class mInterfaceListClass = com.gta.zssx.pub.InterfaceList.class;
    /**
     * 记录的数据存储 -- 多个班级的页面
     */
    public static MyClassRecordDataCache sMyClassRecordDataCache = new MyClassRecordDataCache();
    public static List<MyClassRecordDto> mMyClassRecordDtos; //用于多个班级
    public static MyClassRecordDataCache getMyClassRecordDataCache() {
        return sMyClassRecordDataCache;
    }
    public static void destroyMyClassRecordDataCache() {
        sMyClassRecordDataCache = new MyClassRecordDataCache();
    }

    public static class MyClassRecordDataCache {

        //初始化数组
        public void initMyClassRecordDtos(List<String> classIdList){
            mMyClassRecordDtos = new ArrayList<>();
            for(int i = 0; i< classIdList.size();i++){
                MyClassRecordDto lMyClassRecordDto = new MyClassRecordDto();
                lMyClassRecordDto.setClassID(Integer.valueOf(classIdList.get(i)));
                mMyClassRecordDtos.add(lMyClassRecordDto);
            }
        }

        //获得我的班级多tab数据
        public List<MyClassRecordDto> getMyClassRecordDtos() {
            return mMyClassRecordDtos;
        }

        //更新我的班级多tab数据
        public void updateMyClassRecordDtos(int ClassId, MyClassRecordDto myClassRecordDto, boolean isUpdate, String date, String week){
            if(myClassRecordDto != null && isUpdate){
                myClassRecordDto.setDate(date);
                myClassRecordDto.setWeek(week);
                for(int i = 0 ;i < mMyClassRecordDtos.size();i++){
                    if(mMyClassRecordDtos.get(i).getClassID() == ClassId){
                        mMyClassRecordDtos.set(i,myClassRecordDto);
                    }
                }
            }
        }

        //获取我的班级多tab其中一个tab的数据
        public MyClassRecordDto getMyClassRecordSingleDto(int classId){
            MyClassRecordDto lMyClassRecordDto = new MyClassRecordDto();
            for(int i = 0; i < mMyClassRecordDtos.size();i++){
                if(mMyClassRecordDtos.get(i).getClassID() == classId){
                    lMyClassRecordDto =  mMyClassRecordDtos.get(i);
                }
            }
            return lMyClassRecordDto;
        }

        //数据信息存储
        MyClassSubTabInfoDto mMyClassSubTabInfoDto;
        public void setMyClassSubTabInfoDto(MyClassSubTabInfoDto myClassSubTabInfoDto){
            mMyClassSubTabInfoDto = myClassSubTabInfoDto;
        }

        public MyClassSubTabInfoDto getMyClassSubTabInfoDto(){
            return mMyClassSubTabInfoDto;
        }

    }

    /**
     * 记录的数据存储 -- 单个班级的页面
     */
    private static DataCache sDataCache = new DataCache();
    public static List<MyClassRecordDto> mRegisteredRecordDto;
    public static DataCache getDataCache() {
        return sDataCache;
    }

    public static void destroyDataCache() {
        sDataCache = new DataCache();
    }

    public static class DataCache {

        public void setRecordFromSignatureDtos(List<MyClassRecordDto> registeredRecordDto) {
            mRegisteredRecordDto = registeredRecordDto;
        }

        public List<MyClassRecordDto> getRecordFromSignatureDtos() {
            return mRegisteredRecordDto;
        }

        public void setDateandWeek(String date,String week){
            if(mMyClassRecordDtos  != null){
                mMyClassRecordDtos.get(0).setDate(date);
                mMyClassRecordDtos.get(0).setWeek(week);
            }
        }
    }

    /**
     * 记录的数据存储--“已登记”
     */
    public static SaveCacheDto mSaveCacheDto;
    public static SaveCacheDto getSaveCacheDto() {
        return mSaveCacheDto;
    }
    public static void setSaveCacheDto(SaveCacheDto saveCacheDto) {
        mSaveCacheDto = saveCacheDto;
    }
    /**
     * 记录的数据存储--从签名确认 或 已登记的下一个页面来的数据
     */
    public static SaveCacheDto mSaveCacheDtoFromSignature;
    public static SaveCacheDto getSaveCacheDtoFromSignature() {
        return mSaveCacheDtoFromSignature;
    }
    public static void setSaveCacheDtoFromSignature(SaveCacheDto saveCacheDto) {
        mSaveCacheDtoFromSignature = saveCacheDto;
    }

    /**
     * 记录数据存储--“我的班级”--单班级
     */
    public static SaveCacheDto mSaveCacheDtoDate;
    public static SaveCacheDto getSaveCacheDtoDate() {
        return mSaveCacheDtoDate;
    }
    public static void setSaveCacheDtoDate(SaveCacheDto saveCacheDto) {
        mSaveCacheDtoDate = saveCacheDto;
    }

    /**
     * 获取系统时间
     * @param
     * @return
     */
    public static Observable<ServerTimeDto> getServerTime() {
        //去获取服务器时间
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass)
                .getServerTime());
    }

    /**
     * 一段时间的已登记课堂日志
     * @param teacherId
     * @param beginDate
     * @param endDate
     * @param
     * @return
     */
    public static Observable<RegisteredRecordDto> getRegisterRecordList(String teacherId, String beginDate, String endDate, int pageCount) {
        //获取一段时间的已登记课堂日志
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass)
                .getRegisteredRecordList(teacherId,beginDate,endDate,pageCount));
    }

    /**
     * 详细已登记课堂日志内容
     * @param classInfoDto
     * @return
     */
    public static Observable<DetailRegisteredRecordDto> getRegisteredRecordDetailList(ClassInfoDto classInfoDto) {

        String teacherId = classInfoDto.getTeacherID();
        String signDate = classInfoDto.getSignDate();
        String classId = classInfoDto.getClassID();
        int sectionId = classInfoDto.getSectionID();

        return HttpMethod.getInstance().call(getInterfaceList(com.gta.zssx.pub.InterfaceList.class)
                .getRegisteredRecordDetailList(teacherId,signDate,classId,sectionId));
    }

    /**
     * 某一天的已登记课堂日志
     * @param teacherId
     * @param signDate
     * @param classId
     * @param
     * @return
     */
    public static Observable<List<RegisteredRecordFromSignatureDto>> getRegisteredRecordFromSignatureList(String teacherId, String signDate, String classId,boolean isGetClassIDAllInfo) {

        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass)
                .getRegisteredRecordFromSignatureList(teacherId,signDate,classId,isGetClassIDAllInfo));
    }

    /**
     * 删除某条登记信息
     * @param deleteRecordDto
     * @return
     */
    public static Observable<String> deleteSignInfo(List<DeleteRecordDto> deleteRecordDto) {
//        int classId = deleteRecordDto.get(0).getClassID();  //班级id
//        String signDate = deleteRecordDto.get(0).getSignDate(); //日期
//        int sectionId = deleteRecordDto.get(0).getSectionID(); //节次
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass)
                .deleteSignInfo(deleteRecordDto));
    }


    /**
     * 获取我的班级
     *
     * @param teacherId
     * @param date
     * @param
     * @return
     */
    public static Observable<List<MyClassRecordDto>> getMyclass(String teacherId, String date, int classId) {

        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass)
                .getMyClassRecordList(teacherId, date,classId));
                /*.doOnNext(new Action1<List<MyClassRecordDto>>() {
                    @Override
                    public void call(List<MyClassRecordDto> recordFromSignatureDtos) {
                        if(!MyClassRecordFragment.mIsSubTab){
                            //单页面
                            getDataCache().setRecordFromSignatureDtos(recordFromSignatureDtos);
                        }
                    }
                });*/
    }

    public static String getWeek(String date) {
        SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String week = "";
        //获取星期几
        try {
            long signTimeInMillis = lSimpleDateFormat.parse(date).getTime();
            Calendar lCalendar = Calendar.getInstance();
            lCalendar.setTimeInMillis(signTimeInMillis);
            int weekDay =  lCalendar.get(Calendar.DAY_OF_WEEK);
            week = StringUtils.changeWeekDaytoString(weekDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return week;
    }

}