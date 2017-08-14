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
public class TeacherBean implements Serializable {


    /**
     * orgId : 1
     * teacherList : [{"orgId":1,"teacherName":"胡燕时[H00046]","teacherId":null,"teacherBId":"05a8c7dc-c49a-4e54-ac3c-7e1caecaf38f"},{"orgId":1,"teacherName":"高仲燕[H00047]","teacherId":null,"teacherBId":"182fbd58-7f8a-4721-a695-8435fe52fdd3"},{"orgId":1,"teacherName":"成绩教师玲[T1018]","teacherId":null,"teacherBId":"1b6daf27-1787-432c-9766-e096d557560a"},{"orgId":1,"teacherName":"甘元芬[H00040]","teacherId":null,"teacherBId":"1da95cd8-ef7e-462f-bb2f-292bb3db5612"},{"orgId":1,"teacherName":"李光陆[H00044]","teacherId":null,"teacherBId":"34add1ac-3133-47a5-8fbb-e4fbaace5f7a"},{"orgId":1,"teacherName":"秦朝方[H00039]","teacherId":null,"teacherBId":"46060c47-2e5e-4238-9dc6-88414ca8fa08"},{"orgId":1,"teacherName":"毛世方[H00038]","teacherId":null,"teacherBId":"52855033-2363-4ea9-9206-67c2d22359cc"},{"orgId":1,"teacherName":"丁拥军[H00035]","teacherId":null,"teacherBId":"8c289938-d399-4c40-80f6-6efa1ea6d708"},{"orgId":1,"teacherName":"郑灿炎[H00042]","teacherId":null,"teacherBId":"94748344-7475-4b44-b827-9fae1bedd016"},{"orgId":1,"teacherName":"李秀桃[H00037]","teacherId":null,"teacherBId":"c1e84d15-9e40-4ba1-a410-3451a679376f"},{"orgId":1,"teacherName":"李光明[H00043]","teacherId":null,"teacherBId":"cb1f54b9-5ada-49cc-a70d-e868850b2558"},{"orgId":1,"teacherName":"谢勇军[H00034]","teacherId":null,"teacherBId":"cca626a4-a4b5-4247-b75f-6019e410205d"},{"orgId":1,"teacherName":"杨锡南[H00041]","teacherId":null,"teacherBId":"f51e0cd8-c002-43ee-9b82-d6c2d9edf41f"}]
     * parentId : null
     * deptName : 中山沙溪理工学校
     */

    private int orgId;
    private String parentId;
    private String deptName;
    /**
     * orgId : 1
     * teacherName : 胡燕时[H00046]
     * teacherId : null
     * teacherBId : 05a8c7dc-c49a-4e54-ac3c-7e1caecaf38f
     */

    private List<TeacherListBean> teacherList;

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public List<TeacherListBean> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<TeacherListBean> teacherList) {
        this.teacherList = teacherList;
    }

    public static class TeacherListBean implements Serializable{
        private int orgId;
        private String teacherName;
        private String teacherId;
        private String teacherBId;
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getOrgId() {
            return orgId;
        }

        public void setOrgId(int orgId) {
            this.orgId = orgId;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public String getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(String teacherId) {
            this.teacherId = teacherId;
        }

        public String getTeacherBId() {
            return teacherBId;
        }

        public void setTeacherBId(String teacherBId) {
            this.teacherBId = teacherBId;
        }
    }
}
