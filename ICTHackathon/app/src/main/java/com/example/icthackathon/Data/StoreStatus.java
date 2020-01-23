package com.example.icthackathon.Data;

import java.io.Serializable;

public class StoreStatus implements Serializable {

    private String serial;
    private String name_kr;
    private String image1;
    private String image2;
    private String image3;
    private String category;
    private String address_kr;
    private String lat;
    private String lon;
    private String comment_kr;
    private String waiting_count;
    private String waiting_time;



    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getName_kr() {
        return name_kr;
    }

    public void setName_kr(String name_kr) {
        this.name_kr = name_kr;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress_kr() {
        return address_kr;
    }

    public void setAddress_kr(String address_kr) {
        this.address_kr = address_kr;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getComment_kr() {
        return comment_kr;
    }

    public void setComment_kr(String comment_kr) {
        this.comment_kr = comment_kr;
    }

    public String getWaiting_count() {
        return waiting_count;
    }

    public void setWaiting_count(String waiting_count) {
        this.waiting_count = waiting_count;
    }

    public String getWaiting_time() {
        return waiting_time;
    }

    public void setWaiting_time(String waiting_time) {
        this.waiting_time = waiting_time;
    }
}
