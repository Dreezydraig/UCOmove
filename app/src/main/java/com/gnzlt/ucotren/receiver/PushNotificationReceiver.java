package com.gnzlt.ucotren.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gnzlt.ucotren.MainActivity;
import com.gnzlt.ucotren.util.Constants;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;

public class PushNotificationReceiver extends ParsePushBroadcastReceiver {

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        ParseAnalytics.trackAppOpenedInBackground(intent);
        FirebaseAnalytics.getInstance(context).logEvent(Constants.EVENT_NOTIFICATION_OPEN,
                new Bundle());

        Intent activityIntent = MainActivity.getNewIntent(context);
        activityIntent.putExtras(intent.getExtras());
        context.startActivity(activityIntent);
    }
}
