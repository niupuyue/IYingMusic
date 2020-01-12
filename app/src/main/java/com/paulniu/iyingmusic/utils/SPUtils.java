package com.paulniu.iyingmusic.utils;

import android.app.Activity;
import android.content.SharedPreferences;

import com.paulniu.iyingmusic.App;
import com.paulniu.iyingmusic.Constant;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 21:31
 * Desc: SharedPerference
 * Version:
 */
public class SPUtils {

    private static final String KEY_BOOLEAN_ISFIRSTOPEN = "isFirstOpen";

    private static final String KEY_BOOLEAN_ISSHAKE = "isShake";

    private static final String KEY_INT_MUSICSIZE = "musicSize";

    private static SharedPreferences sharedPreferences = null;

    private static SharedPreferences getSharedPreferences() {
        if (null == sharedPreferences && null != App.getContext()) {
            sharedPreferences = App.getContext().getSharedPreferences(Constant.SP_NAME, Activity.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    /**
     * 设置是否是第一次打开应用
     */
    public static void setIsFirstOpen(boolean isFirstOpen) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(KEY_BOOLEAN_ISFIRSTOPEN, isFirstOpen);
        editor.apply();
    }

    /**
     * 获取是否是第一次打开应用
     */
    public static boolean getIsFirstOpen() {
        return getSharedPreferences().getBoolean(KEY_BOOLEAN_ISFIRSTOPEN, true);
    }

    /**
     * 设置是否甩动
     *
     * @param isShake
     */
    public static void setShake(boolean isShake) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(KEY_BOOLEAN_ISSHAKE, isShake);
        editor.apply();
    }

    /**
     * 获取是否甩动
     *
     * @return
     */
    public static boolean getShake() {
        return getSharedPreferences().getBoolean(KEY_BOOLEAN_ISSHAKE, true);
    }

    /**
     * 设置获取音乐的最小限制
     */
    public static void setMusicSizeLimit(int size) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(KEY_INT_MUSICSIZE, size);
        editor.apply();
    }

    /**
     * 获取音乐的最小限制
     * 默认最小值是2M
     */
    public static int getMusicSizeLimit() {
        return getSharedPreferences().getInt(KEY_INT_MUSICSIZE, 2);
    }

}
