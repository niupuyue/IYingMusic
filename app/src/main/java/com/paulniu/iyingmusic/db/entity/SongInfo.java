package com.paulniu.iyingmusic.db.entity;

import android.provider.MediaStore;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Coder: niupuyue
 * Date: 2020/1/16
 * Time: 19:55
 * Desc: 歌曲实体类
 * Version:
 */
@Entity(indices = {@Index(value = {"id"})})
public class SongInfo implements MediaStore.Audio.AudioColumns {

    /**
     * 数据库唯一标志
     */
    @PrimaryKey(autoGenerate = true)
    public int id;

    /**
     * 歌曲名称
     */
    @ColumnInfo(name = "songName")
    public String songName;

    /**
     * 时间 ms
     */
    @ColumnInfo(name = "duration")
    public int duration;

    /**
     * 艺术家
     */
    @ColumnInfo(name = "artist")
    public String artist;

    /**
     * 专辑
     */
    @ColumnInfo(name = "album")
    public String album;

    /**
     * 专辑id
     */
    @ColumnInfo(name = "album_id")
    public String album_id;

    /**
     * 专辑图片路径
     */
    @ColumnInfo(name = "album_path")
    public String album_path;

    /**
     * 专辑录制时间
     */

}
