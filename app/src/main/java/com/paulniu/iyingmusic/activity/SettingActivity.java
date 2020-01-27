package com.paulniu.iyingmusic.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.base.BaseActivity;
import com.paulniu.iyingmusic.widget.MyAppTitle;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 23:06
 * Desc: 设置页面
 * Version:
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        return intent;
    }

    private MyAppTitle myAppTitle;

    @Override
    public int initViewLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void initViewById() {
        myAppTitle = findViewById(R.id.myAppTitle);
    }

    @Override
    public void initData() {
        if (null != myAppTitle) {
            myAppTitle.initViewsVisible(true, true, false, false);
            myAppTitle.setAppTitle(R.string.SettingActivity_title);
            myAppTitle.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
                @Override
                public void onLeftButtonClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {

    }
}
