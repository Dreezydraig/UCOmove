package com.gnzlt.ucotren.about;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnzlt.ucotren.BuildConfig;
import com.gnzlt.ucotren.R;
import com.gnzlt.ucotren.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment implements AboutContract.View {

    private AboutContract.Presenter mPresenter;
    private FragmentAboutBinding mBinding;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPresenter = new AboutPresenter(this);

        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_about,
                container,
                false);

        mBinding.version.setText(
                String.format(getString(R.string.app_version),
                        getString(R.string.app_name),
                        BuildConfig.VERSION_NAME,
                        BuildConfig.VERSION_CODE));

        mBinding.twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.openTwitter();
            }
        });

        mBinding.rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.openGooglePlay();
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        mPresenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void startBrowserIntent(String url) {
        PackageManager packageManager = getActivity().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent);
        }
    }
}
