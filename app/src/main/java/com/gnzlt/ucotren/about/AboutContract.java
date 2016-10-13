package com.gnzlt.ucotren.about;

public interface AboutContract {

    interface View {

        void startBrowserIntent(String url);
    }

    interface Presenter {

        void openTwitter();

        void openGooglePlay();

        void detachView();
    }
}
