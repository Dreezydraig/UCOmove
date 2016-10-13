package com.gnzlt.ucotren.adapter.viewholder;

import android.support.v7.widget.RecyclerView;

import com.gnzlt.ucotren.databinding.ItemPriceBinding;
import com.gnzlt.ucotren.model.Price;

public class PricesViewHolder extends RecyclerView.ViewHolder {

    private final ItemPriceBinding mBinding;

    public PricesViewHolder(ItemPriceBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public void bind(Price item) {
        mBinding.name.setText(item.getName());
        mBinding.price.setText(item.getPrice());
    }
}
