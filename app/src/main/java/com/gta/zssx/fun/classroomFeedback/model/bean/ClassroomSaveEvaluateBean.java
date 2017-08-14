package com.gta.zssx.fun.classroomFeedback.model.bean;

import java.io.Serializable;

/**
 * [Description]
 * <p> 保存评价实体
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/6/28 10:27.
 * @since 2.0.0
 */
public class ClassroomSaveEvaluateBean implements Serializable {
    private int ClassId;
    private int Weekly;
    private String AuditContent;
    private int ClassTeachId;

    public int getClassTeachId () {
        return ClassTeachId;
    }

    public void setClassTeachId (int classTeachId) {
        ClassTeachId = classTeachId;
    }

    public int getClassId () {
        return ClassId;
    }

    public void setClassId (int classId) {
        ClassId = classId;
    }

    public int getWeekly () {
        return Weekly;
    }

    public void setWeekly (int weekly) {
        Weekly = weekly;
    }

    public String getContent () {
        return AuditContent;
    }

    public void setContent (String content) {
        AuditContent = content;
    }
}
