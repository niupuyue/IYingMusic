package com.paulniu.iyingmusic.interfaces;

import com.paulniu.iyingmusic.model.MusicInfo;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-14
 * Time: 20:53
 * Desc: 文件夹中音乐列表
 * Version:
 */
public interface IOnFolderMusicListener {

    void onFavorite(MusicInfo musicInfo, boolean isFavorite);

    void onMusicItemClick(MusicInfo musicInfo);

}
