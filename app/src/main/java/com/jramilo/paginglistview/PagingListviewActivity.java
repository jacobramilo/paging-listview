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
import com.jramilo.paginglistview.data.YahooFeedAPI;

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

    private PagingListviewAdapter pagingListviewAdapter;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paging_listview);

        pagingListviewAdapter = new PagingListviewAdapter(this);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

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
                    if(!progressBar.isShown()) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    getFeeds(totalItemCount);
                }
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(pagingListviewAdapter);

        getFeeds(0);
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
