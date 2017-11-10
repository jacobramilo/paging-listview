package com.jramilo.paginglistview.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jacobramilo on 8/11/17.
 */
public class NewsFeed implements Parcelable {
    private String imgUrl;
    private String headline;

    public NewsFeed(String imgUrl, String headline) {
        this.imgUrl = imgUrl;
        this.headline = headline;
    }

    protected NewsFeed(Parcel in) {
        imgUrl = in.readString();
        headline = in.readString();
    }

    public static final Creator<NewsFeed> CREATOR = new Creator<NewsFeed>() {
        @Override
        public NewsFeed createFromParcel(Parcel in) {
            return new NewsFeed(in);
        }

        @Override
        public NewsFeed[] newArray(int size) {
            return new NewsFeed[size];
        }
    };

    public String getImgUrl() {
        return imgUrl;
    }

    public String getHeadline() {
        return headline;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imgUrl);
        parcel.writeString(headline);
    }
}
