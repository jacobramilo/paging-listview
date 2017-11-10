package com.jramilo.paginglistview.model.api;

import android.util.Log;

import com.jramilo.paginglistview.model.NewsFeed;

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
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by jacobramilo on 2/11/17.
 */
public class YahooFeedAPI {
    private static final String TAG = YahooFeedAPI.class.getName();
    private static final String STRING_URL = "https://aboutdoor.info/news?index=";

    public static ArrayList<NewsFeed> retrieveFeed(final int pageIndex) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        InputStream inputstream = null;

        URL url = new URL(STRING_URL + pageIndex);
        Log.d(TAG, "Connecting to " + url.toString());

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            inputstream = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputstream, Charset.defaultCharset()));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }

            return convertToJsonArray(responseStrBuilder.toString());
        } finally {
            if(urlConnection != null) {
                Log.d(TAG, "Disconnecting the urlConnection");
                urlConnection.disconnect();
            }

            if(inputstream != null) {
                Log.d(TAG, "Closing the inputstream");
                inputstream.close();
            }
        }
    }

    private static ArrayList<NewsFeed> convertToJsonArray(final String jsonString) throws JSONException {
        ArrayList<NewsFeed> feeds = new ArrayList<>();
        JSONArray feedsJsonArray = new JSONArray(jsonString);
        int feedsLen = feedsJsonArray.length();
        Log.d(TAG, "Feed size : " + feedsLen);

        for(int i = 0; i < feedsLen; i++) {
            JSONObject singleItem = feedsJsonArray.getJSONObject(i);
            String headline = singleItem.getString("headline");
            String imgUrl = singleItem.getString("image");

            Log.d(TAG, "Image URL : " + imgUrl + " Headline: " + headline);

            NewsFeed feed = new NewsFeed(imgUrl, headline);
            feeds.add(feed);
        }
        return feeds;
    }
}
