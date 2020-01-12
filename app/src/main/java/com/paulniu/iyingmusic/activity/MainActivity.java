package com.paulniu.iyingmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    public int initViewLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initViewById() {

    }

    @Override
    public void initData() {
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MusicScanActivity.getIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

}
