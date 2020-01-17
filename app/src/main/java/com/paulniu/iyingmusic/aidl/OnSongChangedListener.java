package com.paulniu.iyingmusic.aidl;

import android.os.IBinder;
import android.os.RemoteException;

/**
 * Coder: niupuyue
 * Date: 2020/1/17
 * Time: 10:08
 * Desc: 用户主动切换歌曲时回调
 *      1.播放相同播放列表中指定曲目
 *      2.切换到 前一首
 *      3.切换到 后一首
 *      4.切换播放列表
 * Version:
 */
public abstract class OnSongChangedListener extends IOnSongChangedListener.Stub {

    @Override
    public IBinder asBinder() {
        return super.asBinder();
    }

    /**
     * 在服务端线程的binder 线程池中运行，客户端调用时不能操作UI控件
     * @param which
     * @param index
     * @param isNext
     */
    @Override
    public abstract void onSongChange(Song which, int index, boolean isNext);
}
