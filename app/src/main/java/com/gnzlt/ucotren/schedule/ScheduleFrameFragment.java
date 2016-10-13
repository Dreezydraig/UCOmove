package com.gnzlt.ucotren.schedule;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnzlt.ucotren.R;
import com.gnzlt.ucotren.adapter.ScheduleFragmentPagerAdapter;
import com.gnzlt.ucotren.databinding.FragmentScheduleFrameBinding;

public class ScheduleFrameFragment extends Fragment {

    private FragmentScheduleFrameBinding mBinding;

    private AppBarLayout.LayoutParams mToolbarParams;
    private AppBarLayout mAppBarLayout;
    private TabLayout mTabLayout;

    public static ScheduleFrameFragment newInstance() {
        return new ScheduleFrameFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_schedule_frame,
                container,
                false);

        mTabLayout = (TabLayout) inflater.inflate(
                R.layout.partial_tab_layout,
                container,
                false);

        mAppBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);

        Toolbar toolbar = (Toolbar) mAppBarLayout.findViewById(R.id.toolbar);
        mToolbarParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        mToolbarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
                AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED);

        if (mAppBarLayout.findViewById(R.id.tablayout) == null) {
            AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
            mAppBarLayout.addView(mTabLayout, params);
        }

        mBinding.viewpager.setAdapter(
                new ScheduleFragmentPagerAdapter(getChildFragmentManager(), getResources()));
        mTabLayout.setupWithViewPager(mBinding.viewpager);

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        mAppBarLayout.setExpanded(true, false);
        mToolbarParams.setScrollFlags(0);
        mAppBarLayout.removeView(mTabLayout);
        super.onDestroyView();
    }
}
