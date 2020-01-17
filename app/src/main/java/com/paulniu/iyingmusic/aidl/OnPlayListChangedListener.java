package com.paulniu.iyingmusic.aidl;

import android.os.IBinder;

/**
 * Coder: niupuyue
 * Date: 2020/1/17
 * Time: 10:07
 * Desc:
 * Version:
 */
public abstract class OnPlayListChangedListener extends IOnPlayListChangedListener.Stub {

    @Override
    public IBinder asBinder() {
        return super.asBinder();
    }

    /**
     * 服务端的播放列表改变时回调
     *
     * @param current 当前曲目
     * @param index   曲目下标
     * @param id      歌单 id
     */
    @Override
    public abstract void onPlayListChange(Song current, int index, int id);


}
