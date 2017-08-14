package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/10/20.
 */

public class Tasks implements Serializable {
    /**
     * 总任务数
     */
    private int totalCount;
    /**
     * 任务List
     */
    List<TaskInfo> tasks;

    public static class TaskInfo implements Serializable{

        /**
         * 任务id
         */
        private String taskId;
        /**
         * 主题
         */
        private String topic;

        /**
         * 类别
         */
        private String category;

        /**
         * 接待部门
         */
        private String department;

        /**
         * 任务时间
         */
        private String taskTime;

        /**
         * 任务状态：进行中、未完成、已完成
         */
        private int status;

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getTaskTime() {
            return taskTime;
        }

        public void setTaskTime(String taskTime) {
            this.taskTime = taskTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public List<TaskInfo> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskInfo> tasks) {
        this.tasks = tasks;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
