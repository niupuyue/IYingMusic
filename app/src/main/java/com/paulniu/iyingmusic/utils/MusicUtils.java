package com.paulniu.iyingmusic.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.paulniu.iyingmusic.model.MusicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 17:53
 * Desc: 音乐播放公共方法
 * Version:
 */
public class MusicUtils {

    /**
     * 查找当前音乐在列表中的位置(下标)
     *
     * @param list
     * @param id
     * @return
     */
    public static int seekCurMusicPositionInList(List<MusicInfo> list, int id) {
        if (id <= -1) {
            return -1;
        }
        if (null == list || list.size() <= 0) {
            return -1;
        }
        int result = -1;
        for (int i = 0; i < list.size(); i++) {
            if (id == list.get(i).songId) {
                result = i;
                break;
            }
        }
        return result;
    }

    /**
     * 查询本地音乐
     */
    public static List<MusicInfo> getLocalStorageMusics(Context context) {
        if (null == context) {
            return null;
        }
        List<MusicInfo> musicInfos = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (null != cursor) {
            while (cursor.moveToNext()) {
                MusicInfo musicInfo = new MusicInfo();
                musicInfo.musicName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                musicInfo.artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                musicInfo.data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                musicInfo.duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                musicInfo.size = (int) cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                // TODO 此处拿到的大小是B,需要将其转换成对应的KB，或者MB 并且需要文件以mp3结尾
                if (musicInfo.size >= SPUtils.getMusicSizeLimit() * 1024  && musicInfo.musicName.endsWith(".mp3")) {
                    if (musicInfo.musicName.contains("-")) {
                        String[] str = musicInfo.musicName.split("-");
                        musicInfo.artist = str[0];
                        musicInfo.musicName = str[1];
                    }
                    musicInfos.add(musicInfo);
                }
            }
        }
        return musicInfos;
    }


}
