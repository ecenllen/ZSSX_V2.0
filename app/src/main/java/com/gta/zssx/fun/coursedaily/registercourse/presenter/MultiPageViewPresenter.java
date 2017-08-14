package com.gta.zssx.fun.coursedaily.registercourse.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.coursedaily.registercourse.view.MultiPageView;
import com.gta.zssx.fun.coursedaily.registerrecord.model.RegisteredRecordManager;
import com.gta.zssx.pub.exception.CustomException;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/7/20.
 * @since 1.0.0
 */
public class MultiPageViewPresenter extends BasePresenter<MultiPageView> {

    public int mLastPage;

    /**
     * 获取服务器时间
     *
     * @param TeacherID
     */
    public void getServerTime(final String TeacherID) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mCompositeSubscription.add(RegisteredRecordManager.getServerTime()
                .subscribe(serverTimeDto -> {
                    String lDate = serverTimeDto.getDate();
                    String[] lSplit = lDate.split(" ");
                    String lNowDate = lSplit[0];


                    DateTime lDateTime = new DateTime(lNowDate);
                    Calendar lCalendar = Calendar.getInstance();
                    lCalendar.setTime(lDateTime.toDate());
                    lCalendar.set(Calendar.DAY_OF_MONTH, 1);
                    Date lFirstDateOfMonth = lCalendar.getTime();

                    lCalendar.set(Calendar.DAY_OF_MONTH, lCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));

                    Date lLastDateOfMonth = lCalendar.getTime();
                    DateTime lFirstDatetime = new DateTime(lFirstDateOfMonth);
                    DateTime lLastDatetime = new DateTime(lLastDateOfMonth);

                    String lFirstString = lFirstDatetime.toString("yyyy-MM-dd");
                    String lLastString = lLastDatetime.toString("yyyy-MM-dd");

                    if (getView() != null) {
                        getView().setDate(lNowDate, lFirstString, lLastString);
                    }
                    getRegisteredRecordData(TeacherID, lFirstString, lLastString, 1);


                }, throwable -> {
                    if (getView() != null) {
                        getView().hideDialog();
                        getView().onErrorHandle(throwable);
                    }

                }));
    }

    /**
     * 获取记录
     *
     * @param teacherId
     * @param beginDate
     * @param endDate
     * @param pageCount
     */
    public void getRegisteredRecordData(String teacherId, String beginDate, String endDate, int pageCount) {
        if (!isViewAttached()) {
            return;
        }


        mCompositeSubscription.add(RegisteredRecordManager.getRegisterRecordList(teacherId, beginDate, endDate, pageCount)
                .subscribe(registeredRecordDto -> {
                    if (getView() != null) {
                        if (pageCount == 1) {
                            getView().LoadFirst(registeredRecordDto.getDetail());
                        } else {
                            getView().LoadMore(pageCount, registeredRecordDto.getDetail());
                        }
                        getView().hideDialog();
                    }
                }, throwable -> {
                    if (getView() != null) {
                        if (throwable instanceof CustomException) {
                            CustomException lCustomException = (CustomException) throwable;
                            if (lCustomException.getCode() == CustomException.NO_MORE_RECORD) {
                                mLastPage = pageCount;
                                getView().LoadEnd(pageCount);
                            } else {
                                getView().onErrorHandle(throwable);
                            }
                        } else {
                            getView().onErrorHandle(throwable);
                        }
                        getView().hideDialog();
                    }
                }));
    }

    public String getFormatDate(Date date) {
        DateTime lDateTime = new DateTime(date);
        return lDateTime.toString("yyyy-MM-dd");
    }
}
