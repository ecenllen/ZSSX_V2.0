package com.gta.zssx.mobileOA.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ServerTimeDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.RegisteredRecordManager;
import com.gta.zssx.mobileOA.model.OAMainModel;
import com.gta.zssx.mobileOA.model.bean.Tasks;
import com.gta.zssx.mobileOA.view.TaskMainView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by xiaoxia.rang on 2016/10/19.
 */

public class TaskMainPresenter extends BasePresenter<TaskMainView> {

    Tasks tasks;
    public Subscription mSubscribe;

    public void getTasks(int opStatus) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();

        mSubscribe = RegisteredRecordManager.getServerTime().subscribe(new Subscriber<ServerTimeDto>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ServerTimeDto serverTimeDto) {
                if(isViewAttached()){
                    getView().hideDialog();
                    Tasks tasks = new Tasks();
                    List<Tasks.TaskInfo> taskInfos = new ArrayList<Tasks.TaskInfo>();
                    for(int i = 0 ;i<10;i++){
                        Tasks.TaskInfo taskInfo =  new Tasks.TaskInfo();
                        taskInfo.setTopic("任务"+i);
                        taskInfo.setDepartment("接待部门" +i);
                        taskInfo.setStatus(i%3);
                        taskInfo.setTaskTime("2016-10-26");
                        taskInfo.setCategory("接待类型" + i%3);
                        taskInfos.add(taskInfo);
                    }
                    tasks.setTasks(taskInfos);
                    getView().showTasksList(opStatus,taskInfos);
                }
            }
        });
        mCompositeSubscription.add(mSubscribe);
    }

}
