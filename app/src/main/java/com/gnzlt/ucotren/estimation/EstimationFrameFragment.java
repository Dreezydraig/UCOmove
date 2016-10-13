package com.gnzlt.ucotren.estimation;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnzlt.ucotren.R;
import com.gnzlt.ucotren.adapter.EstimationFragmentPagerAdapter;
import com.gnzlt.ucotren.databinding.FragmentEstimationFrameBinding;

public class EstimationFrameFragment extends Fragment {

    private static final String ARG_TRANSPORT_TYPE = "ARG_TRANSPORT_TYPE";

    private String mTransportType;

    private FragmentEstimationFrameBinding mBinding;
    private AppBarLayout mAppBarLayout;
    private TabLayout mTabLayout;

    public static EstimationFrameFragment newInstance(String transportType) {
        EstimationFrameFragment fragment = new EstimationFrameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRANSPORT_TYPE, transportType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTransportType = getArguments().getString(ARG_TRANSPORT_TYPE, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_estimation_frame,
                container,
                false);

        mTabLayout = (TabLayout) inflater.inflate(
                R.layout.partial_tab_layout,
                container,
                false);
        mAppBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        if (mAppBarLayout.findViewById(R.id.tablayout) == null) {
            mAppBarLayout.addView(mTabLayout,
                    new LinearLayoutCompat.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        mBinding.viewpager.setAdapter(
                new EstimationFragmentPagerAdapter(
                        getChildFragmentManager(),
                        mTransportType,
                        getResources()));
        mTabLayout.setupWithViewPager(mBinding.viewpager);

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        mAppBarLayout.removeView(mTabLayout);
        super.onDestroyView();
    }
}
