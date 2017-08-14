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
public class ApplyBean implements Serializable, EmptyFlagInterface {


    /**
     * dbtype : mysql
     * toPage : 1
     * isLastPage : false
     * prevPage : 1
     * firstPage : 1
     * startRow : 0
     * endRow : 15
     * totalPage : 3
     * list : [{"applyType":"D","applyPolysyllabicWord":"3,4","adjustPolysyllabicWord":null,"applyCourseId":109,"applyClassId":114,"adjustCourseId":null,"adjustClassId":null,"adjustDate":null,"applyDate":1535990400000,"applyWeek":null,"adjustWeek":null,"auditStatus":"N","adjustTeacherId":"13","transferApplyId":58,"applyClassName":"15会计2","adjustClassName":null,"applyCourseName":"会计基础","adjustCourseName":null,"applyTeacherName":"李霞","adjustTeacherName":"徐勇"},{"applyType":"D","applyPolysyllabicWord":"3,4","adjustPolysyllabicWord":null,"applyCourseId":109,"applyClassId":114,"adjustCourseId":null,"adjustClassId":null,"adjustDate":null,"applyDate":1528128000000,"applyWeek":null,"adjustWeek":null,"auditStatus":"N","adjustTeacherId":"13","transferApplyId":57,"applyClassName":"15会计2","adjustClassName":null,"applyCourseName":"会计基础","adjustCourseName":null,"applyTeacherName":"李霞","adjustTeacherName":"徐勇"},{"applyType":"D","applyPolysyllabicWord":"3,4","adjustPolysyllabicWord":null,"applyCourseId":109,"applyClassId":114,"adjustCourseId":null,"adjustClassId":null,"adjustDate":null,"applyDate":1526918400000,"applyWeek":null,"adjustWeek":null,"auditStatus":"N","adjustTeacherId":"13","transferApplyId":44,"applyClassName":"15会计2","adjustClassName":null,"applyCourseName":"会计基础","adjustCourseName":null,"applyTeacherName":"李霞","adjustTeacherName":"徐勇"},{"applyType":"D","applyPolysyllabicWord":"3,4","adjustPolysyllabicWord":null,"applyCourseId":109,"applyClassId":114,"adjustCourseId":null,"adjustClassId":null,"adjustDate":null,"applyDate":1520870400000,"applyWeek":null,"adjustWeek":null,"auditStatus":"N","adjustTeacherId":"13","transferApplyId":56,"applyClassName":"15会计2","adjustClassName":null,"applyCourseName":"会计基础","adjustCourseName":null,"applyTeacherName":"李霞","adjustTeacherName":"徐勇"},{"applyType":"D","applyPolysyllabicWord":"3,4","adjustPolysyllabicWord":null,"applyCourseId":109,"applyClassId":114,"adjustCourseId":null,"adjustClassId":null,"adjustDate":null,"applyDate":1519660800000,"applyWeek":null,"adjustWeek":null,"auditStatus":"N","adjustTeacherId":"13","transferApplyId":43,"applyClassName":"15会计2","adjustClassName":null,"applyCourseName":"会计基础","adjustCourseName":null,"applyTeacherName":"李霞","adjustTeacherName":"徐勇"},{"applyType":"D","applyPolysyllabicWord":"3,4","adjustPolysyllabicWord":null,"applyCourseId":109,"applyClassId":114,"adjustCourseId":null,"adjustClassId":null,"adjustDate":null,"applyDate":1514217600000,"applyWeek":null,"adjustWeek":null,"auditStatus":"N","adjustTeacherId":"13","transferApplyId":55,"applyClassName":"15会计2","adjustClassName":null,"applyCourseName":"会计基础","adjustCourseName":null,"applyTeacherName":"李霞","adjustTeacherName":"徐勇"},{"applyType":"D","applyPolysyllabicWord":"3,4","adjustPolysyllabicWord":null,"applyCourseId":109,"applyClassId":114,"adjustCourseId":null,"adjustClassId":null,"adjustDate":null,"applyDate":1513008000000,"applyWeek":null,"adjustWeek":null,"auditStatus":"N","adjustTeacherId":"13","transferApplyId":42,"applyClassName":"15会计2","adjustClassName":null,"applyCourseName":"会计基础","adjustCourseName":null,"applyTeacherName":"李霞","adjustTeacherName":"徐勇"},{"applyType":"D","applyPolysyllabicWord":"3,4","adjustPolysyllabicWord":null,"applyCourseId":109,"applyClassId":114,"adjustCourseId":null,"adjustClassId":null,"adjustDate":null,"applyDate":1508169600000,"applyWeek":null,"adjustWeek":null,"auditStatus":"N","adjustTeacherId":"13","transferApplyId":54,"applyClassName":"15会计2","adjustClassName":null,"applyCourseName":"会计基础","adjustCourseName":null,"applyTeacherName":"李霞","adjustTeacherName":"徐勇"},{"applyType":"D","applyPolysyllabicWord":"3,4","adjustPolysyllabicWord":null,"applyCourseId":109,"applyClassId":114,"adjustCourseId":null,"adjustClassId":null,"adjustDate":null,"applyDate":1506960000000,"applyWeek":null,"adjustWeek":null,"auditStatus":"N","adjustTeacherId":"13","transferApplyId":41,"applyClassName":"15会计2","adjustClassName":null,"applyCourseName":"会计基础","adjustCourseName":null,"applyTeacherName":"李霞","adjustTeacherName":"徐勇"},{"applyType":"D","applyPolysyllabicWord":"3,4","adjustPolysyllabicWord":null,"applyCourseId":109,"applyClassId":114,"adjustCourseId":null,"adjustClassId":null,"adjustDate":null,"applyDate":1502726400000,"applyWeek":null,"adjustWeek":null,"auditStatus":"N","adjustTeacherId":"13","transferApplyId":53,"applyClassName":"15会计2","adjustClassName":null,"applyCourseName":"会计基础","adjustCourseName":null,"applyTeacherName":"李霞","adjustTeacherName":"徐勇"},{"applyType":"D","applyPolysyllabicWord":"3,4","adjustPolysyllabicWord":null,"applyCourseId":109,"applyClassId":114,"adjustCourseId":null,"adjustClassId":null,"adjustDate":null,"applyDate":1501516800000,"applyWeek":null,"adjustWeek":null,"auditStatus":"N","adjustTeacherId":"13","transferApplyId":40,"applyClassName":"15会计2","adjustClassName":null,"applyCourseName":"会计基础","adjustCourseName":null,"applyTeacherName":"李霞","adjustTeacherName":"徐勇"},{"applyType":"D","applyPolysyllabicWord":"3,4","adjustPolysyllabicWord":null,"applyCourseId":109,"applyClassId":114,"adjustCourseId":null,"adjustClassId":null,"adjustDate":null,"applyDate":1497888000000,"applyWeek":null,"adjustWeek":null,"auditStatus":"N","adjustTeacherId":"13","transferApplyId":52,"applyClassName":"15会计2","adjustClassName":null,"applyCourseName":"会计基础","adjustCourseName":null,"applyTeacherName":"李霞","adjustTeacherName":"徐勇"},{"applyType":"D","applyPolysyllabicWord":"3,4","adjustPolysyllabicWord":null,"applyCourseId":109,"applyClassId":114,"adjustCourseId":null,"adjustClassId":null,"adjustDate":null,"applyDate":1496678400000,"applyWeek":null,"adjustWeek":null,"auditStatus":"N","adjustTeacherId":"13","transferApplyId":39,"applyClassName":"15会计2","adjustClassName":null,"applyCourseName":"会计基础","adjustCourseName":null,"applyTeacherName":"李霞","adjustTeacherName":"徐勇"},{"applyType":"D","applyPolysyllabicWord":"3,4","adjustPolysyllabicWord":null,"applyCourseId":109,"applyClassId":114,"adjustCourseId":null,"adjustClassId":null,"adjustDate":null,"applyDate":1493654400000,"applyWeek":null,"adjustWeek":null,"auditStatus":"N","adjustTeacherId":"13","transferApplyId":51,"applyClassName":"15会计2","adjustClassName":null,"applyCourseName":"会计基础","adjustCourseName":null,"applyTeacherName":"李霞","adjustTeacherName":"徐勇"},{"applyType":"D","applyPolysyllabicWord":"3,4","adjustPolysyllabicWord":null,"applyCourseId":109,"applyClassId":114,"adjustCourseId":null,"adjustClassId":null,"adjustDate":null,"applyDate":1492444800000,"applyWeek":null,"adjustWeek":null,"auditStatus":"N","adjustTeacherId":"13","transferApplyId":38,"applyClassName":"15会计2","adjustClassName":null,"applyCourseName":"会计基础","adjustCourseName":null,"applyTeacherName":"李霞","adjustTeacherName":"徐勇"}]
     * pageSize : 15
     * maxPageCount : 15
     * pageNumList : [1,2,3]
     * isFirstPage : true
     * nextPage : 2
     * totalItem : 40
     * lastPage : 3
     */

    private String dbtype;
    private int toPage;
    private boolean isLastPage;
    private int prevPage;
    private int firstPage;
    private int startRow;
    private int endRow;
    private int totalPage;
    private int pageSize;
    private int maxPageCount;
    private boolean isFirstPage;
    private int nextPage;
    private int totalItem;
    private int lastPage;
    /**
     * applyType : D
     * applyPolysyllabicWord : 3,4
     * adjustPolysyllabicWord : null
     * applyCourseId : 109
     * applyClassId : 114
     * adjustCourseId : null
     * adjustClassId : null
     * adjustDate : null
     * applyDate : 1535990400000
     * applyWeek : null
     * adjustWeek : null
     * auditStatus : N
     * adjustTeacherId : 13
     * transferApplyId : 58
     * applyClassName : 15会计2
     * adjustClassName : null
     * applyCourseName : 会计基础
     * adjustCourseName : null
     * applyTeacherName : 李霞
     * adjustTeacherName : 徐勇
     */

    private List<ListBean> list;
    private List<Integer> pageNumList;

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

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<Integer> getPageNumList() {
        return pageNumList;
    }

    public void setPageNumList(List<Integer> pageNumList) {
        this.pageNumList = pageNumList;
    }

    public static class ListBean {
        private String applyType;
        private String applyPolysyllabicWord;
        private String adjustPolysyllabicWord;
        private int applyCourseId;
        private int applyClassId;
        private long adjustCourseId;
        private long adjustClassId;
        private long adjustDate;
        private long applyDate;
        private String applyWeek;
        private String adjustWeek;
        private String auditStatus;
        private String adjustTeacherId;
        private int transferApplyId;
        private String applyClassName;
        private String adjustClassName;
        private String applyCourseName;
        private String adjustCourseName;
        private String applyTeacherName;
        private String adjustTeacherName;
//        0有值为已确认，null为未确认
        private String confimType;
//        1为已审核，null为未审核或者没有审核功能，需结合是否需要审核字段判断
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

        public String getApplyType() {
            return applyType;
        }

        public void setApplyType(String applyType) {
            this.applyType = applyType;
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

        public long getAdjustCourseId() {
            return adjustCourseId;
        }

        public void setAdjustCourseId(long adjustCourseId) {
            this.adjustCourseId = adjustCourseId;
        }

        public long getAdjustClassId() {
            return adjustClassId;
        }

        public void setAdjustClassId(long adjustClassId) {
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

        public String getApplyWeek() {
            return applyWeek;
        }

        public void setApplyWeek(String applyWeek) {
            this.applyWeek = applyWeek;
        }

        public String getAdjustWeek() {
            return adjustWeek;
        }

        public void setAdjustWeek(String adjustWeek) {
            this.adjustWeek = adjustWeek;
        }

        public String getAuditStatus() {
            return auditStatus;
        }

        public void setAuditStatus(String auditStatus) {
            this.auditStatus = auditStatus;
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
    }
}
