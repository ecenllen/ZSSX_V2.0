package com.gta.zssx.fun.coursedaily.registercourse.model.bean;

import java.io.Serializable;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/30.
 * @since 1.0.0
 */
public class OriginalClassBean implements Serializable {

    private int OriginalClassID;
    private int OriginalSectionID;
    private String OriginalSignDate;

    public int getOriginalClassID() {
        return OriginalClassID;
    }

    public void setOriginalClassID(int originalClassID) {
        OriginalClassID = originalClassID;
    }

    public int getOriginalSectionID() {
        return OriginalSectionID;
    }

    public void setOriginalSectionID(int originalSectionID) {
        OriginalSectionID = originalSectionID;
    }

    public String getOriginalSignDate() {
        return OriginalSignDate;
    }

    public void setOriginalSignDate(String originalSignDate) {
        OriginalSignDate = originalSignDate;
    }
}
