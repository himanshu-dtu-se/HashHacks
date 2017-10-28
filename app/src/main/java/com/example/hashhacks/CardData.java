package com.example.hashhacks;

/**
 * Created by anonymous on 28/10/17.
 */

public class CardData {

    String url;
    String title;

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public CardData(String url, String title) {

        this.url = url;
        this.title = title;
    }
}
