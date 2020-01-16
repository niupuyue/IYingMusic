package com.paulniu.iyingmusic.service;

import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-16
 * Time: 23:23
 * Desc: 歌曲播放服务
 * Version:
 */
public class SongPlayKit extends BaseService {

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
