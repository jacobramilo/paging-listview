package com.jramilo.paginglistview.view;

import com.jramilo.paginglistview.model.NewsFeed;

import java.util.ArrayList;

/**
 * Created by jacobramilo on 11/11/17.
 */

public interface DataFeedView {

    void addToList(ArrayList<NewsFeed> feeds);

    void showProgress();

    void showError(String message);
}
