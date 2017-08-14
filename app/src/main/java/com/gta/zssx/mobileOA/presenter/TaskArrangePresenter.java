package com.gta.zssx.mobileOA.presenter;

import android.text.TextUtils;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.mobileOA.model.bean.TaskArrange;
import com.gta.zssx.mobileOA.view.TaskArrangeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/11/4.
 * 任务安排presenter
 */

public class TaskArrangePresenter extends BasePresenter<TaskArrangeView> {
    private List<TaskArrange> taskArranges = new ArrayList<>();

    public void addTaskArrange(){
        TaskArrange arrange = new TaskArrange();
        taskArranges.add(arrange);
        getView().showTaskArranges(taskArranges);
    }

    public void submitTaskArranges(){
        if(taskArranges.size() == 0){
            getView().showToast("无任务安排！");
            return;
        }
        for(int i = 0;i<taskArranges.size();i++){
            TaskArrange taskArrange = taskArranges.get(i);
            if(TextUtils.isEmpty(taskArrange.getItem())||TextUtils.isEmpty(taskArrange.getPlanTime())
                    || TextUtils.isEmpty(taskArrange.getPerson())||TextUtils.isEmpty(taskArrange.getDept())){
                getView().showUnCompleteDialog();
                return;
            }else{
                //组装提交数据
            }
        }
    }


}
