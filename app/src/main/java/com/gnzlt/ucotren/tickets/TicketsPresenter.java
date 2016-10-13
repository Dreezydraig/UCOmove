package com.gnzlt.ucotren.tickets;

import com.gnzlt.ucotren.model.Price;
import com.gnzlt.ucotren.util.Constants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class TicketsPresenter implements TicketsContract.Presenter {

    private WeakReference<TicketsContract.View> mView;

    private ParseQuery<Price> mParseQuery;

    public TicketsPresenter(TicketsContract.View view) {
        mView = new WeakReference<>(view);
    }

    @Override
    public void getPrices() {
        cancelQuery();

        mParseQuery = ParseQuery.getQuery(Price.class)
                .setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK)
                .setMaxCacheAge(Constants.CACHE_MAX_AGE_IN_MS)
                .whereEqualTo("enable", true)
                .orderByAscending("order");

        mParseQuery.findInBackground(new FindCallback<Price>() {
            @Override
            public void done(List<Price> prices, ParseException error) {
                if (isViewAttached()) {
                    if (error == null && !prices.isEmpty()) {
                        List<Price> trainPrices = new ArrayList<>();
                        List<Price> busPrices = new ArrayList<>();
                        for (Price price : prices) {
                            if (price.isTrain()) {
                                trainPrices.add(price);
                            } else {
                                busPrices.add(price);
                            }
                        }
                        getView().showTrainPrices(trainPrices);
                        getView().showBusPrices(busPrices);
                        getView().setEmptyView(false);
                    } else {
                        getView().setEmptyView(true);
                        getView().showToastMessage(error.getMessage());
                    }
                    getView().setProgressView(false);
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

    public TicketsContract.View getView() {
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
