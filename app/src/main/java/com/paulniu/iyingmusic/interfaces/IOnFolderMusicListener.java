package com.paulniu.iyingmusic.interfaces;

import com.paulniu.iyingmusic.db.entity.SongInfo;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-14
 * Time: 20:53
 * Desc: 文件夹中音乐列表
 * Version:
 */
public interface IOnFolderMusicListener {

    void onFavorite(SongInfo musicInfo, boolean isFavorite);

    void onMusicItemClick(SongInfo musicInfo);

}
