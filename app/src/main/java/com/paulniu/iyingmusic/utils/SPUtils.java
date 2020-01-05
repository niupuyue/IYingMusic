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

    private static final String KEY_ISSHAKE = "isShake";

    private static SharedPreferences sharedPreferences = null;

    private static SharedPreferences getSharedPreferences() {
        if (null == sharedPreferences && null != App.getContext()) {
            sharedPreferences = App.getContext().getSharedPreferences(Constant.SP_NAME, Activity.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    /**
     * 设置是否甩动
     *
     * @param isShake
     */
    public static void setShake(boolean isShake) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(KEY_ISSHAKE, isShake);
        editor.apply();
    }

    /**
     * 获取是否甩动
     *
     * @return
     */
    public static boolean getShake() {
        return getSharedPreferences().getBoolean(KEY_ISSHAKE, true);
    }

}
