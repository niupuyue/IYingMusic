package com.paulniu.iyingmusic.db.entity;

import androidx.room.Ignore;

/**
 * Coder: niupuyue
 * Date: 2020/1/13
 * Time: 18:15
 * Desc: 带音乐数量的文件夹
 * Version:
 */
public class FolderInfoWithMusicCount extends FolderInfo {

    /**
     * 文件夹中音乐的数量
     */
    public int musicCount;

}
