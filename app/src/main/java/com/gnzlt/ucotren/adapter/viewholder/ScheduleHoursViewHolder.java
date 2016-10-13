package com.gnzlt.ucotren.adapter.viewholder;

import android.support.v7.widget.RecyclerView;

import com.gnzlt.ucotren.databinding.ItemScheduleHoursBinding;

public class ScheduleHoursViewHolder extends RecyclerView.ViewHolder {

    private final ItemScheduleHoursBinding mBinding;

    public ScheduleHoursViewHolder(ItemScheduleHoursBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public void bind(String item) {
        mBinding.hour.setText(item);
    }
}
