package com.gnzlt.ucotren.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.gnzlt.ucotren.databinding.ItemNewBinding;
import com.gnzlt.ucotren.model.New;
import com.gnzlt.ucotren.util.DateUtils;

public class NewsViewHolder extends RecyclerView.ViewHolder {

    private final ItemNewBinding mBinding;

    public NewsViewHolder(ItemNewBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public void bind(New item) {
        mBinding.title.setText(item.getTitle());
        mBinding.body.setText(item.getFormattedBody());
        mBinding.date.setText(DateUtils.getFormattedNewDate(item.getDate()));

        if (item.getImage() != null) {
            Glide.with(itemView.getContext())
                    .load(item.getImage().getUrl())
                    .centerCrop()
                    .into(mBinding.image);
        } else {
            mBinding.image.setVisibility(View.GONE);
        }
    }
}
