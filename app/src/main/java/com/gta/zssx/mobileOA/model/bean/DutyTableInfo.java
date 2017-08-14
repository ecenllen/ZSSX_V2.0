package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/11/19.
 */

public class DutyTableInfo implements Serializable{

    /**
     * 列表
     */
    private List<DutyArrange> Arranges;

    /**
     * 备注
     */
    private String Remark;

    public List<DutyArrange> getArranges() {
        return Arranges;
    }

    public void setArranges(List<DutyArrange> arranges) {
        Arranges = arranges;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public static class DutyArrange {
        /**
         * 星期几
         */
        private String Week;
        /**
         * 周次段
         */
        private String WeekDuration;

        private List<DutyDtail> Details;

        public String getWeek() {
            return Week;
        }

        public void setWeek(String week) {
            Week = week;
        }

        public String getWeekDuration() {
            return WeekDuration;
        }

        public void setWeekDuration(String weekDuration) {
            WeekDuration = weekDuration;
        }

        public List<DutyDtail> getDetails() {
            return Details;
        }

        public void setDetails(List<DutyDtail> details) {
            Details = details;
        }


        public static class DutyDtail {
            /**
             * 值班地点
             */
            private String Address;
            /**
             * 值班人员
             */
            private String People;

            public java.lang.String getAddress() {
                return Address;
            }

            public void setAddress(java.lang.String address) {
                Address = address;
            }

            public java.lang.String getPeople() {
                return People;
            }

            public void setPeople(java.lang.String people) {
                People = people;
            }
        }
    }

}
