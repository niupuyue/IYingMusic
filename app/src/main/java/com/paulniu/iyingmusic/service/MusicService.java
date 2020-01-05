package com.paulniu.iyingmusic.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.RemoteViews;

import com.paulniu.iyingmusic.Constant;
import com.paulniu.iyingmusic.IMusicService;
import com.paulniu.iyingmusic.MainActivity;
import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.interfaces.IOnShakeListener;
import com.paulniu.iyingmusic.model.MusicInfo;
import com.paulniu.iyingmusic.operation.ShakeDetector;
import com.paulniu.iyingmusic.utils.SPUtils;

import java.util.List;


/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 16:47
 * Desc: 后台service 用来控制音乐的播放，暂停，显示通知栏信息等内容
 * Version:
 */
public class MusicService extends Service implements IOnShakeListener {

    private static final String PAUSE_BROADCAST_NAME = "com.paulniu.iyingmusic.pause.broadcast";
    private static final String NEXT_BROADCAST_NAME = "com.paulniu.iyingmusic.next.broadcast";
    private static final String PRE_BROADCAST_NAME = "com.paulniu.iyingmusic.pre.broadcast";
    private static final int PAUSE_FLAG = 0x1;
    private static final int NEXT_FLAG = 0x2;
    private static final int PRE_FLAG = 0x3;

    private MusicControl mMc;
    private NotificationManager mNotificationManager;
    //	private Notification mNotification;
    private int NOTIFICATION_ID = 0x1;
    private RemoteViews rv;
    private ShakeDetector mShakeDetector;
    /**
     * 当前是否正在播放
     */
    private boolean mIsPlaying;
    /**
     * 在设置界面是否开启了摇一摇的监听
     */
    public boolean mShake;
    private ControlBroadcast mConrolBroadcast;
    private MusicPlayBroadcast mPlayBroadcast;

    @Override
    public void onCreate() {
        super.onCreate();

        mMc = new MusicControl(this);
        mShakeDetector = new ShakeDetector(this);
        mShakeDetector.setListener(this);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mConrolBroadcast = new ControlBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PAUSE_BROADCAST_NAME);
        filter.addAction(NEXT_BROADCAST_NAME);
        filter.addAction(PRE_BROADCAST_NAME);
        registerReceiver(mConrolBroadcast, filter);

        mPlayBroadcast = new MusicPlayBroadcast();
        IntentFilter filter1 = new IntentFilter(Constant.BROADCAST_NAME);
        filter1.addAction(Constant.BROADCAST_SHAKE);
        registerReceiver(mPlayBroadcast, filter1);
    }


    /**
     * 更新notification
     *
     * @param bitmap
     * @param title
     * @param name
     */
    private void updateNotification(Bitmap bitmap, String title, String name) {
        Intent intent = new Intent(getApplicationContext(),
                MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv = new RemoteViews(this.getPackageName(), R.layout.view_notification);
        Notification notification = new Notification();
        notification.icon = R.mipmap.ic_launcher;
        notification.tickerText = title;
        notification.contentIntent = pi;
        notification.contentView = rv;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;

        if (bitmap != null) {
            rv.setImageViewBitmap(R.id.image, bitmap);
        } else {
            rv.setImageViewResource(R.id.image, R.mipmap.img_album_background);
        }
        rv.setTextViewText(R.id.title, title);
        rv.setTextViewText(R.id.text, name);
//		mNotificationManager.notify(NOTIFICATION_ID, mNotification);

        //此处action不能是一样的 如果一样的 接受的flag参数只是第一个设置的值
        Intent pauseIntent = new Intent(PAUSE_BROADCAST_NAME);
        pauseIntent.putExtra("FLAG", PAUSE_FLAG);
        PendingIntent pausePIntent = PendingIntent.getBroadcast(this, 0, pauseIntent, 0);
        rv.setOnClickPendingIntent(R.id.iv_pause, pausePIntent);

        Intent nextIntent = new Intent(NEXT_BROADCAST_NAME);
        nextIntent.putExtra("FLAG", NEXT_FLAG);
        PendingIntent nextPIntent = PendingIntent.getBroadcast(this, 0, nextIntent, 0);
        rv.setOnClickPendingIntent(R.id.iv_next, nextPIntent);

        Intent preIntent = new Intent(PRE_BROADCAST_NAME);
        preIntent.putExtra("FLAG", PRE_FLAG);
        PendingIntent prePIntent = PendingIntent.getBroadcast(this, 0, preIntent, 0);
        rv.setOnClickPendingIntent(R.id.iv_previous, prePIntent);

        startForeground(NOTIFICATION_ID, notification);
    }

    private class MusicPlayBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.BROADCAST_NAME)) {
                int playState = intent.getIntExtra(Constant.PLAY_STATE_NAME, Constant.MPS_NOFILE);
                switch (playState) {
                    case Constant.MPS_PLAYING:
                        mIsPlaying = true;
                        if (SPUtils.getShake()) {
                            mShakeDetector.start();
                        }
                        break;
                    default:
                        mIsPlaying = false;
                        mShakeDetector.stop();
                }
            } else if (intent.getAction().equals(Constant.BROADCAST_SHAKE)) {
                mShake = intent.getBooleanExtra(Constant.SHAKE_ON_OFF, false);
                if (mShake && mIsPlaying) {//如果开启了监听并且歌曲正在播放
                    mShakeDetector.start();
                } else if (!mShake) {
                    mShakeDetector.stop();
                }
            }
        }
    }

    private class ControlBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int flag = intent.getIntExtra("FLAG", -1);
            switch (flag) {
                case PAUSE_FLAG:
//				MediaService.this.stopForeground(true);
                    mMc.pause();
                    break;
                case NEXT_FLAG:
                    mMc.next();
                    break;
                case PRE_FLAG:
                    mMc.prev();
                    break;
            }
        }
    }

    private void cancelNotification() {
        stopForeground(true);
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private class ServerStub extends IMusicService.Stub {

        @Override
        public boolean pause() throws RemoteException {
//			MediaService.this.stopForeground(true);
            return mMc.pause();
        }

        @Override
        public boolean prev() throws RemoteException {
            return mMc.prev();
        }

        @Override
        public boolean next() throws RemoteException {
            return mMc.next();
        }

        @Override
        public boolean play(int pos) throws RemoteException {
            return mMc.play(pos);
        }

        @Override
        public int duration() throws RemoteException {
            return mMc.duration();
        }

        @Override
        public int position() throws RemoteException {
            return mMc.position();
        }

        @Override
        public boolean seekTo(int progress) throws RemoteException {
            return mMc.seekTo(progress);
        }

        @Override
        public void refreshMusicList(List<MusicInfo> musicList) throws RemoteException {
            mMc.refreshMusicList(musicList);
        }

        @Override
        public void getMusicList(List<MusicInfo> musicList) throws RemoteException {
            List<MusicInfo> music = mMc.getMusicList();
            for (MusicInfo m : music) {
                musicList.add(m);
            }
        }

        @Override
        public int getPlayState() throws RemoteException {
            return mMc.getPlayState();
        }

        @Override
        public int getPlayMode() throws RemoteException {
            return mMc.getPlayMode();
        }

        @Override
        public void setPlayMode(int mode) throws RemoteException {
            mMc.setPlayMode(mode);
        }

        @Override
        public void sendPlayStateBrocast() throws RemoteException {
            mMc.sendBroadcast();
        }

        @Override
        public void exit() throws RemoteException {
            cancelNotification();
            stopSelf();
            mMc.exit();
        }

        @Override
        public boolean rePlay() throws RemoteException {
            return mMc.replay();
        }

        @Override
        public int getCurMusicId() throws RemoteException {
            return mMc.getCurMusicId();
        }

        @Override
        public void updateNotification(Bitmap bitmap, String title, String name)
                throws RemoteException {
            MusicService.this.updateNotification(bitmap, title, name);
        }

        @Override
        public void cancelNotification() throws RemoteException {
            MusicService.this.cancelNotification();
        }

        @Override
        public boolean playById(int id) throws RemoteException {
            return mMc.playById(id);
        }

        @Override
        public MusicInfo getCurMusic() throws RemoteException {
            return mMc.getCurMusic();
        }

    }

    private final IBinder mBinder = new ServerStub();

    @Override
    public void onShake() {
        mMc.next();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mConrolBroadcast != null) {
            unregisterReceiver(mConrolBroadcast);
        }
        if (mPlayBroadcast != null) {
            unregisterReceiver(mPlayBroadcast);
        }
    }
}
