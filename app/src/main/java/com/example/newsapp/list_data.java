package com.example.newsapp;

import com.google.firebase.Timestamp;

public class list_data {
    private String articleurl;
    private String content;
    private String imageUrl;
    private String showurl;
    private String title;
    private Timestamp ttime;
    private Boolean zbusiness;
    private Boolean zcricket;
    private Boolean zentertainment;
    private Boolean zpolitics;
    private Boolean ztechnology;
    private Boolean ztrending;
    private Boolean zworld;
    private Boolean zyoutube;


    private list_data(){

    }

    public list_data(String articleurl, String content, String imageUrl, String showurl, String title, Timestamp ttime,Boolean zbusiness, Boolean zcricket, Boolean zentertainment, Boolean zpolitics, Boolean ztechnology, Boolean ztrending, Boolean zworld, Boolean zyoutube) {
        this.articleurl = articleurl;
        this.content = content;
        this.imageUrl = imageUrl;
        this.showurl = showurl;
        this.title = title;
        this.ttime = ttime;
        this.zbusiness = zbusiness;
        this.zcricket = zcricket;
        this.zentertainment = zentertainment;
        this.zpolitics = zpolitics;
        this.ztechnology = ztechnology;
        this.ztrending = ztrending;
        this.zworld = zworld;
        this.zyoutube = zyoutube;
    }

    public String getArticleurl() {
        return articleurl;
    }

    public void setArticleurl(String articleurl) {
        this.articleurl = articleurl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getShowurl() {
        return showurl;
    }

    public void setShowurl(String showurl) {
        this.showurl = showurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getTtime() {
        return ttime;
    }

    public void setTtime(Timestamp ttime) {
        this.ttime = ttime;
    }

    public Boolean getZbusiness() {
        return zbusiness;
    }

    public void setZbusiness(Boolean zbusiness) {
        this.zbusiness = zbusiness;
    }

    public Boolean getZcricket() {
        return zcricket;
    }

    public void setZcricket(Boolean zcricket) {
        this.zcricket = zcricket;
    }

    public Boolean getZentertainment() {
        return zentertainment;
    }

    public void setZentertainment(Boolean zentertainment) {
        this.zentertainment = zentertainment;
    }

    public Boolean getZpolitics() {
        return zpolitics;
    }

    public void setZpolitics(Boolean zpolitics) {
        this.zpolitics = zpolitics;
    }

    public Boolean getZtechnology() {
        return ztechnology;
    }

    public void setZtechnology(Boolean ztechnology) {
        this.ztechnology = ztechnology;
    }

    public Boolean getZtrending() {
        return ztrending;
    }

    public void setZtrending(Boolean ztrending) {
        this.ztrending = ztrending;
    }

    public Boolean getZworld() {
        return zworld;
    }

    public void setZworld(Boolean zworld) {
        this.zworld = zworld;
    }

    public Boolean getZyoutube() {
        return zyoutube;
    }

    public void setZyoutube(Boolean zyoutube) {
        this.zyoutube = zyoutube;
    }
}
