package com.paulniu.iyingmusic.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.adapter.MusicScanAdapter;
import com.paulniu.iyingmusic.base.BaseActivity;
import com.paulniu.iyingmusic.db.entity.FolderInfo;
import com.paulniu.iyingmusic.model.MusicInfo;
import com.paulniu.iyingmusic.model.event.OnFolderMusicListChangeEvent;
import com.paulniu.iyingmusic.utils.MusicUtils;
import com.paulniu.iyingmusic.widget.MyAppTitle;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-11
 * Time: 23:50
 * Desc: 扫描本地音乐Activity
 * Version:
 */
public class MusicScanActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, MusicScanActivity.class);
        return intent;
    }

    private MyAppTitle myAppTitle;
    private TextView tvMusicScanStartScan;
    private RelativeLayout rlMusicScanStart;
    private RelativeLayout rlMusicScanResult;
    private RecyclerView rvMusciScan;
    private RelativeLayout rlMusicScanSelectContainer;
    private CheckBox cbMusciScanSelectAll;
    private TextView tvMusicScanSelectAllConfirm;
    private TextView tvMusicScanSelectFolder;

    private List<MusicInfo> musicInfos = new ArrayList<>();
    // 选择需要被导入的音乐
    private List<MusicInfo> selectMusicInfos = new ArrayList<>();
    private MusicScanAdapter adapter;
    // 选择被导入的文件夹
    private FolderInfo selectFolderInfo;

    @Override
    public int initViewLayout() {
        return R.layout.activity_music_scan;
    }

    @Override
    public boolean needInitEvent() {
        return true;
    }

    @Override
    public void initViewById() {
        myAppTitle = findViewById(R.id.myAppTitle);

        rlMusicScanStart = findViewById(R.id.rlMusicScanStart);
        tvMusicScanStartScan = findViewById(R.id.tvMusicScanStartScan);

        rlMusicScanResult = findViewById(R.id.rlMusicScanResult);
        rvMusciScan = findViewById(R.id.rvMusciScan);

        rlMusicScanSelectContainer = findViewById(R.id.rlMusicScanSelectContainer);
        cbMusciScanSelectAll = findViewById(R.id.cbMusciScanSelectAll);
        tvMusicScanSelectAllConfirm = findViewById(R.id.tvMusicScanSelectAllConfirm);
        tvMusicScanSelectFolder = findViewById(R.id.tvMusicScanSelectFolder);
    }

    @Override
    public void initListener() {
        if (null != tvMusicScanStartScan) {
            tvMusicScanStartScan.setOnClickListener(this);
        }
        if (null != tvMusicScanSelectAllConfirm) {
            tvMusicScanSelectAllConfirm.setOnClickListener(this);
        }
        if (null != tvMusicScanSelectFolder) {
            tvMusicScanSelectFolder.setOnClickListener(this);
        }
    }

    @Override
    public void initData() {
        // 初始化标题
        myAppTitle.setAppTitle(R.string.MusicScanActivity_title);
        myAppTitle.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                onBackPressed();
            }
        });
        myAppTitle.initViewsVisible(true, true, false, true);
    }

    /**
     * 扫描本地音乐
     */
    private void scanLocalMusic() {
        if (null != musicInfos) {
            musicInfos.clear();
        }
        musicInfos = MusicUtils.getLocalStorageMusics(this);
        if (null != musicInfos && musicInfos.size() > 0) {
            // 本地音乐数量大于0
            if (null != rlMusicScanStart) {
                rlMusicScanStart.setVisibility(View.GONE);
            }
            if (null != rlMusicScanResult) {
                rlMusicScanResult.setVisibility(View.VISIBLE);
            }
            // 初始化适配器
            adapter = new MusicScanAdapter(R.layout.item_music_scan, musicInfos);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            rvMusciScan.setLayoutManager(manager);
            rvMusciScan.setAdapter(adapter);
            // 重新设置标题
            if (null != myAppTitle) {
                myAppTitle.setRightTitle(getString(R.string.MusicScanActivity_title_right));
                myAppTitle.setOnRightButtonClickListener(new MyAppTitle.OnRightButtonClickListener() {
                    @Override
                    public void OnRightButtonClick(View v) {
                        // 设置列表中的编辑按钮可以点击
                        if (null != adapter) {
                            adapter.setCheckBoxVisiable();
                            // 设置全选布局展示
                            if (null != rlMusicScanSelectContainer) {
                                rlMusicScanSelectContainer.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * 导入音乐
     */
    private void importMusics() {
        // 更改数据库中的数据

        // 导入音乐之后，发送event，通知文件夹列表更新
        OnFolderMusicListChangeEvent event = new OnFolderMusicListChangeEvent();
        EventBus.getDefault().post(event);
    }

    /**
     * 弹出选择文件夹弹窗
     */
    private void selectFolder() {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean selectAll) {
        if (compoundButton.getId() == R.id.cbMusciScanSelectAll) {
            // 选择全部，获取取消选中全部
            if (null != adapter) {
                adapter.setCheckBoxSelectAll(selectAll);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (null != view) {
            switch (view.getId()) {
                case R.id.tvMusicScanStartScan:
                    scanLocalMusic();
                    break;
                case R.id.tvMusicScanSelectAllConfirm:
                    // 选择全部/导入文件夹确认按钮
                    importMusics();
                    break;
                case R.id.tvMusicScanSelectFolder:
                    // 选择文件夹，弹出pop
                    selectFolder();
                    break;
            }
        }
    }
}
