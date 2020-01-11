package com.paulniu.iyingmusic.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.paulniu.iyingmusic.model.MusicInfo;

import java.util.List;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 22:26
 * Desc: 音乐存储数据库操作类
 * Version:
 */
@Dao
public abstract class MusicInfoDao {

    @Update
    public abstract void update(MusicInfo... musicInfos);

    @Insert
    public abstract void insert(MusicInfo... musicInfos);

    @Delete
    public abstract void delete(MusicInfo... musicInfos);

    /**
     * 找到所有的本地存储的音乐
     */
    @Query("select * from MusicInfo")
    public abstract List<MusicInfo> getAllMusic();

    /**
     * 根据id找到本地音乐
     */
    @Query("select * from MusicInfo where songId = :id")
    public abstract MusicInfo getMusicById(int id);

    /**
     * 根据分类查找本地音乐
     */
    @Query("select * from MusicInfo where albumId = :albumId")
    public abstract List<MusicInfo> getMusicByAlbumId(int albumId);

    /**
     * 根据文件夹查找本地音乐
     */
    @Query("select * from MusicInfo where folder = :folderId")
    public abstract List<MusicInfo> getMusicByFolder(int folderId);

    /**
     * 根据音乐关键字查找本地音乐
     */
    @Query("select * from MusicInfo where musicNameKey like :musicNameKey")
    public abstract List<MusicInfo> getMusicByMusicNameKey(String musicNameKey);

    /**
     * 更新/插入对象
     */
    public void updateOrInsert(MusicInfo musicInfo) {
        if (musicInfo == null) {
            return;
        }
        MusicInfo oldMusicInfo = getMusicById(musicInfo._id);
        if (null == oldMusicInfo || oldMusicInfo._id <= 0) {
            insert(musicInfo);
        } else {
            musicInfo._id = oldMusicInfo._id;
            update(musicInfo);
        }
    }
}
