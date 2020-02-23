package com.omarfaruk.mymessmeal;

public class TempMeal {
    public int m1, m2, m3;
    public String uid;

    public TempMeal(){

    }

    public TempMeal(int m1, int m2, int m3, String uid) {
        this.m1 = m1;
        this.m2 = m2;
        this.m3 = m3;
        this.uid = uid;
    }

    public int getM1() {
        return m1;
    }

    public void setM1(int m1) {
        this.m1 = m1;
    }

    public int getM2() {
        return m2;
    }

    public void setM2(int m2) {
        this.m2 = m2;
    }

    public int getM3() {
        return m3;
    }

    public void setM3(int m3) {
        this.m3 = m3;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
