package com.gnzlt.ucotren.schedule;

import com.gnzlt.ucotren.model.Schedule;
import com.gnzlt.ucotren.util.Constants;
import com.gnzlt.ucotren.util.DateUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SchedulePresenter implements ScheduleContract.Presenter {

    private WeakReference<ScheduleContract.View> mView;

    private ParseQuery<Schedule> mParseQuery;

    public SchedulePresenter(ScheduleContract.View view) {
        mView = new WeakReference<>(view);
    }

    @Override
    public void getSchedules(String transportType) {
        cancelQuery();

        mParseQuery = ParseQuery.getQuery(Schedule.class)
                .setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK)
                .setMaxCacheAge(Constants.CACHE_MAX_AGE_IN_MS)
                .whereEqualTo("published", true)
                .whereEqualTo("type", transportType);

        if (Constants.TRANSPORT_TYPE_TRAIN.equals(transportType)) {
            mParseQuery.whereEqualTo("date", DateUtils.getCurrentDayInParseFormat());
        }

        mParseQuery.findInBackground(new FindCallback<Schedule>() {
            @Override
            public void done(List<Schedule> schedules, ParseException error) {
                if (isViewAttached()) {
                    if (error == null) {
                        List<Date> rabanalesDepartures = new ArrayList<>();
                        List<Date> cordobaDepartures = new ArrayList<>();
                        for (Schedule schedule : schedules) {
                            try {
                                if (Constants.ORIGIN_RABANALES.equals(schedule.getTypeOrigin())) {
                                    rabanalesDepartures = schedule.getDepartureTimes();
                                } else {
                                    cordobaDepartures = schedule.getDepartureTimes();
                                }
                            } catch (JSONException | java.text.ParseException e) {
                                getView().showToastMessage(e.getMessage());
                                e.printStackTrace();
                            }
                        }

                        processSchedules(rabanalesDepartures, cordobaDepartures);
                    } else {
                        getView().setEmptyView(true);
                        getView().showToastMessage(error.getMessage());
                    }
                    getView().setProgressView(false);
                }
            }
        });
    }

    private void processSchedules(List<Date> rabanalesDepartures, List<Date> cordobaDepartures) {
        int listSize = Math.max(cordobaDepartures.size(), rabanalesDepartures.size()) * 2;
        List<String> departures = new ArrayList<>(listSize);
        for (int i = 0; i < listSize; i++) {
            int relativeIndex = Math.round(i / 2);
            if ((i % 2) == 0) {
                String stringDate = "";
                if (relativeIndex < cordobaDepartures.size()) {
                    stringDate = DateUtils.HHmmFormat.format(cordobaDepartures.get(relativeIndex));
                }
                departures.add(stringDate);
            } else {
                String stringDate = "";
                if (relativeIndex < rabanalesDepartures.size()) {
                    stringDate = DateUtils.HHmmFormat.format(rabanalesDepartures.get(relativeIndex));
                }
                departures.add(stringDate);
            }
        }

        if (!departures.isEmpty()) {
            getView().showSchedules(departures);
            getView().setEmptyView(false);
        } else {
            getView().setEmptyView(true);
        }
    }

    private void cancelQuery() {
        if (mParseQuery != null) {
            mParseQuery.cancel();
        }
    }

    private boolean isViewAttached() {
        return (mView != null) && (mView.get() != null);
    }

    public ScheduleContract.View getView() {
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
