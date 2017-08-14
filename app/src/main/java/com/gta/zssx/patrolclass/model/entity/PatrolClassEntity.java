package com.gta.zssx.patrolclass.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liang.lu1 on 2016/6/28.
 */
public class PatrolClassEntity {
    /**
     * 日期
     */
    private String date;
    /**
     * 状态   1  未送审   2 待审核   3未通过   4  已通过
     */
    private int status;
    /**
     * 巡课Id
     */
    private int xId;

    public String getDate () {
        return date;
    }

    public void setDate (String date) {
        this.date = date;
    }

    public int getStatus () {
        return status;
    }

    public void setStatus (int status) {
        this.status = status;
    }

    public int getxId () {
        return xId;
    }

    public void setxId (int xId) {
        this.xId = xId;
    }

    public static ArrayList<PatrolClassEntity> getDatas(){
        ArrayList<PatrolClassEntity> models=new ArrayList<>();
        for (int i=0;i<12;i++){
            PatrolClassEntity model=new PatrolClassEntity ();
            if(i%2==0){
                model.setDate("2016-5-12（周五）");
                model.setStatus (1);
            }else if(i==11) {
                model.setDate("2016-7-12（周四）");
                model.setStatus (3);
            }else if(i%2==1&&i!=11) {
                model.setDate("2016-9-12（周二）");
                model.setStatus (2);
            }
            models.add(model);
        }
        return models;
    }

    public static ArrayList<PatrolClassEntity> getModels(){
        ArrayList<PatrolClassEntity> models=new ArrayList<>();
        for(int i =0; i<15; i++){
            PatrolClassEntity model=new PatrolClassEntity ();
            model.setDate("2016-5-12（周五）");
            model.setStatus (4);
            models.add(model);
        }
        return models;
    }

    /**
     * 时间段巡课记录搜索结果页的Model
     */

    public static List<PatrolClassEntity> getSearchResult(){
        List<PatrolClassEntity> list= new ArrayList<>();
        for(int i=0;i<10;i++){
            PatrolClassEntity model= new PatrolClassEntity ();
            if(i==1){
                model.setDate("2016-6-14（周三）");
                model.setStatus (1);
            }else if(i==2){
                model.setDate("2016-7-14（周五）");
                model.setStatus (2);
            }else if(i%2==0){
                model.setDate("2016-8-14（周三）");
                model.setStatus (3);
            }else if(i%2==1){
                model.setDate("2016-6-14（周三）");
                model.setStatus (4);
            }
            list.add(model);
        }
        return list;
    }
}

