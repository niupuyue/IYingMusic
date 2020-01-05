package com.paulniu.iyingmusic.utils;

import com.paulniu.iyingmusic.model.MusicInfo;

import java.util.List;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 17:53
 * Desc: 音乐播放公共方法
 * Version:
 */
public class MusicUtils {

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

}
