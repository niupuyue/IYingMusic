package com.paulniu.iyingmusic.db.source;

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
     * 获取所有本地语音消息
     *
     * @return
     */
    public static List<MusicInfo> getMusicInfos() {
        return AppDataBase.getInstance(App.getContext()).getMusicInfoDao().getAllMusic();
    }



}
