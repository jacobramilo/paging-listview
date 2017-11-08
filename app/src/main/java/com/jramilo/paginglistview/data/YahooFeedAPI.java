package com.jramilo.paginglistview.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jacobramilo on 2/11/17.
 */
public class YahooFeedAPI {
    private static final String TAG = YahooFeedAPI.class.getName();
    private static final String STRING_URL = "https://aboutdoor.info/news?index=";

    public static ArrayList<NewsFeed> retrieveFeed(final int pageIndex) throws IOException, JSONException {
        ArrayList<NewsFeed> feeds = new ArrayList<>();
        HttpURLConnection urlConnection = null;
        InputStream inputstream = null;

        URL url = new URL(STRING_URL + pageIndex);
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            inputstream = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }

            JSONArray feedsJsonArray = new JSONArray(responseStrBuilder.toString());
            int feedsLen = feedsJsonArray.length();

            for(int i = 0; i < feedsLen; i++) {
                JSONObject singleItem = feedsJsonArray.getJSONObject(i);

                String id =  singleItem.getString("id");
                String headline = singleItem.getString("headline");
                String imgUrl = singleItem.getString("image");

                NewsFeed feed = new NewsFeed(imgUrl, headline);
                feeds.add(feed);
            }
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }

            if(inputstream != null) {
                inputstream.close();
            }
        }

        return feeds;
    }
}
