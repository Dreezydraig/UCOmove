package com.gnzlt.ucotren.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gnzlt.ucotren.R;
import com.gnzlt.ucotren.adapter.viewholder.PricesViewHolder;
import com.gnzlt.ucotren.databinding.ItemPriceBinding;
import com.gnzlt.ucotren.model.Price;

import java.util.List;

public class PricesAdapter extends RecyclerView.Adapter<PricesViewHolder> {

    private List<Price> mPrices;

    public PricesAdapter(List<Price> prices) {
        mPrices = prices;
    }

    @Override
    public int getItemCount() {
        return mPrices.size();
    }

    @Override
    public PricesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemPriceBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_price, parent, false);
        return new PricesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(PricesViewHolder viewHolder, final int position) {
        viewHolder.bind(mPrices.get(position));
    }

    public void updateList(List<Price> prices) {
        mPrices = prices;
        notifyDataSetChanged();
    }
}
