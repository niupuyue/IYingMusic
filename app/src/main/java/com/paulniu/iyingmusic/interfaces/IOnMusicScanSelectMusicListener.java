package com.paulniu.iyingmusic.interfaces;

import com.paulniu.iyingmusic.model.MusicInfo;

/**
 * Coder: niupuyue
 * Date: 2020/1/13
 * Time: 17:40
 * Desc: 扫描本地音乐--选择需要导入的音乐
 * Version:
 */
public interface IOnMusicScanSelectMusicListener {

    void onMusicScanSelectMusic(MusicInfo musicInfo);

    void onMusicScanUnselectMusic(MusicInfo musicInfo);
}
