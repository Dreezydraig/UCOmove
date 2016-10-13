package com.gnzlt.ucotren.estimation;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gnzlt.ucotren.R;
import com.gnzlt.ucotren.databinding.FragmentEstimationBinding;
import com.gnzlt.ucotren.util.Constants;

import at.grabner.circleprogress.TextMode;

public class EstimationFragment extends Fragment
        implements EstimationContract.View, View.OnClickListener {

    private static final String ARG_TRANSPORT_TYPE = "ARG_TRANSPORT_TYPE";
    private static final String ARG_STATION_TYPE = "ARG_STATION_TYPE";

    private static final int ANIMATION_DURATION = 1000;

    private EstimationPresenter mPresenter;
    private FragmentEstimationBinding mBinding;
    private String mTransportType;
    private String mStationName;

    public static EstimationFragment newInstance(String transportType, int stationType) {
        EstimationFragment fragment = new EstimationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRANSPORT_TYPE, transportType);
        args.putInt(ARG_STATION_TYPE, stationType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTransportType = getArguments().getString(ARG_TRANSPORT_TYPE);
        mStationName = Constants.ORIGIN_TYPES[getArguments().getInt(ARG_STATION_TYPE)];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPresenter = new EstimationPresenter(this, mTransportType, mStationName);

        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_estimation,
                container,
                false);

        mBinding.progressCircle.setOnClickListener(this);

        mPresenter.getDepartures();

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override
    public void showTimeLeft(int minutes) {
        mBinding.progressCircle.setUnitVisible(true);
        mBinding.progressCircle.setTextMode(TextMode.VALUE);
        mBinding.progressCircle.setValueAnimated(minutes, ANIMATION_DURATION);
    }

    @Override
    public void showNoEstimations() {
        mBinding.progressCircle.setUnitVisible(false);
        mBinding.progressCircle.setShowTextWhileSpinning(true);
        mBinding.progressCircle.setTextMode(TextMode.TEXT);
        mBinding.progressCircle.setText(getString(R.string.error_no_estimations));
    }

    @Override
    public void setSpinCircle(boolean active) {
        if (active) {
            mBinding.progressCircle.setShowTextWhileSpinning(false);
            mBinding.progressCircle.spin();
        } else {
            mBinding.progressCircle.stopSpinning();
        }
    }

    @Override
    public void setOnlineIcon(boolean active) {
        if (active) {
            if (mBinding.online.getVisibility() != View.VISIBLE) {
                mBinding.online.setVisibility(View.VISIBLE);
            }
        } else {
            if (mBinding.online.getVisibility() != View.GONE) {
                mBinding.online.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void setClickableProgress(boolean active) {
        mBinding.progressCircle.setClickable(active);
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.progressCircle:
                mPresenter.getDepartures();
                break;
            default:
                break;
        }
    }
}
