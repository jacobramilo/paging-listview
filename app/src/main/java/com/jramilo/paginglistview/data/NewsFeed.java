package com.jramilo.paginglistview.data;

/**
 * Created by jacobramilo on 8/11/17.
 */
public class NewsFeed {
    private String imgUrl;
    private String headline;

    public NewsFeed(String imgUrl, String headline) {
        this.imgUrl = imgUrl;
        this.headline = headline;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getHeadline() {
        return headline;
    }
}
