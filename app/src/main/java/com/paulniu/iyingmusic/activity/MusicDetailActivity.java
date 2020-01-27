package com.paulniu.iyingmusic.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.base.BaseActivity;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 23:11
 * Desc: 音乐播放页面展示类(页面详情类)
 * Version:
 */
public class MusicDetailActivity extends BaseActivity implements View.OnClickListener {

    public static Intent getInstance(Context context) {
        Intent intent = new Intent(context, MusicDetailActivity.class);

        return intent;
    }

    @Override
    public int initViewLayout() {
        // 设置页面展示动画
        overridePendingTransition(R.anim.view_enter, R.anim.view_exit);
        return R.layout.activity_music_detail;
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
