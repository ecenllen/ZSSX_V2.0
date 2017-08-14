package com.gta.zssx.dormitory.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lan.zheng on 2017/6/26.
 */

public class DormitoryRankingBean implements Serializable{
    private String ItemName;  //指标项名称
    private int RecordId;  //记录Id
    private int ItemID; //指标项Id
    private int State; //状态  1未送审,  2.不通过 3.送审中  4.已通过  5.已发布
    private int ItemType;   // 1.常规项   2.增分  3.减分
    private int ItemMode;   // 1.记分    2.扣分
    private int DimensionType;  //1.宿舍维度   2.班级维度
    private int ScoringTemplateType;  //1.录入姓名单项    2.录入姓名多项  3.不录入姓名有选项设置  4.不录入姓名无选项设置
    private String ScoreDate;  //日期
    private String buildingNames; //显示宿舍楼专业部的字符串
    private boolean IsAll;  //是否显示“全部”
    private List<DormitoryOrClassSingleInfoBean> DormitoryOrClassList;

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String ItemName) {
        this.ItemName = ItemName;
    }

    public int getRecordId() {
        return RecordId;
    }

    public void setRecordId(int RecordId) {
        this.RecordId = RecordId;
    }

    public int getItemId() {
        return ItemID;
    }

    public void setItemId(int ItemID) {
        this.ItemID = ItemID;
    }

    public int getStates() {
        return State;
    }

    public void setStates(int State) {
        this.State = State;
    }

    public int getItemType() {
        return ItemType;
    }

    public void setItemType(int ItemType) {
        this.ItemType = ItemType;
    }

    public int getItemMode() {
        return ItemMode;
    }

    public void setItemMode(int ItemMode) {
        this.ItemMode = ItemMode;
    }

    public int getDimensionType() {
        return DimensionType;
    }

    public void setDimensionType(int DimensionType) {
        this.DimensionType = DimensionType;
    }

    public int getScoringTemplateType() {
        return ScoringTemplateType;
    }

    public void setScoringTemplateType(int ScoringTemplateType) {
        this.ScoringTemplateType = ScoringTemplateType;
    }

    public String getScoreDate() {
        return ScoreDate;
    }

    public void setScoreDate(String ScoreDate) {
        this.ScoreDate = ScoreDate;
    }

    public String getbuildingNames() {
        return buildingNames;
    }

    public void setbuildingNames(String buildingNames) {
        this.buildingNames = buildingNames;
    }

    public boolean getIsAll() {
        return IsAll;
    }

    public void setIsAll(boolean IsAll) {
        this.IsAll = IsAll;
    }

    public List<DormitoryOrClassSingleInfoBean> getDormitoryOrClassInfoBean() {
        return DormitoryOrClassList;
    }

    public void setDormitoryOrClassInfoBean(List<DormitoryOrClassSingleInfoBean> DormitoryOrClassList) {
        this.DormitoryOrClassList = DormitoryOrClassList;
    }
}
