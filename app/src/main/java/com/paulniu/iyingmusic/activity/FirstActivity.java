package com.paulniu.iyingmusic.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.base.BaseActivity;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 23:06
 * Desc: 引导页面 倒计时三秒钟跳出该页面
 * Version:
 */
public class FirstActivity extends BaseActivity implements View.OnClickListener {

    private static final int TYPE_COUNT = 1;

    private int delay_time = 3;

    private static class FirstHandler extends Handler {
        WeakReference<FirstActivity> mWeakRef;

        FirstHandler(FirstActivity mActivity) {
            mWeakRef = new WeakReference<>(mActivity);
        }

        @Override
        public void handleMessage(@NonNull Message message) {
            super.handleMessage(message);
            FirstActivity activity = mWeakRef.get();
            switch (message.what) {
                case FirstActivity.TYPE_COUNT:
                    int count = (int) message.obj;
                    if (count >= 0) {
                        activity.setTimeCountText(count + "s");
                    } else {
                        // 跳转页面
                        activity.jumpToMainActivity();
                    }
                    break;
            }
        }
    }

    // 页面控件
    private TextView tvFirstActivityCount;
    private FrameLayout flFirstActivityContainer;
    private Timer timer;
    private FirstHandler handler = new FirstHandler(this);

    @Override
    public int initViewLayout() {
        return R.layout.activity_first;
    }

    @Override
    public void initViewById() {
        tvFirstActivityCount = findViewById(R.id.tvFirstActivityCount);
        flFirstActivityContainer = findViewById(R.id.flFirstActivityContainer);
    }

    @Override
    public void initData() {
        try {
            // 通过声明一个Timer对象实现
            timer = new Timer();
            final TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (delay_time >= 0) {
                        delay_time--;
                        Message message = Message.obtain();
                        message.what = TYPE_COUNT;
                        message.obj = delay_time;
                        handler.sendMessage(message);
                    }
                }
            };
            if (task != null && timer != null) {
                if (null != tvFirstActivityCount) {
                    tvFirstActivityCount.setVisibility(View.VISIBLE);
                    timer.schedule(task, 0, 1000);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initListener() {
        if (null != tvFirstActivityCount) {
            tvFirstActivityCount.setOnClickListener(this);
        }
    }

    /**
     * 设置倒计时时间
     *
     * @param value 需要设置的文本信息
     */
    private void setTimeCountText(String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        if (null == tvFirstActivityCount) {
            return;
        }
        try {
            tvFirstActivityCount.setText(value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jumpToMainActivity() {
        // 如果倒计时未结束，则直接发送消息，停止倒计时
        if (handler != null) {
            handler.removeCallbacks(null);
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvFirstActivityCount) {
            // 执行跳过欢迎页面
            jumpToMainActivity();
        }
    }
}
