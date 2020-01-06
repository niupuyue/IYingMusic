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

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 23:06
 * Desc: 引导页面 倒计时三秒钟跳出该页面
 * Version:
 */
public class FirstActivity extends BaseActivity implements View.OnClickListener {

    private static final long DEFAULT_HANDLER_DELAY_TIME = 1000L;

    private static final int DEFAULT_HANDLER_MESSAGE_TYPE_COUNT = 1;
    private static final int DEFAULT_HANDLER_MESSAGE_TYPE_CANCEL = 2;

    private static int delay_time = 3;

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
                case 0:
                    int count = (int) message.obj;
                    count--;
                    if (count >= 0) {
                        activity.setTimeCountText(count + "s");
                        Message m = Message.obtain();
                        m.what = 0;
                        m.obj = count;
                        activity.handler.sendMessageDelayed(m, DEFAULT_HANDLER_DELAY_TIME);
                    }
                    break;
                case 1:
                    activity.setTimeCountText(message.obj + "s");
                    break;
            }
        }
    }

    // 页面控件
    private TextView tvFirstActivityCount;
    private FrameLayout flFirstActivityContainer;

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
            // 开始倒计时
            Message message = Message.obtain();
            message.what = DEFAULT_HANDLER_MESSAGE_TYPE_COUNT;
            message.obj = delay_time;
            handler.sendMessage(message);
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
        // 如果倒计时未结束，则直接发送消息，停止倒计时 TODO

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvFirstActivityCount) {
            // 执行跳过欢迎页面
            jumpToMainActivity();
        }
    }
}
