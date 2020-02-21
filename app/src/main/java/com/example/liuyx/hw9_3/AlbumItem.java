package com.example.liuyx.hw9_3;

import java.util.ArrayList;

/**
 * Created by Liuyx on 4/22/17.
 */

public class AlbumItem {

    String name;
    ArrayList<String> urls;

    public AlbumItem(String name, ArrayList<String> urls) {
        this.name = name;
        this.urls = urls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }
}
