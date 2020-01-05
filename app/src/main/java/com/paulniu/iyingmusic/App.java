package com.paulniu.iyingmusic;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.paulniu.iyingmusic.service.ServiceManager;

import java.io.File;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 16:10
 * Desc:
 * Version:
 */
public class App extends Application {

    public static ServiceManager mServiceManager = null;
    private static String rootPath = "/iyingmusic";
    public static String lrcPath = "/lrc";

    private static App mApp;
    private static Context mContext;

    public static Context getContext() {
        return getContext();
    }

    public static App getApp() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        mServiceManager = new ServiceManager(this);
        initPath();
    }

    /**
     * 初始化本地音乐下载路径
     */
    private void initPath() {
        String ROOT = "";
        if (TextUtils.equals(Environment.getExternalStorageState(), Environment.MEDIA_MOUNTED)) {
            ROOT = Environment.getExternalStorageDirectory().getParent();
        }
        rootPath = ROOT + rootPath;
        lrcPath = rootPath + lrcPath;
        // 新建文件用来存放歌词
        File lrcFile = new File(lrcPath);
        if (!lrcFile.exists()) {
            lrcFile.mkdirs();
        }
    }
}
