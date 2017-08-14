package com.gta.zssx.pub.update;

import java.io.Serializable;

/**
 * [Description]
 *
 * [How to use]
 *
 * [Tips]
 * @author
 *   Created by Zhimin.Huang on 2015/10/26.
 * @since
 *   1.0.0
 */
public class UpdataInfo implements Serializable {
    //服务器版本
    private String serverVersion;
    //当前版本
    private String currentVersion;
    //服务器地址
    private String url;
    //描述
    private String description;
    //描述
    private int updateLevel;
    //版本更新標題
    private String title;

    public String getVersionName() {
        return VersionName;
    }

    public void setVersionName(String versionName) {
        VersionName = versionName;
    }

    private String VersionName;

    public int getUpdateLevel() {
        return updateLevel;
    }

    public void setUpdateLevel(int updateLevel) {
        this.updateLevel = updateLevel;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }
    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getServerVersion() {
        return serverVersion;
    }
    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

