package com.paulniu.iyingmusic.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.paulniu.iyingmusic.aidl.Song;
import com.paulniu.iyingmusic.model.PlayListModel;
import com.paulniu.iyingmusic.utils.SPUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Coder: niupuyue
 * Date: 2020/1/17
 * Time: 10:56
 * Desc: 音乐播放控制器
 * Version:
 */
public class SongPlayController {
    /**
     * 上下文对象
     */
    private final Context context;
    /**
     * 当前类对象
     */
    private static volatile SongPlayController MANAGER = null;
    /**
     * 音乐播放时需要获取手机的焦点
     */
    private final SongFocusManager focusManager;
    /**
     * 手机添加
     */
    private final SongSessionManager sessionManager;

    /**
     * 当前正在播放的音乐的下标
     */
    private int mCurrentSong = -1;
    /**
     * 当前音乐的播放状态
     */
    private int mPlayState;
    /**
     * 当前的音乐播放列表
     */
    private List<Song> mPlayList = Collections.synchronizedList(new ArrayList<Song>());
    /**
     * 多媒体播放对象
     */
    private final MediaPlayer mPlayer;

    /**
     * 是否可以点击下一首
     */
    private boolean isNext = true;
    private int mPlayListId = -1;

    // MediaPlayer 是否调用过 setDataSource，
    // 否则第一次调用 changeSong 里的 _.reset 方法时 MediaPlayer 会抛 IllegalStateException
    private boolean hasMediaPlayerInit = false;

    public interface NotifyStatusChanged {
        void notify(Song song, int index, int status);
    }

    public interface NotifySongChanged {
        void notify(Song song, int index, boolean isNext);
    }

    public interface NotifyPlayListChanged {
        void notify(Song current, int index, int id);
    }

    private final NotifySongChanged mNotifySongChanged;
    private final NotifyPlayListChanged mNotifyPlayListChanged;
    private final NotifyStatusChanged mNotifyStatusChanged;

    //未知错误
    public static final int ERROR_UNKNOWN = -1;

    public static final int ERROR_INVALID = -2;

    //歌曲文件解码错误
    public static final int ERROR_DECODE = -3;

    //没有指定歌曲
    public static final int ERROR_NO_RESOURCE = -4;

    //正在播放
    public static final int STATUS_PLAYING = 10;

    //播放结束
    public static final int STATUS_COMPLETE = 11;

    //开始播放
    public static final int STATUS_START = 12;

    //播放暂停
    public static final int STATUS_PAUSE = 13;

    //停止
    public static final int STATUS_STOP = 14;

    //默认播放模式，列表播放，播放至列表末端时停止播放
    public static final int MODE_DEFAULT = 20;

    //列表循环
    public static final int MODE_LIST_LOOP = 21;

    //单曲循环
    public static final int MODE_SINGLE_LOOP = 22;

    //随机播放
    public static final int MODE_RANDOM = 23;

    private int mPlayMode = MODE_DEFAULT;

    private SongPlayController(Context context, SongFocusManager focusManager,
                               SongSessionManager sessionManager,
                               NotifyStatusChanged sl,
                               NotifySongChanged sc,
                               NotifyPlayListChanged pl) {
        this.context = context;
        this.focusManager = focusManager;
        this.sessionManager = sessionManager;
        this.mNotifyStatusChanged = sl;
        this.mNotifySongChanged = sc;
        this.mNotifyPlayListChanged = pl;

        mPlayState = STATUS_STOP;
        mPlayer = new MediaPlayer();
        // 添加音乐播放完成自定播放下一首
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextSong();
            }
        });

    }

    /**
     * 通过静态方法暴露出创建音乐播放操作类对象
     *
     * @param context        上下文对象
     * @param focusManager   音乐播放焦点工具类
     * @param sessionManager 音乐播放缓存工具类
     * @param sl             通知栏状态改变回调
     * @param sc             通知栏歌曲改变回调
     * @param pl             通知栏播放列表改变回调
     * @return 音乐播放操作工具类对象
     */
    public static SongPlayController getMediaController(Context context, SongFocusManager focusManager, SongSessionManager sessionManager, NotifyStatusChanged sl, NotifySongChanged sc, NotifyPlayListChanged pl) {
        if (MANAGER == null) {
            synchronized (SongPlayController.class) {
                if (MANAGER == null)
                    MANAGER = new SongPlayController(context, focusManager, sessionManager, sl, sc, pl);
            }
        }
        return MANAGER;
    }

    //设置播放模式
    public void setPlayMode(int mode) {
        this.mPlayMode = mode;
    }

    //获得播放模式
    public int getPlayMode() {
        return this.mPlayMode;
    }

    //返回播放列表
    public List<Song> getSongsList() {
        if (null == mPlayList || mPlayList.size() <= 0) {
            // 当前播放列表中的数据为空，则从本地数据中获取数据
            if (null != SPUtils.getPlayList()){
                return SPUtils.getPlayList().songList;
            }else {
                return null;
            }
        } else {
            return mPlayList;
        }
    }

    //设置播放列表
    public Song setPlayList(List<Song> songs, int current, int id) {
        this.mPlayList = songs;
        this.mPlayListId = id;

        mCurrentSong = current;
        changeSong();

        Song currentS = songs.get(mCurrentSong);
        mNotifyPlayListChanged.notify(currentS, current, id);

        // 改变播放列表之后，将播放列表写入本地存储
        if (null != songs && songs.size() > 0) {
            PlayListModel model = new PlayListModel();
            model.listId = id;
            model.songList = songs;
            SPUtils.setPlayList(model);
        }
        return currentS;
    }

    public int getPlayListId() {
        if (mPlayListId < 0) {
            PlayListModel model = SPUtils.getPlayList();
            if (null != model) {
                return model.listId;
            }
        }
        return mPlayListId;
    }

    /**
     * 设置当前正在播放的音乐，并且把歌曲信息更新到底部sheet TODO
     *
     * @param sheetID
     * @param current
     * @return
     */
    public Song setPlaySheet(int sheetID, int current) {
//        DBMusicocoController dbController = new DBMusicocoController(context, false);
//        List<DBSongInfo> ds;
//        if (sheetID < 0) {
//            MainSheetHelper helper = new MainSheetHelper(context, dbController);
//            ds = helper.getMainSheetSongInfo(sheetID);
//        } else {
//            ds = dbController.getSongInfos(sheetID);
//        }
//        dbController.close();
//
//        if (ds == null || ds.size() == 0) {
//            return null;
//        }
//
//        List<Song> list = new ArrayList<>();
//        for (DBSongInfo d : ds) {
//            Song song = new Song(d.path);
//            list.add(song);
//        }
//
//        mPlayList = list;
//        mPlayListId = sheetID;
//
//        mCurrentSong = current;
//        changeSong();
//
//        Song currentS = mPlayList.get(mCurrentSong);
//        mNotifyPlayListChanged.notify(currentS, current, sheetID);

        return null;
    }

    //当前正在播放曲目
    public Song getCurrentSong() {
        return mPlayList.size() == 0 ? null : mPlayList.get(mCurrentSong);
    }

    public int getCurrentSongIndex() {
        return mCurrentSong;
    }

    //播放指定曲目
    public int play(@NonNull Song song) {
        return play(mPlayList.indexOf(song));
    }

    public int play(int index) {
        int result = ERROR_INVALID;
        if (index != -1) { //列表中有该歌曲
            if (mCurrentSong != index) { //不是当前歌曲
                isNext = mCurrentSong < index;
                mCurrentSong = index;
                if (mPlayState != STATUS_PLAYING) {
                    mNotifyStatusChanged.notify(getCurrentSong(), mCurrentSong, STATUS_START);
                    mPlayState = STATUS_PLAYING; //切换并播放
                }
                result = changeSong();
            } else if (mPlayState != STATUS_PLAYING) { // 是但没在播放
                mPlayState = STATUS_PAUSE;
                resume();//播放
            } else  // 是且已经在播放
                return 1;
        } else return ERROR_NO_RESOURCE;
        return result;
    }

    public int prepare(@NonNull Song song) {
        int result = ERROR_INVALID;
        int index = mPlayList.indexOf(song);
        if (index != -1) { //列表中有该歌曲
            if (mCurrentSong != index) { //不是当前歌曲
                mCurrentSong = index;
                if (mPlayState == STATUS_PLAYING)
                    pause();
                result = changeSong();
            }
        } else return ERROR_NO_RESOURCE;
        return result;
    }

    //获得播放状态
    public int getPlayState() {
        return mPlayState;
    }

    //释放播放器，服务端停止时，该方法才应该被调用
    public void releaseMediaPlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayState = STATUS_STOP;

            sessionManager.release();
        }
    }

    //上一曲
    public Song preSong() {
        isNext = false;
        switch (mPlayMode) {
            case MODE_SINGLE_LOOP: {
                changeSong();
                break;
            }
            case MODE_RANDOM: {
                int pre = new Random().nextInt(mPlayList.size());
                if (pre != mCurrentSong) {
                    mCurrentSong = pre;
                    changeSong();
                }
                break;
            }
            case MODE_LIST_LOOP:
            default: {
                if (mCurrentSong == 0) {
                    mCurrentSong = mPlayList.size() - 1;
                } else {
                    mCurrentSong--;
                }
                changeSong();

                break;
            }
        }

        return mPlayList.size() == 0 ? null : mPlayList.get(mCurrentSong);
    }

    //下一曲
    public Song nextSong() {
        isNext = true;
        switch (mPlayMode) {
            case MODE_SINGLE_LOOP: {
                changeSong();
                break;
            }
            case MODE_LIST_LOOP: {
                if (mCurrentSong == mPlayList.size() - 1) {
                    mCurrentSong = 0;
                } else {
                    mCurrentSong++;
                }
                changeSong();
                break;
            }
            case MODE_RANDOM: {
                // UPDATE: 2017/8/26 修复 正在播放的歌单最后一首歌曲被移除歌单时 mPlayList.size() == 0 使 nextInt 方法出错
                int next = new Random().nextInt(mPlayList.size());
                if (next != mCurrentSong) {
                    mCurrentSong = next;
                    changeSong();
                }
                break;
            }
            default: {
                if (mCurrentSong == mPlayList.size() - 1) { // 最后一首
                    mCurrentSong = 0;
                    changeSong();
                    pause();//使暂停播放
                } else {
                    mCurrentSong++;
                    changeSong();
                }
                break;
            }
        }

        return mPlayList.size() == 0 ? null : mPlayList.get(mCurrentSong);
    }

    //暂停播放
    public int pause() {
        if (mPlayState == STATUS_PLAYING) {
            sessionManager.updatePlaybackState();
            mPlayer.pause();
            mPlayState = STATUS_PAUSE;

            // 放在最后 mPlayState 修改之后
            mNotifyStatusChanged.notify(getCurrentSong(), mCurrentSong, STATUS_STOP);
        }
        return mPlayState;
    }

    //继续播放
    public int resume() {
        if (mPlayState != STATUS_PLAYING) {
            focusManager.requestAudioFocus();
            sessionManager.updatePlaybackState();
            mPlayer.start();
            mPlayState = STATUS_PLAYING;

            mNotifyStatusChanged.notify(getCurrentSong(), mCurrentSong, STATUS_START);
        }
        return 1;
    }

    //定位到
    public int seekTo(int to) {
        sessionManager.updatePlaybackState();
        mPlayer.seekTo(to);
        return 1;
    }

    //获得播放进度
    public int getProgress() {
        return mPlayer.getCurrentPosition();
    }

    /**
     * 切换曲目
     *
     * @return 切换成功返回 1
     */
    private synchronized int changeSong() {

        if (mPlayState == STATUS_PLAYING || mPlayState == STATUS_PAUSE) {
            mPlayer.stop();
        }

        if (hasMediaPlayerInit) {
            mPlayer.reset();
        }

        if (mPlayList.size() == 0) {
            mCurrentSong = 0;
            mNotifySongChanged.notify(null, -1, isNext);
            return ERROR_NO_RESOURCE;
        } else {
            String next = mPlayList.get(mCurrentSong).path;
            try {
                sessionManager.updateMetaData(next);
                mPlayer.setDataSource(next);
                if (!hasMediaPlayerInit) {
                    hasMediaPlayerInit = true;
                }
                mPlayer.prepare();

            } catch (IOException e) {
                e.printStackTrace();
                return ERROR_DECODE;
            }

            if (mPlayState == STATUS_PLAYING) {
                focusManager.requestAudioFocus();
                sessionManager.updatePlaybackState();
                mPlayer.start();
            }

            mNotifySongChanged.notify(getCurrentSong(), mCurrentSong, isNext);
            return 1;
        }
    }

    //用于提取频谱
    public int getAudioSessionId() {
        return mPlayer.getAudioSessionId();
    }

    public void remove(Song song) {
        if (song == null) {
            return;
        }

        int index = mPlayList.indexOf(song);
        if (index != -1) {
            if (mCurrentSong == index) {
                int tempS = mPlayMode;
                mPlayMode = MODE_LIST_LOOP;
                mPlayList.remove(index);
                mCurrentSong--;
                nextSong();
                mPlayMode = tempS;
            } else {
                mPlayList.remove(index);
                if (index < mCurrentSong) {
                    mCurrentSong--;
                }
            }

            if (mPlayList.size() == 0 || mCurrentSong < 0) {
                // 服务器的播放列表是空的，这可能是因为仅有一首歌曲的播放列表被清空
                // 此时重新设置为【全部歌曲】，该过程在服务端完成，若在客户端的 onPlayListChange
                // 回调中重置播放列表会得到异常：beginBroadcast() called while already in a broadcast
                setPlaySheet(MainSheetHelper.SHEET_ALL, 0);
            } else {
                mNotifyPlayListChanged.notify(mPlayList.get(mCurrentSong), mCurrentSong, mPlayListId);
            }
        }
    }

}
