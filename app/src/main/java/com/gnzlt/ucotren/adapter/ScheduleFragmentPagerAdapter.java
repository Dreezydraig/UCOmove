package com.gnzlt.ucotren.adapter;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gnzlt.ucotren.R;
import com.gnzlt.ucotren.schedule.ScheduleFragment;

public class ScheduleFragmentPagerAdapter extends FragmentPagerAdapter {

    private final String[] mTitles;

    public ScheduleFragmentPagerAdapter(FragmentManager fragmentManager, Resources resources) {
        super(fragmentManager);
        mTitles = resources.getStringArray(R.array.transports_name);
    }

    @Override
    public Fragment getItem(int position) {
        return ScheduleFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
