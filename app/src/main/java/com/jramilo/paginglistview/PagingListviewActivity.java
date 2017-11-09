package com.jramilo.paginglistview;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.jramilo.paginglistview.adapter.PagingListviewAdapter;
import com.jramilo.paginglistview.api.YahooFeedAPI;
import com.jramilo.paginglistview.data.NewsFeed;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PagingListviewActivity extends AppCompatActivity {
    private static final int MAX_LIST_REFRESH_COUNT = 10;
    private static final String STATE_LIST_NAME = "list-data";

    private PagingListviewAdapter pagingListviewAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paging_listview);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        pagingListviewAdapter = new PagingListviewAdapter(this);

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
                    getFeeds(totalItemCount);
                }
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(pagingListviewAdapter);

        if(savedInstanceState != null) {
            ArrayList<NewsFeed> savedFeedItems = savedInstanceState.getParcelableArrayList(STATE_LIST_NAME);
            pagingListviewAdapter.addNewsFeeds(savedFeedItems);
        } else {
            getFeeds(0);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_LIST_NAME, pagingListviewAdapter.getNewsFeeds());
    }

    private Observable<ArrayList> getFeedObservable(final int pageIndex) {
        return Observable.create(new ObservableOnSubscribe<ArrayList>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList> e) throws Exception {
                e.onNext(YahooFeedAPI.retrieveFeed(pageIndex));
            }
        });
    }

    public void getFeeds(final int items) {
        if(!progressBar.isShown()) {
            progressBar.setVisibility(View.VISIBLE);
        }

        Observer<ArrayList> feedObserver = new Observer<ArrayList>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(ArrayList feed) {
                if(progressBar.isShown()) {
                    progressBar.setVisibility(View.GONE);
                }
                pagingListviewAdapter.addNewsFeeds(feed);
            }

            @Override
            public void onError(Throwable e) {
                ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.container);
                Snackbar.make(constraintLayout, e.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onComplete() {}
        };
        getFeedObservable(items).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(feedObserver);
    }
}