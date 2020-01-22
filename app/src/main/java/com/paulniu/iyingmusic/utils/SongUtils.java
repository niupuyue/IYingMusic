package com.paulniu.iyingmusic.utils;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.paulniu.iyingmusic.App;
import com.paulniu.iyingmusic.aidl.Song;
import com.paulniu.iyingmusic.db.entity.SongInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 17:53
 * Desc: 音乐播放公共方法
 * Version:
 */
public class SongUtils {

    private static List<SongInfo> songInfos = new ArrayList<>();

    /**
     * 查找当前音乐在列表中的位置(下标)
     *
     * @param list
     * @param id
     * @return
     */
    public static int seekCurMusicPositionInList(List<SongInfo> list, int id) {
        if (id <= -1) {
            return -1;
        }
        if (null == list || list.size() <= 0) {
            return -1;
        }
        int result = -1;
        for (int i = 0; i < list.size(); i++) {
//            if (id == list.get(i).songId) {
//                result = i;
//                break;
//            }
        }
        return result;
    }

    /**
     * 根据传递过来的路径查找到对应的数据库歌曲
     */
    public static SongInfo getSongInfoBySongPath(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        SongInfo tmpSong = null;
        if (null == songInfos || songInfos.size() <= 0) {
            getLocalStorageMusics(App.getContext());
        }
        for (SongInfo songInfo : songInfos) {
            if (null != songInfo) {
                if (TextUtils.equals(songInfo.data, path)) {
                    tmpSong = songInfo;
                    break;
                }
            }
        }
        return tmpSong;
    }

    /**
     * 查询本地音乐
     */
    public static List<SongInfo> getLocalStorageMusics(Context context) {
        if (null == context) {
            return null;
        }
        List<SongInfo> songs = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, null);
        if (cursor == null) {
            return songs;
        }
        while (cursor.moveToNext()) {
            SongInfo song = new SongInfo();
            song.setAlbum_id(cursor.getString(cursor.getColumnIndex(SongInfo.ALBUM_ID)));
            song.setAlbum_path(getAlbumArtPicPath(context, song.getAlbum_id()));
            song.setArtist(cursor.getString(cursor.getColumnIndex(SongInfo.ARTIST)));
            song.setAlbum(cursor.getString(cursor.getColumnIndex(SongInfo.ALBUM)));
            song.setData(cursor.getString(cursor.getColumnIndex(SongInfo.DATA)));
            song.setDisplay_name(cursor.getString(cursor.getColumnIndex(SongInfo.DISPLAY_NAME)));
            song.setMime_type(cursor.getString(cursor.getColumnIndex(SongInfo.MIME_TYPE)));
            song.setYear(cursor.getLong(cursor.getColumnIndex(SongInfo.YEAR)));
            song.setDuration(cursor.getInt(cursor.getColumnIndex(SongInfo.DURATION)));
            song.setSize(cursor.getLong(cursor.getColumnIndex(SongInfo.SIZE)));
            song.setDate_added(cursor.getLong(cursor.getColumnIndex(SongInfo.DATE_ADDED)));
            song.setDate_modified(cursor.getLong(cursor.getColumnIndex(SongInfo.DATE_MODIFIED)));
            // 只有音乐大于2M/自定义大小时才会加入到列表中
            if ((song.size >> 10 >> 10) >= SPUtils.getMusicSizeLimit()) {
                songs.add(song);
            }
        }
        cursor.close();
        songInfos = songs;
        return songs;
    }

    //根据专辑 id 获得专辑图片保存路径
    private static synchronized String getAlbumArtPicPath(Context context, String albumId) {

        // 小米应用商店检测crash ，错误信息：[31188,0,com.duan.musicoco,13155908,java.lang.IllegalStateException,Unknown URL: content://media/external/audio/albums/null,Parcel.java,1548]
        if (!StringUtils.isReal(albumId)) {
            return null;
        }

        String[] projection = {MediaStore.Audio.Albums.ALBUM_ART};
        String imagePath = null;
        Uri uri = Uri.parse("content://media" + MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI.getPath() + "/" + albumId);

        Cursor cur = context.getContentResolver().query(uri, projection, null, null, null);
        if (cur == null) {
            return null;
        }

        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            imagePath = cur.getString(0);
        }
        cur.close();


        return imagePath;
    }

    /**
     * SongInfo对象转换成Song对象
     */
    public static List<Song> formatSongInfoToSong(List<SongInfo> songInfos) {
        if (null == songInfos || songInfos.size() <= 0) {
            return null;
        }
        List<Song> songs = new ArrayList<>();
        for (SongInfo songInfo : songInfos) {
            Song song = new Song(songInfo.data);
            songs.add(song);
        }
        return songs;
    }

    /**
     * 将Song对象转换成SongInfo对象
     */
    public static List<SongInfo> formatSongsToSongInfos(List<Song> songs) {
        if (null == songs || songs.size() <= 0) {
            return null;
        }
        List<SongInfo> songInfos = new ArrayList<>();
        for (Song song : songs) {
            if (null != song && !TextUtils.isEmpty(song.path)) {
                List<SongInfo> infos = getLocalStorageMusics(App.getContext());
                if (null != infos && infos.size() > 0) {
                    for (SongInfo songInfo : infos) {
                        if (null != song && TextUtils.equals(song.path, songInfo.data)) {
                            songInfos.add(songInfo);
                            break;
                        }
                    }
                }
            }
        }
        return songInfos;
    }

    public static SongInfo formatSongsToSongInfos(Song song) {
        if (null != song) {
            List<SongInfo> infos = getLocalStorageMusics(App.getContext());
            if (null != infos && infos.size() > 0) {
                for (SongInfo songInfo : infos) {
                    if (null != songInfo && TextUtils.equals(song.path, songInfo.data)) {
                        return songInfo;
                    }
                }
            }
        }
        return null;
    }

}
