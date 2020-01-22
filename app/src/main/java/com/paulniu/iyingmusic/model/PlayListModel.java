package com.paulniu.iyingmusic.model;

import com.paulniu.iyingmusic.aidl.Song;

import java.io.Serializable;
import java.util.List;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-19
 * Time: 10:33
 * Desc: 播放列表对象
 * Version:
 */
public class PlayListModel implements Serializable {

    public int listId;
    public List<Song> songList;
    public Song curSong;

}
