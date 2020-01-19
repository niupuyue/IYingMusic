package com.paulniu.iyingmusic.utils;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.gson.GsonBuilder;
import com.paulniu.iyingmusic.App;
import com.paulniu.iyingmusic.Constant;
import com.paulniu.iyingmusic.model.PlayListModel;

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

    private static final String KEY_INT_PLAYMODE = "playmodel";

    private static final String KEY_OBJECT_PLAYLIST = "playList";

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

    /**
     * 获取播放模式
     */
    public static int getPlayMode() {
        return getSharedPreferences().getInt(KEY_INT_PLAYMODE, 0);
    }

    /**
     * 设置播放模式
     */
    public static void setPlayMode(int mode) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(KEY_INT_PLAYMODE, mode);
        editor.apply();
    }

    /**
     * 获取播放列表
     */
    public static PlayListModel getPlayList() {
        String value = getSharedPreferences().getString(KEY_OBJECT_PLAYLIST, null);
        if (null != value) {
            PlayListModel playListModel = new GsonBuilder().create().fromJson(value, PlayListModel.class);
            return playListModel;
        } else {
            return null;
        }
    }

    /**
     * 设置播放列表
     */
    public static void setPlayList(PlayListModel playList) {
        if (null == playList) {
            return;
        }
        String value = new GsonBuilder().create().toJson(playList);
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(KEY_OBJECT_PLAYLIST, value);
        editor.apply();
    }

}
