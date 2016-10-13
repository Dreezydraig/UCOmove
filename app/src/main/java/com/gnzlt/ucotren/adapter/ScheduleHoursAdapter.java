package com.gnzlt.ucotren.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gnzlt.ucotren.R;
import com.gnzlt.ucotren.adapter.viewholder.ScheduleHoursViewHolder;
import com.gnzlt.ucotren.databinding.ItemScheduleHoursBinding;

import java.util.List;

public class ScheduleHoursAdapter extends RecyclerView.Adapter<ScheduleHoursViewHolder> {

    private List<String> mDepartures;

    public ScheduleHoursAdapter(List<String> departures) {
        mDepartures = departures;
    }

    @Override
    public int getItemCount() {
        return mDepartures.size();
    }

    @Override
    public ScheduleHoursViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ItemScheduleHoursBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_schedule_hours, parent, false);
        return new ScheduleHoursViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ScheduleHoursViewHolder viewHolder, final int position) {
        viewHolder.bind(mDepartures.get(position));
    }

    public void updateList(List<String> departures) {
        mDepartures = departures;
        notifyDataSetChanged();
    }
}
