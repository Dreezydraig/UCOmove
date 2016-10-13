package com.gnzlt.ucotren.schedule;

import java.util.List;

public interface ScheduleContract {

    interface View {

        void showSchedules(List<String> departures);

        void setProgressView(boolean active);

        void setEmptyView(boolean active);

        void showToastMessage(String message);
    }

    interface Presenter {

        void getSchedules(String transportType);

        void detachView();
    }
}
