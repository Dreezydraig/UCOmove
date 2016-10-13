package com.gnzlt.ucotren.estimation;

import android.os.CountDownTimer;

import com.gnzlt.ucotren.BuildConfig;
import com.gnzlt.ucotren.model.Schedule;
import com.gnzlt.ucotren.model.bus.BusResponse;
import com.gnzlt.ucotren.util.Constants;
import com.gnzlt.ucotren.util.DateUtils;
import com.gnzlt.ucotren.util.RestClient;
import com.google.firebase.crash.FirebaseCrash;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstimationPresenter implements EstimationContract.Presenter {

    private WeakReference<EstimationContract.View> mView;
    private final String mTransportType;
    private final String mStationName;

    private ParseQuery<Schedule> mParseQuery;
    private Call<BusResponse> mRealtimeNetworkCall;

    private List<Date> mParseDepartures;
    private List<Date> mRealtimeDepartures;
    private CountDownTimer mCountDownTimer;

    public EstimationPresenter(EstimationContract.View view, String transportType, String stationName) {
        mView = new WeakReference<>(view);
        mTransportType = transportType;
        mStationName = stationName;
    }

    @Override
    public void getDepartures() {
        final boolean isTrain = Constants.TRANSPORT_TYPE_TRAIN.equals(mTransportType);
        if (isViewAttached()) {
            getView().setClickableProgress(!isTrain);
            getView().setOnlineIcon(false);
            getView().setSpinCircle(true);
        }
        cancelQuery();

        mParseQuery = ParseQuery.getQuery(Schedule.class)
                .setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK)
                .setMaxCacheAge(Constants.CACHE_MAX_AGE_IN_MS)
                .whereEqualTo("published", true)
                .whereEqualTo("type", mTransportType)
                .whereEqualTo("type_origin", mStationName);

        if (isTrain) {
            mParseQuery.whereEqualTo("date", DateUtils.getCurrentDayInParseFormat());
        }

        mParseQuery.getFirstInBackground(new GetCallback<Schedule>() {
            @Override
            public void done(Schedule schedule, ParseException error) {
                if (isViewAttached()) {
                    if (error == null) {
                        if (schedule.areDeparturesAvailable()) {
                            try {
                                mParseDepartures = schedule.getDepartureTimes();

                                if (isTrain) {
                                    checkNextDeparture();
                                } else {
                                    getRealtimeDepartures();
                                }
                            } catch (JSONException | java.text.ParseException e) {
                                e.printStackTrace();
                                FirebaseCrash.log(e.getMessage());

                                getView().showToastMessage(e.getMessage());
                                if (!isTrain) {
                                    getRealtimeDepartures();
                                }
                            }
                        } else {
                            if (isTrain) {
                                getView().showNoEstimations();
                            } else {
                                getRealtimeDepartures();
                            }
                        }
                    } else {
                        error.printStackTrace();
                        FirebaseCrash.log(error.getMessage());

                        if (isTrain) {
                            if (error.getCode() == ParseException.OBJECT_NOT_FOUND) {
                                getView().showNoEstimations();
                            } else {
                                getView().showToastMessage(error.getMessage());
                            }
                        } else {
                            getRealtimeDepartures();
                        }
                    }
                }
            }
        });
    }

    private void getRealtimeDepartures() {
        boolean isCordoba = Constants.ORIGIN_CORDOBA.equals(mStationName);
        mRealtimeDepartures = null;
        cancelNetworkCall();

        mRealtimeNetworkCall = RestClient.getApiService().getBusEstimation(
                BuildConfig.BUS_API_TOKEN,
                RestClient.BUS_CALL_DEFAULT_OPERATION,
                isCordoba ?
                        RestClient.BUS_STOP_CORDOBA :
                        RestClient.BUS_STOP_RABANALES);

        mRealtimeNetworkCall.enqueue(new Callback<BusResponse>() {
            @Override
            public void onResponse(Call<BusResponse> call, Response<BusResponse> response) {
                if (response.isSuccessful() && response.body().isValid()) {
                    mRealtimeDepartures = response.body().getDepartureTimes();
                }
                checkNextDeparture();
            }

            @Override
            public void onFailure(Call<BusResponse> call, Throwable t) {
                t.printStackTrace();
                FirebaseCrash.log(t.getMessage());

                checkNextDeparture();
            }
        });
    }

    private void checkNextDeparture() {
        if (isViewAttached()) {
            Date nextRealtimeDeparture = null;
            if ((mRealtimeDepartures != null) && !mRealtimeDepartures.isEmpty()) {
                nextRealtimeDeparture = mRealtimeDepartures.get(0);
            }

            if ((mParseDepartures != null) && !mParseDepartures.isEmpty()) {
                boolean nextDepartureFound = false;

                Date now = new Date();
                for (Date departure : mParseDepartures) {
                    if (departure.after(now)) {
                        Date departureWithMargin = DateUtils.getDateWithMargin(
                                departure, DateUtils.DEFAULT_MARGIN_IN_MINS);
                        if ((nextRealtimeDeparture != null) &&
                                nextRealtimeDeparture.before(departureWithMargin)) {
                            getView().setOnlineIcon(true);
                            setTimer(nextRealtimeDeparture);
                        } else {
                            setTimer(departure);
                        }
                        nextDepartureFound = true;
                        break;
                    }
                }

                if (!nextDepartureFound) {
                    getView().showNoEstimations();
                }
            } else {
                if (nextRealtimeDeparture != null) {
                    setTimer(nextRealtimeDeparture);
                } else {
                    getView().showNoEstimations();
                }
            }
        }
    }

    private void setTimer(Date nextDeparture) {
        if (isViewAttached()) {
            final boolean isTrain = Constants.TRANSPORT_TYPE_TRAIN.equals(mTransportType);
            cancelCountDown();

            final long timeLeft = nextDeparture.getTime() - new Date().getTime();
            mCountDownTimer = new CountDownTimer(timeLeft, DateUtils.ONE_MINUTE_IN_MILLIS) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int minutesLeft = (int) (millisUntilFinished / DateUtils.ONE_MINUTE_IN_MILLIS);
                    getView().showTimeLeft(minutesLeft);
                    if (!isTrain &&
                            ((timeLeft - millisUntilFinished) >= DateUtils.ONE_MINUTE_IN_MILLIS)) {
                        getRealtimeDepartures();
                    }
                }

                @Override
                public void onFinish() {
                    getView().setSpinCircle(true);
                    if (!isTrain) {
                        getRealtimeDepartures();
                    } else {
                        checkNextDeparture();
                    }
                }
            };

            mCountDownTimer.start();
        }
    }


    private void cancelQuery() {
        if (mParseQuery != null) {
            mParseQuery.cancel();
        }
    }

    private void cancelNetworkCall() {
        if ((mRealtimeNetworkCall != null) && mRealtimeNetworkCall.isExecuted()) {
            mRealtimeNetworkCall.cancel();
        }
    }

    private void cancelCountDown() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    private boolean isViewAttached() {
        return (mView != null) && (mView.get() != null);
    }

    public EstimationContract.View getView() {
        return mView != null ? mView.get() : null;
    }

    @Override
    public void detachView() {
        cancelCountDown();
        cancelQuery();
        cancelNetworkCall();

        if (mView != null) {
            mView.clear();
            mView = null;
        }
    }
}
