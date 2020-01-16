package com.paulniu.iyingmusic.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.paulniu.iyingmusic.db.entity.FolderInfoWithMusicCount;
import com.paulniu.iyingmusic.db.entity.SongInfo;

import java.util.List;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-16
 * Time: 22:16
 * Desc: 歌曲数据库操作类
 * Version:
 */
@Dao
public abstract class SongInfoDao {

    @Update
    public abstract void update(SongInfo songInfo);

    @Insert
    public abstract void insert(SongInfo songInfo);

    @Delete
    public abstract void delete(SongInfo songInfo);

    /**
     * 根据本地索引id找到音乐对象
     */
    @Query("select * from SongInfo where id = :id")
    public abstract SongInfo getSongInfoById(int id);

    /**
     * 找到所有本地存储的音乐
     */
    @Query("select * from SongInfo")
    public abstract List<SongInfo> getAllSongInfos();

    /**
     * 根据文件夹查找本地音乐
     */
    @Query("select * from SongInfo where folderId = :folderId")
    public abstract List<SongInfo> getSongInfosByFolderId(int folderId);

    /**
     * 查询文件中音乐的数量
     */
    @Query("select FolderInfo.*,(select COUNT(*) FROM SongInfo where SongInfo.folderId = FolderInfo.folderId) AS musicCount from FolderInfo")
    public abstract List<FolderInfoWithMusicCount> getFolderSongCount();

    /**
     * 插入/更新对象
     */
    public void updateOrInsert(SongInfo songInfo) {
        if (null == songInfo) {
            return;
        }
        SongInfo oldSong = getSongInfoById(songInfo.id);
        if (null == oldSong || oldSong.id <= 0) {
            insert(songInfo);
        } else {
            songInfo.id = oldSong.id;
            update(songInfo);
        }
    }

    /**
     * 更新数据库中音乐对象的文件夹
     */
    public void updateSOngInfoToFolderId(SongInfo songInfo, int folderId, boolean isFavorite) {
        if (null == songInfo || folderId <= 0) {
            return;
        }
        songInfo.folderId = folderId;
        songInfo.favorite = isFavorite;
        if (songInfo.id <= 0) {
            insert(songInfo);
        } else {
            update(songInfo);
        }
    }

    /**
     * 更新歌曲是否加入我的最爱
     */
    public void updateSongInfoToFavorite(SongInfo songInfo, boolean isFavorite) {
        if (null == songInfo && songInfo.id <= 0) {
            return;
        }
        SongInfo oldSongInfo = getSongInfoById(songInfo.id);
        if (null != oldSongInfo) {
            oldSongInfo.favorite = isFavorite;
        }
        update(oldSongInfo);
    }

}
