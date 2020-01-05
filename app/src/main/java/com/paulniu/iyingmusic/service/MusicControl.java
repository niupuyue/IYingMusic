package com.paulniu.iyingmusic.service;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import com.paulniu.iyingmusic.Constant;
import com.paulniu.iyingmusic.model.MusicInfo;
import com.paulniu.iyingmusic.utils.MusicUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 17:38
 * Desc: 歌曲控制
 * Version:
 */
public class MusicControl implements MediaPlayer.OnCompletionListener {

    private MediaPlayer mMediaPlayer;
    private int mPlayMode;
    private List<MusicInfo> mMusicLists = new ArrayList<>();
    private int mPlayState;
    private int mCurPlayIndex;
    private Context mContext;
    private Random mRandom;
    private int mCurMusicId;
    private MusicInfo mMusicInfo;

    public MusicControl(Context context) {
        this.mContext = context;
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnCompletionListener(this);
        mPlayMode = Constant.MPM_LIST_LOOP_PLAY;
        mPlayState = Constant.MPS_NOFILE;
        mCurPlayIndex = -1;
        mCurMusicId = -1;
        mRandom = new Random();
        mRandom.setSeed(System.currentTimeMillis());
    }

    /**
     * 播放音乐
     *
     * @param position
     * @return
     */
    public boolean play(int position) {
        if (mCurPlayIndex == position) {
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
                mPlayState = Constant.MPS_PLAYING;
                sendBroadcast();
            } else {
                pause();
            }
            return true;
        }
        if (!prepare(position)) {
            return false;
        }
        return replay();
    }

    /**
     * 根据音乐id来播放
     *
     * @param id
     * @return
     */
    public boolean playById(int id) {
        int position = MusicUtils.seekCurMusicPositionInList(mMusicLists, id);
        mCurPlayIndex = position;
        if (mCurMusicId == id) {
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
                mPlayState = Constant.MPS_PLAYING;
                sendBroadcast();
            } else {
                pause();
            }
            return true;
        }
        if (!prepare(position)) {
            return false;
        }
        return replay();
    }

    /**
     * 重新播放
     */
    public boolean replay() {
        if (mPlayState == Constant.MPS_INVALID || mPlayState == Constant.MPS_NOFILE) {
            return false;
        }
        mMediaPlayer.start();
        mPlayState = Constant.MPS_PLAYING;
        sendBroadcast();
        return true;
    }

    /**
     * 暂停播放
     */
    public boolean pause() {
        if (mPlayState != Constant.MPS_PLAYING) {
            return false;
        }
        mMediaPlayer.pause();
        mPlayState = Constant.MPS_PAUSE;
        sendBroadcast();
        return true;
    }

    /**
     * 播放上一首
     */
    public boolean prev() {
        if (mPlayState == Constant.MPS_NOFILE) {
            return false;
        }
        mCurPlayIndex--;
        mCurPlayIndex = reviseIndex(mCurPlayIndex);
        if (!prepare(mCurPlayIndex)) {
            return false;
        }
        return replay();
    }

    /**
     * 播放下一首
     */
    public boolean next() {
        if (mPlayState == Constant.MPS_NOFILE) {
            return false;
        }
        mCurPlayIndex++;
        mCurPlayIndex = reviseIndex(mCurPlayIndex);
        if (!prepare(mCurPlayIndex)) {
            return false;
        }
        return replay();
    }

    private int reviseIndex(int index) {
        if (index < 0) {
            index = mMusicLists.size() - 1;
        } else if (index >= mMusicLists.size()) {
            index = 0;
        }
        return index;
    }

    /**
     * 获取当前正在播放音乐的位置
     * @return
     */
    public int position(){
        if (mPlayState == Constant.MPS_PAUSE || mPlayState == Constant.MPS_PLAYING){
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public int duration() {
        if(mPlayState == Constant.MPS_INVALID || mPlayState == Constant.MPS_NOFILE) {
            return 0;
        }
        return mMediaPlayer.getDuration();
    }

    public boolean seekTo(int progress) {
        if(mPlayState == Constant.MPS_INVALID || mPlayState == Constant.MPS_NOFILE) {
            return false;
        }
        int pro = reviseSeekValue(progress);
        int time = mMediaPlayer.getDuration();
        int curTime = (int)((float)pro / 100 * time);
        mMediaPlayer.seekTo(curTime);
        return true;
    }

    private int reviseSeekValue(int progress) {
        if(progress < 0) {
            progress = 0;
        } else if(progress > 100) {
            progress = 100;
        }
        return progress;
    }

    public void refreshMusicList(List<MusicInfo> musicList) {
        mMusicLists.clear();
        mMusicLists.addAll(musicList);
        if(mMusicLists.size() == 0) {
            mPlayState = Constant.MPS_NOFILE;
            mCurPlayIndex = -1;
            return;
        }
//		sendBroadCast();
//		switch(mPlayState) {
//		case MPS_INVALID:
//		case MPS_NOFILE:
//		case MPS_PREPARE:
//			prepare(0);
//			break;
//		}
    }

    private boolean prepare(int pos) {
        mCurPlayIndex = pos;
        mMediaPlayer.reset();
        String path = mMusicLists.get(pos).data;
        try {
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare();
            mPlayState = Constant.MPS_PREPARE;
        } catch (Exception e) {
            Log.e("NPL", "", e);
            mPlayState = Constant.MPS_INVALID;
            if(pos < mMusicLists.size()) {
                pos++;
                playById(mMusicLists.get(pos).songId);
            }
//			sendBroadCast();
            return false;
        }
        sendBroadcast();
        return true;
    }

    public void sendBroadcast() {
        Intent intent = new Intent(Constant.BROADCAST_NAME);
        intent.putExtra(Constant.PLAY_STATE_NAME, mPlayState);
        intent.putExtra(Constant.PLAY_MUSIC_INDEX, mCurPlayIndex);
        intent.putExtra("music_num", mMusicLists.size());
        if(mPlayState != Constant.MPS_NOFILE && mMusicLists.size() > 0) {
            Bundle bundle = new Bundle();
            mMusicInfo = mMusicLists.get(mCurPlayIndex);
            mCurMusicId = mMusicInfo.songId;
            bundle.putParcelable(MusicInfo.KEY_MUSIC, mMusicInfo);
            intent.putExtra(MusicInfo.KEY_MUSIC, bundle);
        }
        mContext.sendBroadcast(intent);
    }

    public int getCurMusicId() {
        return mCurMusicId;
    }

    public MusicInfo getCurMusic() {
        return mMusicInfo;
    }

    public int getPlayState() {
        return mPlayState;
    }

    public int getPlayMode() {
        return mPlayMode;
    }

    public void setPlayMode(int mode) {
        switch(mode) {
            case Constant.MPM_LIST_LOOP_PLAY:
            case Constant.MPM_ORDER_PLAY:
            case Constant.MPM_RANDOM_PLAY:
            case Constant.MPM_SINGLE_LOOP_PLAY:
                mPlayMode = mode;
                break;
        }
    }

    public List<MusicInfo> getMusicList() {
        return mMusicLists;
    }

    private int getRandomIndex() {
        int size = mMusicLists.size();
        if(size == 0) {
            return -1;
        }
        return Math.abs(mRandom.nextInt() % size);
    }

    public void exit() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mCurPlayIndex = -1;
        mMusicLists.clear();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        switch(mPlayMode) {
            case Constant.MPM_LIST_LOOP_PLAY:
                next();
                break;
            case Constant.MPM_ORDER_PLAY:
                if(mCurPlayIndex != mMusicLists.size() - 1) {
                    next();
                } else {
                    prepare(mCurPlayIndex);
                }
                break;
            case Constant.MPM_RANDOM_PLAY:
                int index = getRandomIndex();
                if(index != -1) {
                    mCurPlayIndex = index;
                } else {
                    mCurPlayIndex = 0;
                }
                if(prepare(mCurPlayIndex)) {
                    replay();
                }
                break;
            case Constant.MPM_SINGLE_LOOP_PLAY:
                play(mCurPlayIndex);
                break;
        }
    }

}
