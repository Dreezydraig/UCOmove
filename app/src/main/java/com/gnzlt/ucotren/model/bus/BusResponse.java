package com.gnzlt.ucotren.model.bus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BusResponse {

    private static final String STATUS_OK = "On";
    private static final String QUERY_OK = "Ok";

    public String status;
    public String queryState;
    public String reason;
    public DatetimeOfUpdated datetimeOfUpdated;
    public List<BusEstimations> response = new ArrayList<BusEstimations>();

    public BusResponse() {
    }

    public List<Date> getDepartureTimes() {
        List<Date> departures = new ArrayList<>();

        if (!response.isEmpty()) {
            for (BusEstimations estimation : response) {
                if (estimation.isValidBusLine()) {
                    departures.addAll(estimation.getEstimationsDate());
                }
            }
            Collections.sort(departures);
        }

        return departures;
    }

    public boolean isValid() {
        return status.equals(STATUS_OK) && queryState.equals(QUERY_OK);
    }
}
