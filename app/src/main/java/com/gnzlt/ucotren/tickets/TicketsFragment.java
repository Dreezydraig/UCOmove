package com.gnzlt.ucotren.tickets;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gnzlt.ucotren.R;
import com.gnzlt.ucotren.adapter.PricesAdapter;
import com.gnzlt.ucotren.databinding.FragmentTicketsBinding;
import com.gnzlt.ucotren.model.Price;
import com.gnzlt.ucotren.util.view.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class TicketsFragment extends Fragment implements TicketsContract.View {

    private TicketsPresenter mPresenter;
    private FragmentTicketsBinding mBinding;

    private PricesAdapter mTrainAdapter;
    private PricesAdapter mBusAdapter;

    public static TicketsFragment newInstance() {
        return new TicketsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPresenter = new TicketsPresenter(this);

        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_tickets,
                container,
                false);

        mTrainAdapter = new PricesAdapter(new ArrayList<Price>(0));
        mBinding.listTrain.setHasFixedSize(true);
        mBinding.listTrain.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.listTrain.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        mBinding.listTrain.setAdapter(mTrainAdapter);

        mBusAdapter = new PricesAdapter(new ArrayList<Price>(0));
        mBinding.listBus.setHasFixedSize(true);
        mBinding.listBus.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.listBus.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        mBinding.listBus.setAdapter(mBusAdapter);

        mBinding.empty.message.setText(getString(R.string.error_no_tickets));

        mPresenter.getPrices();

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        mPresenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void showTrainPrices(List<Price> prices) {
        if (isAdded()) {
            mTrainAdapter.updateList(prices);
            mBinding.listTrain.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showBusPrices(List<Price> prices) {
        if (isAdded()) {
            mBusAdapter.updateList(prices);
            mBinding.listBus.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setProgressView(boolean active) {
        mBinding.progress.progressView.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setEmptyView(boolean active) {
        mBinding.empty.emptyView.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
