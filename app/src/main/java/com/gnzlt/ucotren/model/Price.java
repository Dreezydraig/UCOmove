package com.gnzlt.ucotren.model;

import com.gnzlt.ucotren.util.Constants;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Prices")
public class Price extends ParseObject {

    public Price() {
    }

    public String getType() {
        return getString("type");
    }

    public String getPrice() {
        return getString("price");
    }

    public String getName() {
        return getString("name");
    }

    public String getDescription() {
        return getString("description");
    }

    public String getOrder() {
        return getString("order");
    }

    public boolean isTrain() {
        return getType().equals(Constants.TRANSPORT_TYPE_TRAIN);
    }
}
