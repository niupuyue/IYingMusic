package com.paulniu.iyingmusic.db.entity;

import android.provider.MediaStore;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
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
     * 歌曲存储的文件夹
     */
    @ColumnInfo(name = "folderId")
    public int folderId;

    /**
     * 是否是我的最爱
     */
    @ColumnInfo(name = "favorite")
    public boolean favorite;

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
    @ColumnInfo(name = "year")
    public long year;

    /**
     * 文件存储路径
     */
    @ColumnInfo(name = "data")
    public String data;

    /**
     * 文件大小
     */
    @ColumnInfo(name = "size")
    public long size;

    /**
     * 文件类型
     */
    @ColumnInfo(name = "mime_type")
    public String mime_type;

    /**
     * 文件被修改的时间
     */
    @ColumnInfo(name = "date_modified")
    public long date_modified;

    /**
     * 显示的名字(后面可以自定义名字)
     */
    @ColumnInfo(name = "display_name")
    public String display_name;

    /**
     * 加入数据库的时间
     */
    @ColumnInfo(name = "date_added")
    public long date_added;

    @Ignore
    public boolean isShowChecked;

    @Ignore
    public boolean isChecked;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_path() {
        return album_path;
    }

    public void setAlbum_path(String album_path) {
        this.album_path = album_path;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public long getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(long date_modified) {
        this.date_modified = date_modified;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public long getDate_added() {
        return date_added;
    }

    public void setDate_added(long date_added) {
        this.date_added = date_added;
    }

    public boolean isShowChecked() {
        return isShowChecked;
    }

    public void setShowChecked(boolean showChecked) {
        isShowChecked = showChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
