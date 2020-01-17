package com.paulniu.iyingmusic.interfaces;

import com.paulniu.iyingmusic.db.entity.SongInfo;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-18
 * Time: 04:18
 * Desc: 播放列表回调
 * Version:
 */
public interface IOnCurrSongListListener {

    void onCurrSongListClick(SongInfo songInfo);

    void onCurrSongListDelete(SongInfo songInfo);

}
