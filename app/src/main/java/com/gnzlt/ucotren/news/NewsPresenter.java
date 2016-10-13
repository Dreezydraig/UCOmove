package com.gnzlt.ucotren.news;

import com.gnzlt.ucotren.model.New;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.lang.ref.WeakReference;
import java.util.List;

public class NewsPresenter implements NewsContract.Presenter {

    private WeakReference<NewsContract.View> mView;

    private ParseQuery<New> mParseQuery;

    public NewsPresenter(NewsContract.View view) {
        mView = new WeakReference<>(view);
    }

    @Override
    public void getNews() {
        cancelQuery();

        mParseQuery = ParseQuery.getQuery(New.class)
                .setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE)
                .whereEqualTo("published", true)
                .orderByDescending("date");

        mParseQuery.findInBackground(new FindCallback<New>() {
            @Override
            public void done(List<New> news, ParseException error) {
                if (isViewAttached()) {
                    if (error == null) {
                        if (!news.isEmpty()) {
                            getView().showNews(news);
                            getView().setEmptyView(false);
                        } else {
                            getView().setEmptyView(true);
                        }
                    } else {
                        getView().setEmptyView(true);
                        getView().showToastMessage(error.getMessage());
                    }
                    getView().setProgressIndicator(false);
                }
            }
        });
    }

    private void cancelQuery() {
        if (mParseQuery != null) {
            mParseQuery.cancel();
        }
    }

    private boolean isViewAttached() {
        return (mView != null) && (mView.get() != null);
    }

    public NewsContract.View getView() {
        return mView != null ? mView.get() : null;
    }

    @Override
    public void detachView() {
        cancelQuery();

        if (mView != null) {
            mView.clear();
            mView = null;
        }
    }
}
