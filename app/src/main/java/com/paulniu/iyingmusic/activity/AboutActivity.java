package com.paulniu.iyingmusic.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.base.BaseActivity;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 23:07
 * Desc: 关于我 页面
 * Version:
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);

        return intent;
    }

    @Override
    public int initViewLayout() {
        return R.layout.activity_about;
    }

    @Override
    public void initViewById() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        if (null == view) {
            return;
        }
        switch (view.getId()) {

        }
    }
}
