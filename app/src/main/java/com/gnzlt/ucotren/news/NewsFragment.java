package com.gnzlt.ucotren.news;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gnzlt.ucotren.R;
import com.gnzlt.ucotren.adapter.NewsAdapter;
import com.gnzlt.ucotren.databinding.FragmentNewsBinding;
import com.gnzlt.ucotren.model.New;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment implements NewsContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    private NewsPresenter mPresenter;
    private FragmentNewsBinding mBinding;

    private NewsAdapter mAdapter;

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPresenter = new NewsPresenter(this);

        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_news,
                container,
                false);

        mAdapter = new NewsAdapter(new ArrayList<New>(0));
        mBinding.list.setHasFixedSize(true);
        mBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.list.setAdapter(mAdapter);
        mBinding.swipe.setOnRefreshListener(this);
        mBinding.empty.message.setText(getString(R.string.error_no_news));

        mPresenter.getNews();

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        mPresenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        mPresenter.getNews();
    }

    @Override
    public void showNews(List<New> news) {
        if (isAdded()) {
            mAdapter.updateList(news);
        }
    }

    @Override
    public void setProgressIndicator(boolean active) {
        mBinding.swipe.setRefreshing(active);

        if (!active && (mBinding.progress.progressView.getVisibility() != View.GONE)) {
            mBinding.progress.progressView.setVisibility(View.GONE);
        }
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
