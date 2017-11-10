package com.jramilo.paginglistview.model;

import com.jramilo.paginglistview.model.api.YahooFeedAPI;
import com.jramilo.paginglistview.presenter.DataFeedPresenter;
import com.jramilo.paginglistview.view.DataFeedView;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jacobramilo on 11/11/17.
 */
public class DataFeedPresenterImpl implements DataFeedPresenter {
    private DataFeedView dataFeedView;

    public DataFeedPresenterImpl(DataFeedView dataFeedView) {
        this.dataFeedView = dataFeedView;
    }

    @Override
    public void loadMore(int index) {
        dataFeedView.showProgress();

        Observer<ArrayList> feedObserver = new Observer<ArrayList>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(ArrayList feed) {
                dataFeedView.addToList(feed);
            }

            @Override
            public void onError(Throwable e) {
                dataFeedView.showError(e.getMessage());
            }

            @Override
            public void onComplete() {}
        };
        getFeedObservable(index).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(feedObserver);
    }

    private Observable<ArrayList> getFeedObservable(final int pageIndex) {
        return Observable.create(new ObservableOnSubscribe<ArrayList>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList> e) throws Exception {
                e.onNext(YahooFeedAPI.retrieveFeed(pageIndex));
            }
        });
    }
}
