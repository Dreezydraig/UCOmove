package com.gnzlt.ucotren.adapter;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gnzlt.ucotren.R;
import com.gnzlt.ucotren.estimation.EstimationFragment;
import com.gnzlt.ucotren.util.Constants;

public class EstimationFragmentPagerAdapter extends FragmentPagerAdapter {

    private final String mTransportType;
    private final String[] mTitles;

    public EstimationFragmentPagerAdapter(FragmentManager fragmentManager, String transportType, Resources resources) {
        super(fragmentManager);
        mTransportType = transportType;
        mTitles = mTransportType.equals(Constants.TRANSPORT_TYPE_TRAIN) ?
                resources.getStringArray(R.array.train_origins_name) :
                resources.getStringArray(R.array.bus_origins_name);
    }

    @Override
    public Fragment getItem(int position) {
        return EstimationFragment.newInstance(mTransportType, position);
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
