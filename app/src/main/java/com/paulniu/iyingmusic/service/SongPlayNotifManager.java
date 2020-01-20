package com.paulniu.iyingmusic.service;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.RemoteException;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.activity.MainActivity;
import com.paulniu.iyingmusic.aidl.IPlayControl;
import com.paulniu.iyingmusic.aidl.Song;
import com.paulniu.iyingmusic.db.entity.SongInfo;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-19
 * Time: 10:53
 * Desc: 音乐播放状态栏通知管理类
 * Version:
 */
public class SongPlayNotifManager {

    private static final String PLAY_NOTIFY = "play_notify";
    private static final String PLAY_NOTIFY_CODE = "play_notify_code";

    private static final int PLAY_STATUS_SWITCH = 0;
    private static final int PLAY_NEXT = 1;
    private static final int PLAY_PREVIOUS = 2;
    private static final int PLAY_FAVORITE_STATUS_SWITCH = 3;
    private static final int PLAY_NOTIFY_CLOSE = 4;

    private static final int PLAY_NOTIFY_ID = 0x1213;

    private Activity mActivity;
    private IPlayControl playControl;
    private SongPlayNotifyReceiver playNotifyReceiver;
    private NotificationManagerCompat managerCompat;

    private SongInfo curSong;
    private boolean isPlaying;

    public SongPlayNotifManager(Activity activity, IPlayControl playControl) {
        this.mActivity = activity;
        this.playControl = playControl;
        playNotifyReceiver = new SongPlayNotifyReceiver();
        managerCompat = NotificationManagerCompat.from(activity);
    }

    private Notification buildNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mActivity);
        Intent intent = new Intent(mActivity, MainActivity.class);
        PendingIntent startMainActivity = PendingIntent.getActivity(mActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(startMainActivity)
                .setTicker(mActivity.getString(R.string.app_name))
                .setSmallIcon(R.mipmap.logo_small_icon)
                .setWhen(System.currentTimeMillis())
                .setOngoing(true)
                .setCustomContentView(createContentView())
                .setCustomBigContentView(createContentBigView())
                .setPriority(Notification.PRIORITY_HIGH);

        return builder.build();
    }

    private RemoteViews createContentBigView() {
        RemoteViews view = new RemoteViews(mActivity.getPackageName(), R.layout.view_notification_bigview);

        return view;
    }

    private RemoteViews createContentView() {
        RemoteViews view = new RemoteViews(mActivity.getPackageName(), R.layout.view_notification_normal);
        setContentView(view);
        setCommonClickPending(view);
        return view;
    }

    private void setContentView(RemoteViews contentView) {
        if (null != contentView && null != curSong) {
            String name = curSong.getSongName();
            String arts = curSong.getArtist();
            Bitmap bitmap = createCorver(curSong.album_path);
            contentView.setImageViewBitmap(R.id.play_notify_cover, bitmap);
            contentView.setTextViewText(R.id.play_notify_name, name);
            contentView.setTextViewText(R.id.play_notify_arts, arts + " - " + name);
            contentView.setImageViewResource(R.id.play_notify_play, isPlaying ? R.mipmap.ic_pause : R.mipmap.ic_play_arrow);
        }
    }

    private Bitmap createCorver(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (null == bitmap) {
            bitmap = BitmapFactory.decodeResource(mActivity.getResources(), R.mipmap.ic_launcher);
        }
        return bitmap;
    }

    // 播放或暂停，下一曲，关闭
    private void setCommonClickPending(RemoteViews view) {
        Intent playOrPause = new Intent(PLAY_NOTIFY);
        playOrPause.putExtra(PLAY_NOTIFY_CODE, PLAY_STATUS_SWITCH);
        PendingIntent p1 = PendingIntent.getBroadcast(mActivity, PLAY_STATUS_SWITCH, playOrPause, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.play_notify_play, p1);

        Intent next = new Intent(PLAY_NOTIFY);
        next.putExtra(PLAY_NOTIFY_CODE, PLAY_NEXT);
        PendingIntent p2 = PendingIntent.getBroadcast(mActivity, PLAY_NEXT, next, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.play_notify_next, p2);

        Intent close = new Intent(PLAY_NOTIFY);
        close.putExtra(PLAY_NOTIFY_CODE, PLAY_NOTIFY_CLOSE);
        PendingIntent p3 = PendingIntent.getBroadcast(mActivity, PLAY_NOTIFY_CLOSE, close, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.play_notify_close, p3);
    }

    public void initBroadcastReceivers() {
        SongBroadcastManager bd = SongBroadcastManager.getInstance();
        bd.registerBroadReceiver(mActivity, playNotifyReceiver, PLAY_NOTIFY);
    }

    public void unregisterReceiver() {
        SongBroadcastManager.getInstance().unregisterReceiver(mActivity, playNotifyReceiver);
    }

    public void show() {
        if (null == curSong) {
            return;
        }
        Notification f = buildNotification();
        managerCompat.notify(PLAY_NOTIFY_ID, f);
    }

    public void hide() {
        if (null != managerCompat) {
            managerCompat.cancelAll();
        }
    }

    /**
     * 状态栏消息广播接收器
     */
    private class SongPlayNotifyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int code = intent.getIntExtra(PLAY_NOTIFY_CODE, -1);
            if (code == -1) {
                return;
            }

            switch (code) {
                case PLAY_STATUS_SWITCH:
                    handlePlayStatusSwitch();
                    break;
                case PLAY_NEXT:
                    try {
                        playControl.next();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case PLAY_PREVIOUS:
                    try {
                        playControl.pre();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case PLAY_FAVORITE_STATUS_SWITCH:
                    SongBroadcastManager.getInstance().sendBroadcast(mActivity, SongBroadcastManager.FILTER_MAIN_SHEET_UPDATE, null);
                    break;
                case PLAY_NOTIFY_CLOSE:
                    try {
                        if (playControl.status() == SongPlayController.STATUS_PLAYING) {
                            playControl.pause();
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    hide();
                    break;
                default:
                    break;
            }
        }

        private void handlePlayStatusSwitch() {
            try {
                if (playControl.status() == SongPlayController.STATUS_PLAYING) {
                    playControl.pause();
                } else {
                    playControl.resume();
                }
                isPlaying = !isPlaying;

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

}
