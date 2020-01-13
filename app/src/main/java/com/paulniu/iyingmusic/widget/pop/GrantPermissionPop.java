package com.paulniu.iyingmusic.widget.pop;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.utils.StringUtils;

import java.lang.reflect.Field;

/**
 * Coder: niupuyue
 * Date: 2020/1/13
 * Time: 10:36
 * Desc: 首次登录请求权限弹窗
 * Version:
 */
public class GrantPermissionPop extends PopupWindow implements View.OnClickListener {

    public interface OnGrantPermissionListener {
        void onConfirm();

        void onRefuse();
    }

    private OnGrantPermissionListener listener;
    private Activity mActivity;
    private View mRoot;

    private TextView tvGrantPermissionPopContent;
    private TextView tvGrantPermissionPopRefuse;
    private TextView tvGrantPermissionPopConfirm;

    public GrantPermissionPop(Activity activity, OnGrantPermissionListener listener) {
        if (null == activity || null == listener) {
            return;
        }
        this.mActivity = activity;
        this.listener = listener;
        initContentView();
        initSetup();
    }

    private void initContentView() {
        if (null == mActivity) {
            return;
        }
        mRoot = LayoutInflater.from(mActivity).inflate(R.layout.view_pop_grant_permission, null);
        tvGrantPermissionPopContent = mRoot.findViewById(R.id.tvGrantPermissionPopContent);
        tvGrantPermissionPopRefuse = mRoot.findViewById(R.id.tvGrantPermissionPopRefuse);
        tvGrantPermissionPopConfirm = mRoot.findViewById(R.id.tvGrantPermissionPopConfirm);

        tvGrantPermissionPopRefuse.setOnClickListener(this);
        tvGrantPermissionPopConfirm.setOnClickListener(this);

        // 设置显示热词
        String[] hotWords = {mActivity.getString(R.string.GrantPermissionPop_storage), mActivity.getString(R.string.GrantPermissionPop_net), mActivity.getString(R.string.GrantPermissionPop_location)};
        StringUtils.setHotWordsText(tvGrantPermissionPopContent, mActivity.getString(R.string.GrantPermissionPop_content), hotWords, R.color.colorPrimary);
    }

    private void initSetup() {
        setFocusable(true);
        setOutsideTouchable(true);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setAnimationStyle(R.style.FadeAnimationShort);
        setContentView(mRoot);
        fitPopupWindowOverStatusBar(true);
        update();
    }

    /**
     * 设置popupWindow全屏展示(但是不包括虚拟键盘)
     */
    public void fitPopupWindowOverStatusBar(boolean needFullScreen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Field mLayoutInScreen = PopupWindow.class.getDeclaredField("mLayoutInScreen");
                mLayoutInScreen.setAccessible(true);
                mLayoutInScreen.set(this, needFullScreen);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (null != v) {
            switch (v.getId()) {
                case R.id.tvGrantPermissionPopConfirm:
                    dismiss();
                    if (null != listener) {
                        listener.onConfirm();
                    }
                    break;
                case R.id.tvGrantPermissionPopRefuse:
                    dismiss();
                    if (null != listener) {
                        listener.onRefuse();
                    }
                    break;
            }
        }
    }
}
