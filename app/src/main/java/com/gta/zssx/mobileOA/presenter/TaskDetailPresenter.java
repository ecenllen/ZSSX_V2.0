package com.gta.zssx.mobileOA.presenter;

import android.app.Activity;
import android.content.Intent;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.mobileOA.model.bean.SubTask;
import com.gta.zssx.mobileOA.model.bean.TaskDetail;
import com.gta.zssx.mobileOA.model.bean.Tasks;
import com.gta.zssx.mobileOA.view.TaskDetailView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by xiaoxia.rang on 2016/10/26.
 */

public class TaskDetailPresenter  extends BasePresenter<TaskDetailView>{
    private String taskId;

    public void getTaskDetail(Activity activity){
        Intent intent = activity.getIntent();
        Tasks.TaskInfo taskInfo = (Tasks.TaskInfo)intent.getSerializableExtra("task");
        taskId = taskInfo.getTaskId();
        TaskDetail detail = new TaskDetail();
        detail.setType(new Random().nextInt(2));
        detail.setGoal("加快信息化建设");
        detail.setDept("李晓华");
        detail.setContent("加快信息化建设");
        detail.setEndTime("2016-11-05 18:00");
        detail.setStartTime("2016-11-05 11");
        detail.setTopic("关于李书记到校访谈接待安排");

        List<SubTask> subTasks = new ArrayList<>();
        int count = 1;
        if(detail.getType() == 1){
            count = new Random().nextInt(5);
        }
        for(int i = 0;i<count;i++){
            SubTask task = new SubTask();
            task.setTopic("接待任务");
            task.setPerson("张三");
            task.setStatus(new Random().nextInt(3));
            task.setTime("2016-11-05 18:00");
            subTasks.add(task);
        }
        detail.setSubTasks(subTasks);
        getView().showTaskDetail(detail);
    }

    public String getTaskId() {
        return taskId;
    }

    /**
     * 修改任务状态
     * @param status
     */
    public void changeTaskStatus(int status){

    }
}
