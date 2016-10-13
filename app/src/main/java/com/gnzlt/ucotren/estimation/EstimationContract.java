package com.gnzlt.ucotren.estimation;

public interface EstimationContract {

    interface View {

        void showTimeLeft(int minutes);

        void showNoEstimations();

        void setSpinCircle(boolean active);

        void setOnlineIcon(boolean active);

        void setClickableProgress(boolean active);

        void showToastMessage(String message);
    }

    interface Presenter {

        void getDepartures();

        void detachView();
    }
}
