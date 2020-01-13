package com.paulniu.iyingmusic;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 17:45
 * Desc: 常量对象
 * Version:
 */
public class Constant {

    // 数据库名
    public static final String DATABASE_NAME = "iyingmusic.db";

    // 播放模式
    public static final int MPM_LIST_LOOP_PLAY = 0; // 列表循环
    public static final int MPM_ORDER_PLAY = 1; // 顺序播放
    public static final int MPM_RANDOM_PLAY = 2; // 随机播放
    public static final int MPM_SINGLE_LOOP_PLAY = 3; // 单曲循环

    // 播放状态
    public static final int MPS_NOFILE = -1; // 无音乐文件
    public static final int MPS_INVALID = 0; // 当前音乐文件无效
    public static final int MPS_PREPARE = 1; // 准备就绪
    public static final int MPS_PLAYING = 2; // 播放中
    public static final int MPS_PAUSE = 3; // 暂停

    public static final String BROADCAST_NAME = "com.paulniu.iyingmusic.broadcast";
    public static final String SERVICE_NAME = "com.paulniu.iyingmusic.service.MediaService";
    public static final String BROADCAST_QUERY_COMPLETE_NAME = "com.paulniu.iyingmusic.querycomplete.broadcast";
    public static final String BROADCAST_CHANGEBG = "com.paulniu.iyingmusic.changebg";
    public static final String BROADCAST_SHAKE = "com.paulniu.iyingmusic.shake";

    public static final String PLAY_STATE_NAME = "PLAY_STATE_NAME";
    public static final String PLAY_MUSIC_INDEX = "PLAY_MUSIC_INDEX";

    //是否开启了振动模式
    public static final String SHAKE_ON_OFF = "SHAKE_ON_OFF";

    // SPUtils
    public static final String SP_NAME = "com.paulniu.iyingmusic.preference";

}
