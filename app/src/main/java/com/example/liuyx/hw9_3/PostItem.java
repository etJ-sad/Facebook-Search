package com.example.liuyx.hw9_3;

/**
 * Created by Liuyx on 4/22/17.
 */

public class PostItem {

    String pictureUrl;
    String message;
    String time;
    String name;

    public PostItem(String message, String time, String pictureUrl, String name) {
        this.message = message;
        this.time = time;
        this.pictureUrl = pictureUrl;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
