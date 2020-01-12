package com.paulniu.iyingmusic.db.source;

import android.content.Intent;

import com.paulniu.iyingmusic.App;
import com.paulniu.iyingmusic.db.AppDataBase;
import com.paulniu.iyingmusic.model.MusicInfo;

import java.util.List;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 22:42
 * Desc: 语音数据库操作对外结构
 * Version:
 */
public class MusicInfoSource {

    /**
     * 获取所有本地音乐列表
     *
     * @return
     */
    public static List<MusicInfo> getMusicInfos() {
        return AppDataBase.getInstance(App.getContext()).getMusicInfoDao().getAllMusic();
    }

    /**
     * 根据本地音乐id获取音乐对象
     */
    public static MusicInfo getMusicInfo(int id) {
        return AppDataBase.getInstance(App.getContext()).getMusicInfoDao().getMusicById(id);
    }

    /**
     * 更新数据库音乐对象
     */
    public static void updateMusicInfos(List<MusicInfo> musicInfos) {
        if (null == musicInfos || musicInfos.size() <= 0) {
            return;
        }
        for (MusicInfo musicInfo : musicInfos) {
            AppDataBase.getInstance(App.getContext()).getMusicInfoDao().update(musicInfo);
        }
    }

    /**
     * 批量更新数据库音乐对象的收藏文件夹
     */
    public static void updateMusicInfoFolderId(List<MusicInfo> musicInfos, int folderId) {
        if (null == musicInfos || musicInfos.size() <= 0 || folderId <= 0) {
            return;
        }
        for (MusicInfo musicInfo : musicInfos) {
            AppDataBase.getInstance(App.getContext()).getMusicInfoDao().updateMusicInfoToFolderid(musicInfo, folderId);
        }
    }

}
