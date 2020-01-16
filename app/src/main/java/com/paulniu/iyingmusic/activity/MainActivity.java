package com.paulniu.iyingmusic.activity;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.adapter.MainFolderAdapter;
import com.paulniu.iyingmusic.base.BaseActivity;
import com.paulniu.iyingmusic.db.entity.FolderInfo;
import com.paulniu.iyingmusic.db.entity.FolderInfoWithMusicCount;
import com.paulniu.iyingmusic.db.source.FolderInfoSource;
import com.paulniu.iyingmusic.db.source.MusicInfoSource;
import com.paulniu.iyingmusic.widget.MyAppTitle;
import com.paulniu.iyingmusic.widget.dialog.FolderAddDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

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
        folderList = MusicInfoSource.getFolderMusicCount();
        if (null != folderList && folderList.size() > 0) {
            if (null != rvMainFolderList) {
                GridLayoutManager manager = new GridLayoutManager(this, 3);
                rvMainFolderList.setLayoutManager(manager);
                adapter = new MainFolderAdapter(this, R.layout.item_main_folder, folderList);
                rvMainFolderList.setAdapter(adapter);
            }
        }
    }

    private void jumpToMusicScan() {
        Intent intent = MusicScanActivity.getIntent(this);
        startActivity(intent);
    }

    private void jumpToSetting() {
        Intent intent = SettingActivity.getIntent(this);
        startActivity(intent);
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
                break;
        }
    }
}
