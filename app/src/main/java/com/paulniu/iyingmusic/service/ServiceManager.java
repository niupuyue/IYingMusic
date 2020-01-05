package com.paulniu.iyingmusic.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;

import com.paulniu.iyingmusic.IMusicService;
import com.paulniu.iyingmusic.interfaces.IOnServiceConnectComplete;
import com.paulniu.iyingmusic.model.MusicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 16:39
 * Desc: 全局控制service
 * Version:
 */
public class ServiceManager {

    private IMusicService mService;
    private Context mContext;
    private ServiceConnection mConn;
    private IOnServiceConnectComplete mIOnServiceConnectComplete;

    public ServiceManager(Context context) {
        this.mContext = context;
        initConn();
    }

    /**
     * 初始化连接
     */
    private void initConn() {
        mConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.e("NPL", "ServiceManager-onServiceConnected");
                mService = IMusicService.Stub.asInterface(iBinder);
                if (null != mService && null != mIOnServiceConnectComplete) {
                    mIOnServiceConnectComplete.onServiceConnnectComplete(mService);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.e("NPL", "ServiceManager-onServiceDisconnected");
            }
        };
    }

    public void connectService() {
        Intent intent = new Intent("com.paulniu.iyingmusic.service.MusicService");
        if (null != mContext) {
            mContext.bindService(intent, mConn, Context.BIND_AUTO_CREATE);
        }
    }

    public void disConnectService() {
        if (null != mContext) {
            mContext.unbindService(mConn);
            mContext.stopService(new Intent("com.paulniu.iyingmusic.service.MusicService"));
        }
    }

    /**
     * 刷新音乐列表
     *
     * @param musicInfos
     */
    public void refreshMusicList(List<MusicInfo> musicInfos) {
        if (null != mService && null != musicInfos) {
            try {
                mService.refreshMusicList(musicInfos);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 获取音乐列表
     *
     * @return
     */
    public List<MusicInfo> getMusicInfos() {
        if (null != mService) {
            List<MusicInfo> musicInfos = new ArrayList<>();
            try {
                mService.getMusicList(musicInfos);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return musicInfos;
        }
        return null;
    }

    /**
     * 根据相对位置播放音乐
     */
    public boolean play(int position) {
        if (null != mService) {
            try {
                return mService.play(position);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 根据音乐id播放音乐
     */
    public boolean playById(int id) {
        if (null != mService) {
            try {
                return mService.playById(id);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 重新播放
     */
    public boolean replay() {
        if (null != mService) {
            try {
                return mService.rePlay();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 暂停播放
     */
    public boolean pause() {
        if (null != mService) {
            try {
                return mService.pause();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 上一首
     */
    public boolean prev() {
        if (null != mService) {
            try {
                return mService.prev();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 下一首
     */
    public boolean next() {
        if (null != mService) {
            try {
                return mService.next();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 拖动播放进度条(从某一个位置继续播放)
     */
    public boolean seekTo(int progress) {
        if (null != mService) {
            try {
                return mService.seekTo(progress);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 当前位置(相对位置)
     */
    public int position() {
        if (null != mService) {
            try {
                return mService.position();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }

    public int duration() {
        if (null != mService) {
            try {
                return mService.duration();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 获取播放的状态
     *
     * @return
     */
    public int getPlayState() {
        if (null != mService) {
            try {
                return mService.getPlayState();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 设置播放模式
     */
    public void setPlayMode(int mode) {
        if (null != mService) {
            try {
                mService.setPlayMode(mode);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 获取播放模式
     */
    public int getPlayMode() {
        if (null != mService) {
            try {
                return mService.getPlayMode();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 获取当前正在播放的音乐id
     */
    public int getCurMusicId() {
        if (null != mService) {
            try {
                return mService.getCurMusicId();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return -1;
    }

    /**
     * 获取当前音乐对象
     */
    public MusicInfo getCurMusicInfo() {
        if (null != mService) {
            try {
                return mService.getCurMusic();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 发送音乐播放状态广播
     */
    public void sendBroadcast() {
        if (null != mService) {
            try {
                mService.sendPlayStateBrocast();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 退出
     */
    public void exit() {
        if (null != mService) {
            try {
                mService.exit();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // 解除音乐服务
        if (null != mContext) {
            mContext.unbindService(mConn);
            mContext.stopService(new Intent("com.paulniu.iyingmusic.service.MusicService"));
        }
    }

    /**
     * 更新状态栏
     */
    public void updateNotification(Bitmap bitmap, String title, String name) {
        if (null != mService) {
            try {
                mService.updateNotification(bitmap, title, name);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 取消(关闭)状态栏
     */
    public void cancelNotification() {
        if (null != mService) {
            try {
                mService.cancelNotification();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 设置服务连接完成回调
     */
    public void setmIOnServiceConnectComplete(IOnServiceConnectComplete IServiceConnect) {
        this.mIOnServiceConnectComplete = IServiceConnect;
    }

}
