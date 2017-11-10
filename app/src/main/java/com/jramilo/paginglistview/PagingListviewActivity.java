package com.jramilo.paginglistview;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.jramilo.paginglistview.adapter.PagingListviewAdapter;
import com.jramilo.paginglistview.model.DataFeedPresenterImpl;
import com.jramilo.paginglistview.model.NewsFeed;
import com.jramilo.paginglistview.presenter.DataFeedPresenter;
import com.jramilo.paginglistview.view.DataFeedView;

import java.util.ArrayList;

public class PagingListviewActivity extends AppCompatActivity implements DataFeedView {
    private static final int MAX_LIST_REFRESH_COUNT = 10;
    private static final String STATE_LIST_NAME = "list-data";

    private PagingListviewAdapter pagingListviewAdapter;
    private ProgressBar progressBar;

    private DataFeedPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paging_listview);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        pagingListviewAdapter = new PagingListviewAdapter(this);

        presenter = new DataFeedPresenterImpl(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_feeds);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager=LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= MAX_LIST_REFRESH_COUNT) {
                    presenter.loadMore(totalItemCount);
                }
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(pagingListviewAdapter);

        if(savedInstanceState != null) {
            ArrayList<NewsFeed> savedFeedItems = savedInstanceState.getParcelableArrayList(STATE_LIST_NAME);
            addToList(savedFeedItems);
        } else {
            presenter.loadMore(0);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_LIST_NAME, pagingListviewAdapter.getNewsFeeds());
    }

    @Override
    public void addToList(ArrayList<NewsFeed> feeds) {
        pagingListviewAdapter.addNewsFeeds(feeds);
        if(progressBar.isShown()) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProgress() {
        if(!progressBar.isShown()) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showError(String message) {
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.container);
        Snackbar.make(layout, message, Snackbar.LENGTH_SHORT).show();
    }
}