package com.example.liuyx.hw9_3;

/**
 * Created by Liuyx on 4/20/17.
 */

public class Item {
    String id;
    String name;
    String type;
    String pictureURL;
    boolean isFav;

    public Item(String id, String name, String type, String pictureURL, boolean isFav) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.pictureURL = pictureURL;
        this.isFav = isFav;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }
}
