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
 * @author Created by Zhimin.Huang on 2016/10/27.
 * @since 1.0.0
 */
public class ApplyRecordBean implements Serializable{

    /**
     * totalPage : 1
     * maxPageCount : 15
     * pageNumList : [1]
     * isFirstPage : true
     * list : [{"modifierId":null,"recordId":1,"applyTime":"2016-10-25","adjustTime":"2016-10-27",
     * "auditTime":1477444996000,"auditorName":"系统管理员","applyType":"T","semester":20161,
     * "creatorId":"11de4615-d04f-4a7d-9cce-ec5622898766","createTime":1477444768000,
     * "modifiedTime":null,"applyPolysyllabicWord":"5,6","adjustPolysyllabicWord":"5,6",
     * "applyClassName":"班级人33","adjustClassName":"班级人33","applyCourseCode":"C001","adjustCourseCode"
     * :null,"applyCourseName":"人文素养","adjustCourseName":"投资理财","applyTeacherCode":"T10047",
     * "applyTeacherName":"李秋莲","adjustTeacherCode":"T20001","adjustTeacherName":"明金兰"},
     * {"modifierId":null,"recordId":2,"applyTime":"2016-10-26","adjustTime":null,"auditTime":1477445003000,
     * "auditorName":"系统管理员","applyType":"D","semester":20161,"creatorId":
     * "11de4615-d04f-4a7d-9cce-ec5622898766","createTime":1477444798000,"modifiedTime":null,"applyPolysyllabicWord":"5,6","adjustPolysyllabicWord":"","applyClassName":"班级人33","adjustClassName":null,
     * "applyCourseCode":"C001","adjustCourseCode":null,"applyCourseName":"人文素养","adjustCourseName":null,"applyTeacherCode":"T10047",
     * "applyTeacherName":"李秋莲","adjustTeacherCode":"T20001","adjustTeacherName":"明金兰"}]
     * pageSize : 15
     * lastPage : 1
     * nextPage : 1
     * dbtype : mysql
     * toPage : 1
     * totalItem : 2
     * isLastPage : true
     * prevPage : 1
     * firstPage : 1
     * startRow : 0
     * endRow : 15
     */

    private int totalPage;
    private int maxPageCount;
    private boolean isFirstPage;
    private int pageSize;
    private int lastPage;
    private int nextPage;
    private String dbtype;
    private int toPage;
    private int totalItem;
    private boolean isLastPage;
    private int prevPage;
    private int firstPage;
    private int startRow;
    private int endRow;
    private List<Integer> pageNumList;
    /**
     * modifierId : null
     * recordId : 1
     * applyTime : 2016-10-25
     * adjustTime : 2016-10-27
     * auditTime : 1477444996000
     * auditorName : 系统管理员
     * applyType : T
     * semester : 20161
     * creatorId : 11de4615-d04f-4a7d-9cce-ec5622898766
     * createTime : 1477444768000
     * modifiedTime : null
     * applyPolysyllabicWord : 5,6
     * adjustPolysyllabicWord : 5,6
     * applyClassName : 班级人33
     * adjustClassName : 班级人33
     * applyCourseCode : C001
     * adjustCourseCode : null
     * applyCourseName : 人文素养
     * adjustCourseName : 投资理财
     * applyTeacherCode : T10047
     * applyTeacherName : 李秋莲
     * adjustTeacherCode : T20001
     * adjustTeacherName : 明金兰
     */

    private List<ListBean> list;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getMaxPageCount() {
        return maxPageCount;
    }

    public void setMaxPageCount(int maxPageCount) {
        this.maxPageCount = maxPageCount;
    }

    public boolean isIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public String getDbtype() {
        return dbtype;
    }

    public void setDbtype(String dbtype) {
        this.dbtype = dbtype;
    }

    public int getToPage() {
        return toPage;
    }

    public void setToPage(int toPage) {
        this.toPage = toPage;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public boolean isIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public int getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(int prevPage) {
        this.prevPage = prevPage;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public List<Integer> getPageNumList() {
        return pageNumList;
    }

    public void setPageNumList(List<Integer> pageNumList) {
        this.pageNumList = pageNumList;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean{
        private String modifierId;
        private int recordId;
        private String applyTime;
        private String adjustTime;
        private long auditTime;
        private String auditorName;
        private String applyType;
        private int semester;
        private String creatorId;
        private long createTime;
        private String modifiedTime;
        private String applyPolysyllabicWord;
        private String adjustPolysyllabicWord;
        private String applyClassName;
        private String adjustClassName;
        private String applyCourseCode;
        private String adjustCourseCode;
        private String applyCourseName;
        private String adjustCourseName;
        private String applyTeacherCode;
        private String applyTeacherName;
        private String adjustTeacherCode;
        private String adjustTeacherName;

        public String getModifierId() {
            return modifierId;
        }

        public void setModifierId(String modifierId) {
            this.modifierId = modifierId;
        }

        public int getRecordId() {
            return recordId;
        }

        public void setRecordId(int recordId) {
            this.recordId = recordId;
        }

        public String getApplyTime() {
            return applyTime;
        }

        public void setApplyTime(String applyTime) {
            this.applyTime = applyTime;
        }

        public String getAdjustTime() {
            return adjustTime;
        }

        public void setAdjustTime(String adjustTime) {
            this.adjustTime = adjustTime;
        }

        public long getAuditTime() {
            return auditTime;
        }

        public void setAuditTime(long auditTime) {
            this.auditTime = auditTime;
        }

        public String getAuditorName() {
            return auditorName;
        }

        public void setAuditorName(String auditorName) {
            this.auditorName = auditorName;
        }

        public String getApplyType() {
            return applyType;
        }

        public void setApplyType(String applyType) {
            this.applyType = applyType;
        }

        public int getSemester() {
            return semester;
        }

        public void setSemester(int semester) {
            this.semester = semester;
        }

        public String getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(String creatorId) {
            this.creatorId = creatorId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public Object getModifiedTime() {
            return modifiedTime;
        }

        public void setModifiedTime(String modifiedTime) {
            this.modifiedTime = modifiedTime;
        }

        public String getApplyPolysyllabicWord() {
            return applyPolysyllabicWord;
        }

        public void setApplyPolysyllabicWord(String applyPolysyllabicWord) {
            this.applyPolysyllabicWord = applyPolysyllabicWord;
        }

        public String getAdjustPolysyllabicWord() {
            return adjustPolysyllabicWord;
        }

        public void setAdjustPolysyllabicWord(String adjustPolysyllabicWord) {
            this.adjustPolysyllabicWord = adjustPolysyllabicWord;
        }

        public String getApplyClassName() {
            return applyClassName;
        }

        public void setApplyClassName(String applyClassName) {
            this.applyClassName = applyClassName;
        }

        public String getAdjustClassName() {
            return adjustClassName;
        }

        public void setAdjustClassName(String adjustClassName) {
            this.adjustClassName = adjustClassName;
        }

        public String getApplyCourseCode() {
            return applyCourseCode;
        }

        public void setApplyCourseCode(String applyCourseCode) {
            this.applyCourseCode = applyCourseCode;
        }

        public Object getAdjustCourseCode() {
            return adjustCourseCode;
        }

        public void setAdjustCourseCode(String adjustCourseCode) {
            this.adjustCourseCode = adjustCourseCode;
        }

        public String getApplyCourseName() {
            return applyCourseName;
        }

        public void setApplyCourseName(String applyCourseName) {
            this.applyCourseName = applyCourseName;
        }

        public String getAdjustCourseName() {
            return adjustCourseName;
        }

        public void setAdjustCourseName(String adjustCourseName) {
            this.adjustCourseName = adjustCourseName;
        }

        public String getApplyTeacherCode() {
            return applyTeacherCode;
        }

        public void setApplyTeacherCode(String applyTeacherCode) {
            this.applyTeacherCode = applyTeacherCode;
        }

        public String getApplyTeacherName() {
            return applyTeacherName;
        }

        public void setApplyTeacherName(String applyTeacherName) {
            this.applyTeacherName = applyTeacherName;
        }

        public String getAdjustTeacherCode() {
            return adjustTeacherCode;
        }

        public void setAdjustTeacherCode(String adjustTeacherCode) {
            this.adjustTeacherCode = adjustTeacherCode;
        }

        public String getAdjustTeacherName() {
            return adjustTeacherName;
        }

        public void setAdjustTeacherName(String adjustTeacherName) {
            this.adjustTeacherName = adjustTeacherName;
        }
    }
}
