package com.gnzlt.ucotren.schedule;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gnzlt.ucotren.R;
import com.gnzlt.ucotren.adapter.ScheduleHoursAdapter;
import com.gnzlt.ucotren.databinding.FragmentScheduleBinding;
import com.gnzlt.ucotren.util.Constants;
import com.gnzlt.ucotren.util.view.GridItemOffsetDecoration;

import java.util.ArrayList;
import java.util.List;

public class ScheduleFragment extends Fragment implements ScheduleContract.View {

    private static final String ARG_TRANSPORT_TYPE = "ARG_TRANSPORT_TYPE";

    private SchedulePresenter mPresenter;
    private FragmentScheduleBinding mBinding;
    private String mTransportType;

    private ScheduleHoursAdapter mAdapter;

    public static ScheduleFragment newInstance(int transportType) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TRANSPORT_TYPE, transportType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTransportType = Constants.TRANSPORT_TYPES[getArguments().getInt(ARG_TRANSPORT_TYPE)];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPresenter = new SchedulePresenter(this);

        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_schedule,
                container,
                false);

        mAdapter = new ScheduleHoursAdapter(new ArrayList<String>(0));
        mBinding.list.setHasFixedSize(true);
        mBinding.list.addItemDecoration(
                new GridItemOffsetDecoration(getContext(),
                        R.dimen.grid_item_offset));
        mBinding.list.setLayoutManager(
                new GridLayoutManager(getContext(), 2));
        mBinding.list.setAdapter(mAdapter);

        if (Constants.TRANSPORT_TYPE_BUS.equals(mTransportType)) {
            mBinding.titleCordoba.setText(getString(R.string.text_colon));
        }

        mPresenter.getSchedules(mTransportType);

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override
    public void showSchedules(List<String> departures) {
        mAdapter.updateList(departures);
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
