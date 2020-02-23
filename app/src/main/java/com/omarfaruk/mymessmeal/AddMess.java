package com.omarfaruk.mymessmeal;

public class AddMess {
    public String admin;
    public int total_cost, meal_rate, total_members;


    public AddMess(){

    }

    public AddMess(String admin, int total_cost, int meal_rate, int total_members) {
        this.admin = admin;
        this.total_cost = total_cost;
        this.meal_rate = meal_rate;
        this.total_members = total_members;
    }

    public String getAdmin() {
        return admin;
    }

    public int getTotal_cost() {
        return total_cost;
    }

    public int getMeal_rate() {
        return meal_rate;
    }

    public int getTotal_members() {
        return total_members;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public void setTotal_cost(int total_cost) {
        this.total_cost = total_cost;
    }

    public void setMeal_rate(int meal_rate) {
        this.meal_rate = meal_rate;
    }

    public void setTotal_members(int total_members) {
        this.total_members = total_members;
    }
}
