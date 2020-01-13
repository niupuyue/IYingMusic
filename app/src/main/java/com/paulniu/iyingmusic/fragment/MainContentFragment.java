package com.paulniu.iyingmusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.base.BaseFragment;

/**
 * Coder: niupuyue
 * Date: 2020/1/13
 * Time: 20:48
 * Desc: 首页主页面布局
 * Version:
 */
public class MainContentFragment extends BaseFragment {

    private View root;

    private boolean mIsVisibleToUser = false;
    private boolean mIsInitData = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == root) {
            root = View.inflate(getActivity(), R.layout.fragment_main_content, null);
            initExtraData();
            initLayout();
        }
        if (mIsVisibleToUser && !mIsInitData) {
            mIsInitData = true;
            initData();
        }
        return root;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        mIsVisibleToUser = isVisibleToUser;
        if (null != root && isVisibleToUser && !mIsInitData) {
            mIsInitData = true;
            initData();
        }
    }

    private void initExtraData() {

    }

    private void initLayout() {

    }

    private void initData() {

    }

}
