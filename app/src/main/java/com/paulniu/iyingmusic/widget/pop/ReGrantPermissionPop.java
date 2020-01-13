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

import java.lang.reflect.Field;

/**
 * Coder: niupuyue
 * Date: 2020/1/13
 * Time: 11:45
 * Desc: 取消获取权限后请求再次设置的弹窗
 * Version:
 */
public class ReGrantPermissionPop extends PopupWindow implements View.OnClickListener {

    public interface OnReGrantPermissionListener{
        void onCancel();
        void onSetting();
    }

    private Activity mActivity;
    private OnReGrantPermissionListener listener;
    private View mRoot;

    private TextView tvReGrantPermissionPopContent;
    private TextView tvReGrantPermissionPopCancel;
    private TextView tvReGrantPermissionPopSetting;

    public ReGrantPermissionPop(Activity activity,OnReGrantPermissionListener listener){
        if (null == activity || null == listener){
            return;
        }
        this.mActivity = activity;
        this.listener = listener;
        initContentView();
        initSetup();
    }

    private void initContentView(){
        if (null == mActivity){
            return;
        }
        mRoot = LayoutInflater.from(mActivity).inflate(R.layout.view_pop_regrant_permission,null);
        tvReGrantPermissionPopCancel = mRoot.findViewById(R.id.tvReGrantPermissionPopCancel);
        tvReGrantPermissionPopSetting = mRoot.findViewById(R.id.tvReGrantPermissionPopSetting);

        tvReGrantPermissionPopCancel.setOnClickListener(this);
        tvReGrantPermissionPopSetting.setOnClickListener(this);
    }

    private void initSetup(){
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
        if (null != v){
            switch (v.getId()){
                case R.id.tvReGrantPermissionPopCancel:
                    dismiss();
                    if (null != listener){
                        listener.onCancel();
                    }
                    break;
                case R.id.tvReGrantPermissionPopSetting:
                    dismiss();
                    if (null != listener){
                        listener.onSetting();
                    }
                    break;
            }
        }
    }
}
