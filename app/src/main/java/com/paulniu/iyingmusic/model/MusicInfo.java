package com.paulniu.iyingmusic.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 16:26
 * Desc: 本地存储歌曲
 * Version:
 */
@Entity(indices = {@Index(value = {"_id", "songId", "albumId"})})
public class MusicInfo implements Parcelable {

    public static final String KEY_MUSIC = "music";

    public static final String KEY_ID = "_id";
    public static final String KEY_SONG_ID = "songid";
    public static final String KEY_ALBUM_ID = "albumid";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_MUSIC_NAME = "musicname";
    public static final String KEY_ARTIST = "artist";
    public static final String KEY_DATA = "data";
    public static final String KEY_SIZE = "size";
    public static final String KEY_FOLDER = "folder";
    public static final String KEY_MUSIC_NAME_KEY = "musicnamekey";
    public static final String KEY_ARTIST_KEY = "artistkey";
    public static final String KEY_FAVORITE = "favorite";

    /**
     * 数据库存储id
     */
    @PrimaryKey(autoGenerate = true)
    public int _id;
    /**
     * 数据库存储歌曲id
     */
    @ColumnInfo(name = "songId")
    public int songId;
    /**
     * 数据库存储歌曲所在的文件夹id
     */
    @ColumnInfo(name = "albumId")
    public int albumId;
    /**
     * 数据库存储歌曲时间
     */
    @ColumnInfo(name = "duration")
    public int duration;
    /**
     * 数据库存储歌曲名称
     */
    @ColumnInfo(name = "musicName")
    public String musicName;
    /**
     * 数据库存储歌曲艺术家
     */
    @ColumnInfo(name = "artist")
    public String artist;
    /**
     * 数据库存储歌曲路径
     */
    @ColumnInfo(name = "data")
    public String data;
    /**
     * 数据库歌曲的大小
     */
    @ColumnInfo(name="size")
    public int size;
    /**
     * 数据库存储歌曲文件夹名称
     */
    @ColumnInfo(name = "folder")
    public String folder;
    /**
     * 数据库存储歌曲的名字关键字
     */
    @ColumnInfo(name = "musicNameKey")
    public String musicNameKey;
    /**
     * 数据库存储歌曲艺术家关键字
     */
    @ColumnInfo(name = "artistKey")
    public String artistKey;
    /**
     * 0表示没有收藏 1表示收藏
     */
    @ColumnInfo(name = "favorite")
    public int favorite = 0;

    /**
     * 扫描本地音乐，当前音乐是否本选中
     */
    @Ignore
    public boolean isChecked = false;


    public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {
        @Override
        public MusicInfo createFromParcel(Parcel source) {
            MusicInfo music = new MusicInfo();
            Bundle bundle = new Bundle();
            bundle = source.readBundle();
            music._id = bundle.getInt(KEY_ID);
            music.songId = bundle.getInt(KEY_SONG_ID);
            music.albumId = bundle.getInt(KEY_ALBUM_ID);
            music.duration = bundle.getInt(KEY_DURATION);
            music.musicName = bundle.getString(KEY_MUSIC_NAME);
            music.artist = bundle.getString(KEY_ARTIST);
            music.data = bundle.getString(KEY_DATA);
            music.size = bundle.getInt(KEY_SIZE);
            music.folder = bundle.getString(KEY_FOLDER);
            music.musicNameKey = bundle.getString(KEY_MUSIC_NAME_KEY);
            music.favorite = bundle.getInt(KEY_FAVORITE);
            return music;
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ID, _id);
        bundle.putInt(KEY_SONG_ID, songId);
        bundle.putInt(KEY_ALBUM_ID, albumId);
        bundle.putInt(KEY_DURATION, duration);
        bundle.putString(KEY_MUSIC_NAME, musicName);
        bundle.putString(KEY_ARTIST, artist);
        bundle.putString(KEY_DATA, data);
        bundle.putInt(KEY_SIZE,size);
        bundle.putString(KEY_FOLDER, folder);
        bundle.putString(KEY_MUSIC_NAME_KEY, musicNameKey);
        bundle.putInt(KEY_FAVORITE, favorite);
        parcel.writeBundle(bundle);
    }
}
