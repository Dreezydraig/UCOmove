package com.gnzlt.ucotren.model;

import com.gnzlt.ucotren.util.DateUtils;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ParseClassName("Schedule")
public class Schedule extends ParseObject {

    public Schedule() {
    }

    public String getType() {
        return getString("type");
    }

    public String getOrigin() {
        return getString("origin");
    }

    public String getTypeOrigin() {
        return getString("type_origin");
    }

    public String getOriginId() {
        return getString("originId");
    }

    public boolean areDeparturesAvailable() {
        return getJSONArray("schedule").length() > 0;
    }

    public ParseGeoPoint getOriginLocation() {
        return getParseGeoPoint("date");
    }

    public Date getDate() {
        return getDate("date");
    }

    public String getStationName() {
        return getString("stationName");
    }

    public List<Date> getDepartureTimes() throws JSONException, ParseException {
        List<Date> departureTimes = new ArrayList<Date>();
        JSONArray jsonArray = getJSONArray("schedule");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
            String departureHour = jsonObject.getString("departureTime");

            if (jsonObject.has("workingDays")) {
                String workingDays = jsonObject.getString("workingDays");
                if (DateUtils.isValidBusDay(workingDays)) {
                    departureTimes.add(DateUtils.getDateFromDepartureHour(departureHour));
                }
            } else {
                departureTimes.add(DateUtils.getDateFromDepartureHour(departureHour));
            }
        }
        return departureTimes;
    }
}
