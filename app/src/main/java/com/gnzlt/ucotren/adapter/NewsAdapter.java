package com.gnzlt.ucotren.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gnzlt.ucotren.R;
import com.gnzlt.ucotren.adapter.viewholder.NewsViewHolder;
import com.gnzlt.ucotren.databinding.ItemNewBinding;
import com.gnzlt.ucotren.model.New;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private List<New> mNews;

    public NewsAdapter(List<New> news) {
        mNews = news;
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemNewBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_new, parent, false);
        return new NewsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder viewHolder, final int position) {
        viewHolder.bind(mNews.get(position));
    }

    public void updateList(List<New> prices) {
        mNews = prices;
        notifyDataSetChanged();
    }
}
