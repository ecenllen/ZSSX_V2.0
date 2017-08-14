package com.gta.zssx.fun.assetmanagement.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/8.
 * @since 1.0.0
 */
public class ListBean implements Serializable {

    private boolean success;
    private String errorMsg;
    private List<dataEntry> datas;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<dataEntry> getDatas() {
        return datas;
    }

    public void setDatas(List<dataEntry> datas) {
        this.datas = datas;
    }

    public static class dataEntry {
        String chapterID;
        String textbookID;
        String knowledgeName;
        String courseKnowledgeID;
        String ParentId;
        String recordStatus;
        int leaf;
        int orderNum;
        boolean isKnowledge;
        boolean isLeaf;

        public String getChapterID() {
            return chapterID;
        }

        public void setChapterID(String chapterID) {
            this.chapterID = chapterID;
        }

        public String getTextbookID() {
            return textbookID;
        }

        public void setTextbookID(String textbookID) {
            this.textbookID = textbookID;
        }

        public String getKnowledgeName() {
            return knowledgeName;
        }

        public void setKnowledgeName(String knowledgeName) {
            this.knowledgeName = knowledgeName;
        }

        public String getCourseKnowledgeID() {
            return courseKnowledgeID;
        }

        public void setCourseKnowledgeID(String courseKnowledgeID) {
            this.courseKnowledgeID = courseKnowledgeID;
        }

        public String getParentId() {
            return ParentId;
        }

        public void setParentId(String parentId) {
            ParentId = parentId;
        }

        public String getRecordStatus() {
            return recordStatus;
        }

        public void setRecordStatus(String recordStatus) {
            this.recordStatus = recordStatus;
        }

        public int getLeaf() {
            return leaf;
        }

        public void setLeaf(int leaf) {
            this.leaf = leaf;
        }

        public int getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(int orderNum) {
            this.orderNum = orderNum;
        }

        public boolean isKnowledge() {
            return isKnowledge;
        }

        public void setKnowledge(boolean knowledge) {
            isKnowledge = knowledge;
        }

        public boolean isLeaf() {
            return isLeaf;
        }

        public void setLeaf(boolean leaf) {
            isLeaf = leaf;
        }
    }

}
