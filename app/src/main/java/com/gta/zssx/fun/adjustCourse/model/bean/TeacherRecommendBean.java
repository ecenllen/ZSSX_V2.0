package com.gta.zssx.fun.adjustCourse.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/10/28.
 * @since 1.0.0
 */
public class TeacherRecommendBean implements Serializable {


    /**
     * name : 陈碧兰[T20025]
     * id : 11
     * open : false
     * count : null
     * checked : false
     * nocheck : false
     * pId : 0
     * times : null
     * chkDisabled : false
     * isParent : false
     */

    private List<SameClassTeacherBean> sameClassTeacher;
    /**
     * name : 卢永辉[T20037]
     * id : 17
     * open : false
     * count : null
     * checked : false
     * nocheck : false
     * pId : 0
     * times : null
     * chkDisabled : false
     * isParent : false
     */

    private List<SameClassTeacherBean> sameDeptTeacher;
    /**
     * name : 冯子川[T20015]
     * id : 6
     * open : false
     * count : null
     * checked : false
     * nocheck : false
     * pId : 0
     * times : null
     * chkDisabled : false
     * isParent : false
     */

    private List<SameClassTeacherBean> sameSubjectTeacher;

    public List<SameClassTeacherBean> getSameClassTeacher() {
        return sameClassTeacher;
    }

    public void setSameClassTeacher(List<SameClassTeacherBean> sameClassTeacher) {
        this.sameClassTeacher = sameClassTeacher;
    }

    public List<SameClassTeacherBean> getSameDeptTeacher() {
        return sameDeptTeacher;
    }

    public void setSameDeptTeacher(List<SameClassTeacherBean> sameDeptTeacher) {
        this.sameDeptTeacher = sameDeptTeacher;
    }

    public List<SameClassTeacherBean> getSameSubjectTeacher() {
        return sameSubjectTeacher;
    }

    public void setSameSubjectTeacher(List<SameClassTeacherBean> sameSubjectTeacher) {
        this.sameSubjectTeacher = sameSubjectTeacher;
    }

    public static class SameClassTeacherBean implements Serializable{
        private String name;
        private String id;
        private boolean open;
        private String count;
        private boolean checked;
        private boolean nocheck;
        private String teacherBId;
        private String pId;
        private String times;
        private boolean chkDisabled;
        private boolean isParent;

        public String getTeacherBId() {
            return teacherBId;
        }

        public void setTeacherBId(String teacherBId) {
            this.teacherBId = teacherBId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isOpen() {
            return open;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }

        public Object getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public boolean isNocheck() {
            return nocheck;
        }

        public void setNocheck(boolean nocheck) {
            this.nocheck = nocheck;
        }

        public String getPId() {
            return pId;
        }

        public void setPId(String pId) {
            this.pId = pId;
        }

        public Object getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public boolean isChkDisabled() {
            return chkDisabled;
        }

        public void setChkDisabled(boolean chkDisabled) {
            this.chkDisabled = chkDisabled;
        }

        public boolean isIsParent() {
            return isParent;
        }

        public void setIsParent(boolean isParent) {
            this.isParent = isParent;
        }
    }

    public static class SameDeptTeacherBean {
        private String name;
        private String id;
        private boolean open;
        private String count;
        private boolean checked;
        private String teacherBId;
        private boolean nocheck;
        private String pId;
        private String times;
        private boolean chkDisabled;
        private boolean isParent;

        public String getTeacherBId() {
            return teacherBId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isOpen() {
            return open;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }

        public Object getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public boolean isNocheck() {
            return nocheck;
        }

        public void setNocheck(boolean nocheck) {
            this.nocheck = nocheck;
        }

        public String getPId() {
            return pId;
        }

        public void setPId(String pId) {
            this.pId = pId;
        }

        public Object getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public boolean isChkDisabled() {
            return chkDisabled;
        }

        public void setChkDisabled(boolean chkDisabled) {
            this.chkDisabled = chkDisabled;
        }

        public boolean isIsParent() {
            return isParent;
        }

        public void setIsParent(boolean isParent) {
            this.isParent = isParent;
        }
    }

    public static class SameSubjectTeacherBean {
        private String name;
        private String id;
        private boolean open;
        private String teacherBId;
        private String count;
        private boolean checked;
        private boolean nocheck;
        private String pId;
        private String times;
        private boolean chkDisabled;
        private boolean isParent;

        public String getTeacherBId() {
            return teacherBId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isOpen() {
            return open;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }

        public Object getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public boolean isNocheck() {
            return nocheck;
        }

        public void setNocheck(boolean nocheck) {
            this.nocheck = nocheck;
        }

        public String getPId() {
            return pId;
        }

        public void setPId(String pId) {
            this.pId = pId;
        }

        public Object getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public boolean isChkDisabled() {
            return chkDisabled;
        }

        public void setChkDisabled(boolean chkDisabled) {
            this.chkDisabled = chkDisabled;
        }

        public boolean isIsParent() {
            return isParent;
        }

        public void setIsParent(boolean isParent) {
            this.isParent = isParent;
        }
    }
}
