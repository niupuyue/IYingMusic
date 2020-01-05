package com.paulniu.iyingmusic.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 22:59
 * Desc: Activity基类
 * Version:
 */
public abstract class BaseActivity extends FragmentActivity {

    public abstract int initViewLayout();

    public void initExtraData() {
    }

    public abstract void initViewById();

    public abstract void initData();

    public void initListener() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initViewLayout());
        initExtraData();
        initViewById();
        initListener();
        initData();
    }
}
