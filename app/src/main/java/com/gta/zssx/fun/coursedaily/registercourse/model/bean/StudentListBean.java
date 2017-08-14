package com.gta.zssx.fun.coursedaily.registercourse.model.bean;

import java.io.Serializable;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/20.
 * @since 1.0.0
 */
@Deprecated
public class StudentListBean implements Serializable {
    private String name;
    /**
     * StudentID : “bdf3e816-bb5b-4539-a163-fc35ffc27032”
     * StundentName : “蔡晓雯”
     * StudentNO : “G44200020000107570X”
     * LESSON1 : 1
     * LESSON2 : 1
     * LESSON3 : 2
     * LESSON4 : 3
     * LESSON5 : 4
     * LESSON6 : 1
     * LESSON7 : 2
     */

    private String StudentID;
    private String StundentName;
//    private String NameOfPinYin;  //学生首字母CXW
    private String StudentNO;
    private int LESSON1;
    private int LESSON2;
    private int LESSON3;
    private int LESSON4;
    private int LESSON5;
    private int LESSON6;
    private int LESSON7;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String StudentID) {
        this.StudentID = StudentID;
    }

    public String getStundentName() {
        return StundentName;
    }

    public void setStundentName(String StundentName) {
        this.StundentName = StundentName;
    }

    public String getStudentNO() {
        return StudentNO;
    }

    public void setStudentNO(String StudentNO) {
        this.StudentNO = StudentNO;
    }

    public int getLESSON1() {
        return LESSON1;
    }

    public void setLESSON1(int LESSON1) {
        this.LESSON1 = LESSON1;
    }

    public int getLESSON2() {
        return LESSON2;
    }

    public void setLESSON2(int LESSON2) {
        this.LESSON2 = LESSON2;
    }

    public int getLESSON3() {
        return LESSON3;
    }

    public void setLESSON3(int LESSON3) {
        this.LESSON3 = LESSON3;
    }

    public int getLESSON4() {
        return LESSON4;
    }

    public void setLESSON4(int LESSON4) {
        this.LESSON4 = LESSON4;
    }

    public int getLESSON5() {
        return LESSON5;
    }

    public void setLESSON5(int LESSON5) {
        this.LESSON5 = LESSON5;
    }

    public int getLESSON6() {
        return LESSON6;
    }

    public void setLESSON6(int LESSON6) {
        this.LESSON6 = LESSON6;
    }

    public int getLESSON7() {
        return LESSON7;
    }

    public void setLESSON7(int LESSON7) {
        this.LESSON7 = LESSON7;
    }


    /**
     * 获得某个学生某节课的上课状态
     *
     * @param sectionID
     * @param studentListBean
     * @return
     */
    public static int getLessonState(int sectionID, StudentListBean studentListBean) {
        int lessonState;
        switch (sectionID) {
            case 1:
                lessonState = studentListBean.getLESSON1();
                break;
            case 2:
                lessonState = studentListBean.getLESSON2();
                break;
            case 3:
                lessonState = studentListBean.getLESSON3();
                break;
            case 4:
                lessonState = studentListBean.getLESSON4();
                break;
            case 5:
                lessonState = studentListBean.getLESSON5();
                break;
            case 6:
                lessonState = studentListBean.getLESSON6();
                break;
            case 7:
                lessonState = studentListBean.getLESSON7();
                break;
            default:
                lessonState = 1;

        }
        return lessonState;
    }
}
