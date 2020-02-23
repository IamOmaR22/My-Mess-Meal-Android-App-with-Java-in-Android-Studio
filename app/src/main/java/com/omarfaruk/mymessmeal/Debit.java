package com.omarfaruk.mymessmeal;

public class Debit {
    public String taka;
    public String uId;

    public Debit (){}

    public Debit(String taka, String uId) {
        this.taka = taka;
        this.uId = uId;
    }

    public String getTaka() {
        return taka;
    }

    public void setTaka(String taka) {
        this.taka = taka;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
