package com.paulniu.iyingmusic.widget.pop;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.view.View;
import android.widget.TextView;

import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.utils.StringUtils;

import java.lang.reflect.Field;

/**
 * Coder: niupuyue
 * Date: 2020/1/13
 * Time: 14:55
 * Desc: 没有选择文件夹，默认放在我的最爱中(只需要弹出来一次)
 * Version:
 */
public class MusicScanSelectEmptyFolderPop extends PopupWindow implements View.OnClickListener {

    public interface OnMusicScanSelectEmptyFolderListener {
        void onConfirm();

        void onCancel();
    }

    private OnMusicScanSelectEmptyFolderListener listener;
    private Activity mActivity;
    private View mRoot;

    private TextView tvMusicScanSelectEmptyFolderContent;
    private TextView tvMusicScanSelectEmptyFolderCancel;
    private TextView tvMusicScanSelectEmptyFolderConfirm;

    public MusicScanSelectEmptyFolderPop(Activity activity, OnMusicScanSelectEmptyFolderListener listener) {
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
        mRoot = LayoutInflater.from(mActivity).inflate(R.layout.view_pop_music_scan_select_empty_folder, null);
        tvMusicScanSelectEmptyFolderContent = mRoot.findViewById(R.id.tvMusicScanSelectEmptyFolderContent);
        tvMusicScanSelectEmptyFolderCancel = mRoot.findViewById(R.id.tvMusicScanSelectEmptyFolderCancel);
        tvMusicScanSelectEmptyFolderConfirm = mRoot.findViewById(R.id.tvMusicScanSelectEmptyFolderConfirm);

        tvMusicScanSelectEmptyFolderCancel.setOnClickListener(this);
        tvMusicScanSelectEmptyFolderConfirm.setOnClickListener(this);

        String[] hotWords = {mActivity.getString(R.string.MusicScanSelectEmptyFolderPop_content_hotword)};
        StringUtils.setHotWordsText(tvMusicScanSelectEmptyFolderContent, mActivity.getString(R.string.MusicScanSelectEmptyFolderPop_content), hotWords, R.color.colorPrimary);
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
            dismiss();
            if (null == listener) {
                return;
            }
            switch (v.getId()) {
                case R.id.tvMusicScanSelectEmptyFolderCancel:
                    listener.onCancel();
                    break;
                case R.id.tvMusicScanSelectEmptyFolderConfirm:
                    listener.onConfirm();
                    break;
            }
        }
    }
}
