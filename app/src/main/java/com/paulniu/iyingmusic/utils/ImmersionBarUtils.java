package com.paulniu.iyingmusic.utils;

import android.app.Activity;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.paulniu.iyingmusic.base.BaseActivity;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 23:18
 * Desc: 沉浸式状态栏显示工具类
 * 三星手机调试状态栏时奔溃
 * Version:
 */
public class ImmersionBarUtils {

    public static void destory(BaseActivity activity) {
        if (PhoneBaseUtils.isSamsung()) {
            return;
        }
        try {
            ImmersionBar.with(activity).destroy();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity
     * @param colorId
     */
    public static void setStatusBarForHrHome(Activity activity, int colorId) {
        if (PhoneBaseUtils.isSamsung()) {
            return;
        }
        try {
            ImmersionBar.with(activity)
                    .statusBarDarkFont(true, 0.2f)
                    .statusBarColor(colorId)
                    .init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param activity
     */
    public static void initStatusBar(Activity activity) {
        if (PhoneBaseUtils.isSamsung()) {
            return;
        }
        try {
            ImmersionBar.with(activity)
                    .statusBarDarkFont(true, 0.2f)
                    .init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置状态栏透明
     * TODO 待优化
     *
     * @param activity
     * @param view
     */
    public static void setTransStatusBar(Activity activity, View view) {
        if (PhoneBaseUtils.isSamsung()) {
            return;
        }
        try {
            ImmersionBar.with(activity)
                    .titleBar(view)
                    .init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
