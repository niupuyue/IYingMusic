package com.paulniu.iyingmusic.activity;

import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.widget.FrameLayout;

import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.base.BaseActivity;

public class MainActivity extends BaseActivity {

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context,MainActivity.class);
        return intent;
    }

    private DrawerLayout drawer_layout;
    private FrameLayout flMainActivityMainContainer;
    private FrameLayout flMainActivitySlideContainer;

    @Override
    public int initViewLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initViewById() {
        drawer_layout = findViewById(R.id.drawer_layout);
        flMainActivityMainContainer = findViewById(R.id.flMainActivityMainContainer);
        flMainActivitySlideContainer = findViewById(R.id.flMainActivitySlideContainer);
    }

    @Override
    public void initData() {

    }

}
