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
public class ApplyConfirmBean implements Serializable, EmptyFlagInterface {


    /**
     * isFirstPage : true
     * pageNumList : [1]
     * maxPageCount : 15
     * dbtype : mysql
     * toPage : 1
     * totalItem : 1
     * pageSize : 15
     * totalPage : 1
     * isLastPage : true
     * prevPage : 1
     * nextPage : 1
     * firstPage : 1
     * lastPage : 1
     * startRow : 0
     * endRow : 15
     * list : [{"auditStatus":"N","applyCourseId":221,"applyClassId":108,"adjustCourseId":106,"adjustClassId":114,"adjustDate":1477324800000,"applyDate":1477324800000,"applyType":"T","adjustTeacherId":"8","transferApplyId":5,"applyClassName":"15汽车营销","adjustClassName":"15会计2","applyCourseName":"汽车底盘IMI","adjustCourseName":"人文素养","applyTeacherName":"李坚","adjustTeacherName":"张维哲","applyPolysyllabicWord":"5,6","adjustPolysyllabicWord":null}]
     */

    private boolean isFirstPage;
    private int maxPageCount;
    private String dbtype;
    private int toPage;
    private int totalItem;
    private int pageSize;
    private int totalPage;
    private boolean isLastPage;
    private int prevPage;
    private int nextPage;
    private int firstPage;
    private int lastPage;
    private int startRow;
    private int endRow;
    private List<Integer> pageNumList;
    /**
     * auditStatus : N
     * applyCourseId : 221
     * applyClassId : 108
     * adjustCourseId : 106
     * adjustClassId : 114
     * adjustDate : 1477324800000
     * applyDate : 1477324800000
     * applyType : T
     * adjustTeacherId : 8
     * transferApplyId : 5
     * applyClassName : 15汽车营销
     * adjustClassName : 15会计2
     * applyCourseName : 汽车底盘IMI
     * adjustCourseName : 人文素养
     * applyTeacherName : 李坚
     * adjustTeacherName : 张维哲
     * applyPolysyllabicWord : 5,6
     * adjustPolysyllabicWord : null
     */

    private List<ListBean> list;

    public boolean isIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public int getMaxPageCount() {
        return maxPageCount;
    }

    public void setMaxPageCount(int maxPageCount) {
        this.maxPageCount = maxPageCount;
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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
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

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
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

    public static class ListBean {
        private String auditStatus;
        private int applyCourseId;
        private int applyClassId;
        private int adjustCourseId;
        private int adjustClassId;
        private long adjustDate;
        private long applyDate;
        private String applyType;
        private String adjustTeacherId;
        private int transferApplyId;
        private String applyClassName;
        private String adjustClassName;
        private String applyCourseName;
        private String adjustCourseName;
        private String applyTeacherName;
        private String adjustTeacherName;
        private String applyPolysyllabicWord;
        private String adjustPolysyllabicWord;
        private String confimType;
        private String auditType;

        public String getConfimType() {
            return confimType;
        }

        public void setConfimType(String confimType) {
            this.confimType = confimType;
        }

        public String getAuditType() {
            return auditType;
        }

        public void setAuditType(String auditType) {
            this.auditType = auditType;
        }

        public String getAuditStatus() {
            return auditStatus;
        }

        public void setAuditStatus(String auditStatus) {
            this.auditStatus = auditStatus;
        }

        public int getApplyCourseId() {
            return applyCourseId;
        }

        public void setApplyCourseId(int applyCourseId) {
            this.applyCourseId = applyCourseId;
        }

        public int getApplyClassId() {
            return applyClassId;
        }

        public void setApplyClassId(int applyClassId) {
            this.applyClassId = applyClassId;
        }

        public int getAdjustCourseId() {
            return adjustCourseId;
        }

        public void setAdjustCourseId(int adjustCourseId) {
            this.adjustCourseId = adjustCourseId;
        }

        public int getAdjustClassId() {
            return adjustClassId;
        }

        public void setAdjustClassId(int adjustClassId) {
            this.adjustClassId = adjustClassId;
        }

        public long getAdjustDate() {
            return adjustDate;
        }

        public void setAdjustDate(long adjustDate) {
            this.adjustDate = adjustDate;
        }

        public long getApplyDate() {
            return applyDate;
        }

        public void setApplyDate(long applyDate) {
            this.applyDate = applyDate;
        }

        public String getApplyType() {
            return applyType;
        }

        public void setApplyType(String applyType) {
            this.applyType = applyType;
        }

        public String getAdjustTeacherId() {
            return adjustTeacherId;
        }

        public void setAdjustTeacherId(String adjustTeacherId) {
            this.adjustTeacherId = adjustTeacherId;
        }

        public int getTransferApplyId() {
            return transferApplyId;
        }

        public void setTransferApplyId(int transferApplyId) {
            this.transferApplyId = transferApplyId;
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

        public String getApplyTeacherName() {
            return applyTeacherName;
        }

        public void setApplyTeacherName(String applyTeacherName) {
            this.applyTeacherName = applyTeacherName;
        }

        public String getAdjustTeacherName() {
            return adjustTeacherName;
        }

        public void setAdjustTeacherName(String adjustTeacherName) {
            this.adjustTeacherName = adjustTeacherName;
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
    }
}
