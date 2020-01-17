package com.paulniu.iyingmusic.interfaces;

import com.paulniu.iyingmusic.aidl.IPlayControl;
import com.paulniu.iyingmusic.aidl.Song;

public interface PlayServiceCallback {

    /**
     * 当前歌曲改变时回调
     * @see com.paulniu.iyingmusic.aidl.OnSongChangedListener#onSongChange(Song, int, boolean)
     */
    void songChanged(Song song, int index, boolean isNext);

    /**
     * 此方法由服务端控制调用
     * @see com.paulniu.iyingmusic.aidl.OnPlayStatusChangedListener#playStart(Song, int, int)
     */
    void startPlay(Song song, int index, int status);

    /**
     * 此方法由服务端控制调用
     * @see com.paulniu.iyingmusic.aidl.OnPlayStatusChangedListener#playStop(Song, int, int)
     */
    void stopPlay(Song song, int index, int status);


    /**
     * 此方法由服务端控制调用
     *
     * @see com.paulniu.iyingmusic.aidl.OnPlayListChangedListener#onPlayListChange(Song, int, int)
     */
    void onPlayListChange(Song current, int index, int id);

    /**
     * 服务端数据初始化完成时回调，客户端与服务器的交互应在此调用到达时才开始
     *
     * @param mControl
     */
    void dataIsReady(IPlayControl mControl);

}