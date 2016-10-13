package com.gnzlt.ucotren.about;

import com.gnzlt.ucotren.BuildConfig;

import java.lang.ref.WeakReference;

public class AboutPresenter implements AboutContract.Presenter {

    private static final String URL_TWITTER = "https://twitter.com/UCOmove";
    private static final String URL_GOOGLE_PLAY = "market://details?id=";

    private WeakReference<AboutContract.View> mView;

    public AboutPresenter(AboutContract.View view) {
        mView = new WeakReference<>(view);
    }

    @Override
    public void openTwitter() {
        if (isViewAttached()) {
            getView().startBrowserIntent(URL_TWITTER);
        }
    }

    @Override
    public void openGooglePlay() {
        if (isViewAttached()) {
            getView().startBrowserIntent(URL_GOOGLE_PLAY + BuildConfig.APPLICATION_ID);
        }
    }

    private boolean isViewAttached() {
        return (mView != null) && (mView.get() != null);
    }

    public AboutContract.View getView() {
        return mView != null ? mView.get() : null;
    }

    @Override
    public void detachView() {
        if (mView != null) {
            mView.clear();
            mView = null;
        }
    }
}
