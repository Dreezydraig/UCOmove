package com.gnzlt.ucotren;

import android.app.Application;

import com.gnzlt.ucotren.model.New;
import com.gnzlt.ucotren.model.ParseLocation;
import com.gnzlt.ucotren.model.Price;
import com.gnzlt.ucotren.model.Schedule;
import com.gnzlt.ucotren.util.RestClient;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParsePush;

public class UCOmoveApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RestClient.init();
        setupParse();
    }

    private void setupParse() {
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(BuildConfig.PARSE_APP_ID)
                .clientKey(BuildConfig.PARSE_CLIENT_KEY)
                .server(BuildConfig.PARSE_SERVER)
                .build());

        Parse.setLogLevel(
                BuildConfig.DEBUG ?
                        Parse.LOG_LEVEL_VERBOSE :
                        Parse.LOG_LEVEL_NONE);

        ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
        parseInstallation.increment("uses");
        parseInstallation.saveInBackground();

        ParsePush.subscribeInBackground("");
        ParsePush.subscribeInBackground("warning");
        ParsePush.subscribeInBackground("general");
        ParsePush.subscribeInBackground("news");

        ParseLocation.registerSubclass(ParseLocation.class);
        Schedule.registerSubclass(Schedule.class);
        Price.registerSubclass(Price.class);
        New.registerSubclass(New.class);
    }
}
