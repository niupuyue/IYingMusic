package com.paulniu.iyingmusic.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Process;

import androidx.annotation.Nullable;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-16
 * Time: 23:23
 * Desc: 歌曲播放服务
 * Version:
 */
public class SongPlayService extends BaseService {

    public SongPlayServiceIBinder iBinder;

    private SongBroadcastManager broadcastManager;
    private BroadcastReceiver serviceQuitReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        broadcastManager = SongBroadcastManager.getInstance();
        iBinder = new SongPlayServiceIBinder(this);

        iBinder.notifyDataIsReady();

        initBroadcast();
    }

    private void initBroadcast() {
        serviceQuitReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (iBinder.status() == SongPlayController.STATUS_PLAYING) {
                    iBinder.pause();
                }
                iBinder.releaseMediaPlayer();
                stopSelf();
            }
        };
        broadcastManager.registerBroadReceiver(this, serviceQuitReceiver, SongBroadcastManager.FILTER_PLAY_SERVICE_QUIT);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int check = checkCallingOrSelfPermission("com.paulniu.iyingmusic.permission.ACCESS_PLAY_SERVICE");
        if (check == PackageManager.PERMISSION_DENIED) {
            return null;
        }
        return iBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        iBinder.releaseMediaPlayer();
        unRegisterReceiver();

        // 释放
        Process.killProcess(Process.myPid());
    }

    /**
     * 解绑广播
     */
    private void unRegisterReceiver() {
        if (null != serviceQuitReceiver) {
            broadcastManager.unregisterReceiver(this, serviceQuitReceiver);
        }
    }
}
