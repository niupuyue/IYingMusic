package com.paulniu.iyingmusic.activity;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.adapter.MainFolderAdapter;
import com.paulniu.iyingmusic.aidl.IPlayControl;
import com.paulniu.iyingmusic.aidl.Song;
import com.paulniu.iyingmusic.base.BaseActivity;
import com.paulniu.iyingmusic.db.entity.FolderInfo;
import com.paulniu.iyingmusic.db.entity.FolderInfoWithMusicCount;
import com.paulniu.iyingmusic.db.entity.SongInfo;
import com.paulniu.iyingmusic.db.source.FolderInfoSource;
import com.paulniu.iyingmusic.db.source.SongInfoSource;
import com.paulniu.iyingmusic.interfaces.OnServiceConnect;
import com.paulniu.iyingmusic.interfaces.PlayServiceCallback;
import com.paulniu.iyingmusic.model.PlayListModel;
import com.paulniu.iyingmusic.service.PlayServiceConnection;
import com.paulniu.iyingmusic.service.SongPlayServiceManager;
import com.paulniu.iyingmusic.utils.SPUtils;
import com.paulniu.iyingmusic.widget.MyAppTitle;
import com.paulniu.iyingmusic.widget.dialog.FolderAddDialog;
import com.paulniu.iyingmusic.widget.pop.CurrSongListPop;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, OnServiceConnect, PlayServiceCallback {

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    private boolean mIsPrepareLogout = false;

    private DrawerLayout drawerLayout;
    private RelativeLayout rlMainContent;
    private RelativeLayout rlMainSlide;

    private LinearLayout llMainContentContainer;
    private MyAppTitle myAppTitle;

    private LinearLayout llScanLocalMusic;
    private LinearLayout llSetting;
    private LinearLayout llExit;

    private RecyclerView rvMainFolderList;

    private LinearLayout llMainCurrMusic;
    private TextView tvMainCurrMusicName;
    private TextView tvMainCurrMusicArtist;
    private ImageView tvMainCurrMusicPlayState;
    private ImageView tvMainCurrMusicPlayList;
    private ImageView ivMainCurrMusicAvator;

    private List<FolderInfoWithMusicCount> folderList = new ArrayList<>();
    private MainFolderAdapter adapter;

    private PlayServiceConnection sServiceConnection;
    private SongPlayServiceManager playServiceManager;
    private IPlayControl playControl;

    private SongInfo currSongInfo;
    private List<SongInfo> currSongInfos = new ArrayList<>();
    private PlayListModel playListModel;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        }
    }

    @Override
    public int initViewLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initViewById() {
        drawerLayout = findViewById(R.id.drawerLayout);
        rlMainContent = findViewById(R.id.rlMainContent);
        rlMainSlide = findViewById(R.id.rlMainSlide);

        llMainContentContainer = findViewById(R.id.llMainContentContainer);
        myAppTitle = findViewById(R.id.myAppTitle);
        rvMainFolderList = findViewById(R.id.rvMainFolderList);

        llScanLocalMusic = findViewById(R.id.llScanLocalMusic);
        llSetting = findViewById(R.id.llSetting);
        llExit = findViewById(R.id.llExit);

        tvMainCurrMusicName = findViewById(R.id.tvMainCurrMusicName);
        tvMainCurrMusicArtist = findViewById(R.id.tvMainCurrMusicArtist);
        tvMainCurrMusicPlayState = findViewById(R.id.tvMainCurrMusicPlayState);
        tvMainCurrMusicPlayList = findViewById(R.id.tvMainCurrMusicPlayList);
        ivMainCurrMusicAvator = findViewById(R.id.ivMainCurrMusicAvator);
    }

    @Override
    public void initListener() {

        if (null != llScanLocalMusic) {
            llScanLocalMusic.setOnClickListener(this);
        }
        if (null != llSetting) {
            llSetting.setOnClickListener(this);
        }
        if (null != llExit) {
            llExit.setOnClickListener(this);
        }

        if (null != drawerLayout) {
            drawerLayout.addDrawerListener(new SimpleDrawerListener() {
                @Override
                public void onDrawerOpened(View drawerView) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                }
            });
        }

        // 设置音乐播放点击事件
        if (null != tvMainCurrMusicPlayState) {
            tvMainCurrMusicPlayState.setOnClickListener(this);
        }
        // 展开音乐播放列表
        if (null != tvMainCurrMusicPlayList) {
            tvMainCurrMusicPlayList.setOnClickListener(this);
        }
    }

    @Override
    public void initData() {
        // 初始化标题
        if (null != myAppTitle) {
            myAppTitle.setAppTitle(R.string.MainActivity_title);
            myAppTitle.initViewsVisible(true, true, true, true);
            myAppTitle.setLeftIcon(R.mipmap.ic_main_sort);
            myAppTitle.setRightIcon(R.mipmap.ic_main_add);
            myAppTitle.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
                @Override
                public void onLeftButtonClick(View v) {
                    if (null != drawerLayout) {
                        drawerLayout.openDrawer(Gravity.LEFT);
                    }
                }
            });
            myAppTitle.setOnRightButtonClickListener(new MyAppTitle.OnRightButtonClickListener() {
                @Override
                public void OnRightButtonClick(View v) {
                    FolderAddDialog folderAddDialog = new FolderAddDialog(FolderAddDialog.TYPE_NEW);
                    folderAddDialog.setListener(new FolderAddDialog.IFolderAddDialogListener() {
                        @Override
                        public void onConfirm(String folderName) {
                            FolderInfo createInfo = new FolderInfo();
                            createInfo.folderPath = null;
                            createInfo.folderName = folderName;
                            long folderId = FolderInfoSource.updateOrInsertFolder(createInfo);
                            if (folderId > 0) {
                                // 创建成功
                                Toast.makeText(MainActivity.this, getString(R.string.MainActivity_add_folder_dialog_success), Toast.LENGTH_SHORT).show();
                                FolderInfoWithMusicCount folderInfoWithMusicCount = new FolderInfoWithMusicCount();
                                folderInfoWithMusicCount.folderId = (int) folderId;
                                folderInfoWithMusicCount.folderName = createInfo.folderName;
                                folderInfoWithMusicCount.folderPath = createInfo.folderPath;
                                folderInfoWithMusicCount.musicCount = 0;
                                if (null != adapter) {
                                    adapter.addData(folderInfoWithMusicCount);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                    folderAddDialog.show(getSupportFragmentManager(), MainActivity.this.getLocalClassName());
                }
            });
        }
        if (null != drawerLayout) {
            // 设置初始状态是侧滑内容关闭
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        // 获取本地文件夹列表
        folderList = SongInfoSource.getFolderSongList();
        if (null != folderList && folderList.size() > 0) {
            if (null != rvMainFolderList) {
                GridLayoutManager manager = new GridLayoutManager(this, 3);
                rvMainFolderList.setLayoutManager(manager);
                adapter = new MainFolderAdapter(this, R.layout.item_main_folder, folderList);
                rvMainFolderList.setAdapter(adapter);
            }
        }

        // 初始化歌曲播放服务管理对象
        playServiceManager = new SongPlayServiceManager(this);
        // 初始化音乐播放连接
        sServiceConnection = new PlayServiceConnection(this, this, this);
        // 绑定成功后回调 onConnected
        playServiceManager.bindService(sServiceConnection);
    }

    private void jumpToMusicScan() {
        Intent intent = MusicScanActivity.getIntent(this);
        startActivity(intent);
    }

    private void jumpToSetting() {
        Intent intent = SettingActivity.getIntent(this);
        startActivity(intent);
    }

    private void showSongListPop() {
        CurrSongListPop pop = new CurrSongListPop(this, new CurrSongListPop.OnCurrSongListListener() {
            // TODO 可以考虑是否放在pop中操作
            @Override
            public void clearAll() {
                // 清空本地播放列表
            }

            @Override
            public void onChangePlayMode(int mode) {
                // 改变播放状态
            }
        });
        pop.setSongList(this.currSongInfos);
        pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    /**
     * 初始化广播
     * 后续操作，比如更新状态栏，是需要通过广播的形式来实现，所以需要在此处
     */
    private void initBroadcastReceivers() {

    }

    /**
     * 连接成功
     *
     * @param name
     * @param service
     */
    @Override
    public void onConnected(ComponentName name, IBinder service) {
        // 注册广播
        initBroadcastReceivers();

        playControl = IPlayControl.Stub.asInterface(service);

        // 获取当前歌曲播放列表 TODO
        try {
            List<Song> songs = playControl.getPlayList();
            if (null == songs || songs.size() <= 0){
                // 当前缓存中没有正在播放的歌曲，从本地存储中获取
                PlayListModel playListModel = SPUtils.getPlayList();
                if (null != playListModel){
                    songs = playListModel.songList;
                    if (null != songs && songs.size() >0){
                        this.playListModel = playListModel;
                    }
                }
            }else {
                playListModel = new PlayListModel();
                playListModel.songList = songs;
                playListModel.listId = playControl.getPlayListId();
            }
            // 此时获取到的有播放列表的对象 TODO

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 断开连接
     *
     * @param name
     */
    @Override
    public void disConnected(ComponentName name) {

    }

    @Override
    public void songChanged(Song song, int index, boolean isNext) {

    }

    @Override
    public void startPlay(Song song, int index, int status) {

    }

    @Override
    public void stopPlay(Song song, int index, int status) {

    }

    @Override
    public void onPlayListChange(Song current, int index, int id) {

    }

    @Override
    public void dataIsReady(IPlayControl mControl) {

    }

    @Override
    public void onBackPressed() {
        // 如果侧滑页面处于展开状态，则关闭侧滑页面
        if (null != drawerLayout && drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
            return;
        }
        if (mIsPrepareLogout) {
            try {
                super.onBackPressed();
            } catch (Exception ex) {
                Toast.makeText(this, getString(R.string.MainActivity_exit_error), Toast.LENGTH_SHORT).show();
                System.exit(0);
            }
        } else {
            Toast.makeText(this, getString(R.string.MainActivity_exit_onemore), Toast.LENGTH_SHORT).show();
            mIsPrepareLogout = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mIsPrepareLogout = false;
                }
            }, 2000);
        }
    }

    @Override
    public void onClick(View v) {
        if (null == v) {
            return;
        }
        // 关闭侧滑
        if (null != drawerLayout && drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        switch (v.getId()) {
            case R.id.llScanLocalMusic:
                // 跳转到本地音乐扫描页面
                jumpToMusicScan();
                break;
            case R.id.llSetting:
                // 跳转到设置页面
                jumpToSetting();
                break;
            case R.id.llExit:
                // 退出应用
                System.exit(0);
                break;
            case R.id.tvMainCurrMusicPlayState:
                // 继续播放当前音乐
                break;
            case R.id.tvMainCurrMusicPlayList:
                // 显示当前音乐播放列表
                showSongListPop();
                break;
        }
    }

}
