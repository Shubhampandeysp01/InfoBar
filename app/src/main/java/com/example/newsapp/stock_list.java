package com.example.newsapp;

public class stock_list {
    private String fifwhigh;
    private String fifwlow;
    private String mcapital;
    private String name;
    private String todayprice;
    private String yestprice;

    stock_list(){

    }

    public stock_list(String fifwhigh, String fifwlow, String mcapital, String name, String todayprice, String yestprice) {
        this.fifwhigh = fifwhigh;
        this.fifwlow = fifwlow;
        this.mcapital = mcapital;
        this.name = name;
        this.todayprice = todayprice;
        this.yestprice = yestprice;
    }

    public String getFifwhigh() {
        return fifwhigh;
    }

    public void setFifwhigh(String fifwhigh) {
        this.fifwhigh = fifwhigh;
    }

    public String getFifwlow() {
        return fifwlow;
    }

    public void setFifwlow(String fifwlow) {
        this.fifwlow = fifwlow;
    }

    public String getMcapital() {
        return mcapital;
    }

    public void setMcapital(String mcapital) {
        this.mcapital = mcapital;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTodayprice() {
        return todayprice;
    }

    public void setTodayprice(String todayprice) {
        this.todayprice = todayprice;
    }

    public String getYestprice() {
        return yestprice;
    }

    public void setYestprice(String yestprice) {
        this.yestprice = yestprice;
    }
}
