package com.paulniu.iyingmusic.db.source;

import com.paulniu.iyingmusic.App;
import com.paulniu.iyingmusic.db.AppDataBase;
import com.paulniu.iyingmusic.db.entity.FolderInfoWithMusicCount;
import com.paulniu.iyingmusic.db.entity.SongInfo;

import java.util.List;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-16
 * Time: 22:19
 * Desc: 本地歌曲操作对外工具类
 * Version:
 */
public class SongInfoSource {

    /**
     * 获取本地所有音乐列表
     */
    public static List<SongInfo> getSongInfos() {
        return AppDataBase.getInstance(App.getContext()).getSongInfoDao().getAllSongInfos();
    }

    /**
     * 根据文件夹id获取所有音乐
     */
    public static List<SongInfo> getSongInfosByFolderId(int folderId) {
        if (folderId <= 0) {
            return null;
        }
        return AppDataBase.getInstance(App.getContext()).getSongInfoDao().getSongInfosByFolderId(folderId);
    }

    /**
     * 更新数据库音乐对象
     */
    public static void updateSongInfos(List<SongInfo> songInfos) {
        if (null == songInfos || songInfos.size() <= 0) {
            return;
        }
        for (SongInfo songInfo : songInfos) {
            AppDataBase.getInstance(App.getContext()).getSongInfoDao().update(songInfo);
        }
    }

    /**
     * 批量更新数据库中音乐对象的(收藏)文件夹
     */
    public static void updateSongInfoFolderId(List<SongInfo> songInfos, int folderId, boolean isFavorite) {
        if (null == songInfos || songInfos.size() <= 0 || folderId <= 0) {
            return;
        }
        for (SongInfo songInfo : songInfos) {
            AppDataBase.getInstance(App.getContext()).getSongInfoDao().updateSOngInfoToFolderId(songInfo, folderId, isFavorite);
        }
    }

    /**
     * 获取每个文件夹中音乐的数量
     */
    public static List<FolderInfoWithMusicCount> getFolderSongList() {
        return AppDataBase.getInstance(App.getContext()).getSongInfoDao().getFolderSongCount();
    }


    /**
     * 修改音乐是否是我的最爱
     */
    public static void updateSongInfoToFavorite(SongInfo songInfo, boolean isFavorite) {
        if (null != songInfo && songInfo.id > 0) {
            AppDataBase.getInstance(App.getContext()).getSongInfoDao().updateSongInfoToFavorite(songInfo, isFavorite);
        }
    }
}
