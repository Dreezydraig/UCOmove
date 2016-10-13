package com.gnzlt.ucotren.tickets;

import com.gnzlt.ucotren.model.Price;

import java.util.List;

public interface TicketsContract {

    interface View {

        void showTrainPrices(List<Price> prices);

        void showBusPrices(List<Price> prices);

        void setProgressView(boolean active);

        void setEmptyView(boolean active);

        void showToastMessage(String message);
    }

    interface Presenter {

        void getPrices();

        void detachView();
    }
}
