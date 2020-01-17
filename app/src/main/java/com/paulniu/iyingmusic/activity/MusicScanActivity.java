package com.paulniu.iyingmusic.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paulniu.iyingmusic.R;
import com.paulniu.iyingmusic.adapter.MusicScanAdapter;
import com.paulniu.iyingmusic.base.BaseActivity;
import com.paulniu.iyingmusic.db.entity.FolderInfo;
import com.paulniu.iyingmusic.db.entity.SongInfo;
import com.paulniu.iyingmusic.db.source.FolderInfoSource;
import com.paulniu.iyingmusic.db.source.SongInfoSource;
import com.paulniu.iyingmusic.interfaces.IOnMusicScanSelectMusicListener;
import com.paulniu.iyingmusic.model.event.OnFolderMusicListChangeEvent;
import com.paulniu.iyingmusic.utils.SongUtils;
import com.paulniu.iyingmusic.widget.MyAppTitle;
import com.paulniu.iyingmusic.widget.pop.MusicScanSelectEmptyFolderPop;
import com.paulniu.iyingmusic.widget.pop.MusicScanSelectFolderPop;

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
public class MusicScanActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, IOnMusicScanSelectMusicListener {

    public static Intent getIntent(Context context) {
        return new Intent(context, MusicScanActivity.class);
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
    private RelativeLayout rlMusicScanResultEmpty;

    private List<SongInfo> musicInfos = new ArrayList<>();
    // 选择需要被导入的音乐
    private List<SongInfo> selectMusicInfos = new ArrayList<>();
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

        rlMusicScanResultEmpty = findViewById(R.id.rlMusicScanResultEmpty);
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
        if (null != cbMusciScanSelectAll) {
            cbMusciScanSelectAll.setOnCheckedChangeListener(this);
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
        musicInfos = SongUtils.getLocalStorageMusics(this);
        if (null != musicInfos && musicInfos.size() > 0) {
            // 本地音乐数量大于0
            if (null != rlMusicScanStart) {
                rlMusicScanStart.setVisibility(View.GONE);
            }
            if (null != rlMusicScanResult) {
                rlMusicScanResult.setVisibility(View.VISIBLE);
            }
            if (null != rlMusicScanResultEmpty) {
                rlMusicScanResultEmpty.setVisibility(View.GONE);
            }
            // 初始化适配器
            adapter = new MusicScanAdapter(R.layout.item_music_scan, musicInfos, this);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            rvMusciScan.setLayoutManager(manager);
            rvMusciScan.setAdapter(adapter);
            // 重新设置标题
            if (null != myAppTitle) {
                myAppTitle.setRightTitle(getString(R.string.MusicScanActivity_title_right));
                myAppTitle.setOnRightButtonClickListener(new MyAppTitle.OnRightButtonClickListener() {
                    @Override
                    public void OnRightButtonClick(View v) {
                        if (TextUtils.equals(myAppTitle.getRightTitle(), getString(R.string.MusicScanActivity_title_right))) {
                            myAppTitle.setRightTitle(getString(R.string.MusicScanAcitivyt_title_cancel));
                            // 设置列表中的编辑按钮可以点击
                            setCheckBoxVisiable(true);
                            // 设置全选布局展示
                            if (null != rlMusicScanSelectContainer) {
                                rlMusicScanSelectContainer.setVisibility(View.VISIBLE);
                            }
                        } else {
                            myAppTitle.setRightTitle(getString(R.string.MusicScanActivity_title_right));
                            setCheckBoxVisiable(false);
                            // 设置全选布局隐藏
                            if (null != rlMusicScanSelectContainer) {
                                rlMusicScanSelectContainer.setVisibility(View.GONE);
                            }
                            // 设置选择的数据为空
                            selectMusicInfos.clear();
                        }
                    }
                });
            }
        } else {
            // 扫描本地音乐数量为空
            // 本地音乐数量大于0
            if (null != rlMusicScanStart) {
                rlMusicScanStart.setVisibility(View.GONE);
            }
            if (null != rlMusicScanResult) {
                rlMusicScanResult.setVisibility(View.GONE);
            }
            if (null != rlMusicScanResultEmpty) {
                rlMusicScanResultEmpty.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 导入音乐
     */
    private void ShowImportMusics() {
        if (null == selectMusicInfos || selectMusicInfos.size() <= 0) {
            Toast.makeText(this, getString(R.string.MusicScanActivity_select_music_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        if (null == selectFolderInfo) {
            // 没有选择文件夹，则默认放到我的最爱文件夹中
            MusicScanSelectEmptyFolderPop pop = new MusicScanSelectEmptyFolderPop(this, new MusicScanSelectEmptyFolderPop.OnMusicScanSelectEmptyFolderListener() {
                @Override
                public void onConfirm() {
                    selectFolderInfo = FolderInfoSource.getDefaultFolder();
                    importMusics();
                }

                @Override
                public void onCancel() {

                }
            });
            pop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        } else {
            importMusics();
        }
    }

    private void importMusics() {
        // 更改数据库中的数据
        if (null != selectFolderInfo) {
            if (null != selectMusicInfos && selectMusicInfos.size() > 0) {
                SongInfoSource.updateSongInfoFolderId(selectMusicInfos, selectFolderInfo.folderId, selectFolderInfo.folderId == FolderInfoSource.getDefaultFolder().folderId);
                // 导入音乐之后，发送event，通知文件夹列表更新
                OnFolderMusicListChangeEvent event = new OnFolderMusicListChangeEvent();
                EventBus.getDefault().post(event);
                Toast.makeText(this, getString(R.string.MusicScanActivity_import_music_success), Toast.LENGTH_SHORT).show();
                // 导入音乐成功，回复初始状态
                myAppTitle.setRightTitle(getString(R.string.MusicScanActivity_title_right));
                setCheckBoxVisiable(false);
                // 设置全选布局隐藏
                if (null != rlMusicScanSelectContainer) {
                    rlMusicScanSelectContainer.setVisibility(View.GONE);
                }
                // 设置选择的数据为空
                selectMusicInfos.clear();
            } else {
                Toast.makeText(this, getString(R.string.MusicScanActivity_select_music_empty), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 弹出选择文件夹弹窗
     */
    private void selectFolder() {
        MusicScanSelectFolderPop selectFolderPop = new MusicScanSelectFolderPop(this, new MusicScanSelectFolderPop.OnMusicScanSelectFolderListener() {
            @Override
            public void onConfirm(FolderInfo folderInfo) {
                if (null != folderInfo) {
                    selectFolderInfo = folderInfo;
                    // 设置文字
                    if (null != tvMusicScanSelectFolder) {
                        tvMusicScanSelectFolder.setText(folderInfo.folderName);
                    }
                }
            }

            @Override
            public void onCancel() {

            }
        });
        selectFolderPop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /**
     * 设置item中的checkbox可见
     */
    public void setCheckBoxVisiable(boolean isVisiable) {
        if (null != musicInfos && musicInfos.size() > 0) {
            for (int i = 0; i < musicInfos.size(); i++) {
                musicInfos.get(i).isShowChecked = isVisiable;
            }
            if (null != adapter) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 设置是否选中全部的checkbox
     */
    public void setCheckBoxSelectAll(boolean isSelectAll) {
        if (null != musicInfos && musicInfos.size() > 0) {
            for (int i = 0; i < musicInfos.size(); i++) {
                if (null != musicInfos.get(i)) {
                    musicInfos.get(i).isChecked = isSelectAll;
                }
            }
            if (null != adapter) {
                adapter.notifyDataSetChanged();
            }
            if (null != selectMusicInfos) {
                selectMusicInfos.clear();
                selectMusicInfos.addAll(musicInfos);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean selectAll) {
        if (compoundButton.getId() == R.id.cbMusciScanSelectAll) {
            // 选择全部，获取取消选中全部
            setCheckBoxSelectAll(selectAll);
        }
    }

    /**
     * 选择音乐加入到需要导入的列表中
     *
     * @param musicInfo
     */
    @Override
    public void onMusicScanSelectMusic(SongInfo musicInfo) {
        if (null != musicInfo && null != selectMusicInfos) {
            int position = -1;
            for (int i = 0; i < selectMusicInfos.size(); i++) {
                if (null != selectMusicInfos.get(i) && TextUtils.equals(selectMusicInfos.get(i).data, musicInfo.data)) {
                    position = i;
                    break;
                }
            }
            if (position == -1) {
                selectMusicInfos.add(musicInfo);
            }
        }
    }

    /**
     * 选择音乐移除需要导入的列表
     *
     * @param musicInfo
     */
    @Override
    public void onMusicScanUnselectMusic(SongInfo musicInfo) {
        if (null != musicInfo && null != selectMusicInfos && selectMusicInfos.size() > 0) {
            int position = -1;
            for (int i = 0; i < selectMusicInfos.size(); i++) {
                if (null != selectMusicInfos.get(i) && TextUtils.equals(selectMusicInfos.get(i).data, musicInfo.data)) {
                    position = i;
                    break;
                }
            }
            selectMusicInfos.remove(position);
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
                    ShowImportMusics();
                    break;
                case R.id.tvMusicScanSelectFolder:
                    // 选择文件夹，弹出pop
                    selectFolder();
                    break;
            }
        }
    }

}
