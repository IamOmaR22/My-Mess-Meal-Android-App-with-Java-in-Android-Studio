package com.omarfaruk.mymessmeal;

public class Cost {
    public int taka;
    public String date;

    public Cost(){}

    public Cost(int taka, String date) {
        this.taka = taka;
        this.date = date;
    }

    public int getTaka() {
        return taka;
    }

    public void setTaka(int taka) {
        this.taka = taka;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
