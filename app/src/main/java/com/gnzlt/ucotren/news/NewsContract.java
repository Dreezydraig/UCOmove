package com.gnzlt.ucotren.news;

import com.gnzlt.ucotren.model.New;

import java.util.List;

public interface NewsContract {

    interface View {

        void showNews(List<New> news);

        void setProgressIndicator(boolean active);

        void setEmptyView(boolean active);

        void showToastMessage(String message);
    }

    interface Presenter {

        void getNews();

        void detachView();
    }
}
