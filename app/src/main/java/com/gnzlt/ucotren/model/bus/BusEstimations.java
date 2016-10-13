package com.gnzlt.ucotren.model.bus;

import com.gnzlt.ucotren.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BusEstimations {

    private static final String LINE_E = "E";
    private static final String LINE_RB = "RB";

    public String linea;
    public int minutos1 = -1;
    public int minutos2 = -1;

    public BusEstimations() {
    }

    public boolean isValidBusLine() {
        return linea.equals(LINE_E) || linea.equals(LINE_RB);
    }

    public List<Date> getEstimationsDate() {
        List<Date> estimations = new ArrayList<>();

        Date now = new Date();
        if (minutos1 != -1) {
            estimations.add(DateUtils.getDateWithMargin(now, minutos1));
        }
        if (minutos2 != -1) {
            estimations.add(DateUtils.getDateWithMargin(now, minutos2));
        }

        return estimations;
    }
}
