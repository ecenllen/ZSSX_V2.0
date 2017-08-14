package com.gta.zssx.patrolclass.model.entity;

import android.content.Context;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.gta.zssx.pub.util.JsonHelper;
import com.gta.zssx.pub.util.LogUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.List;

/**
 * Created by liang.lu on 2017/3/2 15:42.
 */

public class PatrolRegisterBeanNew implements Serializable {

    /**
     * currentdate : 2017年3月2日
     * servicedate : 2017-03-02
     * className : 15物流
     * classId : 109
     * sectionName : 第6节
     * sectionId : 6
     * type : null
     * DeptId:000
     * Xid:0
     * PId : 0
     * teacherList : [{"teacherId":1,"teacherName":"刘能","autoScore":"1","dockScore":[{"options":[{"Id":211,"type":2,"title":"测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标(-25分)","score":"25","getScore":"","classid":138,"isTitle":false,"isCheck":false},{"Id":212,"type":2,"title":"测试指标新指标测试指标新指(-25分)","score":"25","getScore":"10","classid":138,"isTitle":false,"isCheck":false},{"Id":213,"type":2,"title":"测试指标新指指标(-25分)","score":"25","getScore":"5","classid":138,"isTitle":false,"isCheck":false}],"Id":138,"name":"卫生分","allScore":"125","sort":0,"getAllScore":"110"},{"options":[{"Id":212,"type":2,"title":"测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标(-99分)","score":"99","getScore":"","classid":139,"isTitle":false,"isCheck":false}],"Id":139,"name":"教学分","allScore":"123","sort":2,"getAllScore":"123"},{"options":[{"Id":213,"type":2,"title":"测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标(-123分)","score":"123","getScore":"","classid":140,"isTitle":false,"isCheck":false},{"Id":214,"type":2,"title":"测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标(-12分)","score":"12","getScore":"","classid":140,"isTitle":false,"isCheck":false}],"Id":140,"name":"学生分","allScore":"105","sort":7,"getAllScore":"105"}]},{"teacherId":1,"teacherName":"赵四","autoScore":"1","dockScore":[{"options":[{"Id":211,"type":2,"title":"测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标(-25分)","score":"25","getScore":"","classid":138,"isTitle":false,"isCheck":false},{"Id":212,"type":2,"title":"测试指标新指标测试指标新指(-25分)","score":"25","getScore":"10","classid":138,"isTitle":false,"isCheck":false},{"Id":213,"type":2,"title":"测试指标新指指标(-25分)","score":"25","getScore":"5","classid":138,"isTitle":false,"isCheck":false}],"Id":138,"name":"卫生分","allScore":"125","sort":0,"getAllScore":"110"},{"options":[{"Id":212,"type":2,"title":"测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标(-99分)","score":"99","getScore":"","classid":139,"isTitle":false,"isCheck":false}],"Id":139,"name":"教学分","allScore":"123","sort":2,"getAllScore":"123"},{"options":[{"Id":213,"type":2,"title":"测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标(-123分)","score":"123","getScore":"","classid":140,"isTitle":false,"isCheck":false},{"Id":214,"type":2,"title":"测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标(-12分)","score":"12","getScore":"","classid":140,"isTitle":false,"isCheck":false}],"Id":140,"name":"学生分","allScore":"105","sort":7,"getAllScore":"105"}]},{"teacherId":1,"teacherName":"谢广坤","autoScore":"1","dockScore":[{"options":[{"Id":211,"type":2,"title":"测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标(-25分)","score":"25","getScore":"","classid":138,"isTitle":false,"isCheck":false},{"Id":212,"type":2,"title":"测试指标新指标测试指标新指(-25分)","score":"25","getScore":"10","classid":138,"isTitle":false,"isCheck":false},{"Id":213,"type":2,"title":"测试指标新指指标(-25分)","score":"25","getScore":"5","classid":138,"isTitle":false,"isCheck":false}],"Id":138,"name":"卫生分","allScore":"125","sort":0,"getAllScore":"110"},{"options":[{"Id":212,"type":2,"title":"测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标(-99分)","score":"99","getScore":"","classid":139,"isTitle":false,"isCheck":false}],"Id":139,"name":"教学分","allScore":"123","sort":2,"getAllScore":"123"},{"options":[{"Id":213,"type":2,"title":"测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标(-123分)","score":"123","getScore":"","classid":140,"isTitle":false,"isCheck":false},{"Id":214,"type":2,"title":"测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标(-12分)","score":"12","getScore":"","classid":140,"isTitle":false,"isCheck":false}],"Id":140,"name":"学生分","allScore":"105","sort":7,"getAllScore":"105"}]}]
     * sectionsList : [{"type":0,"sectionName":"第1节","sectionId":1},{"type":0,"sectionName":"第2节","sectionId":2},{"type":0,"sectionName":"第3节","sectionId":3},{"type":0,"sectionName":"第4节","sectionId":4},{"type":0,"sectionName":"第5节","sectionId":5},{"type":0,"sectionName":"第6节","sectionId":6},{"type":0,"sectionName":"第7节","sectionId":7},{"type":0,"sectionName":"第8节","sectionId":8}]
     */

    private String currentdate;
    private String servicedate;
    private String className;
    private int classId;
    private String sectionName;
    private int sectionId;
    private Object type;
    private int XId;
    private int PId;
    private int DeptId;
    /**
     * teacherId : 1
     * teacherName : 刘能
     * autoScore : 1
     * dockScore : [{"options":[{"Id":211,"type":2,"title":"测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标(-25分)","score":"25","getScore":"","classid":138,"isTitle":false,"isCheck":false},{"Id":212,"type":2,"title":"测试指标新指标测试指标新指(-25分)","score":"25","getScore":"10","classid":138,"isTitle":false,"isCheck":false},{"Id":213,"type":2,"title":"测试指标新指指标(-25分)","score":"25","getScore":"5","classid":138,"isTitle":false,"isCheck":false}],"Id":138,"name":"卫生分","allScore":"125","sort":0,"getAllScore":"110"},{"options":[{"Id":212,"type":2,"title":"测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标(-99分)","score":"99","getScore":"","classid":139,"isTitle":false,"isCheck":false}],"Id":139,"name":"教学分","allScore":"123","sort":2,"getAllScore":"123"},{"options":[{"Id":213,"type":2,"title":"测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标(-123分)","score":"123","getScore":"","classid":140,"isTitle":false,"isCheck":false},{"Id":214,"type":2,"title":"测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标(-12分)","score":"12","getScore":"","classid":140,"isTitle":false,"isCheck":false}],"Id":140,"name":"学生分","allScore":"105","sort":7,"getAllScore":"105"}]
     */

    private List<TeacherListBean> teacherList;
    /**
     * type : 0
     * sectionName : 第1节
     * sectionId : 1
     */

    private List<SectionsListBean> sectionsList;

    public static PatrolRegisterBeanNew getData (Context context) {
        try {
            InputStream inputStream = context.getAssets ().open ("classsel.json");

            InputStreamReader reader = new InputStreamReader (inputStream);

            BufferedReader bufferedReader = new BufferedReader (reader);

            String readerData = "";
            StringBuffer stringBuffer = new StringBuffer ();
            while ((readerData = bufferedReader.readLine ()) != null) {
                stringBuffer.append (readerData);
            }
            reader.close ();
            bufferedReader.close ();
            inputStream.close ();
            LogUtil.e ("解析成功：" + stringBuffer.toString ());
            JSONObject jsonObject = new JSONObject (stringBuffer.toString ());
            PatrolRegisterBeanNew model = JsonHelper.parseObject (jsonObject.toString (), PatrolRegisterBeanNew.class);

            return model;

        } catch (Exception e) {
            e.printStackTrace ();
        }
        return null;
    }

    public int getDeptId () {
        return DeptId;
    }

    public void setDeptId (int deptId) {
        DeptId = deptId;
    }

    public int getXId () {
        return XId;
    }

    public void setXId (int XId) {
        this.XId = XId;
    }

    public String getCurrentdate () {
        return currentdate;
    }

    public void setCurrentdate (String currentdate) {
        this.currentdate = currentdate;
    }

    public String getServicedate () {
        return servicedate;
    }

    public void setServicedate (String servicedate) {
        this.servicedate = servicedate;
    }

    public String getClassName () {
        return className;
    }

    public void setClassName (String className) {
        this.className = className;
    }

    public int getClassId () {
        return classId;
    }

    public void setClassId (int classId) {
        this.classId = classId;
    }

    public String getSectionName () {
        return sectionName;
    }

    public void setSectionName (String sectionName) {
        this.sectionName = sectionName;
    }

    public int getSectionId () {
        return sectionId;
    }

    public void setSectionId (int sectionId) {
        this.sectionId = sectionId;
    }

    public Object getType () {
        return type;
    }

    public void setType (Object type) {
        this.type = type;
    }

    public int getPId () {
        return PId;
    }

    public void setPId (int PId) {
        this.PId = PId;
    }

    public List<TeacherListBean> getTeacherList () {
        return teacherList;
    }

    public void setTeacherList (List<TeacherListBean> teacherList) {
        this.teacherList = teacherList;
    }

    public List<SectionsListBean> getSectionsList () {
        return sectionsList;
    }

    public void setSectionsList (List<SectionsListBean> sectionsList) {
        this.sectionsList = sectionsList;
    }


    public static class TeacherListBean implements Serializable {
        private String teacherId;
        private String teacherName;
        //1  自动   2  手动
        private String autoScore;
        private boolean isOld;
        /**
         * options : [{"Id":211,"type":2,"title":"测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标(-25分)","score":"25","getScore":"","classid":138,"isTitle":false,"isCheck":false},{"Id":212,"type":2,"title":"测试指标新指标测试指标新指(-25分)","score":"25","getScore":"10","classid":138,"isTitle":false,"isCheck":false},{"Id":213,"type":2,"title":"测试指标新指指标(-25分)","score":"25","getScore":"5","classid":138,"isTitle":false,"isCheck":false}]
         * Id : 138
         * name : 卫生分
         * allScore : 125
         * sort : 0
         * getAllScore : 110
         */

        private List<DockScoreBean> dockScore;

        public boolean isOld () {
            return isOld;
        }

        public void setOld (boolean old) {
            isOld = old;
        }

        public String getTeacherId () {
            return teacherId;
        }

        public void setTeacherId (String teacherId) {
            this.teacherId = teacherId;
        }

        public String getTeacherName () {
            return teacherName;
        }

        public void setTeacherName (String teacherName) {
            this.teacherName = teacherName;
        }

        public String getAutoScore () {
            return autoScore;
        }

        public void setAutoScore (String autoScore) {
            this.autoScore = autoScore;
        }

        public List<DockScoreBean> getDockScore () {
            return dockScore;
        }

        public void setDockScore (List<DockScoreBean> dockScore) {
            this.dockScore = dockScore;
        }

        public static class DockScoreBean implements Serializable {
            private int Id;
            private String name;
            /*满分*/
            private String allScore;
            /*该扣分指标非自动计算状态的总得分*/
            private String unAutoScore;
            private int sort;
            /*总得分，自动计算的情况下*/
            private String getAllScore;
            /**
             * Id : 211
             * type : 2
             * title : 测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标测试指标新指标(-25分)
             * score : 25
             * getScore :
             * classid : 138
             * isTitle : false
             * isCheck : false
             */

            private List<OptionsBean> options;

            public String getUnAutoScore () {
                return unAutoScore;
            }

            public void setUnAutoScore (String unAutoScore) {
                this.unAutoScore = unAutoScore;
            }

            public int getId () {
                return Id;
            }

            public void setId (int Id) {
                this.Id = Id;
            }

            public String getName () {
                return name;
            }

            public void setName (String name) {
                this.name = name;
            }

            public String getAllScore () {
                return allScore;
            }

            public void setAllScore (String allScore) {
                this.allScore = allScore;
            }

            public int getSort () {
                return sort;
            }

            public void setSort (int sort) {
                this.sort = sort;
            }

            public String getGetAllScore () {
                return getAllScore;
            }

            public void setGetAllScore (String getAllScore) {
                this.getAllScore = getAllScore;
            }

            public List<OptionsBean> getOptions () {
                return options;
            }

            public void setOptions (List<OptionsBean> options) {
                this.options = options;
            }

            public static class OptionsBean implements Serializable, MultiItemEntity {
                private int Id;
                private int type;
                private String title;
                private String score;//每一项默认的分数
                private String getScore;//每一项的最后得分
                private int classid;
                private boolean isTitle;
                private boolean isCheck;
                private String allScore;//标题满分，用于比较所填分数是否大于自身，如果大于，就提示错误
                private String IncomeScore = "0";//所得到的总分数
                private String ChangeScore = "0";//修改后的分数

                private String inputScore;

                public String getInputScore () {
                    return inputScore;
                }

                public void setInputScore (String inputScore) {
                    this.inputScore = inputScore;
                }

                //标记是否有输入
                private boolean isInputNull;

                public void setInputNull (boolean inputNull) {
                    isInputNull = inputNull;
                }

                public boolean isInputNull () {
                    return isInputNull;
                }

                private int itemType;

                public String getChangeScore () {
                    return ChangeScore;
                }

                public void setChangeScore (String ChangeScore) {
                    this.ChangeScore = ChangeScore;
                }

                public String getIncomeScore () {
                    return IncomeScore;
                }

                public void setIncomeScore (String IncomeScore) {
                    this.IncomeScore = IncomeScore;
                }

                public int getId () {
                    return Id;
                }

                public void setId (int Id) {
                    this.Id = Id;
                }

                public int getType () {
                    return type;
                }

                public void setType (int type) {
                    this.type = type;
                }

                public String getTitle () {
                    return title;
                }

                public void setTitle (String title) {
                    this.title = title;
                }

                public String getScore () {
                    return score;
                }

                public void setScore (String score) {
                    this.score = score;
                }

                public String getAllScore () {
                    return allScore;
                }

                public void setAllScore (String allScore) {
                    this.allScore = allScore;
                }

                public String getGetScore () {
                    return getScore;
                }

                public void setGetScore (String getScore) {
                    this.getScore = getScore;
                }

                public int getClassid () {
                    return classid;
                }

                public void setClassid (int classid) {
                    this.classid = classid;
                }

                public boolean isIsTitle () {
                    return isTitle;
                }

                public void setIsTitle (boolean isTitle) {
                    this.isTitle = isTitle;
                }

                public boolean isIsCheck () {
                    return isCheck;
                }

                public void setIsCheck (boolean isCheck) {
                    this.isCheck = isCheck;
                }

                public void setItemType (int itemType) {
                    this.itemType = itemType;
                }

                @Override
                public int getItemType () {
                    return itemType;
                }
            }
        }
    }

    public static class SectionsListBean implements Serializable {
        private int type;
        private String sectionName;
        private int sectionId;
        private boolean isCheck;

        public boolean isCheck () {
            return isCheck;
        }

        public void setCheck (boolean check) {
            isCheck = check;
        }

        public int getType () {
            return type;
        }

        public void setType (int type) {
            this.type = type;
        }

        public String getSectionName () {
            return sectionName;
        }

        public void setSectionName (String sectionName) {
            this.sectionName = sectionName;
        }

        public int getSectionId () {
            return sectionId;
        }

        public void setSectionId (int sectionId) {
            this.sectionId = sectionId;
        }
    }
}
