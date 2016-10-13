package com.gnzlt.ucotren.model;

import android.location.Location;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("Locations")
public class ParseLocation extends ParseObject {

    public ParseLocation() {
    }

    public ParseLocation(Location location) {
        setInstallation(ParseInstallation.getCurrentInstallation());
        setGeolocation(location);
        setUserDate(new Date());
    }

    public void setInstallation(ParseInstallation installation) {
        put("installation", installation);
    }

    public void setGeolocation(Location location) {
        put("geolocation",
                new ParseGeoPoint(
                        location.getLatitude(),
                        location.getLongitude()));
    }

    public void setUserDate(Date userDate) {
        put("userDate", userDate);
    }
}
