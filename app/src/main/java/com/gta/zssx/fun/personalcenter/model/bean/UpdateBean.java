package com.gta.zssx.fun.personalcenter.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/28.
 * @since 1.0.0
 */
public class UpdateBean implements Serializable {


    /**
     * VersionCode : 1.5.3
     * UpdateMessage : 优化我的班级BUG
     * UpdateLevel : 1
     * FilePath : http://192.168.105.192:7072
     */

    private String VersionCode;
    private String UpdateMessage;
    private int UpdateLevel;
    private String FilePath;
    private String VersionName;

    public String getVersionName() {
        return VersionName;
    }

    public void setVersionName(String versionName) {
        VersionName = versionName;
    }

    public String getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(String VersionCode) {
        this.VersionCode = VersionCode;
    }

    public String getUpdateMessage() {
        return UpdateMessage;
    }

    public void setUpdateMessage(String UpdateMessage) {
        this.UpdateMessage = UpdateMessage;
    }

    public int getUpdateLevel() {
        return UpdateLevel;
    }

    public void setUpdateLevel(int UpdateLevel) {
        this.UpdateLevel = UpdateLevel;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String FilePath) {
        this.FilePath = FilePath;
    }
}
