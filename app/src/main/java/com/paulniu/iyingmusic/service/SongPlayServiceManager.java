package com.paulniu.iyingmusic.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

/**
 * Coder: niupuyue
 * Date: 2020/1/17
 * Time: 11:27
 * Desc: 音乐播放服务管理类(待优化) TODO
 * Version:
 */
public class SongPlayServiceManager {

    private Context context;

    public SongPlayServiceManager(Context context) {
        this.context = context;
    }

    //启动服务，需要关闭记得一定要使用 stopService 关闭，即使没有组件绑定到服务服务也会一直运行，因为此时他是以启动的方式启动的，而不是绑定。
    public static void startPlayService(Context context) {
        Intent intent = new Intent(context, SongPlayService.class);
        context.startService(intent);
    }

    //绑定服务
    public void bindService(ServiceConnection connection) {
        Intent intent = new Intent(context, SongPlayService.class);
        context.bindService(intent, connection, Service.BIND_AUTO_CREATE);
    }

}
